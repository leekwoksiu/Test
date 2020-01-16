# MAD Gaze X5S: Android SDK

![X5S logo](http://file.madgaze.cn/sdk/images/x5s-banner-2.png)

**MAD Gaze X5S** is a hybrid version of MAD Gaze smart glasses for enterprise use. It is compatible with mobile phones with type-C display port. It is equipped with camera, speaker, microphone and other sensors such as Accelerometer, Gyroscope and Magnetometer.
When connected to mobile phones, it has a longer battery life with better computation power.


For sales enquiries, please contact us at [info@madgaze.com](mailto:info@madgaze.com).

---

## Table of Content
* [Change Log](#changelog)
* [Build History](#build-history)
* [Getting Started](#getting-started)
* [Basic Usage](#basic-usage)
	* [Minimal Setup](#minimal-setup)
	* [Take Photos](#take-photos)
	* [Record Videos](#record-videos)
* [Advanced Usage](#advanced-usage)
	* [Retrieve Video Data](#retrieve-video-data)
	* [Set Preview Format & Resolution](#preview-configuration)
	* [Retrieve Audio Data](#retrieve-audio-data)
	* [Set Audio Frequency](#audio-configuration)
* [Sample Project](#sample-project)
* [Troubleshooting](#troubleshooting)
* [Disclaimer](#disclaimer)
---

## Changelog
[28/05/2019][v1.1.1]	Fix Camera Preview Issues and Added Audio Support.

[26/03/2019][v1.0.0]	First launch.

---

## Build History

|Date| X5S-SDK (.aar) | UVCCamera (.aar) | Demo Project (.zip) |
| --- | --- | --- | --- |
| 28/05/2019 | [v1.1.1](http://file.madgaze.cn/sdk/files/x5s/X5S-devkit-release-v1.1.1.aar) | [v1.1.1](http://file.madgaze.cn/sdk/files/x5s/libuvccamera-v1.1.1.aar) | [Download](http://file.madgaze.cn/sdk/files/x5s/ProjectSDKDemo-v1.1.1.zip)|
| 26/03/2019 | [v1.0.0](http://file.madgaze.cn/sdk/files/x5s/X5S-devkit-release-v1.0.0.aar) | [v1.0.0](http://file.madgaze.cn/sdk/files/x5s/libuvccamera-v1.0.0.aar) | [Download](http://file.madgaze.cn/sdk/files/x5s/ProjectSDKDemo-v1.0.0.zip)|

---

## Getting Started
1. Download **UVC Library** file [here](http://file.madgaze.cn/sdk/files/x5s/libuvccamera-v1.1.1.aar) and **X5S Library** file [here](http://file.madgaze.cn/sdk/files/x5s/X5S-devkit-release-v1.1.1.aar).

2. Import downloaded modules into your project.
	1. Open your Android Studio project
	2. **File** > **Project Structure** > **Press the button '+' in the top left**
	3. Select **Import .JAR/.AAR Package**
	4. Select downloaded **UVC Library** file.
	5. Press **Next** and finish the Import

3. Repeat the **Step (2)** with **X5S Library** file.

4. Add repository in build.gradle (the root level).
	``` java
	allprojects {
		repositories {
			...
			maven { url 'http://raw.github.com/saki4510t/libcommon/master/repository/' }
			//add this line
			...
		}
	}
	```
5. Set Target SDK to 27 in build.gradle (the root level).
	``` java
	android {
		...
		defaultConfig {
			...
			minSdkVersion 21
			targetSdkVersion 27 //set this to 27 or below 27.
			...
		}
	}
	```
6. Add the depdencies in app/build.gradle
	``` java
	implementation("com.serenegiant:common:1.5.20")
	//add this line before UVC Library and X5S Library

	implementation project(':libuvccamera')
	implementation project(':X5S-devkit-release')
	```

---

## Basic Usage
### Minimal Setup
1. Insert a CameraView into your layout file.
	``` xml
	<com.madgaze.smartglass.view.SplitCameraView
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		android:id="@+id/splitCameraView"
		android:layout_centerInParent="true"/>
	```

2. Bind the created SplitCameraView to SplitCamera for video preview.
	``` java
	SplitCamera.getInstance(this).start(findViewById(R.id.splitCameraView));
	```

3. Monitor the connectivity and start preview when X5S is connected.
	``` java
	SplitCamera.getInstance(this).setCameraCallback(new SplitCameraCallback() {
		@Override
		public void onConnected() {
			//USB Camera (X5S) is connected.
			SplitCamera.getInstance(MainActivity.this).startPreview();
			//Start the preview immediately when it is connected.
		}

		@Override
		public void onDisconnected() {
			//USB Camera (X5S) is disconnected.
		}

		@Override
		public void onError(int code) {
			//Code 1: There is no connecting MAD Gaze Cameras.
		}
	});
	```
4. Bind **onResume()**, **onDestroy()**, **onStart()**, **onStop()**, **onPause()** to your activity.
	``` java
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
	```
5. Congratulation! You are now ready to see the Camera Preview from X5S with your application.

---
#### Code Example (Complete)

- MainActivity.java
``` java
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	SplitCamera.getInstance(this).start(findViewById(R.id.splitCameraView));
	SplitCamera.getInstance(this).setCameraCallback(new SplitCameraCallback() {
		@Override
		public void onConnected() {
			//USB Camera (X5S) is connected.
			SplitCamera.getInstance(MainActivity.this).startPreview();
		}

		@Override
		public void onDisconnected() {
			//USB Camera (X5S) is disconnected.
		}

		@Override
		public void onError(int code) {
			//Code 1: There is no connecting MAD Gaze Cameras.
		}
	});
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
```
- activity_main.xml
``` xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.madgaze.smartglass.view.SplitCameraView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/splitCameraView"
        android:layout_centerInParent="true"/>

</RelativeLayout>
```
---

### Take Photos
#### Take Photo and Photo Callback
``` java
SplitCamera.getInstance(this).takePicture(new TakePictureCallback() {
	@Override
	public void onImageSaved(String path) {
		//When image is saved succesfully
	}

	@Override
	public void onError(int code) {
		//When captured image is not saved successfully.
	}
});
```

---

### Record Videos
#### Start Recording
``` java
SplitCamera.getInstance(this).startRecording();
```
#### Stop Recording
``` java
SplitCamera.getInstance(this).stopRecording();
```
#### Video Recording Callbacks
``` java
SplitCamera.getInstance(this).setRecordVideoCallback(new RecordVideoCallback() {
	@Override
	public void onVideoSaved(String path) {
		//When video is saved succesfully
		//path: the file location has been saved.
	}

	@Override
	public void onError(int code) {
		//When captured video is not saved successfully.
	}
});
```


---

## Advanced Usage
### Retrieve Video Data
- To retrieve video(perview) data for image processing.
	``` java
	SplitCamera.getInstance(this).setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
		@Override
		public void onPreviewResult(byte[] yuv420sp) {
			//Retrieve video data in YUV420sp format
		}
	});
	```

### Preview Configuration
* To set the preview and recording resolution of the video. (Default: 1280x720px)
	``` java
	SplitCamera.getInstance(this).setPreviewSize(SplitCamera.CameraDimension.DIMENSION_1280_720);
	```
	|Width|Height|Configuration|
	|---|---|---|
	|1600|1200|SplitCamera.CameraDimension.DIMENSION_1600_1200 |
	|2592|1944|SplitCamera.CameraDimension.DIMENSION_2592_1944 |
	|2048|1536|SplitCamera.CameraDimension.DIMENSION_2048_1536 |
	|1920|1080|SplitCamera.CameraDimension.DIMENSION_1920_1080 |
	|1280|720|SplitCamera.CameraDimension.DIMENSION_1280_720 |
	|640|480|SplitCamera.CameraDimension.DIMENSION_640_480 |
	|1024|576|SplitCamera.CameraDimension.DIMENSION_1024_576 |

- To set preview format of the video. (Default: YUYV)
	``` java
	SplitCamera.getInstance(this).setFrameFormat(CameraHelper.FRAME_FORMAT_MJPEG);
	```
	|Format|Configuration|
	|---|---|
	|YUYV|CameraHelper.FRAME_FORMAT_YUYV|
	|MJPEG|CameraHelper.FRAME_FORMAT_MJPEG|

---

### Retrieve Audio Data
- To retrieve audio data for application handling.
	``` java
	SplitCamera.getInstance(this).setAudioCallback(new AudioCallback() {
		@Override
		public void onAudioReceived(byte[] decodedAudio) {
			//Retrieve audio data
		}
	});

### Audio Configuration
- To set the audio sampling frequency. (Default: 48,000 Hz)
	``` java
	SplitCamera.getInstance(this).setAudioSamFreq(SplitCamera.SamFreq.SamFreq_48000);
	```
	|Frequency|Configuration|
	|---|---|
	|8000|SplitCamera.SamFreq.SamFreq_8000|
	|11025|SplitCamera.SamFreq.SamFreq_11025|
	|16000|SplitCamera.SamFreq.SamFreq_16000|
	|22050|SplitCamera.SamFreq.SamFreq_22050|
	|24000|SplitCamera.SamFreq.SamFreq_24000|
	|44100|SplitCamera.SamFreq.SamFreq_44100|
	|48000|SplitCamera.SamFreq.SamFreq_48000|

---

## Troubleshooting
- Q: Why error ***"Failed to find byte code for com/felhr/usbserial/UsbSerialInterface$UsbDSRCallback"*** is showed when compiling the codes?

  Please ensure the gradle version is configured to greater or equal to 3.2. There is a bug that not able to compile the bytecodes for Gradle version 3.1 or below.

- Q: Why the USB device is not detecting after completing all steps stated above?

	Excluding the possible causes of hardware issue, you will need to provide permission of Camera and Microphone on Android 9.0 devices.
	``` xml
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
	```
	To request permissions approval from the users, please find article below:
	***Request App Permissions***: https://developer.android.com/training/permissions/requesting


- Q: Why the videos are not successfully recorded?

	You may need to include permission of **Writing External Storage** into your application in order to save the videos to the storage.
	``` xml
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	```


## Disclaimer
This library is licensed under the [MAD Gaze - Terms of Use](http://madgaze.com/terms).





