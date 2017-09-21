package com.varun.agent.archive;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.varun.agent.config.model.AgentProperties;
import com.varun.agent.config.model.Host;
import com.varun.agent.crawler.FileCrawler;
import com.varun.agent.event.processor.FileEventProcessor;
import com.varun.agent.event.type.FileEvent;
import com.varun.agent.corejavautils.event.Event;

/**
 * This class is responsible for all archive building operations.
 *
 * Input: The agent starter class will read the properties file configured with
 * different properties such as the maximum archive size, archive directory
 * path, number of threads, nodes,etc.
 *
 * the archive builder is responsible to reading the configuration object and
 * navigate to the directories and create matching events.
 *
 * Once the list of events is created, the archive builder is responsible to
 * spawn {@link FileEventProcessor} and pass them the events generated for
 * processing in an archive directory. The archive builder will also pass these
 * File event processors with an object in which they will register the event
 * processed. This object will also hold the total size of the events processed
 * so far by all File event processors.
 *
 * @author varunjai
 *
 */
public class LocalArchiveBuilder {

  private final static Logger logger = LogManager
      .getLogger(LocalArchiveBuilder.class);
  /**
   *
   */
  private final Config config;
  /**
   * Directory mapping event id to event
   */
  private final Map<Integer, ? extends Event> eventId2Event = new HashMap<>();
  /**
   *
   * @param config
   *          !null
   */
  public LocalArchiveBuilder(Config config) {
    Validate.notNull(config);
    this.config = config;
  }

  /**
   * 
   * @throws Exception
   */
  public void execute() throws Exception {

    logger.info("Starting process to build local archive under "
        + config.getAgentProperties().getIndexMode());
    long startTimeMS = System.currentTimeMillis();

    // validate
    if (config.getLocalHostConfig().getDirectory() == null || CollectionUtils
        .isEmpty(config.getLocalHostConfig().getDirectory().getPaths())) {
      return;
    }

    // get files to process
    final Collection<File> files = new FileCrawler(new FileCrawler.Config(
        config.getLocalHostConfig().getDirectory().getPaths(),
        config.getLocalHostConfig().getDirectory().getPatterns())).execute();

    // no files to process
    if (CollectionUtils.isEmpty(files)) {
      logger.warn("No matching files found for host: "
          + config.getLocalHostConfig().getName());
      return;
    }

    // create events and trim the count
    final Collection<FileEvent> fileEvents = new ArrayList<>();
    files.forEach(file -> {
      fileEvents.add(new FileEvent(file, System.currentTimeMillis()));
    });

    final List<FileEvent> trimmedFileEvents = ArchiveEventTrimmerUtil
        .trim(fileEvents, config.getAgentProperties().getArchiveSize());

    if (CollectionUtils.isEmpty(trimmedFileEvents)) {
      logger.info("No file events remaining post trim");
      return;
    }

    // create directory
    final String archiveDirPath = config.getAgentProperties()
        .getDestinationPath() + File.separator
        + config.getAgentProperties().getCollectionName();
    FileUtils.forceMkdir(new File(archiveDirPath));

    final Collection<FileEvent> processedEvents = runFileEventProcessors(
        trimmedFileEvents, archiveDirPath);

    // copy files and add to archive entry
    final ArchiveEntry archiveEntry = new ArchiveEntry();
    archiveEntry
        .setCollectionName(config.getAgentProperties().getCollectionName());
    archiveEntry.setHostname(config.getLocalHostConfig().getName());
    archiveEntry.setMetadata(config.getLocalHostConfig().getMetadata());
    archiveEntry.setNumEvents(processedEvents.size());
    archiveEntry.setArchiveTimeStamp(System.currentTimeMillis());
    // using stream
    archiveEntry.setArchiveByteSize(
        processedEvents.stream().mapToLong(FileEvent::getSize).sum());

    // log collection
    new ArchiveEntryLogger(
        new ArchiveEntryLogger.Config(archiveEntry, archiveDirPath)).log();

    logger.info("Archive generation complete in "
        + (System.currentTimeMillis() - startTimeMS) + " ms");
  }

  /**
   * Run the {@link FileEventProcessor} on the trimmed events.
   * 
   * @param trimmedFileEvents
   * @param archiveDir
   *          TODO
   * @return
   * @throws Exception
   */
  private Collection<FileEvent> runFileEventProcessors(
      final List<FileEvent> trimmedFileEvents, String archiveDir)
      throws Exception {

    /*
     * Add processors to run
     */

    return new FileEventProcessor(
        new FileEventProcessor.Config(trimmedFileEvents, archiveDir)).call();
  }

  /**
   * Configuration class for {@link LocalArchiveBuilder}
   *
   * @author varunjai
   *
   */
  public static class Config {

    private final Host localHostConfig;
    private final AgentProperties agentProperties;

    /**
     * Configure the {@link LocalArchiveBuilder}
     * 
     * @param localHostConfig
     *          !null
     * @param agentProperties
     *          !null
     */
    public Config(Host localHostConfig, AgentProperties agentProperties) {
      Validate.notNull(localHostConfig);
      Validate.notNull(agentProperties);
      this.localHostConfig = localHostConfig;
      this.agentProperties = agentProperties;
    }
    /**
     * 
     * @return
     */
    public Host getLocalHostConfig() {
      return localHostConfig;
    }
    /**
     * 
     * @return
     */
    public AgentProperties getAgentProperties() {
      return agentProperties;
    }

  }
}
