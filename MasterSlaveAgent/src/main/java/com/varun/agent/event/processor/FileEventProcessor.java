package com.varun.agent.event.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.varun.agent.core.AgentConstants;
import com.varun.agent.event.type.FileEvent;
import com.varun.agent.corejavautils.event.EventProcessor;
import com.varun.agent.corejavautils.io.CoreFileUtils;
/**
 * Default implementation of an {@link EventProcessor} for a {@link FileEvent}
 * FileEventProcessor is expected to process a collection of file events.
 * {@link FileEventProcessor} will not be multi-threaded but it is possible that
 * multiple {@link FileEventProcessor} may be working on the same machine.
 * Although each processor will be working on independent events but all file
 * event processors will be accessing the common collection-event-info.xml where
 * they will log information about each event processed.
 * 
 * Q. what if file event processor is multi-threaded? Ans
 * {@link FileEventProcessor} owns the task of processing {@link FileEvent}. If
 * can do that by spawning multiple threads or one based on how it is
 * configured. Similarly each event processor will also be a callable allowing
 * it be processed as a separate thread or not.
 * 
 * Use case We will have an ArchiveBuilder which will be like a manager class.
 * This class will identify the number of events and based on work load will
 * spawn FileEvent processors. Lets say on one host we have one file event
 * processor processing 10 file events.
 * 
 * Q. Since our event processor has only an int returning process() method, how
 * will the file event processor update the collection-event-info.xml? Ans. One
 * thing that can be done is the event processor can return a list of events
 * that were processed, but do we expect every event processor to return the
 * list of processed events across the application?
 * 
 * Q. How do we plan to inform the archive builder to process the trimmed event
 * left over event? Ans. Each file event will contain a flag which will be set
 * to true when the file was not fully processed. It will also contain the bytes
 * remaining.
 * 
 * Q. How will a file event processor know when the archive dir is about to
 * reach max size? Ans. We can expose an instance of the archive which will be
 * for read only. The getSize() method of this object will be synchronized and
 * will give the size of the archive at a time.
 * 
 * 
 * @author varunjai
 *
 */
