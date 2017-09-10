package com.varun.agent.archive;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.varun.agent.config.model.Metadata;

/**
 * Class to represent the present state of the archive.
 * 
 * @author varunjai
 *
 */
@XmlRootElement(name = "archive-properties")
@XmlType(propOrder = {"collectionName", "hostname", "numEvents", "archiveTimeStamp", "metadata", "archiveByteSize"})
public class ArchiveEntry implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String collectionName;
  private String hostname;
  private int numEvents;
  private long archiveTimeStamp;
  private Metadata metadata;
  private long archiveByteSize = 0;
  
  
  /**
   * Get the size of the archive in bytes. This value is updated as the archive
   * ingests more events
   * 
   * @return
   */
  @XmlElement(name = "archive-byte-size")
  public long getArchiveByteSize() {
    return archiveByteSize;
  }

  /**
   * see {@link #getArchiveByteSize()}
   * 
   * @param archiveSize
   */
  public void setArchiveByteSize(long archiveSize) {
    this.archiveByteSize = archiveSize;
  }

  /**
   * 
   * @return
   */
  @XmlElement(name = "collection-name")
  public String getCollectionName() {
    return collectionName;
  }

  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }

  @XmlElement(name = "events")
  public int getNumEvents() {
    return numEvents;
  }

  public void setNumEvents(int numEvents) {
    this.numEvents = numEvents;
  }

  @XmlElement(name = "archive-timestamp")
  public long getArchiveTimeStamp() {
    return archiveTimeStamp;
  }

  public void setArchiveTimeStamp(long archiveTimeStamp) {
    this.archiveTimeStamp = archiveTimeStamp;
  }

  @XmlElement
  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }
  @XmlElement(name = "hostname")
  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

}
