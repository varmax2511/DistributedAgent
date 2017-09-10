package com.varun.agent.config.model;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.logging.log4j.core.util.NetUtils;

import com.varun.agent.core.AgentConstants;

/**
 * 
 * @author varunjai
 *
 */
@XmlRootElement(name = "agent-properties")
@XmlType(
    propOrder = {"destinationPath", "agentMode", "archiveSize", "numThreads",
        "minFilesPerThread", "timeOut", "indexMode", "collectionName"})
public class AgentProperties {

  /**
   * {@link #getDestinationPath()}
   */
  private String destinationPath = AgentConstants.AGENT_DEFAULT_ARCHIVE_DESTINATION;
  /**
   * {@link #getAgentMode()}
   */
  private AgentMode agentMode = AgentMode.LOCAL;
  /**
   * {@link #getArchiveSize()}
   */
  private long archiveSize = AgentConstants.ARCHIVE_UPPERBOUND;
  /**
   * {@link #getNumThreads()}
   */
  private int numThreads = AgentConstants.LOCAL_ARCHIVE_NUM_THREADS;
  /**
   * {@link #getMinFilesPerThread()}
   */
  private int minFilesPerThread = AgentConstants.LOCAL_ARCHIVE_MIN_FILE_PER_THREAD;
  /**
   * {@link #getTimeOut()}
   */
  private long timeOut = AgentConstants.TIMEOUT_FILE_EVENT_PROCESSING;
  /**
   * {@link #getTimeUnit()}
   */
  private TimeUnit timeUnit = AgentConstants.DEFAULT_FILEEVENT_TIMEOUT_TIMEUNIT;
  /**
   * {@link #getIndexMode()}
   */
  private IndexMode indexMode = IndexMode.SINGLE;

  /**
   * {@link #getCollectionName()}
   */
  private String collectionName = NetUtils.getLocalHostname()
      + System.currentTimeMillis();
  /**
   * Select the mode in which the agent is configured.
   * 
   * @author varunjai
   *
   */
  public enum AgentMode {
    /**
     * Processor will run on same host, no compression
     */
    LOCAL,
    /**
     * Processor will run on a remote host, compression and transfer
     */
    REMOTE,
    /**
     * One master node and other slave
     */
    MASTER_SLAVE
  }

  /**
   * This enum specifies that multiple archives having the same collection name
   * should be treated.
   * <li>If {@link IndexMode#SINGLE} is selected then each upload will be
   * treated independently having no correlation.</li>
   * <li>If {@link IndexMode#DELTA} is selected then all uploads with same
   * collection name will be treated as related.</li>
   * 
   * @author varunjai
   *
   */
  public enum IndexMode {
    /**
     * If {@link IndexMode#SINGLE} is selected then each upload will be treated
     * independently having no correlation.
     */
    SINGLE,
    /**
     * If {@link IndexMode#DELTA} is selected then all uploads with same
     * collection name will be treated as related.
     */
    DELTA

  }

  /**
   * Get the path where the agent archive directory will be created.
   * 
   * @return
   */
  @XmlElement(name = "destination-path")
  public String getDestinationPath() {
    return destinationPath;
  }
  /**
   * see {@link #getDestinationPath()}
   * 
   * @param destinationPath
   */
  public void setDestinationPath(String destinationPath) {
    this.destinationPath = destinationPath;
  }
  /**
   * Get the agent mode configured.
   * 
   * @return
   */
  @XmlElement(name = "agent-mode")
  public AgentMode getAgentMode() {
    return agentMode;
  }
  /**
   * see {@link #getAgentMode()}
   * 
   * @param agentMode
   */
  public void setAgentMode(AgentMode agentMode) {
    this.agentMode = agentMode;
  }
  /**
   * Max Size of the archives created
   * 
   * @return
   */
  @XmlElement(name = "agent-archive-size")
  public long getArchiveSize() {
    return archiveSize;
  }
  /**
   * See {@link #getArchiveSize()}
   * 
   * @param archiveSize
   */
  public void setArchiveSize(long archiveSize) {
    this.archiveSize = archiveSize;
  }
  /**
   * Get the number of thread the agent is required to spawn for event
   * collection.
   * <p>
   * Defaults to 1 for local event collection.
   * 
   * @return
   */
  @XmlElement(name = "agent-thread-count")
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
  @XmlElement(name = "agent-min-files-per-thread")
  public int getMinFilesPerThread() {
    return minFilesPerThread;
  }
  /**
   * see {@link #getMinFilesPerThread()}s
   * 
   * @param minFilesPerThread
   */
  public void setMinFilesPerThread(int minFilesPerThread) {
    this.minFilesPerThread = minFilesPerThread;
  }
  /**
   * Get the timeout in Seconds.
   * <p>
   * Defaults to 180 seconds.
   * 
   * @return
   */
  @XmlElement(name = "agent-thread-timeout")
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
  /**
   * see {@link IndexMode}
   * <p>
   * Defaults to {@link IndexMode#SINGLE}
   * 
   * @return
   */
  @XmlElement(name = "index-mode")
  public IndexMode getIndexMode() {
    return indexMode;
  }
  /**
   * see {@link #getIndexMode()}
   * 
   * @param indexMode
   */
  public void setIndexMode(IndexMode indexMode) {
    this.indexMode = indexMode;
  }
  /**
   * Collection name to identify the upload.
   * <p>
   * Defaults to a host name + timestamp
   * 
   * @return
   */
  public String getCollectionName() {
    return collectionName;
  }
  /**
   * see {@link #getCollectionName()}
   * 
   * @param collectionName
   */
  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }
}
