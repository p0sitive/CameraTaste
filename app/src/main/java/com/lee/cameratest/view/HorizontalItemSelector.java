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
    private int colorSelectText = Color.WHITE;
    private int colorDefultText = Color.GRAY;
    private int colorSelectSign = Color.WHITE;

    private float mItemDistence = 10;
    private float mTotalDistance;
    private float mPointSize = 20;

    /**
     * 展示数据
     */
    private List<String> mListDatas;

    private List<Node> nodes;

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
    private Paint mSelectPaint;
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
        nodes = new ArrayList<>();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setAlpha(100);
        mTextPaint.setTextSize(60);
        mTextPaint.setColor(colorDefultText);
        //取得字体的高度
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        mTextWidth = mTextPaint.measureText(mListDatas.get(0));

        //选中状态，左侧标示
        mSelectPaint = new Paint();
        mSelectPaint.setAlpha(100);
        mSelectPaint.setAntiAlias(true);
        mSelectPaint.setColor(Color.RED);
    }

    private void init() {
        if (mListDatas != null && mListDatas.size() > 0) {

            calNodesDistance();

            nodes.clear();
            for (int i = 0; i < mListDatas.size(); i++) {
                nodes.add(new Node(i, false, mItemDistence * i + mPointSize * 2, mHeight - mTextHeight / 4, mListDatas.get(i)));
            }
        }
    }

    private void calNodesDistance() {

        mItemDistence = (mWidth - mPointSize) / mListDatas.size();
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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "left:" + left);
        Log.d(TAG, "right:" + right);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mListDatas == null) {
            return;
        }
        init();
        drawPoint(canvas);
        // canvas.drawText(mListDatas.get(mSelectIndex), mPointSize * 2, mHeight - mTextHeight / 4, mTextPaint);
        if (isExpended) {
//            for (int i = 0; i < mListDatas.size(); i++) {
//                if (i != mSelectIndex) {
//                    float tempX = mItemDistence * i + mPointSize * 2;
//                    if (mSelectIndex > i) {
//                        tempX = mItemDistence * (i + 1) + mPointSize * 2;
//                    }
//                    canvas.drawText(mListDatas.get(i), tempX,
//                            mHeight - mTextHeight / 4, mTextPaint);
//                }
//            }
//            for (int i = 0; i < nodes.size(); i++) {
//                Node node = nodes.get(i);
//                if (!node.isSelect()) {
//                    canvas.drawText(node.getText(), node.getX(), node.getY(), mTextPaint);
//                }
//            }
        }
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (isExpended) {
                canvas.drawText(node.getText(), node.getX(), node.getY(), mTextPaint);
            } else {
                if (node.isSelect()) {
                    canvas.drawText(node.getText(), node.getX(), node.getY(), mTextPaint);
                }
            }
        }
    }

    /**
     * 绘制选中的
     */
    void drawPoint(Canvas canvas) {
        canvas.drawCircle(mPointSize, mHeight / 2, 15, mSelectPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int col = (int) (event.getX() - mPointSize * 2);
                if (isExpended) {
//                    mSelectIndex = getPositionItemFromX(col);
//                    if (mListDatas != null) {
//                        if (mSelectIndex == mListDatas.size())
//                            mSelectIndex = mListDatas.size() - 1;
//                        mSelectText = mListDatas.get(mSelectIndex);
//                    }
                    Node n = getSelectNode(col);
                    refreshNodes(n);
                    if (onItemSelected != null) {
                        onItemSelected.onSelected(n.getIndex(), n.getText());
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

    private void refreshNodes(Node n) {
        int index = n.getIndex();
        n.select = true;
        n.setX(mPointSize * 2);
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            node.setSelect(false);
            if (index != i) {
                if (i < index) {
                    node.setX(mItemDistence * (i + 1) + mPointSize * 2);
                }
            }
            nodes.remove(i);
            nodes.add(i, node);
        }
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
        Log.d(TAG, "x:" + x);
        int index = Math.round(x / (mItemDistence));
        if (index < mSelectIndex) {
            index -= 1;
        }
        return index;
    }

    Node getSelectNode(int x) {
        for (Node node : nodes) {
            if (x >= node.getX() && x < node.getX() + mItemDistence) {
                return node;
            }
        }
        return null;
    }

    public interface OnItemSelected {
        void onSelected(int index, String text);
    }

    private class Node {
        private int index;
        private boolean select;

        private float x;
        private float y;
        private String text;

        public Node(int index, boolean select, float x, float y, String text) {
            this.index = index;
            this.select = select;
            this.x = x;
            this.y = y;
            this.text = text;
        }

        public Node() {

        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

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
    }
}
