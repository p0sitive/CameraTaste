package com.lee.cameratest.phoneCamera;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by chenshang on 16/4/13.
 */
public class PhonePreviewSimpleView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private boolean isFront = true;

    public PhonePreviewSimpleView(Context context, boolean isFront) {
        super(context);
        this.isFront = isFront;
        init();
    }

    public PhonePreviewSimpleView(Context context) {
        super(context);
        init();
    }

    public PhonePreviewSimpleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhonePreviewSimpleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null) {
            try {
                if (isFront) {
                    camera = CameraHelper.getDefaultFrontFacingCameraInstance();
                } else {
                    camera = CameraHelper.getDefaultBackFacingCameraInstance();
                }
                if (camera != null) {
                    CameraHelper.enableAutoFocus(camera);
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "无法连接到相机", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            try {
                camera.setDisplayOrientation(90);
                CameraHelper.setOptimalPreviewSize(camera, width, height, width > height);
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseCamera();
    }

    public void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void switchCamera(){
        isFront = !isFront;
        releaseCamera();
        openCamera();
    }

    public void openCamera(){
        if (camera == null) {
            try {

                if (isFront) {
                    camera = CameraHelper.getDefaultFrontFacingCameraInstance();
                } else {
                    camera = CameraHelper.getDefaultBackFacingCameraInstance();
                }
                if (camera != null) {
                    CameraHelper.enableAutoFocus(camera);
                }
                camera.setDisplayOrientation(90);
                CameraHelper.setOptimalPreviewSize(camera, getHolder().getSurfaceFrame().width(), getHolder().getSurfaceFrame().height(), getHolder().getSurfaceFrame().width() > getHolder().getSurfaceFrame().height());
                camera.setPreviewDisplay(getHolder());
                camera.startPreview();
            } catch (Exception e) {
            }
        }
    }

    public boolean isFront(){
        return isFront;
    }

}
