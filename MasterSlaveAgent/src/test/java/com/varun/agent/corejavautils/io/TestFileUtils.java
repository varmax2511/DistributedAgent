package com.varun.agent.corejavautils.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestFileUtils {

  @Test
  public void testCopyFileWithByteRange() throws IOException {

    final File srcFile = new File(
        "src//test//resources//AdminServer-diagnostic-1.log");
    final File destFile = new File(
        "src//test//resources//AdminServer-diagnostic-copy.log");

    int output = CoreFileUtils.copyFileWithByteRange(srcFile, destFile, 0,
        srcFile.length());

    Assert.assertTrue(output == 1);
    Assert.assertTrue(destFile.exists());

    FileUtils.deleteQuietly(destFile);
  }
}
