package com.lee.cameratest.Camera;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lee.cameratest.Camera.bean.CameraBean;
import com.lee.cameratest.Camera.bean.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

/**
 * 相机连接
 *
 * @author lihe6
 */
public class CameraConnectionManager {
    private static final String TAG = "CameraConnectionManager";

    private static final String GET_CAMERA = "camera";
    public String REFRESH_TOKEN = "{\"token\":0,\"msg_id\":257}";
    private String TOKEN_TAG = "TOKEN";
    public String CONNECTION_CAMERA = "{\"token\":0,\"msg_id\":257}";
    public String TAKE_PHOTO = "{\"token\":" + TOKEN_TAG + ",\"msg_id\":769}";
    public String START_TAKE_VIDEO = "{\"token\":" + TOKEN_TAG + ",\"msg_id\":513}";
    public String STOP_TAKE_VIDEO = "{\"token\":" + TOKEN_TAG + ",\"msg_id\":514}";
    public String KEEP_ALIVE = "{\"token\":" + TOKEN_TAG + ",\"msg_id\":13}";

    private static final String RESPONSE_TAKE_PHOTO = "photo_taken";
    private static final String RESPONSE_START_VIDEO = "start_video_record";
    //    private static final String
    private String MSG_TAG;

    static CameraConnectionManager manager;
    Context mContext;
    CameraBean mCamera;

    CameraSearchResult cameraSearchResult;
    CameraConnectionChanged cameraConnectionChanged;
    boolean isConnection;
    //心跳包未回复时，重试次数
    int keepAliveTest = 0;
    long aliveTime = 0;

    private String broadcastIp;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    int token;
    private long beginTime;

    private CameraConnectionManager() {
    }

    public static CameraConnectionManager getInstance() {
        if (manager == null) {
            synchronized (CameraConnectionManager.class) {
                if (manager == null) {
                    manager = new CameraConnectionManager();
                }
            }
        }
        return manager;

    }

    /**
     * 相机连接
     *
     * @param context
     * @param cameraSearchResult
     */
    public void startSearchCamera(Context context, final CameraSearchResult cameraSearchResult) {
        this.mContext = context.getApplicationContext();
        broadcastIp = Utils.getBroadcastIp(mContext);
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            datagramSocket.setSoTimeout(200);
            byte[] data = "hello liveman".getBytes();
            InetAddress i = InetAddress.getByName(broadcastIp);
            final DatagramPacket packet = new DatagramPacket(data, 0, data.length, i, 17890);
            final DatagramSocket finalDatagramSocket = datagramSocket;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        finalDatagramSocket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                        cameraSearchResult.fail();
                    }
                    DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                    long receiveStart = System.currentTimeMillis();

                    //发现相机计数
                    int count = 0;

