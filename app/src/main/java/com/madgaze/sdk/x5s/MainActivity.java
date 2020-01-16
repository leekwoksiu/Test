package com.madgaze.sdk.x5s;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.madgaze.smartglass.otg.AudioCallback;
import com.madgaze.smartglass.otg.CameraHelper;
import com.madgaze.smartglass.otg.RecordVideoCallback;
import com.madgaze.smartglass.otg.SplitCamera;
import com.madgaze.smartglass.otg.SplitCameraCallback;
import com.madgaze.smartglass.otg.TakePictureCallback;
import com.madgaze.smartglass.view.SplitCameraView;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    SplitCameraView mSplitCameraView;
    String[] RequiredPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSplitCameraView = (SplitCameraView) findViewById(R.id.splitCameraView);
        init();
    }

    public void init(){
        setViews();

        if (!permissionReady()) {
            askForPermission();
        } else {
            setVideo();
            setAudio();
        }
    }


    public void askForPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, RequiredPermissions,7);
        }
    }

    public boolean permissionReady(){
        ArrayList<String> PermissionsMissing = new ArrayList();

        for (int i = 0; i < RequiredPermissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, RequiredPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                PermissionsMissing.add(RequiredPermissions[i]);
            }
        }
        if (PermissionsMissing.size() > 0){
            MDToast.makeText(MainActivity.this, String.format("Permission [%s] not allowed, please allows in the Settings."+", ", PermissionsMissing)), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            return false;
        }
        return true;
    }


    public void setViews(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.cameraOnOffBtn:
                        toggleCameraOnOffBtn();sss
                        break;
                    case R.id.takePictureBtn:
                        findViewById(R.id.takePictureBtn).setEnabled(false);
                        takePicture();
                        break;
                    case R.id.videoBtn:
                        findViewById(R.id.videoBtn).setEnabled(false);
                        toogleVideoBtn();
                        break;
                }
            }
        };

        (findViewById(R.id.cameraOnOffBtn)).setOnClickListener(listener);
        (findViewById(R.id.takePictureBtn)).setOnClickListener(listener);
        (findViewById(R.id.videoBtn)).setOnClickListener(listener);

        (findViewById(R.id.takePictureBtn)).setEnabled(false);
        (findViewById(R.id.videoBtn)).setEnabled(false);
    }

    public void setVideo(){
        SplitCamera.getInstance(this).setFrameFormat(CameraHelper.FRAME_FORMAT_MJPEG);
        SplitCamera.getInstance(this).setPreviewSize(SplitCamera.CameraDimension.DIMENSION_1280_720);
        SplitCamera.getInstance(this).start(findViewById(R.id.splitCameraView));

        //////

        /* Insert code segment below if you want to monitor the USB Camera connection status */
        SplitCamera.getInstance(this).setCameraCallback(new SplitCameraCallback() {
            @Override
            public void onConnected() {
                SplitCamera.getInstance(MainActivity.this).startPreview();
                MDToast.makeText(MainActivity.this, "Camera connected", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                updateUI(true);
            }

            @Override
            public void onDisconnected() {
                MDToast.makeText(MainActivity.this, "Camera disconnected", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                updateUI(false);
            }

            @Override
            public void onError(int code) {
                if (code == -1)
                    MDToast.makeText(MainActivity.this, "There is no connecting MAD Gaze Cameras.", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                else
                    MDToast.makeText(MainActivity.this, "MAD Gaze Camera Init Failure (Error=" + code + ")", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                updateUI(false);
            }
        });

        //////

        /* Insert code segment below if you want to retrieve the video frames in nv21 format */
        SplitCamera.getInstance(this).setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] bytes) {

            }
        });

        //////

        /* Insert code segment below if you want to record the video */
        SplitCamera.getInstance(this).setRecordVideoCallback(new RecordVideoCallback() {
            @Override
            public void onVideoSaved(String path) {
                MDToast.makeText(MainActivity.this, "Video saved success in (" + path + ")", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((Button)findViewById(R.id.videoBtn)).setText("START");
                        ((findViewById(R.id.videoBtn))).setEnabled(true);
                    }
                });
            }

            @Override
            public void onError(int code) {
                MDToast.makeText(MainActivity.this, "Video saved (Error=" + code +")", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((Button)findViewById(R.id.videoBtn)).setText("START");
                        ((findViewById(R.id.videoBtn))).setEnabled(true);
                    }
                });
            }
        });
    }

    public void setAudio(){
        SplitCamera.getInstance(this).setAudioSamFreq(SplitCamera.SamFreq.SamFreq_48000);//default 48000

        /* Insert code segment below if you want to retrieve the audio data */
        SplitCamera.getInstance(this).setAudioCallback(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           new AudioCallback() {
            @Override
            public void onAudioReceived(byte[] decodedAudio) {
                //decodedAudio is audio byte data.
            }
        });
    }

    public void toggleCameraOnOffBtn(){
        if (SplitCamera.getInstance(MainActivity.this).isPreviewStarted()) {
            updateUI(false);
            SplitCamera.getInstance(MainActivity.this).stopPreview();
        } else {
            updateUI(true);
            SplitCamera.getInstance(MainActivity.this).startPreview();
        }
    }

    public void toogleVideoBtn(){
        if (!permissionReady()) return;
        if (SplitCamera.getInstance(this).isRecording()) {
            MDToast.makeText(this, "Stop Recording", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
            SplitCamera.getInstance(this).stopRecording();

        } else {
            MDToast.makeText(this, "Start Recording", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
            SplitCamera.getInstance(this).startRecording();
            ((Button)(findViewById(R.id.videoBtn))).setText("STOP VIDEO");
            ((findViewById(R.id.videoBtn))).setEnabled(true);

        }
    }

    public void takePicture() {
        if (!permissionReady()) return;
        SplitCamera.getInstance(this).takePicture(new TakePictureCallback() {
            @Override
            public void onImageSaved(String path) {
                MDToast.makeText(MainActivity.this, "Image saved success in (" + path + ")", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                ((findViewById(R.id.takePictureBtn))).setEnabled(true);
            }

            @Override
            public void onError(int code) {
                MDToast.makeText(MainActivity.this, "Image saved failure (Error="+code+")", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                ((findViewById(R.id.takePictureBtn))).setEnabled(true);
            }
        });
    }

    public void updateUI(boolean on){
        if (on){
            (findViewById(R.id.cameraOnOffBtn)).setEnabled(true);
            ((Button)findViewById(R.id.cameraOnOffBtn)).setText("STOP");
            (findViewById(R.id.videoBtn)).setEnabled(true);
            (findViewById(R.id.takePictureBtn)).setEnabled(true);

        } else {
            (findViewById(R.id.cameraOnOffBtn)).setEnabled(true);
            ((Button)findViewById(R.id.cameraOnOffBtn)).setText("START");
            (findViewById(R.id.videoBtn)).setEnabled(false);
            (findViewById(R.id.takePictureBtn)).setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SplitCamera.getInstance(this).onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SplitCamera.getInstance(this).onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SplitCamera.getInstance(this).onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SplitCamera.getInstance(this).onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SplitCamera.getInstance(this).onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7){
            if (!permissionReady()) {
                askForPermission();
            } else {
                setVideo();
                setAudio();
            }
        }
    }

}
