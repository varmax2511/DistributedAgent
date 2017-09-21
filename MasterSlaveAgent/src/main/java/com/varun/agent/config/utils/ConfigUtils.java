package com.varun.agent.config.utils;

import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.varun.agent.config.model.Configuration;
import com.varun.agent.corejavautils.io.CoreFileUtils;
import com.varun.agent.corejavautils.jaxb.JaxBMarshalUnMarshalUtil;

/**
 * Utility class to validate agent configuration
 * 
 * @author varunjai
 *
 */
public class ConfigUtils {

  public final static Logger logger = LogManager.getLogger(ConfigUtils.class);

  /**
   * validate the configuration file path set and tbe associated config file.
   * The method returns a {@link Configuration} instance if validated
   * successfully, else {@link Throwable}
   * 
   * @param configFilePath
   *          can be empty.
   * @return
   * @throws Throwable
   */
  public static Configuration validateConfiguration(final String configFilePath)
      throws Throwable {
    
    logger.info("Validating configuration");
    // validate agent configuration file
    if (!CoreFileUtils.validateFile(configFilePath)) {
      throw new IllegalArgumentException(
          "No valid agent configuration file path specified");
    }

    // get configuration
    final Configuration configuration = (Configuration) JaxBMarshalUnMarshalUtil
        .unMarshall(Configuration.class, configFilePath);

    // null check
    if (configuration == null) {
      throw new IllegalAccessException("Invalid configuration.");
    }

    // check hosts
    if (CollectionUtils.isEmpty(configuration.getHost())
        || configuration.getHost().size() == 0) {
      throw new IllegalArgumentException("Host configurations missing!");
    }

    // using streams to iterate over each host and validate
    configuration
        .setHost(configuration.getHost().stream()
            .filter(t -> StringUtils.isNotBlank(t.getName())
                && t.getDirectory() != null
                && CollectionUtils.isNotEmpty(t.getDirectory().getPaths()))
            .collect(Collectors.toList()));

    // check if after removal, no host configuration exists
    if (CollectionUtils.isEmpty(configuration.getHost())) {
      throw new IllegalArgumentException("No valid host configuration found!");
    }

    return configuration;
  }

}
