package com.lee.cameratest.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.cameratest.R;
import com.lee.cameratest.util.UIUtils;

/**
 * Created by lihe6 on 2016/8/15.
 */
public class HorizontalTextSelector extends LinearLayout {
    private static final String TAG = "HorizontalTextSelector";

    CharSequence[] mDatas;

    boolean isExpend = false;
    private LinearLayout container;
    String mSelectText;


    /**
     * 文字颜色
     */
    private int textSelectColor = Color.WHITE;
    private int textDefultColor = Color.GRAY;
    private int colorSelectSign = Color.WHITE;

    OnItemTextSelected onItemTextSelected;
    private int mSelectIndex;

    public HorizontalTextSelector(Context context) {
        this(context, null);
    }

    public HorizontalTextSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalTextSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_hrizontal_selector, this, true);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HorizontalItemSelector,
                defStyleAttr, 0);
        textDefultColor = array.getColor(R.styleable.HorizontalItemSelector_android_textColor, Color.GRAY);
        textSelectColor = array.getColor(R.styleable.HorizontalItemSelector_selectedTextColor, Color.WHITE);
        colorSelectSign = array.getColor(R.styleable.HorizontalItemSelector_selectedSignColor, Color.WHITE);
        mDatas = array.getTextArray(R.styleable.HorizontalItemSelector_values);
        array.recycle();
        init();
    }

    private void init() {
        container = (LinearLayout) this.getChildAt(0);
        for (int i = 0; i < container.getChildCount(); i++) {
            if (container.getChildAt(i) instanceof TextView) {
                container.getChildAt(i).setOnClickListener(textItemCleck);
            }
        }

        refreshView();
    }

    public void setTextItemCleck(OnItemTextSelected onItemTextSelected) {
        this.onItemTextSelected = onItemTextSelected;
    }

    OnClickListener textItemCleck = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Message msg = new Message();
            msg.obj = ((TextView) v).getText();
            handler.sendMessage(msg);
            Log.i(TAG, "onClick: --->" + ((TextView) v).getText());
        }
    };

    public interface OnItemTextSelected {
        void onSelected(int index, String text);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isExpend) {
                mSelectText = msg.obj.toString();
                if (mDatas != null) {
                    for (int i = 0; i < mDatas.length; i++) {
                        String temp = mDatas[i].toString();
                        if (temp.equals(mSelectText)) {
                            mSelectIndex = i;
                            break;
                        }
                    }
                }
                if (onItemTextSelected != null) {
                    onItemTextSelected.onSelected(mSelectIndex, mSelectText);
                }
            }
            Log.d(TAG, "mSelectIndex:" + mSelectIndex);
            Log.d(TAG, "mSelectText:" + mSelectText);
            isExpend = !isExpend;
            refreshView();
        }


    };

    private void refreshView() {
        TextView first = (TextView) container.getChildAt(0);
        first.setTextColor(isExpend ? textSelectColor : textDefultColor);
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.left_point_item_selector);
        drawable = UIUtils.tintDrawable(drawable, ColorStateList.valueOf(isExpend ? colorSelectSign : textDefultColor));
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        first.setCompoundDrawables(drawable, null, null, null);
        first.setText(mDatas[mSelectIndex]);
        Log.i(TAG, "refreshView: --->" + mDatas[mSelectIndex]);
        boolean isSelect = false;
        for (int i = 1; i < container.getChildCount(); i++) {
            if (i > mDatas.length - 1) {
                container.getChildAt(i).setVisibility(GONE);
                continue;
            }
            TextView textView = (TextView) container.getChildAt(i);
            if ((i - 1) == mSelectIndex) {
                isSelect = true;
            }
            textView.setTextColor(textDefultColor);
            textView.setText(mDatas[isSelect ? i : i - 1]);
            container.getChildAt(i).setVisibility(isExpend ? VISIBLE : GONE);
        }
    }

    public void setSelectText(String text) {
        mSelectText = text;
        boolean tag = false;
        for (int i = 0; i < mDatas.length; i++) {
            if (text.equals(mDatas[i])) {
                mSelectIndex = i;
                tag = true;
                break;
            }
        }
        if (!tag)
            throw new IllegalArgumentException(text + " is not belong to arrays!");
        refreshView();
    }

    public void setSelectIndex(int index) {
        if (index > mDatas.length) {
            throw new IllegalArgumentException("arrays length is " + mDatas.length);
        }
        mSelectIndex = index;
        mSelectText = mDatas[mSelectIndex].toString();
        refreshView();
    }

    public void setDataArray(String[] array) {
        mDatas = array;
    }
}
