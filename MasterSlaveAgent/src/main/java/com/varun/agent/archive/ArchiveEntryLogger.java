package com.varun.agent.archive;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.varun.agent.event.type.AgentEvent;
import com.varun.agent.corejavautils.io.CoreFileUtils;
import com.varun.agent.corejavautils.jaxb.JaxBMarshalUnMarshalUtil;

/**
 * This class will process an {@link ArchiveEntry} and write the archive details
 * in a file named as agent-archive.xml. This file will contain details about
 * all the {@link AgentEvent} processed during the build of a single archive.
 * 
 * @author varunjai
 *
 */
public class ArchiveEntryLogger {

  /**
   * Logger instance
   */
  public final static Logger logger = LogManager
      .getLogger(ArchiveEntryLogger.class);

  private final Config config;

  /**
   * 
   * @param config
   *          !null
   */
  public ArchiveEntryLogger(Config config) {
    Validate.notNull(config);
    this.config = config;
  }

  /**
   * Create the agent archive xml file
   */
  public void log() {

    try {
      JaxBMarshalUnMarshalUtil.marshall(ArchiveEntry.class,
          config.getArchiveDirPath() + File.separator + "agent-archive.xml",
          config.getArchiveEntry());
    } catch (JAXBException e) {

      logger.warn("Unable to write the agent-archive.xml for "
          + config.getArchiveEntry().getCollectionName(), e);
    }

  }

  /**
   * 
   * @author varunjai
   *
   */
  public static class Config {
    /**
     * {@link #getArchiveEntry()}
     */
    private final ArchiveEntry archiveEntry;
    /**
     * {@link #getArchiveDirPath()}
     */
    private final String archiveDirPath;

    /**
     * Configure the {@link ArchiveEntryLogger}
     * 
     * @param archiveEntry
     *          !null.
     * @param archiveDirPath
     *          valid directory.
     * @throws IllegalArgumentException
     */
    public Config(ArchiveEntry archiveEntry, String archiveDirPath) {
      Validate.notNull(archiveEntry);

      if (CoreFileUtils.validateFilePath(archiveDirPath)) {
        throw new IllegalArgumentException("Archive Directory not valid");
      }

      this.archiveDirPath = archiveDirPath;
      this.archiveEntry = archiveEntry;
    }
    /**
     * Get the {@link ArchiveEntry}
     * 
     * @return
     */
    public ArchiveEntry getArchiveEntry() {
      return archiveEntry;
    }

    /**
     * Get the path where the archive directory is present.
     * 
     * @return
     */
    public String getArchiveDirPath() {
      return archiveDirPath;
    }
  }
}
