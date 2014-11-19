package com.brianjmelton.wvinjector;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import android.content.Context;
import android.util.Log;

public class AssetsUtil {

  private static final String LOG_TAG = "AssetsUtil";

  public static File getAssetFile(Context context, String path, String which) throws IOException {
    String[] assets = context.getAssets().list(path);
    Log.d(LOG_TAG, "Found " + assets.length + " assets for path: " + path);
    for (String asset : assets) {
      Log.d(LOG_TAG, asset);
      if (which.equals(asset)) {
        path = path + File.separator + which;
        break;
      }
    }
    File out = new File(context.getFilesDir(), which);
    // copy which to the files dir for access
    FileUtils.copyInputStreamToFile(context.getAssets().open(path), out);
    return out;
  }

}
