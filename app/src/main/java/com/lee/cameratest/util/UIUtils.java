package com.lee.cameratest.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lihe6 on 2016/8/15.
 */
public class UIUtils {
    private static Toast toast;

    public static void showToast(Context context, String text) {
        if (toast != null) {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * Drawable着色
     *
     * @param drawable
     * @param colors
     * @return
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static void setTextAndDrawable(TextView v, int resDraw, int resStr) {
        Resources resources = v.getResources();
        if (resDraw != 0) {
            Drawable drawable = resources.getDrawable(resDraw);
            /// 这一步必须要做,否则不会显示.
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            v.setCompoundDrawables(drawable, null, null, null);
        }
        v.setText(resStr);
    }

    public static void showAnimation(View mView) {
        final float centerX = mView.getWidth() / 2.0f;
        final float centerY = mView.getHeight() / 2.0f;
        //这个是设置需要旋转的角度，我设置的是180度
        RotateAnimation rotateAnimation = new RotateAnimation(0, 90, centerX,
                centerY);
        //这个是设置通话时间的
        rotateAnimation.setDuration(500);
        rotateAnimation.setFillAfter(true);
        mView.startAnimation(rotateAnimation);
    }
}
