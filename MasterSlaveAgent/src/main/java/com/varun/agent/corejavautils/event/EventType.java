package com.varun.agent.corejavautils.event;

import java.util.EnumSet;

public enum EventType {
  /*
   * Agent Events
   */
  AGENT_FILE, AGENT_DB, AGENT_STREAM;
  /**
   * All agent events
   */
  public static EnumSet<EventType> agentEvents = EnumSet.of(AGENT_FILE,
      AGENT_DB, AGENT_STREAM);

}
