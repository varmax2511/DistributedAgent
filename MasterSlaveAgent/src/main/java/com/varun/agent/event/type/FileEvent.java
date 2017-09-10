package com.varun.agent.event.type;

import java.io.File;

import com.varun.agent.core.AgentConstants;
import com.varun.agent.corejavautils.event.EventType;
import com.varun.agent.corejavautils.io.CoreFileUtils;

/**
 * This event is generated when the agent finds a file. The event encapsulates
 * information for getting this file.
 * 
 * @author varunjai
 *
 */
public class FileEvent extends AgentEvent {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private final String eventId;
  private long startByte = -1;
  private long endByte = -1;
  private long startTimeMS = -1;
  private long endTimeMS = -1;
  private final File file;

  /**
   * 
   * @param filePath
   * @param timeStamp
   */
  public FileEvent(String filePath, long timeStamp) {
    super(EventType.AGENT_FILE, timeStamp);

    // validate
    if (!CoreFileUtils.validateFilePath(filePath)) {
      throw new IllegalArgumentException("Invalid file");
    }

    this.file = new File(filePath);
    this.setStartByte(0);
    this.setEndByte(file.length());
    this.eventId = filePath + "_" + timeStamp;
  }

  /**
   * 
   * @param file
   * @param timeStamp
   */
  public FileEvent(File file, long timeStamp) {
    super(EventType.AGENT_FILE, timeStamp);

    if (!CoreFileUtils.validateFile(file)) {
      throw new IllegalArgumentException("Invalid file");
    }
    this.file = file;

    this.setStartByte(0);
    this.setEndByte(file.length());
    this.eventId = file.getAbsolutePath() + "_" + timeStamp;
  }

  @Override
  public String getEventId() {
    return this.eventId;
  }
  /**
   * Start byte of the file to be read. <br/>
   * Defaults to 0 byte
   * 
   * @return
   */
  public long getStartByte() {
    return startByte;
  }
  /**
   * see {@link #getStartByte()}
   * 
   * @param startByte
   */
  public void setStartByte(long startByte) {
    this.startByte = startByte;
  }
  /**
   * Get the end byte for the file being read. Ideally its the end of file. But
   * in cases where the archive size is reaching
   * {@link AgentConstants#ARCHIVE_UPPERBOUND}, we trim the file before end of
   * file. The remaining bytes will be added in a new {@link FileEvent} with the
   * same event id.
   * 
   * @return
   */
  public long getEndByte() {
    return endByte;
  }
  /**
   * see {@link #getEndByte()}
   * 
   * @param endByte
   */
  public void setEndByte(long endByte) {
    this.endByte = endByte;
  }
  /**
   * Get the size of the File indexed.
   * 
   * @return
   */
  @Override
  public long getSize() {
    return (this.endByte - this.startByte);
  }

  /**
   * see {@link #setStartTimeMS(long)}
   * 
   * @return
   */
  public long getStartTimeMS() {
    return startTimeMS;
  }
  /**
   * Set the time in ms from where the file needs to be collected.
   * 
   * @param startTimeMS
   */
  public void setStartTimeMS(long startTimeMS) {
    this.startTimeMS = startTimeMS;
  }
  /**
   * see {@link #setEndTimeMS(long)}
   * 
   * @return
   */
  public long getEndTimeMS() {
    return endTimeMS;
  }
  /**
   * Set the time in ms till which point the file needs to be collected.
   * 
   * @param endTimeMS
   */
  public void setEndTimeMS(long endTimeMS) {
    this.endTimeMS = endTimeMS;
  }

  public File getFile() {
    return file;
  }

}
