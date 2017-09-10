package com.varun.agent.config.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "directory")
public class Directory {

  private Collection<String> paths = new ArrayList<>();

  private Pattern pattern = new Pattern();

  /**
   * Get the collection of directory paths to be traversed
   * 
   * @return
   */
  @XmlElement(name = "path")
  public Collection<String> getPaths() {
    return paths;
  }
  /**
   * see {@link #getPath()}
   * 
   * @param path
   */
  public void setPaths(Collection<String> paths) {
    this.paths = paths;
  }
  /**
   * Get the {@link Pattern} configured with filters.
   * 
   * @return
   */
  @XmlElement(name = "pattern")
  public Pattern getPatterns() {
    return pattern;
  }
  /**
   * see {@link #getPattern()}
   * 
   * @param pattern
   */
  public void setPatterns(Pattern pattern) {
    this.pattern = pattern;
  }

}
