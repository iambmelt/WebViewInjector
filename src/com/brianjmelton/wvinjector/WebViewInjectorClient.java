package com.brianjmelton.wvinjector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

public class WebViewInjectorClient extends AbstractWebViewInjectorClient {

  private static final String LOG_TAG = "WebViewInjectorClient";

  protected List<File> scripts;

  private static class JsWords {

    static final String CREATE_ELEMENT = "var script_tag = document.createElement('script');";

    static final String PATH_TOKEN = "<path>";

    static final String SCRIPT_SOURCE = "script_tag.src = '" + PATH_TOKEN + "';";

    static final String APPEND_ELEMENT = "document.body.appendChild(script_tag);";

    static final String JS_PREFIX = "javascript:";

    static final String SCRIPT_TEMPLATE = CREATE_ELEMENT + SCRIPT_SOURCE + APPEND_ELEMENT;
  }

  @Override
  public synchronized void appendScripts(File... files) {
    if (null == files) {
      throw new IllegalArgumentException("Scripts cannot be null");
    }

    for (File file : files) {
      if (null == file) {
        throw new IllegalArgumentException("Script array contains null values");
      }

      if (!file.exists()) {
        throw new IllegalArgumentException("Script [" + file.toString() + "] does not exist");
      }
    }
    if (null == scripts) {
      scripts = new ArrayList<File>(Arrays.asList(files));
    } else {
      scripts.addAll(Arrays.asList(files));
    }
  }

  @Override
  public synchronized void stopAppendingScripts(File... files) {
    if (null == files) {
      // clear all scripts
      scripts = null;
      return;
    }

    if (null != scripts) {
      for (File file : files) {
        scripts.remove(file);
      }
    }
  }

  protected static void doAppendScript(final WebView view, File script) {
    if (null == view) {
      throw new IllegalArgumentException("Webview cannot be null");
    }

    if (null == script) {
      throw new IllegalArgumentException("Script cannot be null");
    }

    if (!script.exists()) {
      throw new IllegalArgumentException("Script does not exist");
    }

    final String js =
        JsWords.JS_PREFIX
            + JsWords.SCRIPT_TEMPLATE.replace(JsWords.PATH_TOKEN, script.getAbsolutePath());

    Runnable loadAction = new Runnable() {

      @Override
      public void run() {
        view.loadUrl(js);
      }

    };

    boolean posted = view.post(loadAction);

    if (!posted) {
      Log.e(LOG_TAG, "Could not post Runnable to UI thread (Looper is exiting)");
    }
  }

  @Override
  public synchronized void onPageFinished(WebView view, String url) {
    super.onPageFinished(view, url);

    if (null == scripts) {
      return;
    }

    for (File script : scripts) {
      if (script.exists()) {
        Log.i(LOG_TAG, "appending script: " + script);
        doAppendScript(view, script);
      }
    }
  }

  @Override
  public synchronized WebResourceResponse shouldInterceptRequest(WebView view, String url) {
    // validate each outgoing call - if it's looking for something local, we
    // can service that request here

    if (null == scripts) {
      return super.shouldInterceptRequest(view, url);
    }

    for (File file : scripts) {
      if (url.contains(file.getAbsolutePath())) {
        try {
          return new WebResourceResponse("text/javascript", "UTF-8", new FileInputStream(file));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }
    }

    return super.shouldInterceptRequest(view, url);
  }
}
