package com.varun.agent.corejavautils.event;

import java.io.Serializable;

/**
 * Marker interface to identify an event
 * 
 * @author varunjai
 *
 */
public interface Event extends Serializable {

  
  public String getEventId();
  
  public EventType getEventType();

}
