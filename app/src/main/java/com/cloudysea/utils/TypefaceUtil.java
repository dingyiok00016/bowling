package com.cloudysea.utils;

/**
 * @author roof 2019/9/15.
 * @email lyj@yhcs.com
 * @detail
 */
import android.graphics.Typeface;

import com.cloudysea.BowlingApplication;

public class TypefaceUtil {

    private TypefaceUtil(){

    }

    private static Typeface typefaceStyleOne;
    private static Typeface typefaceStyleTwo;

    public static Typeface getStyleOneInstance(){
        if(typefaceStyleOne == null){
            typefaceStyleOne = Typeface.createFromAsset(BowlingApplication.getContext().getAssets(), "font/ball_use_style_one.ttf");
        }
       return typefaceStyleOne;
    }


    public static Typeface getStyleTwoInstance(){
        if(typefaceStyleTwo == null){
            typefaceStyleTwo = Typeface.createFromAsset(BowlingApplication.getContext().getAssets(), "font/ball_use_style_two.ttf");
        }
        return typefaceStyleTwo;
    }

}
