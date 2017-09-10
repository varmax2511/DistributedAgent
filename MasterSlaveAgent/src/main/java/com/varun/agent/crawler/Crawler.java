package com.varun.agent.crawler;

/**
 * A crawler is a program which can traverse directories, urls and even database
 * for fetching items.
 * 
 * @author varunjai
 *
 * @param <V>
 */
public interface Crawler<V> {

  V execute();
}
