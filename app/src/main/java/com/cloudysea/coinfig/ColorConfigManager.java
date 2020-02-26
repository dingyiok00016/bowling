package com.cloudysea.coinfig;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/9/7.
 * @email lyj@yhcs.com
 * @detail
 */
public class ColorConfigManager {
    private List<ColorConfig> mConfigs = new ArrayList();
    private static final String WHITE_COLOR = "#FFFFFF";
    private static final String BLACK_COLOR = "#000000";
    private static ColorConfigManager mManager;
    private static final int ROUND_COUT = 6; // 一轮为6个
    public ColorConfig getColorConfig(int index){
        try{
            return mConfigs.get(index % ROUND_COUT);
        }catch (Exception e){
            return mConfigs.get(0);
        }
    }
    private  ColorConfigManager(){

    }

    public void init(){
        if(mConfigs.size() > 0){
            return;
        }
        // 1
        ColorConfig firstConfig = new ColorConfig();
        firstConfig.firstBallBg = "#712A2A";
        firstConfig.secondBallBg = "#712A2A";
        firstConfig.thirdBg = "#0F082A";
        firstConfig.turnColor = BLACK_COLOR;
        firstConfig.turnBg = WHITE_COLOR;
        firstConfig.nameBg = "#FFD8D8D8";
        firstConfig.scoreColor = "#F34B4B";
        firstConfig.scoreTotalColor = "#F34B4B";
        mConfigs.add(firstConfig);

        // 2
        ColorConfig secondConfig = new ColorConfig();
        secondConfig.firstBallBg = "#955C12";
        secondConfig.secondBallBg = "#955C12";
        secondConfig.thirdBg = "#0F082A";
        secondConfig.turnColor = BLACK_COLOR;
        secondConfig.nameBg = "#FFA5A5A5";
        secondConfig.scoreColor = "#FFE61F";
        secondConfig.scoreTotalColor = secondConfig.scoreColor;
        mConfigs.add(secondConfig);

        // 3
        ColorConfig thirdConfig = new ColorConfig();
        thirdConfig.firstBallBg = "#1F436A";
        thirdConfig.secondBallBg = "#1F436A";
        thirdConfig.turnBg = WHITE_COLOR;
        thirdConfig.thirdBg = "#0F082A";
        thirdConfig.turnColor = BLACK_COLOR;
        thirdConfig.nameBg = "#FF9ACCFF";
        thirdConfig.scoreColor = "#54B8F5";
        thirdConfig.scoreTotalColor = secondConfig.scoreColor;
        mConfigs.add(thirdConfig);

        // 4
        ColorConfig fourConfig = new ColorConfig();
        fourConfig.firstBallBg = "#415A1C";
        fourConfig.secondBallBg = "#415A1C";
        fourConfig.thirdBg = "#0F082A";
        fourConfig.turnBg = WHITE_COLOR;
        fourConfig.turnColor = BLACK_COLOR;
        fourConfig.nameBg = "#FF396BA7";
        fourConfig.scoreColor = "#B4FF1F";
        fourConfig.scoreTotalColor = secondConfig.scoreColor;
        mConfigs.add(fourConfig);

        // 5
        ColorConfig fiveConfig = new ColorConfig();
        fiveConfig.firstBallBg = "#5A1C4E";
        fiveConfig.secondBallBg = "#5A1C4E";
        fiveConfig.thirdBg = "#0F082A";
        fiveConfig.turnColor = BLACK_COLOR;
        fiveConfig.turnBg = WHITE_COLOR;
        fiveConfig.nameBg = "#FFFF0000";
        fiveConfig.scoreColor = "#FF1FE3";
        fiveConfig.scoreTotalColor = secondConfig.scoreColor;
        mConfigs.add(fiveConfig);

        // 6
        ColorConfig sixConfig = new ColorConfig();
        sixConfig.firstBallBg = "#5A251C";
        sixConfig.secondBallBg = "#5A251C";
        sixConfig.turnBg = WHITE_COLOR;
        sixConfig.thirdBg = "#0F082A";
        sixConfig.turnColor = BLACK_COLOR;
        sixConfig.nameBg = "#FFADD8E6";
        sixConfig.scoreColor = "#F3874B";
        sixConfig.scoreTotalColor = secondConfig.scoreColor;
        mConfigs.add(sixConfig);


    }

    public static ColorConfigManager getInstance(){
        if(mManager == null){
            mManager = new ColorConfigManager();
        }
        return mManager;
    }
}
