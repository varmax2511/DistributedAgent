package com.varun.agent.config.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Model class for the patterns that can be configured in the agent.
 * 
 * @author varunjai
 *
 */
@XmlRootElement(name = "pattern")
@XmlType(
    propOrder = {"dirIncludeFilter", "dirExcludeFilter",
        "fileIncludeFilter", "fileExcludeFilter"})
public class Pattern {
  /**
   * {@link #getDirIncludeFilter()}
   */
  private Collection<String> dirIncludeFilter = new ArrayList<>();
  /**
   * {@link #getDirExcludeFilter()}
   */
  private Collection<String> dirExcludeFilter = new ArrayList<>();
  /**
   * {@link #getFileIncludeFilter()}
   */
  private Collection<String> fileIncludeFilter = new ArrayList<>();
  /**
   * {@link #getFileExcludeFilter()}
   */
  private Collection<String> fileExcludeFilter = new ArrayList<>();
  /**
   * Get the collection of filters specified to include directories.
   * 
   * @return
   */
  @XmlElement(name = "dir-include-filter")
  public Collection<String> getDirIncludeFilter() {
    return dirIncludeFilter;
  }
  /**
   * see {@link #getDirIncludeFilter()}
   * 
   * @param dirIncludeFilter
   */
  public void setDirIncludeFilter(Collection<String> dirIncludeFilter) {
    this.dirIncludeFilter = dirIncludeFilter;
  }
  /**
   * Get the collection of filters configured to exclude the
   * directories/sub-directories.
   * 
   * @return
   */
  @XmlElement(name = "dir-exclude-filter")
  public Collection<String> getDirExcludeFilter() {
    return dirExcludeFilter;
  }
  /**
   * see {@link #getDirExcludeFilter()}
   * 
   * @param dirExcludeFilter
   */
  public void setDirExcludeFilter(Collection<String> dirExcludeFilter) {
    this.dirExcludeFilter = dirExcludeFilter;
  }
  /**
   * Get the collection of patterns configured to include the files in the
   * directories.
   * 
   * @return
   */
  @XmlElement(name = "file-include-filter")
  public Collection<String> getFileIncludeFilter() {
    return fileIncludeFilter;
  }
  /**
   * see {@link #getFileIncludeFilter()}
   * 
   * @param fileIncludeFilter
   */
  public void setFileIncludeFilter(Collection<String> fileIncludeFilter) {
    this.fileIncludeFilter = fileIncludeFilter;
  }
  /**
   * Get the collection of patterns configured to exclude files in directories
   * and sub-directories.
   * 
   * @return
   */
  @XmlElement(name = "file-exclude-filter")
  public Collection<String> getFileExcludeFilter() {
    return fileExcludeFilter;
  }
  /**
   * see {@link #getFileExcludeFilter()}
   * 
   * @param fileExcludeFilter
   */
  public void setFileExcludeFilter(Collection<String> fileExcludeFilter) {
    this.fileExcludeFilter = fileExcludeFilter;
  }

}
