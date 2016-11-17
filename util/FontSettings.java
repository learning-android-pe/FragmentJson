package com.orimex.orimex.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Mi sesión on 15/11/2016.
 */
public class FontSettings {
    public static Typeface typeFace = null;
    public Context context;

    public FontSettings(Context context) {
        this.context = context;
    }

    /**
     * @brief methods for getting the typeface of a desired font. To change the font copy new
     * font to the fonts inside asset folder. Also make necessary changes here.
     * @return Typeface
     */
    public Typeface getThinFontSettings() {
        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_medium.ttf");
        return typeFace;
    }

}

