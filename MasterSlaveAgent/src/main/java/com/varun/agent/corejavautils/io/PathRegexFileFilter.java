package com.varun.agent.corejavautils.io;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PathRegexFileFilter extends AbstractFileFilter
    implements
      Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private final static Logger logger = LogManager
      .getLogger(PathRegexFileFilter.class);

  /**
   * configuration instance
   */
  private final Config config;
  /**
   * 
   * @param config
   *          !null
   */
  public PathRegexFileFilter(final Config config) {
    Validate.notNull(config);
    this.config = config;
  }

  @Override
  public boolean accept(File dir, String name) {
    // matching based on absolute path
    final boolean absPathBased = config.getMatcher()
        .reset(dir.getAbsolutePath() + SystemUtils.FILE_SEPARATOR + name)
        .matches();

    // matching based on canonical path
    boolean canonPathbased = true;
    try {
      canonPathbased = config.getMatcher()
          .reset(dir.getCanonicalPath() + SystemUtils.FILE_SEPARATOR + name)
          .matches();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return absPathBased || canonPathbased;
  }

  /**
   * Configuration class
   * 
   * @author varunjai
   *
   */
  public static class Config {
    /**
     * {@link #getMatcher()}
     */
    private final Matcher matcher;

    /**
     * Construct a new regular expression filter.
     * 
     * @param pattern
     *          regular string expression to match
     * @throws IllegalArgumentException
     *           if the pattern is null
     */
    public Config(String pattern) {
      if (pattern == null) {
        throw new IllegalArgumentException("Pattern is missing");
      }

      this.matcher = Pattern.compile(pattern).matcher(StringUtils.EMPTY);
    }

    /**
     * Construct a new regular expression filter with the specified flags.
     * 
     * @param pattern
     *          regular string expression to match
     * @param flags
     *          pattern flags - e.g. {@link Pattern#CASE_INSENSITIVE}
     * @throws IllegalArgumentException
     *           if the pattern is null
     */
    public Config(String pattern, int flags) {
      if (pattern == null) {
        throw new IllegalArgumentException("Pattern is missing");
      }

      this.matcher = Pattern.compile(pattern, flags).matcher(StringUtils.EMPTY);
    }
    /**
     * {@link Matcher} based on the supplied patterns
     * 
     * @return
     */
    public Matcher getMatcher() {
      return matcher;
    }

  }
}
