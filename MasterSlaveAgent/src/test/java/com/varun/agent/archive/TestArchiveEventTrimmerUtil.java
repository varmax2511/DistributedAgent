package com.varun.agent.archive;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.varun.agent.config.model.Pattern;
import com.varun.agent.crawler.FileCrawler;
import com.varun.agent.event.type.FileEvent;


public class TestArchiveEventTrimmerUtil {

  @Test
  public void testTrim() {
    
    final List<String> paths = new ArrayList<>();
    paths.add("src/main/");
    final Pattern pattern = new Pattern();

    FileCrawler fileCrawler = new FileCrawler(
        new FileCrawler.Config(paths, pattern));

    Collection<File> files = fileCrawler.execute();
    final Collection<FileEvent> fileEvents = new ArrayList<>();
    files.forEach(file -> {
      fileEvents.add(new FileEvent(file, System.currentTimeMillis()));
    });
    
    int totalSize = 0;
    for(FileEvent event : ArchiveEventTrimmerUtil.trim(fileEvents, 1024L)) {
      totalSize += event.getSize();
    }

    Assert.assertTrue(totalSize < 1024);
  }
}
