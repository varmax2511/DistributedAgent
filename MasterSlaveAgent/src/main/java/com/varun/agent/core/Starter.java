package com.varun.agent.core;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.varun.agent.archive.LocalArchiveBuilder;
import com.varun.agent.config.model.Configuration;
import com.varun.agent.config.utils.ConfigUtils;
/**
 * The starter class will first read a properties file or an xml file containing
 * the properties for the agent. The properties file will detail what nodes to
 * run the agent on, single-node , multi-node name of product, name of suite,
 * directories where agent needs to pick up files, file patterns to include,
 * file patterns to exclude, directory patterns to include, directory patterns
 * to exclude. agent archive size such that x specified by user and 100, which
 * ever is less.
 * 
 * submitter name, submitter email etc
 * 
 * @author varunjai
 *
 */
public class Starter {

  public final static Logger logger = LogManager.getLogger(Starter.class);

  public static void main(String[] args) throws Throwable {

    final String configFilePath = System
        .getProperty(AgentConstants.VOOGLE_AGENT_CONFIG_FILE_PROPERTY);

    Configuration configuration = null;
    try {
      try {
        configuration = ConfigUtils.validateConfiguration(configFilePath);
      } catch (Throwable t) {
        logger.error(
            "FATAL: Exception occurred in reading agent configuration : ", t);
        throw t;
      }

      try {
        FileUtils.forceMkdir(
            new File(configuration.getAgentProperties().getDestinationPath()));

      } catch (Throwable t) {
        logger.error("Unable to create destination directory", t);
        throw t;
      }

      new LocalArchiveBuilder(new LocalArchiveBuilder.Config(
          configuration.getHost().iterator().next(),
          configuration.getAgentProperties())).execute();
    } catch (Throwable t) {
      logger.error("FATAL: exiting 0", t);
    }
  }

}
