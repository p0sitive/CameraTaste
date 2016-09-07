package com.lee.cameratest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.cameratest.util.UIUtils;
import com.lee.cameratest.view.HorizontalItemSelector;
import com.lee.cameratest.view.HorizontalTextSelector;
import com.wefika.horizontalpicker.HorizontalPicker;

/**
 * Created by lihe6 on 2016/8/11.
 */
public class ViewActivity extends Activity {

    HorizontalItemSelector selector;
    TextView textView;
    HorizontalTextSelector textSelector;
    private View view;
    RelativeLayout container;
    private GestureDetector detector;
    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view);

//        selector = (HorizontalItemSelector) findViewById(R.id.selector);
//        selector.setOnItemSelected(new HorizontalItemSelector.OnItemSelected() {
//            @Override
//            public void onSelected(int index, String text) {
//                Toast.makeText(ViewActivity.this, "index:" + index, Toast.LENGTH_SHORT).show();
//            }
//        });

        textSelector = (HorizontalTextSelector) findViewById(R.id.aa);
        textSelector.setSelectText("index 3");
        textSelector.setTextItemCleck(new HorizontalTextSelector.OnItemTextSelected() {
            @Override
            public void onSelected(int index, String text) {
                UIUtils.showToast(ViewActivity.this, text);
            }
        });
//        textView = (TextView) findViewById(R.id.text);
//        Matrix matrix = textSelector.getMatrix();
//        matrix.postRotate(90);

        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.picker);

        picker.setSideItems(2);
        //picker.setValues(getResources().getStringArray(R.array.text2));

        view = findViewById(R.id.pic);

        gestureDetector = new GestureDetector(ViewActivity.this,new simpleGestureListener());
        container = (RelativeLayout) findViewById(R.id.container);
        container.setClickable(true);
        container.setFocusable(true);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                detector = new GestureDetector(v.getContext(),new myGestureDetector());
//                startActivity(new Intent(ViewActivity.this,View2Activity.class));
//                return mDetector.onTouchEvent(event);
//                return  detector.onTouchEvent(event);
                //return gestureDetector.onTouchEvent(event);
                return false;
            }
        });

    }


    private class simpleGestureListener extends
            GestureDetector.SimpleOnGestureListener {


        final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {


            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling left
                Log.i("MyGesture", "Fling left");
                Toast.makeText(ViewActivity.this, "Fling Left", Toast.LENGTH_SHORT).show();
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling right
                Log.i("MyGesture", "Fling right");
                Toast.makeText(ViewActivity.this, "Fling Right", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

    }

    private GestureDetector mDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
//            float distance = e2.getX() - e1.getX();
//            if (distance < 0) {
//                mLeftFilter = mCurrentFilter;
//                mRightFilter = getNextFilter(false);
//                mLeftPercent = 1.0f + distance / 600;
//                if (mLeftPercent < 0)
//                    mLeftPercent = 0;
//            } else {
//                mLeftFilter = getNextFilter(true);
//                mRightFilter = mCurrentFilter;
//                mLeftPercent = distance / 600;
//                if (mLeftPercent > 1.0f)
//                    mLeftPercent = 1.0f;
//            }
////				Log.i(TAG, String.format("action_move %d %d %.1f", mLeftFilter.ordinal(), mRightFilter.ordinal(), mLeftPercent));
//            //mGLView.setCameraFilter(mLeftFilter, mRightFilter, mLeftPercent);
//            mGLView.setCameraFilter(mRightFilter);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            if (mLeftPercent > 0.5) {
//                mCurrentFilter = mLeftFilter;
//                mLeftPercent = 1.0f;
//            } else {
//                mLeftFilter = mCurrentFilter = mRightFilter;
//                mLeftPercent = 1.0f;
//            }
////				mGLView.setCameraFilter(mLeftFilter, mRightFilter, mLeftPercent);
//            mGLView.setCameraFilter(mRightFilter);
            Toast.makeText(getApplicationContext(),"更新码率为:"+(motionEvent.getX()-motionEvent1.getX()),Toast.LENGTH_SHORT).show();
//            mGLView.updateBitrate(bitrateTest);
//            bitrateTest += 100000;
            return false;
        }
    });

    private GestureDetector mDetector1 = new GestureDetector(new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float destance = e2.getX() - e1.getX();
            if (Math.abs(destance / 100) > 1) {
                Toast.makeText(ViewActivity.this, "move +1", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });


    class myGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(Math.abs(e1.getX()-e2.getX())>200){
                Toast.makeText(ViewActivity.this, "hahha", Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
//                UIUtils.showAnimation(view);
//                UIUtils.showAnimation(textSelector);

            }
        }, 1000);

    }

    public void add(View view) {
        textView.setText(textView.getText().toString() + "AA");
    }
}
