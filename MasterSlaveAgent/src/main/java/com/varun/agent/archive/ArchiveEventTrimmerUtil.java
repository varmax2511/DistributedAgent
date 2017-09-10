package com.varun.agent.archive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.varun.agent.event.type.FileEvent;

public class ArchiveEventTrimmerUtil {

  /**
   * Returns a collection of {@link FileEvent} such that the the total size of
   * these File events is less than the upperbound passed.
   * 
   * @param events
   * @return
   */
  public static List<FileEvent> trim(Collection<FileEvent> events,
      long maxSize) {

    // validate
    if (CollectionUtils.isEmpty(events)) {
      return new ArrayList<>();
    }

    int totalSize = 0;
    final List<FileEvent> trimmedEvents = new ArrayList<>();
    for (final FileEvent event : events) {
      if (totalSize + event.getSize() > maxSize) {
        continue;
      }

      totalSize += event.getSize();
      trimmedEvents.add(event);
    } // for

    return trimmedEvents;
  }

}
