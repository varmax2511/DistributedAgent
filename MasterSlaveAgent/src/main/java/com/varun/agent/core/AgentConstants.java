package com.varun.agent.core;

import java.util.concurrent.TimeUnit;

public interface AgentConstants {
  /**
   * Maximum size of one archive is 100 MB.
   */
  public static final long ARCHIVE_UPPERBOUND = 100 * 1024 * 1024;
  /**
   * jar environment variable for configuration file property.
   */
  public static final String VOOGLE_AGENT_CONFIG_FILE_PROPERTY = "voogle.agent.config.file";
  /**
   * Default directory for generating agent archives
   */
  public static final String AGENT_DEFAULT_ARCHIVE_DESTINATION = "/tmp/voogle/archive/";
  public int LOCAL_ARCHIVE_MIN_FILE_PER_THREAD = 50;
  public int LOCAL_ARCHIVE_NUM_THREADS = 1;
  public int TIMEOUT_FILE_EVENT_PROCESSING = 3 * 60;
  public TimeUnit DEFAULT_FILEEVENT_TIMEOUT_TIMEUNIT = TimeUnit.SECONDS;
}
