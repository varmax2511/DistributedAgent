package com.varun.agent.config.model;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Model class for configuration of the agent.
 * 
 * @author varunjai
 *
 */
@XmlRootElement(name = "configuration")
@XmlType(propOrder = {"host", "agentProperties"})
public class Configuration implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * {@link #getHost()}
   */
  private Collection<Host> host;
  /**
   * {@link #getAgentProperties()}
   */
  private AgentProperties agentProperties = new AgentProperties();

  /**
   * Get the host and configuration associated for it.
   * 
   * @return
   */
  @XmlElement
  public Collection<Host> getHost() {
    return host;
  }
  /**
   * see {@link #getHost()}
   * 
   * @param host
   */
  public void setHost(Collection<Host> host) {
    this.host = host;
  }
  /**
   * get the agent properties
   * 
   * @return
   */
  @XmlElement(name = "agent-properties")
  public AgentProperties getAgentProperties() {
    return agentProperties;
  }
  /**
   * see {@link #getAgentProperties()}
   * 
   * @param agentProperties
   */
  public void setAgentProperties(AgentProperties agentProperties) {
    this.agentProperties = agentProperties;
  }

}
