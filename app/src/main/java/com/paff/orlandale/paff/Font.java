package com.paff.orlandale.paff;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.nio.file.Path;

/**
 * Created by ALESSANDROSERRAPICA on 22/11/2017.
 */

public class Font {
    AssetManager assetManager;

    public Font(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    public Typeface newFont(String p){
        return Typeface.createFromAsset(assetManager,p);
    }
}
