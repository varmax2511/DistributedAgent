package com.varun.agent.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.varun.agent.config.model.Pattern;
import com.varun.agent.core.RegexUtil;
import com.varun.agent.corejavautils.io.PathRegexFileFilter;

/**
 * Get a list of matching files to be picked
 *
 * @author varunjai
 *
 */
public class FileCrawler implements Crawler<Collection<File>> {

  private final static Logger logger = LogManager
      .getLogger(FileCrawler.class);
  private final Config config;
  public IOFileFilter fileFilters;
  public IOFileFilter dirFilters;

  /**
   * 
   * @param config
   *          !null
   */
  public FileCrawler(Config config) {
    Validate.notNull(config);
    this.config = config;

    /*
     * Configure file and directory filters
     */
    final IOFileFilter includeFileFilters = getUserProvidedFilters(
        config.getPattern().getFileIncludeFilter(), "*");
    final IOFileFilter excludeFileFilters = new NotFileFilter(
        getUserProvidedFilters(config.getPattern().getFileExcludeFilter(),
            null));
    final IOFileFilter includeDirFilters = getUserProvidedFilters(
        config.getPattern().getDirIncludeFilter(), "*");
    final IOFileFilter excludeDirFilters = new NotFileFilter(
        getUserProvidedFilters(config.getPattern().getDirExcludeFilter(),
            null));

    this.fileFilters = new AndFileFilter(includeFileFilters,
        excludeFileFilters);
    this.dirFilters = new AndFileFilter(includeDirFilters, excludeDirFilters);
  }

  /**
   * Visit the configured paths with the configured file and directory filters
   * and return a collection of files matching the criteria.
   * 
   * @return
   */
  @Override
  public Collection<File> execute() {

    final Collection<File> collectFiles = new ArrayList<>();
    
    if(CollectionUtils.isEmpty(config.getPaths())) {
      logger.warn("No paths provided to traverse");
      return collectFiles;
    }
    
    config.getPaths().stream().forEach(path -> {
      final File pathDir = new File(path);
      if (pathDir.exists() && pathDir.isDirectory()) {
        logger.info("Processing path:" + path);
        collectFiles
        .addAll(FileUtils.listFiles(pathDir, fileFilters, dirFilters));
      }else {
        logger.info("Skipping invalid path:" + path);
      }
    });
    return collectFiles;
  }

  /**
   * Utility method converts user provided list of wild card patterns to
   * OrFileFilter patterns, which can be used to filter the files.
   *
   * @param filters
   *          can be null or empty.
   * @param defaultPattern
   *          Specify a default pattern. This will be set if filters is empty.
   * @param regexPriorityList
   *          can be null
   * @param filtersAsString
   *          can be null
   * @return if filters is null and no default pattern is configured then an
   *         unconfigured filter instance is returned.
   */
  private OrFileFilter getUserProvidedFilters(Collection<String> filters,
      String defaultPattern) {

    final OrFileFilter userFilters = new OrFileFilter();
    if (CollectionUtils.isEmpty(filters)) {

      if (StringUtils.isNotBlank(defaultPattern)) {
        userFilters.addFileFilter(
            new PathRegexFileFilter(new PathRegexFileFilter.Config(
                RegexUtil.wildCardToRegEx(defaultPattern),
                java.util.regex.Pattern.CASE_INSENSITIVE)));
      }

      return userFilters;
    } // if

    for (final String filter : filters) {

      final String trimmedFilter = filter.trim();
      userFilters
          .addFileFilter(new PathRegexFileFilter(new PathRegexFileFilter.Config(
              RegexUtil.wildCardToRegEx(trimmedFilter),
              java.util.regex.Pattern.CASE_INSENSITIVE)));

    } // for

    return userFilters;
  }

  /**
   * Configuration class for {@link FileCrawler}
   *
   * @author varunjai
   *
   */
  public static class Config {

    private final Collection<String> paths;
    private final Pattern pattern;

    /**
     * 
     * @param paths
     * @param pattern
     */
    public Config(Collection<String> paths, Pattern pattern) {
      this.paths = paths;
      this.pattern = pattern;
    }

    /**
     * Get the Paths configures
     * 
     * @return
     */
    public Collection<String> getPaths() {
      return paths;
    }
    /**
     * Get the {@link Pattern} instance passed.
     * 
     * @return
     */
    public Pattern getPattern() {
      return pattern;
    }

  }
}
