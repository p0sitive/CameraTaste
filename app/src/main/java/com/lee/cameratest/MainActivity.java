package com.lee.cameratest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.cameratest.Camera.CameraConnectionManager;
import com.lee.cameratest.Camera.bean.CameraBean;
import com.lee.cameratest.Play.VideoController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CameraConnectionManager.CameraConnectionChanged {

    Button find;
    Button connect;
    Button takephoto;
    Button video;
    ProgressBar progressBar;
    private TextView show;
    CameraBean camera;
    private CameraConnectionManager cameraConnectionManager;

    VideoController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        cameraConnectionManager = CameraConnectionManager.getInstance();
        cameraConnectionManager.setCameraConnectionChanged(this);

        controller = (VideoController) findViewById(R.id.controller);

        find = (Button) findViewById(R.id.find);
        connect = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(this);
        find.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        show = (TextView) findViewById(R.id.camera);
        takephoto = (Button) findViewById(R.id.take_photo);
        takephoto.setOnClickListener(this);
        video = (Button) findViewById(R.id.video);
        video.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find:
                if (progressBar.isShown()) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                findCamera();
                break;
            case R.id.connect:
                ConnectCamera();
                break;
            case R.id.take_photo:
                takephoto();
                break;
            case R.id.video:
                video();
                break;
            case R.id.start_video:
                cameraConnectionManager.controlCamera(camera, cameraConnectionManager.START_TAKE_VIDEO, new CameraConnectionManager.CameraOptionResponse() {
                    @Override
                    public void success() {
                        handler.sendEmptyMessage(0x007);
                    }

                    @Override
                    public void fail() {
                        handler.sendEmptyMessage(0x008);
                    }
                });
                break;
            case R.id.stop_video:
                cameraConnectionManager.controlCamera(camera, cameraConnectionManager.STOP_TAKE_VIDEO, new CameraConnectionManager.CameraOptionResponse() {
                    @Override
                    public void success() {
                        handler.sendEmptyMessage(0x009);
                    }

                    @Override
                    public void fail() {
                        handler.sendEmptyMessage(0x010);
                    }
                });
                break;
        }
    }

    boolean isRecording = false;

    private void video() {
        if (camera != null) {
            if (!isRecording) {
                cameraConnectionManager.controlCamera(camera, cameraConnectionManager.START_TAKE_VIDEO, new CameraConnectionManager.CameraOptionResponse() {
                    @Override
                    public void success() {
                        handler.sendEmptyMessage(0x007);
                    }

                    @Override
                    public void fail() {
                        handler.sendEmptyMessage(0x08);
                    }
                });
            } else {
                cameraConnectionManager.controlCamera(camera, cameraConnectionManager.STOP_TAKE_VIDEO, new CameraConnectionManager.CameraOptionResponse() {
                    @Override
                    public void success() {
                        handler.sendEmptyMessage(0x009);
                    }

                    @Override
                    public void fail() {
                        handler.sendEmptyMessage(0x010);
                    }
                });
            }
        } else {
            handler.sendEmptyMessage(0x006);
        }
    }

    private void ConnectCamera() {
        if (camera != null) {
            cameraConnectionManager.connectCamera(camera, new CameraConnectionManager.CameraOptionResponse() {
                @Override
                public void success() {
                    handler.sendEmptyMessage(0x002);
                }

                @Override
                public void fail() {
                    handler.sendEmptyMessage(0x003);
                }
            });
        }
    }

    void findCamera() {
        cameraConnectionManager.startSearchCamera(MainActivity.this,
                new CameraConnectionManager.CameraSearchResult() {
                    @Override
                    public void success(final CameraBean camera) {
                        progressBar.setVisibility(View.INVISIBLE);
                        show.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show.setText(camera.toString());
                                Message msg = handler.obtainMessage();
                                msg.what = 0x001;
                                Bundle b = new Bundle();
                                b.putParcelable("camera", camera);
                                msg.setData(b);
                                handler.sendMessage(msg);
                            }
                        }, 1000);
                    }

                    @Override
                    public void fail() {
                        progressBar.setVisibility(View.INVISIBLE);
                        show.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show.setText("no camera!");
                            }
                        }, 1000);
                    }
                });
    }

    void takephoto() {
        if (camera != null) {
            cameraConnectionManager.controlCamera(camera, cameraConnectionManager.TAKE_PHOTO,
                    new CameraConnectionManager.CameraOptionResponse() {
                        @Override
                        public void success() {
                            handler.sendEmptyMessage(0x004);
                        }

                        @Override
                        public void fail() {
                            handler.sendEmptyMessage(0x005);
                        }
                    });
        } else {
            handler.sendEmptyMessage(0x006);
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x001:
                    Bundle bundle = msg.getData();
                    CameraBean bean = bundle.getParcelable("camera");
                    setBean(bean);
                    break;
                case 0x002:
                    show.setText("连接成功~");
                    break;
                case 0x003:
                    show.setText("连接失败。。。");
                    break;
                case 0x004:
                    show.setText("拍照成功~");
                    break;
                case 0x005:
                    show.setText("拍照失败。。。");
                    break;
                case 0x006:
                    show.setText("相机未连接。。。");
                    break;
                case 0x007:
                    isRecording = true;
                    show.setText("正在摄像中。。。");
                    break;
                case 0x008:
                    isRecording = false;
                    show.setText("开启摄像失败。。。");
                    break;
                case 0x009:
                    isRecording = false;
                    show.setText("摄像完成。。。");
                    break;
                case 0x010:
                    isRecording = false;
                    show.setText("摄像失败。。。");
                    break;
                case 0x011:
                    Toast.makeText(MainActivity.this, "相机连接成功！", Toast.LENGTH_SHORT).show();
                    break;
                case 0x012:
                    Toast.makeText(MainActivity.this, "相机断开连接！", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    public void setBean(CameraBean bean) {
        this.camera = bean;
    }

    @Override
    public void connect() {
        handler.sendEmptyMessage(0x011);
    }

    @Override
    public void disconnect() {
        handler.sendEmptyMessage(0x012);
    }
}
