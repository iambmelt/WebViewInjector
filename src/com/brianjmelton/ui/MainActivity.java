package com.brianjmelton.ui;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.brianjmelton.wvinjector.AssetsUtil;
import com.brianjmelton.wvinjector.R;
import com.brianjmelton.wvinjector.WebViewInjectorClient;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment())
          .commit();
    }
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {

    WebView webview;

    public PlaceholderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      webview = (WebView) inflater.inflate(R.layout.fragment_main, container, false);
      return webview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      webview.getSettings().setJavaScriptEnabled(true);
      webview.setWebChromeClient(new WebChromeClient());
      WebViewInjectorClient client = new WebViewInjectorClient();
      try {
        client.appendScripts(AssetsUtil.getAssetFile(getActivity(), "scripts", "mheEPub-js.js"));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      webview.setWebViewClient(client);
      webview.loadUrl("http://commons.apache.org/");

    }
  }
}
