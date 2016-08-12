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

    private Paint mTextPaint;
    /**
     * 当前是否展开
     */
    boolean isExpended;

    /**
     * view的宽高
     */
    int mWidth, mHeight;

    private float mTextHeight;
    private float mTextWidth;
    List<Node> nodes;


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
        if (nodes == null) {
            nodes = new ArrayList<>();
            mListDatas = new ArrayList<>();
        }
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

//
            for (int i = 0; i < mListDatas.size(); i++) {
                int width = (int) (i * mItemDistence);
                nodes.add(new Node(width, mHeight / 2, mListDatas.get(i), false));
            }
        }
    }

    private void calNodesDistance() {

        mItemDistence = mWidth / mListDatas.size();
        mTotalDistance = mItemDistence * mListDatas.size();
        Log.i(TAG, "calNodesDistance: View width is " + mWidth);
        Log.i(TAG, "calNodesDistance: itemDistance is " + mItemDistence);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

    }

    boolean is = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mListDatas == null) {
            return;
        }
        init();
        for (Node node : nodes) {
            drawText(canvas, node);
        }

    }

    private void drawText(Canvas canvas, Node node) {
        Log.i(TAG, "drawText: node--->" + node.getText() + " :x is" + node.getX());
        canvas.drawText(node.getText(), node.getX(), node.getY(), mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int col = (int) event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                col = (int) event.getX();
                isExpended = !isExpended;

                break;
        }

        return true;
    }

    /**
     * 节点信息类
     */
    private class Node {
        private float x;
        private float y;
        private String text;
        private boolean isSelect;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public Node() {
        }

        public Node(float x, float y, String text, boolean isSelect) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.isSelect = isSelect;
        }

    }

}
