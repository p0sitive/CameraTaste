package com.lee.cameratest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.cameratest.util.UIUtils;
import com.lee.cameratest.view.HorizontalItemSelector;
import com.lee.cameratest.view.HorizontalTextSelector;

/**
 * Created by lihe6 on 2016/8/11.
 */
public class ViewActivity extends Activity {

    HorizontalItemSelector selector;
    TextView textView;
    HorizontalTextSelector textSelector;
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

        textSelector = (HorizontalTextSelector) findViewById(R.id.aa);
        textSelector.setSelectText("index 3");
        textSelector.setTextItemCleck(new HorizontalTextSelector.OnItemTextSelected() {
            @Override
            public void onSelected(int index, String text) {
                UIUtils.showToast(textView.getContext(),text);
            }
        });
        textView = (TextView) findViewById(R.id.text);
    }

    public void add(View view) {
        textView.setText(textView.getText().toString()+"AA");
    }
}