                    while (System.currentTimeMillis() - receiveStart < 1000) {
                        try {
                            finalDatagramSocket.receive(receivePacket);
                            String recMsg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                            Log.i(TAG, "scanLMInLAN: receivePacket--->" + recMsg);
                            CameraBean lmDevice ;
                            if(recMsg.contains("{")){
                               lmDevice = new Gson().fromJson(recMsg, CameraBean.class);
                            }else{
                                lmDevice = CameraBean.parseUdpMsg(recMsg);
                            }
                            if (lmDevice != null) {
                                Log.d(TAG, "scanLMInLAN: lmDevice--->" + lmDevice.toString());
                                cameraSearchResult.success(lmDevice);
                                break;
                            }
                            count++;
                            if (count > 30) {//超过30s未发现相机，即为失败
                                cameraSearchResult.fail();
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            cameraSearchResult.fail();
                        }
                    }
                }
            }).start();

        } catch (Exception e) {
            cameraSearchResult.fail();
        }
    }

    public void connectCamera(final CameraBean camera, final CameraOptionResponse cameraOptionResponse) {
        this.mCamera = camera;
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    if (socket == null) {
                        socket = new Socket();
                        socket.setSoTimeout(3000);
                        socket.connect(new InetSocketAddress(camera.getIp(), 7878), 5000);
                        socket.setKeepAlive(true);
                        inputStream = socket.getInputStream();
                        outputStream = socket.getOutputStream();
                    }

                    MSG_TAG = "\"msg_id\":257";
                    Response response = requestForResponse(CONNECTION_CAMERA);

                    if (judgeResponse(response, camera)) {
                        setToken(getToken(response));
                        beginTime = System.currentTimeMillis();
                        isConnection = true;
                        keepConnection();
                        //相机已连接
                        if(cameraConnectionChanged !=null) {
                            cameraConnectionChanged.connect();
                        }
                        cameraOptionResponse.success();
                    } else {
                        cameraOptionResponse.fail();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    socket = null;
                    cameraOptionResponse.fail();
                    Log.i(TAG, "run: exception " + e.toString());
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    cameraOptionResponse.fail();
                }
            }
        }.start();
    }

    private void keepConnection() throws IOException {
        if (isSocketAvaliable()) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(isConnection) {
                            keepAliveRequest();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.cancel();
                    }
                }
            };
            new Timer().schedule(task, 0, 20000L);
        }
    }

    String mCameraOption;

    public void controlCamera(final CameraBean camera, String camera_option, final CameraOptionResponse cameraOptionResponse) {
        mCameraOption = camera_option;
        this.mCamera = camera;
        camera_option = getOptionString(camera_option);
        final String finalCamera_option = camera_option;
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    if (socket == null) {
                        socket = new Socket();
                        socket.setSoTimeout(3000);
                        socket.connect(new InetSocketAddress(camera.getIp(), 7878), 5000);
                        socket.setKeepAlive(true);
                        inputStream = socket.getInputStream();
                        outputStream = socket.getOutputStream();
                    }
                    Response response = requestForResponse(finalCamera_option);

                    if (judgeResponse(response, camera)) {
                        cameraOptionResponse.success();
                    } else {
                        cameraOptionResponse.fail();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    socket = null;
                    cameraOptionResponse.fail();
                    Log.e(TAG, "run: exception is " + e.toString());
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Nullable
    private String getOptionString(String camera_option) {
        if ((!TextUtils.isEmpty(camera_option))
                && camera_option.contains(TOKEN_TAG)) {
            camera_option = camera_option.replace(TOKEN_TAG, getToken() + "");
            int index = camera_option.indexOf("msg_id");
            MSG_TAG = camera_option.substring(index);
        }
        return camera_option;
    }

    private boolean judgeResponse(Response response, CameraBean cameraBean) {
        boolean isOk = false;
        if (null != response) {
            if (response.getRval() == 0) {
                isOk = true;
            } else if (response.getMsg_id() == 7) {
                if (cameraBean.getType().equals(CameraBean.TYPE_C1)) {
                    if (RESPONSE_TAKE_PHOTO.equals(response.getParam()) || RESPONSE_START_VIDEO.equals(response.getParam())) {
                        isOk = true;
                    }
                } else if (cameraBean.getType().equals(CameraBean.TYPE_M1)) {
                    if (RESPONSE_START_VIDEO.equals(response.getType()) || RESPONSE_TAKE_PHOTO.equals(response.getType())) {
                        isOk = true;
                    }
                }
            }
        }
        return isOk;
    }

    public void setConnectionResult(CameraSearchResult cameraSearchResult) {
        this.cameraSearchResult = cameraSearchResult;
    }

    public void setCameraConnectionChanged(CameraConnectionChanged cameraConnectionChanged) {
        this.cameraConnectionChanged = cameraConnectionChanged;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public interface CameraSearchResult {
        void success(CameraBean camera);

        void fail();
    }

    public interface CameraOptionResponse {
        void success();

        void fail();
    }

    /**
     * 相机连接状态改变接口
     * <p></p>
     * 相机断开连接条件：
     * 发送心跳包--20s未回应;
     * 被其他app连接--response token:-4
     */
    public interface CameraConnectionChanged {
        void connect();

        void disconnect();
    }

    //=========================
    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    private int getToken(Response tokenResponse) {
        Object param = tokenResponse.getParam();
        if (param != null) {
            return Integer.parseInt(param.toString());
        } else {
            return token;
        }
    }

    /**
     * 获取相机连接时长
     * @return
     */
    public long getAliveTime() {
        return aliveTime;
    }

    private void keepAliveRequest() throws IOException {
        //发送心跳包
        writeInternal(getOptionString(KEEP_ALIVE));
        Log.i(TAG, "keepAliveRequest: --->start keepAlive");
        try {
            String readInternal = readInternal();
            if (TextUtils.isEmpty(readInternal)) {//心跳未回复？再次发送，3次未果后，即为失去连接
                if (keepAliveTest < 3) {
                    keepAliveRequest();
                    keepAliveTest++;
                    Log.i(TAG, "keepAliveRequest: disconnection--->reconnect:"+keepAliveTest);
                } else {
                    disconnect();
                }
            }else {
                keepAliveTest = 0;
                Response response = new Gson().fromJson(readInternal, Response.class);
                Log.i(TAG, "keepAliveRequest: response--->" + response.toString());
                if (response.getRval() == Response.INVALID_TOKEN) {
                    disconnect();
                } else {
                    isConnection = true;
                    aliveTime = System.currentTimeMillis() - beginTime;
                    Log.i(TAG, "keepAliveRequest: alive time--->" + aliveTime);
                }
            }
        } catch (Exception e) {
            disconnect();
            Log.i(TAG, "keepAliveRequest: error--->"+e.getMessage());
        }
    }

    private void disconnect() {
        isConnection = false;
        aliveTime = 0;
        Log.i(TAG, "keepAliveRequest: isConnection--->"+isConnection);
        if(cameraConnectionChanged !=null) {
            cameraConnectionChanged.disconnect();
        }
    }

    private Response requestForResponse(String strOption) throws IOException, TimeoutException {
        Log.i(TAG, "requestForResponse: --->" + strOption);
        writeInternal(strOption);
        //延迟2s获取消息
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String readInternal = readInternalComplete("");
        if (TextUtils.isEmpty(readInternal)) {
            return new Response();
        }
        Response response = null;
        try {
            response = new Gson().fromJson(readInternal, Response.class);
            if (response.getRval() == Response.INVALID_TOKEN
                    || response.getMsg_id() == 1793) {
                refreshToken();
                response = requestForResponse(getOptionString(mCameraOption));
            } else {
                response = new Gson().fromJson(readInternal, Response.class);
            }
            return response;
        } catch (Exception e) {
            return new Response();
        }
    }

    private void refreshToken() throws IOException, TimeoutException {
        Response tokenResponse = requestForResponse(REFRESH_TOKEN);
        if (tokenResponse != null && tokenResponse.getRval() == Response.SESSION_START_FAIL) {
            throw new IOException();
        }
        setToken(getToken(tokenResponse));
        isConnection = true;
        keepConnection();
    }

    private void writeInternal(String content) throws IOException {
        getOutputStream().write(content.getBytes());
    }

    private String readInternal() throws IOException {
        final long begin = System.currentTimeMillis();
        String response = "";
        for (; ; ) {
            if (!isSocketAvaliable()) {
                break;
            }
            long current = System.currentTimeMillis();
            if (current - begin > 5000L) {
                return response;
            }
            int available = getInputStream().available();
            if (available != 0) {
                byte[] buffer = new byte[available];
                getInputStream().read(buffer);
                response = new String(buffer);
                break;
            }
        }
        return response;
    }

    public boolean isSocketAvaliable() {
        return socket != null && socket.isConnected();
    }

    private String readInternalComplete(String lastRead) throws IOException, TimeoutException {
        String readInternal = readInternal();
        readInternal = lastRead + readInternal;
        Log.i(TAG, "readInternalComplete: all--->" + readInternal);
        if (readInternal.contains("}{")) {
            String[] strRes = readInternal.split("\\}");
            for (String res : strRes
                    ) {
                res += "}";
                if ((!TextUtils.isEmpty(MSG_TAG))
                        && res.contains(MSG_TAG)) {
                    readInternal = res;
                    break;
                } else if (res.contains(RESPONSE_TAKE_PHOTO) || res.contains(RESPONSE_START_VIDEO)) {
                    readInternal = res;
                }
            }
            if (readInternal.contains("}{")) {
                int i = readInternal.indexOf("}{");
                readInternal = readInternal.substring(0, i + 1);
            }
        }
        Log.i(TAG, "readInternalComplete: " + readInternal);
        return readInternal;
    }


}
