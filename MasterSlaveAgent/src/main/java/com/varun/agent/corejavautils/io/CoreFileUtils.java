package com.varun.agent.corejavautils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility methods for File IO.
 * 
 * @author varunjai
 *
 */
public class CoreFileUtils {

  /**
   * Logger instance
   */
  public final static Logger logger = LogManager.getLogger(CoreFileUtils.class);

  /**
   * Utility method to check if the supplied file path exists.
   * 
   * @param configFilePath
   *          can be null.
   * @return true if file is valid and accessible, false otherwise.
   */
  public static boolean validateFile(final String configFilePath) {

    // check if any configuration file has been specified
    if (StringUtils.isBlank(configFilePath)) {
      return false;
    }

    // check file status
    final File agentConfigFile = new File(configFilePath);
    if (!agentConfigFile.exists() || !agentConfigFile.isFile()) {
      return false;
    }

    return true;
  }

  /**
   * Validate the supplied file path
   * 
   * @param filePath
   */
  public static boolean validateFilePath(String filePath) {

    if (StringUtils.isBlank(filePath)) {
      return false;
    }

    File file = new File(filePath);

    if (file.exists() && !file.isDirectory()) {
      return true;
    }

    return false;
  }

  /**
   * Validate the file path.
   * 
   * @param file
   *          can be null.
   * @return
   */
  public static boolean validateFile(final File file) {

    if (file == null) {
      return false;
    }

    if (file.exists() && !file.isDirectory()) {
      return true;
    }

    return false;
  }

  /**
   * Copy a file within a byte range using the NIO Channels.
   * 
   * @param srcFile
   *          source file from which data needs to be copied.
   * @param destFile
   *          destination file that will be created or replaced.
   * @param startByte
   *          start byte position
   * @param endByte
   *          end byte position
   * @throws IOException
   */
  public static int copyFileWithByteRange(File srcFile, File destFile,
      long startByte, long endByte) throws IOException {

    // validate
    if (srcFile == null || destFile == null || !srcFile.exists()
        || srcFile.isDirectory()) {
      throw new IllegalArgumentException("Invalid input parameters");
    }

    // if nothing required to copy
    if (endByte - startByte < 1) {
      logger.info("Nothing to copy in byte range: " + endByte + "-" + startByte
          + " for file: " + srcFile.getAbsolutePath());
      return 1;
    }

    FileChannel inChannel = null;
    FileChannel outChannel = null;
    FileInputStream inputStream = null;
    FileOutputStream outputStream = null;
    try {

      inputStream = new FileInputStream(srcFile);
      outputStream = new FileOutputStream(destFile);
      inChannel = inputStream.getChannel();
      outChannel = outputStream.getChannel();

      // copy data
      inChannel.transferTo(startByte, endByte - startByte, outChannel);

      return 1;
    } finally {
      if (inChannel != null) {
        try {
          inputStream.close();
          inChannel.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          logger.error(e);
        }
      }

      if (outChannel != null) {
        try {
          outputStream.close();
          outChannel.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          logger.error(e);
        }
      }
    } // finally

  }

}
