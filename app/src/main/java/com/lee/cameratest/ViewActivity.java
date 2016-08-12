package com.lee.cameratest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.lee.cameratest.view.HorizontalItemSelector;

/**
 * Created by lihe6 on 2016/8/11.
 */
public class ViewActivity extends Activity {

    HorizontalItemSelector selector;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view);

        selector = (HorizontalItemSelector) findViewById(R.id.selector);
        selector.setOnItemSelected(new HorizontalItemSelector.OnItemSelected() {
            @Override
            public void onSelected(int index, String text) {
                Toast.makeText(ViewActivity.this, "index:" + index, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
