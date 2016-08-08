package com.lee.cameratest.Camera;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.lee.cameratest.Camera.bean.CameraBean;
import com.lesports.geneliveman.wifi.LMLANScanner;
import com.lesports.geneliveman.wifi.bean.LMDevice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * 相机连接
 *
 * @author lihe6
 */
public class CameraConnectionManager2 {
    private static final String TAG = "CameraConnectionManager";

    private static final String GET_CAMERA="camera";


    static CameraConnectionManager2 manager;

    Context mContext;

    CameraSearchResult cameraSearchResult;

    private LMLANScanner scanner;

    private CameraConnectionManager2() {
    }

    public static CameraConnectionManager2 getInstance() {
        if (manager == null) {
            synchronized (CameraConnectionManager2.class) {
                if (manager == null) {
                    manager = new CameraConnectionManager2();
                }
            }
        }
        return manager;

    }

    /**
     * 相机连接
     * @param context
     * @param cameraSearchResult
     */
    public void startSearchCamera(Context context, final CameraSearchResult cameraSearchResult) {
        scanner = new LMLANScanner(context, new LMLANScanner.Listener() {
            @Override
            public void onScanned(List<LMDevice> list) {
                if(list.size()>0){
                    cameraSearchResult.success(list.get(0));
                }
            }
        });
        scanner.setAutoConnect(true);
        scanner.startScan();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scanner != null) {
                    scanner.stopScan();
                    scanner = null;
                    cameraSearchResult.fail();
                }
            }
        }, 20 * 1000);
    }


    public void setConnectionResult(CameraSearchResult cameraSearchResult) {
        this.cameraSearchResult = cameraSearchResult;
    }
    public interface CameraSearchResult {
        void success(LMDevice camera);

        void fail();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x001:
                    break;
            }
        }
    };
}
