package com.brianjmelton.wvinjector;

import java.io.File;
import java.io.IOException;

import android.webkit.WebViewClient;

public abstract class AbstractWebViewInjectorClient extends WebViewClient {

  /**
   * Appends the supplied Script files to the DOM after page has finished loading
   * 
   * @param files the scripts to append, may not be null
   * 
   * @throws IOException if the scripts could not be read
   */
  abstract void appendScripts(File... files) throws IOException;

  /**
   * Stop appending the supplied scripts to the DOM - takes affect after next page reload
   * 
   * @param files the scripts to no longer append or null if all scripts appending should cease. If
   *        {@code files} contains null values, they are ignored.
   */
  abstract void stopAppendingScripts(File... files);

}