public class FileEventProcessor
    implements
      EventProcessor<Collection<FileEvent>> {

  private final static Logger logger = LogManager
      .getLogger(FileEventProcessor.class);
  private final Config config;

  /**
   * 
   * @param config
   *          !null
   */
  public FileEventProcessor(final Config config) {
    Validate.notNull(config);
    this.config = config;
  }

  @Override
  public Collection<FileEvent> call() throws Exception {

    ExecutorService es = null;

    try {
      // single event processor execution
      if (config.getNumThreads() < 2
          || config.getFileEvents().size() < config.getFilesPerThread()) {

        es = Executors.newSingleThreadExecutor();
        final CopyTask copyTask = new CopyTask(config.getFileEvents(),
            config.getDestinationDirPath());

        final Future<Collection<FileEvent>> future = es.submit(copyTask);

        final List<FileEvent> processedEvents = new ArrayList<>();
        try {
          processedEvents
              .addAll(future.get(config.getTimeOut(), config.getTimeUnit()));
        } catch (TimeoutException e) {

          logger.info("NON-Fatal: Timeout in FileEventProcessor thread", e);
        }

        return processedEvents;
      }

      /*
       * Multi event processor execution. Compute number of threads
       */
      int threadCount = (int) Math
          .round(new Double(config.getFileEvents().size())
              / new Double(config.getFilesPerThread()));

      threadCount = threadCount > config.getNumThreads()
          ? config.getNumThreads()
          : threadCount;

      es = Executors.newFixedThreadPool(threadCount);

      int frmIdx = 0;
      int toIdx = config.getFilesPerThread();
      final int count = threadCount;

      final List<Future<Collection<FileEvent>>> futures = new ArrayList<>();
      while (count > 0) {
        final FileEventProcessor feProcessor = new FileEventProcessor(
            new FileEventProcessor.Config(
                config.getFileEvents().subList(frmIdx, toIdx),
                config.getDestinationDirPath()));
        futures.add(es.submit(feProcessor));

        frmIdx = toIdx + 1;
        toIdx = config.getFileEvents().size() > toIdx
            + config.getFilesPerThread()
                ? toIdx + config.getFilesPerThread()
                : config.getFileEvents().size();
      } // while

      final List<FileEvent> processedEvents = new ArrayList<>();

      for (Future<Collection<FileEvent>> future : futures) {
        try {
          processedEvents
              .addAll(future.get(config.getTimeOut(), config.getTimeUnit()));
        } catch (TimeoutException e) {

          logger.info("NON-Fatal: Timeout in FileEventProcessor thread", e);
        }
      } // for

      return processedEvents;
    } finally {
      if (es != null) {
        es.shutdown();
      }
    }

  }

  /**
   * 
   * @author varunjai
   *
   */
  class CopyTask implements Callable<Collection<FileEvent>> {

    private final List<FileEvent> fileEvents;
    private final String destinationDirPath;

    public CopyTask(List<FileEvent> fileEvents, String destinationDirPath) {
      this.fileEvents = fileEvents;
      this.destinationDirPath = destinationDirPath;
    }

    public List<FileEvent> getFileEvents() {
      return fileEvents;
    }

    public String getDestinationDirPath() {
      return destinationDirPath;
    }

    @Override
    public Collection<FileEvent> call() throws Exception {
      // check
      if (CollectionUtils.isEmpty(config.getFileEvents())) {
        logger.info("No file events to process");
        return new ArrayList<>();
      }

      final List<FileEvent> processedEvents = new ArrayList<>();
      for (final FileEvent fileEvent : this.getFileEvents()) {
        try {
          /*
           * An error in copying file will be logged but will be non-fatal We
           * re-create entire directory structure to avoid conflicts due to
           * duplicates
           */

          final File destFile = new File(
              this.getDestinationDirPath() + File.separator
                  + fileEvent.getFile().getAbsolutePath()
                      .substring(FilenameUtils
                          .getPrefix(fileEvent.getFile().getAbsolutePath())
                          .length()));

          // make all parent directories
          FileUtils.forceMkdirParent(destFile);

          CoreFileUtils.copyFileWithByteRange(fileEvent.getFile(), destFile,
              fileEvent.getStartByte(), fileEvent.getEndByte());

          processedEvents.add(fileEvent);
        } catch (Throwable t) {
          logger.error("NON-FATAL error in copying source file: "
              + fileEvent.getFile().getAbsolutePath(), t);
        }
      } // for

      return processedEvents;
    }

  }

  /**
   * Configuration class for {@link FileEventProcessor}
   * 
   * @author varunjai
   *
   */
  public static class Config {
    /**
     * {@link #getFileEvents()}
     */
    private final List<FileEvent> fileEvents;
    /**
     * {@link #getDestinationDirPath()}
     */
    private final String destinationDirPath;
    /**
     * {@link #getNumThreads()}
     */
    private int numThreads = AgentConstants.LOCAL_ARCHIVE_NUM_THREADS;
    /**
     * {@link #getFilesPerThread()}
     */
    private int filesPerThread = AgentConstants.LOCAL_ARCHIVE_MIN_FILE_PER_THREAD;
    /**
     * {@link #getTimeOut()}
     */
    private long timeOut = AgentConstants.TIMEOUT_FILE_EVENT_PROCESSING;
    /**
     * {@link #getTimeUnit()}
     */
    private TimeUnit timeUnit = AgentConstants.DEFAULT_FILEEVENT_TIMEOUT_TIMEUNIT;

    /**
     * 
     * @param fileEvents
     *          !null
     * @param destinationDirPath
     *          !blank
     */
    public Config(List<FileEvent> fileEvents, String destinationDirPath) {
      Validate.notNull(fileEvents);
      Validate.notBlank(destinationDirPath);

      this.fileEvents = fileEvents;
      this.destinationDirPath = destinationDirPath;
    }

    /**
     * Get the list of {@link FileEvent} which need to be processed.
     * 
     * @return
     */
    public List<FileEvent> getFileEvents() {
      return fileEvents;
    }
    /**
     * Get the path where the files are required to be copied.
     * 
     * @return
     */
    public String getDestinationDirPath() {
      return destinationDirPath;
    }

    /**
     * Get the number of thread the agent is required to spawn for event
     * collection.
     * <p>
     * Defaults to 1 for local event collection.
     * 
     * @return
     */
    public int getNumThreads() {
      return numThreads;
    }
    /**
     * see {@link #getNumThreads()}
     * 
     * @param numThreads
     */
    public void setNumThreads(int numThreads) {
      this.numThreads = numThreads;
    }
    /**
     * Get the minimum number of files per thread.
     * <p>
     * Defaults to 50
     * 
     * @return
     */
    public int getFilesPerThread() {
      return filesPerThread;
    }
    /**
     * see {@link #getMinFilesPerThread()}s
     * 
     * @param minFilesPerThread
     */
    public void setFilesPerThread(int filesPerThread) {
      this.filesPerThread = filesPerThread;
    }
    /**
     * Get the timeout in Seconds.
     * <p>
     * Defaults to 180 seconds.
     * 
     * @return
     */
    public long getTimeOut() {
      return timeOut;
    }
    /**
     * see {@link #getTimeOut()}
     * 
     * @param timeOut
     */
    public void setTimeOut(long timeOut) {
      this.timeOut = timeOut;
    }
    /**
     * TimeUnit for timeout.
     * <p>
     * Defaults to seconds
     * 
     * @return
     */
    @XmlTransient
    public TimeUnit getTimeUnit() {
      return timeUnit;
    }
    /**
     * see {@link #getTimeUnit()}
     * 
     * @param timeUnit
     */
    public void setTimeUnit(TimeUnit timeUnit) {
      this.timeUnit = timeUnit;
    }
  }

}
