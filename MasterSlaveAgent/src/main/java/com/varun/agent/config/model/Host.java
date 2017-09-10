package com.varun.agent.config.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Model class for the configuration on a particular host.
 * 
 * @author varunjai
 *
 */
@XmlRootElement(name = "host")
@XmlType(propOrder = {"name", "metadata", "directory"})
public class Host {

  private String name;
  private Metadata metadata;
  private Directory directory;
  /**
   * Get the name of the host.
   * 
   * @return
   */
  @XmlElement
  public String getName() {
    return name;
  }
  /**
   * see {@link #getName()}
   * 
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * Get the {@link Metadata} for the host.
   * 
   * @return
   */
  @XmlElement
  public Metadata getMetadata() {
    return metadata;
  }
  /**
   * see {@link #getMetadata()}
   * 
   * @param metadata
   */
  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }
  /**
   * Get the {@link Directory} information.
   * 
   * @return
   */
  @XmlElement
  public Directory getDirectory() {
    return directory;
  }
  /**
   * see {@link #getDirectory()}
   * 
   * @param directory
   */
  public void setDirectory(Directory directory) {
    this.directory = directory;
  }

}
