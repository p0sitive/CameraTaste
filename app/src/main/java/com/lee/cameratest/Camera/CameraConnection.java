package com.lee.cameratest.Camera;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lee.cameratest.Camera.bean.Request;
import com.lee.cameratest.Camera.bean.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

/**
 * Created by lihe6 on 2016/6/16.
 */
public class CameraConnection {

    private String TOKEN_TAG = "TOKEN";

    String CONNCTION_CAMERA = "{\"token\":0,\"msg_id\":257}";
    String TAKE_PHOTO = "{\"token\":" + TOKEN_TAG + ",\"msg_id\":301}";
    String START_TAKE_VIDEO = "{\"token\":" + TOKEN_TAG + ",\"msg_id\":201}";
    String STOP_TAKE_VIDEO = "{\"token\":" + TOKEN_TAG + ",\"msg_id\":202}";


    public static int AMBA_START_SESSION = 0x0101;
    String host;
    String mac;
    Context context;

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

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

    int token;
    private Request refreshTokenRequest;


    public CameraConnection() {
        socket = new Socket();
        try {
            socket.setSoTimeout(3000);
            socket.connect(new InetSocketAddress(host, 7878), 5000);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
    }


    public Cancelable request(final Request request, final ResponseListener responseListener) {

        final AsyncTask task = new AsyncTask<Void, Void, Response>() {
            @Override
            protected Response doInBackground(Void... params) {
                Response response = null;
                try {
                    response = requestForResponse(request);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                return response;
            }
        };
        task.execute();
        return new Cancelable() {
            @Override
            public void cancel() {
                task.cancel(true);
            }
        };
    }

    private Response requestForResponse(Request request) throws IOException, TimeoutException {
        if (request.getMsg_id() != AMBA_START_SESSION) {
            request.setToken(token);
        }
        if (request.getToken() == -1000 && request.getMsg_id() != AMBA_START_SESSION) {
            refreshToken(request);
        }
        writeInternal(request.toJson());
        String readInternal = readInternalComplete("");
        if (TextUtils.isEmpty(readInternal)) {
            return new Response();
        }
        Response response = null;
        response = new Gson().fromJson(readInternal, Response.class);
        if (response.getRval() == Response.INVALID_TOKEN) {
            refreshToken(request);
            response = requestForResponse(request);
        }
        if (response.getMsg_id() == request.getMsg_id()) {
            return response;
        } else {
            if (TextUtils.isEmpty(readInternal)) {
                return new Response();
            }
            response = new Gson().fromJson(readInternal, Response.class);
        }

        return response;
    }

    private void refreshToken(Request request) throws IOException, TimeoutException {
        if (refreshTokenRequest != null) {
            Response tokenResponse = requestForResponse(refreshTokenRequest);
            if (tokenResponse != null && tokenResponse.getRval() == Response.SESSION_START_FAIL) {
                throw new IOException();
            }
            setToken(getToken(tokenResponse));
            request.setToken(getToken());
        }
    }

    private void writeInternal(String content) throws IOException {
        getOutputStream().write(content.getBytes());
    }

    private String readInternal() throws IOException, TimeoutException {
        final long begin = System.currentTimeMillis();
        String response = "";
        for (; ; ) {
            if (!isSocketAvaliable()) {
                break;
            }
            long current = System.currentTimeMillis();
            if (current - begin > 5000L) {
                throw new TimeoutException("read timeout");
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

        if (readInternal.contains("}{")) {
            int i = readInternal.indexOf("}{");
            return readInternal.substring(i + 1);
        }
        return readInternalComplete(readInternal);
    }

    public interface ResponseListener {
        void onStarted();

        void onResponse(Response response);

        void onError(Throwable e);
    }

}
