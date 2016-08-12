package com.lee.cameratest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihe6 on 2016/8/11.
 */
public class HorizontalItemSelector extends View {

    private static final String TAG = "HorizontalItemSelector";

    Context context;

    /**
     * 文字颜色
     */
    public static final int colorSelectText = Color.WHITE;
    public static final int colorDefultText = Color.GRAY;

    private float mItemDistence = 10;
    private float mTotalDistance;

    /**
     * 展示数据
     */
    private List<String> mListDatas;

    CharSequence[] values;

    /**
     * 选中的条目
     */
    int mSelectIndex;
    /**
     * 选中的字符串
     */
    String mSelectText;
    /**
     * 选中的字符串
     */
    CharSequence selSequence;

    private Paint mTextPaint;
    /**
     * 当前是否展开
     */
    boolean isExpended = false;

    /**
     * view的宽高
     */
    int mWidth, mHeight;

    private float mTextHeight, mTextWidth;
    private float mTopSpace, mBottomSpace;

    OnItemSelected onItemSelected;

    public HorizontalItemSelector(Context context) {
        this(context, null);
    }

    public HorizontalItemSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalItemSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initData();
        //init();
    }

    void initData() {
        mListDatas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mListDatas.add("index:" + i);
        }
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setAlpha(100);
        mTextPaint.setTextSize(60);
        mTextPaint.setColor(colorDefultText);
        //取得字体的高度
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        mTextWidth = mTextPaint.measureText(mListDatas.get(0));
    }

    private void init() {
        if (mListDatas != null && mListDatas.size() > 0) {

            calNodesDistance();

        }
    }

    private void calNodesDistance() {

        mItemDistence = mWidth / mListDatas.size();
        mTotalDistance = mItemDistence * mListDatas.size();
        Log.i(TAG, "calNodesDistance: View width is " + mWidth);
        Log.i(TAG, "calNodesDistance: itemDistance is " + mItemDistence);
    }

    public void setOnItemSelected(OnItemSelected onItemSelected) {
        this.onItemSelected = onItemSelected;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = MeasureSpec.getSize(widthMeasureSpec);
        mWidth = getSuggestedMinimumWidth();
        switch (measureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                mWidth = measureSize;
                break;
            default:
                break;
        }
        Log.i(TAG, "measureWidth: --->" + mWidth);
        return mWidth;
    }

    private int measureHeight(int heightMeasure) {
        int measureMode = MeasureSpec.getMode(heightMeasure);
        int measureSize = MeasureSpec.getSize(heightMeasure);
        mHeight = (int) (mBottomSpace + mTopSpace * 2 + mTextHeight);
        switch (measureMode) {
            case MeasureSpec.EXACTLY:
                mHeight = Math.max(mHeight, measureSize);
                break;
            case MeasureSpec.AT_MOST:
                mHeight = Math.min(mHeight, measureSize);
                break;
            default:
                break;
        }
        Log.i(TAG, "measureHeight: --->" + mHeight);
        return mHeight;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mListDatas == null) {
            return;
        }
        init();
        if (isExpended) {
            for (int i = 0; i < mListDatas.size(); i++) {
                canvas.drawText(mListDatas.get(i), mItemDistence * i, mHeight, mTextPaint);
            }
        } else {
            canvas.drawText(mListDatas.get(mSelectIndex), 0, mHeight, mTextPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int col = (int) event.getX();
                if (isExpended) {
                    mSelectIndex = getPositionItemFromX(col);
                    if (mListDatas != null) {
                        if (mSelectIndex == mListDatas.size())
                            mSelectIndex = mListDatas.size() - 1;
                        mSelectText = mListDatas.get(mSelectIndex);
                    }
                    if (onItemSelected != null) {
                        onItemSelected.onSelected(mSelectIndex, mSelectText);
                    }
                } else {
                    if (col > mItemDistence) {
                        return true;
                    }
                }
                isExpended = !isExpended;
                setMeasuredDimension(isExpended ? (int) mTotalDistance : (int) mItemDistence, mHeight);
                invalidate();
                break;
        }

        return true;
    }

    public int getmSelectIndex() {
        return mSelectIndex;
    }

    public void setmSelectIndex(int mSelectIndex) {
        this.mSelectIndex = mSelectIndex;
        if (mListDatas != null && mListDatas.size() > mSelectIndex) {
            setmSelectText(mListDatas.get(mSelectIndex));
        }
        invalidate();
    }

    public String getmSelectText() {
        return mSelectText;
    }

    public void setmSelectText(String mSelectText) {
        this.mSelectText = mSelectText;
        if (mListDatas != null) {
            if (mListDatas.contains(mSelectText)) {
                setmSelectIndex(mListDatas.indexOf(mSelectText));
            }
        }
        invalidate();
    }

    int getPositionItemFromX(int x) {
        return Math.round(x / (mItemDistence));
    }


    public interface OnItemSelected {
        void onSelected(int index, String text);
    }

}
