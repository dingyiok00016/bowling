package com.cloudysea.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cloudysea.R;

/**
 * @author roof 2020/1/8.
 * @email lyj@yhcs.com
 * @detail
 */
public class StdVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_video);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.video_container,new StdVideoFragment())
                .commit();
    }
}
