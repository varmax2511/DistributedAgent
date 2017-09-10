package com.varun.agent.corejavautils.event;

import java.util.concurrent.Callable;

/**
 * <pre>
 * Interface for an {@link EventProcessor}
 * 
 * An event processor is responsible to process events. Events can be generated 
 * across the platform by multiple projects and with unique purposes.
 * Example: Agent File Event - which will encapsulate information about file captures
 * by agent.
 * 
 * Similarly we can have multiple event processors for each event. The idea is
 * to define an interface for Event processor which can establish a structure for 
 * event processors.
 * 
 * Here we are defining a method #process() to process a collection of events, such
 * that under each event processor the execution will occur in the process method.
 * Q. How can we be sure that every processor would like to have a collection
 *   of events?
 * Ans. We expect each processor to process multiple events of same type.
 * 
 * Q. The process method has no return type, what if we want to return some value.
 * Ans. The processor should be changed to return int value such that process
 *     completed successfully marked by 1 and for failed execution 0 is returned.
 *     Process methods are not expected to return results. for special use cases
 *     individual processors can set some arbitrary variable post execution
 * 
 *
 * 
 * 
 * 
 * </pre>
 * 
 * @author varunjai
 *
 */
public interface EventProcessor<V>
    extends
      Callable<V> {
  /**
   * 
   * @param events
   *          can be empty.
   * @return 1 for successful processing and 0 for failed execution
   */
  // public int process(Collection<? extends Event> events);
}
