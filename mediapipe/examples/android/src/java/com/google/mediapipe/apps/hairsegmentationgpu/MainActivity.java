// Copyright 2019 The MediaPipe Authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// package com.google.mediapipe.apps.hairsegmentationgpu;

// import android.graphics.SurfaceTexture;
// import android.os.Bundle;
// import androidx.appcompat.app.AppCompatActivity;
// import android.util.Size;
// import android.view.SurfaceHolder;
// import android.view.SurfaceView;
// import android.view.View;
// import android.view.ViewGroup;
// import com.google.mediapipe.components.CameraHelper;
// import com.google.mediapipe.components.CameraXPreviewHelper;
// import com.google.mediapipe.components.ExternalTextureConverter;
// import com.google.mediapipe.components.FrameProcessor;
// import com.google.mediapipe.components.PermissionHelper;
// import com.google.mediapipe.framework.AndroidAssetUtil;
// import com.google.mediapipe.glutil.EglManager;

// /** Main activity of MediaPipe example apps. */
// public class MainActivity extends AppCompatActivity {
//   private static final String TAG = "MainActivity";

//   private static final String BINARY_GRAPH_NAME = "hairsegmentationgpu.binarypb";
//   private static final String INPUT_VIDEO_STREAM_NAME = "input_video";
//   private static final String OUTPUT_VIDEO_STREAM_NAME = "output_video";
//   private static final CameraHelper.CameraFacing CAMERA_FACING = CameraHelper.CameraFacing.FRONT;

//   // Flips the camera-preview frames vertically before sending them into FrameProcessor to be
//   // processed in a MediaPipe graph, and flips the processed frames back when they are displayed.
//   // This is needed because OpenGL represents images assuming the image origin is at the bottom-left
//   // corner, whereas MediaPipe in general assumes the image origin is at top-left.
//   private static final boolean FLIP_FRAMES_VERTICALLY = true;

//   static {
//     // Load all native libraries needed by the app.
//     System.loadLibrary("mediapipe_jni");
//     System.loadLibrary("opencv_java3");
//   }

//   // {@link SurfaceTexture} where the camera-preview frames can be accessed.
//   private SurfaceTexture previewFrameTexture;
//   // {@link SurfaceView} that displays the camera-preview frames processed by a MediaPipe graph.
//   private SurfaceView previewDisplayView;

//   // Creates and manages an {@link EGLContext}.
//   private EglManager eglManager;
//   // Sends camera-preview frames into a MediaPipe graph for processing, and displays the processed
//   // frames onto a {@link Surface}.
//   private FrameProcessor processor;
//   // Converts the GL_TEXTURE_EXTERNAL_OES texture from Android camera into a regular texture to be
//   // consumed by {@link FrameProcessor} and the underlying MediaPipe graph.
//   private ExternalTextureConverter converter;

//   // Handles camera access via the {@link CameraX} Jetpack support library.
//   private CameraXPreviewHelper cameraHelper;

//   @Override
//   protected void onCreate(Bundle savedInstanceState) {
//     super.onCreate(savedInstanceState);
//     setContentView(R.layout.activity_main);

//     previewDisplayView = new SurfaceView(this);
//     setupPreviewDisplayView();

//     // Initialize asset manager so that MediaPipe native libraries can access the app assets, e.g.,
//     // binary graphs.
//     AndroidAssetUtil.initializeNativeAssetManager(this);

//     eglManager = new EglManager(null);
//     processor =
//         new FrameProcessor(
//             this,
//             eglManager.getNativeContext(),
//             BINARY_GRAPH_NAME,
//             INPUT_VIDEO_STREAM_NAME,
//             OUTPUT_VIDEO_STREAM_NAME);
//     processor.getVideoSurfaceOutput().setFlipY(FLIP_FRAMES_VERTICALLY);

//     PermissionHelper.checkAndRequestCameraPermissions(this);
//   }

//   @Override
//   protected void onResume() {
//     super.onResume();
//     converter = new ExternalTextureConverter(eglManager.getContext());
//     converter.setFlipY(FLIP_FRAMES_VERTICALLY);
//     converter.setConsumer(processor);
//     if (PermissionHelper.cameraPermissionsGranted(this)) {
//       startCamera();
//     }
//   }

//   @Override
//   protected void onPause() {
//     super.onPause();
//     converter.close();
//   }

//   @Override
//   public void onRequestPermissionsResult(
//       int requestCode, String[] permissions, int[] grantResults) {
//     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//     PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
//   }

//   private void setupPreviewDisplayView() {
//     previewDisplayView.setVisibility(View.GONE);
//     ViewGroup viewGroup = findViewById(R.id.preview_display_layout);
//     viewGroup.addView(previewDisplayView);

//     previewDisplayView
//         .getHolder()
//         .addCallback(
//             new SurfaceHolder.Callback() {
//               @Override
//               public void surfaceCreated(SurfaceHolder holder) {
//                 processor.getVideoSurfaceOutput().setSurface(holder.getSurface());
//               }

//               @Override
//               public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                 // (Re-)Compute the ideal size of the camera-preview display (the area that the
//                 // camera-preview frames get rendered onto, potentially with scaling and rotation)
//                 // based on the size of the SurfaceView that contains the display.
//                 Size viewSize = new Size(width, height);
//                 Size displaySize = cameraHelper.computeDisplaySizeFromViewSize(viewSize);

//                 // Connect the converter to the camera-preview frames as its input (via
//                 // previewFrameTexture), and configure the output width and height as the computed
//                 // display size.
//                 converter.setSurfaceTextureAndAttachToGLContext(
//                     previewFrameTexture, displaySize.getWidth(), displaySize.getHeight());
//               }

//               @Override
//               public void surfaceDestroyed(SurfaceHolder holder) {
//                 processor.getVideoSurfaceOutput().setSurface(null);
//               }
//             });
//   }

//   private void startCamera() {
//     cameraHelper = new CameraXPreviewHelper();
//     cameraHelper.setOnCameraStartedListener(
//         surfaceTexture -> {
//           previewFrameTexture = surfaceTexture;
//           // Make the display view visible to start showing the preview. This triggers the
//           // SurfaceHolder.Callback added to (the holder of) previewDisplayView.
//           previewDisplayView.setVisibility(View.VISIBLE);
//         });
//     cameraHelper.startCamera(this, CAMERA_FACING, /*surfaceTexture=*/ null);
//   }
// }


package com.google.mediapipe.apps.hairsegmentationgpu;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mediapipe.components.CameraHelper;
import com.google.mediapipe.components.CameraXPreviewHelper;
import com.google.mediapipe.components.ExternalTextureConverter;

import com.google.mediapipe.components.TextureFrameConsumer;
import com.google.mediapipe.framework.TextureFrame;

import com.google.mediapipe.components.FrameProcessor;
import com.google.mediapipe.components.PermissionHelper;
import com.google.mediapipe.framework.AndroidAssetUtil;
import com.google.mediapipe.glutil.EglManager;
import com.google.mediapipe.framework.Packet;

import com.google.mediapipe.framework.PacketCreator;

import com.google.mediapipe.framework.AndroidPacketCreator;


/**
 * Main activity of MediaPipe example apps.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String BINARY_GRAPH_NAME = "hairsegmentationgpu.binarypb";
    private static final String INPUT_VIDEO_STREAM_NAME = "input_video";
    private static final String OUTPUT_VIDEO_STREAM_NAME = "output_video";
    private static final String RED_INPUT_STREAM ="red";
    private static final String GREEN_INPUT_STREAM ="green";
    private static final String BLUE_INPUT_STREAM ="blue";

    private static final CameraHelper.CameraFacing CAMERA_FACING = CameraHelper.CameraFacing.FRONT;
    private static final boolean FLIP_FRAMES_VERTICALLY = true;

    protected int red_progress = 0;
    protected int blue_progress = 0;
    protected int green_progress = 0;
    public Packet red_packet;
    public Packet green_packet;
    public Packet blue_packet;
    SeekBar red_seekBar;
    SeekBar green_seekBar;
    SeekBar blue_seekBar;
    static {
        // Load all native libraries needed by the app.
        System.loadLibrary("mediapipe_jni");
        System.loadLibrary("opencv_java3");
    }

    // {@link SurfaceTexture} where the camera-preview frames can be accessed.
    private SurfaceTexture previewFrameTexture;
    // {@link SurfaceView} that displays the camera-preview frames processed by a MediaPipe graph.
    private SurfaceView previewDisplayView;

    // Creates and manages an {@link EGLContext}.
    private EglManager eglManager;
    // Sends camera-preview frames into a MediaPipe graph for processing, and displays the processed
    // frames onto a {@link Surface}.
    private FrameProcessor processor;
    // Converts the GL_TEXTURE_EXTERNAL_OES texture from Android camera into a regular texture to be
    // consumed by {@link FrameProcessor} and the underlying MediaPipe graph.
    private ExternalTextureConverter converter;

    // Handles camera access via the {@link CameraX} Jetpack support library.
    private CameraXPreviewHelper cameraHelper;

  private RGBHandler rgbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        red_seekBar = (SeekBar) findViewById(R.id.red_seekbar);
        blue_seekBar = (SeekBar) findViewById(R.id.green_seekbar);
        green_seekBar = (SeekBar) findViewById(R.id.blue_seekbar);

        red_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                red_progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Seek bar progress is :" + red_progress,
                        Toast.LENGTH_SHORT).show();

            }
        });

        green_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                green_progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Seek bar progress is :" + green_progress,
                        Toast.LENGTH_SHORT).show();

            }
        });
        blue_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blue_progress= i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Seek bar progress is :" + blue_progress,
                        Toast.LENGTH_SHORT).show();

            }
        });



        previewDisplayView = new SurfaceView(this);
        setupPreviewDisplayView();
        // Initilize asset manager so that MediaPipe native libraries can access the app assets, e.g.,
        // binary graphs.
        AndroidAssetUtil.initializeNativeAssetManager(this);
//        packetcreator =new PacketCreator();
//        red_packet = packetcreator.createInt32(red_progress);
//        green_packet = packetcreator.createInt32(green_progress);
//        blue_packet = packetcreator.createInt32(blue_progress);
         eglManager = new EglManager(null);
        processor =
        new FrameProcessor(
            this,
            eglManager.getNativeContext(),
            BINARY_GRAPH_NAME,
            INPUT_VIDEO_STREAM_NAME,
            OUTPUT_VIDEO_STREAM_NAME);


        rgbHandler = new RGBHandler();
        processor.setOnWillAddFrameListener(rgbHandler);

        processor.getVideoSurfaceOutput().setFlipY(FLIP_FRAMES_VERTICALLY);
        PermissionHelper.checkAndRequestCameraPermissions(this);



//     processor.setInputSidePackets("rgb_reference", red_progress);
//        processor.setInputSidePackets("green_value",green_progress);
//        processor.setInputSidePackets("blue_value", blue_progress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        converter = new ExternalTextureConverter(eglManager.getContext());
        converter.setFlipY(FLIP_FRAMES_VERTICALLY);
        converter.setConsumer(processor);
        if (PermissionHelper.cameraPermissionsGranted(this)) {
            startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        converter.close();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupPreviewDisplayView() {
        previewDisplayView.setVisibility(View.GONE);
        ViewGroup viewGroup = findViewById(R.id.preview_display_layout);
        viewGroup.addView(previewDisplayView);

        previewDisplayView
                .getHolder()
                .addCallback(
                        new SurfaceHolder.Callback() {
                            @Override
                            public void surfaceCreated(SurfaceHolder holder) {
                                processor.getVideoSurfaceOutput().setSurface(holder.getSurface());
                            }

                            @Override
                            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                // (Re-)Compute the ideal size of the camera-preview display (the area that the
                                // camera-preview frames get rendered onto, potentially with scaling and rotation)
                                // based on the size of the SurfaceView that contains the display.
                                Size viewSize = new Size(width, height);
                                Size displaySize = cameraHelper.computeDisplaySizeFromViewSize(viewSize);

                                // Connect the converter to the camera-preview frames as its input (via
                                // previewFrameTexture), and configure the output width and height as the computed
                                // display size.
                                converter.setSurfaceTextureAndAttachToGLContext(
                                        previewFrameTexture, displaySize.getWidth(), displaySize.getHeight());
                            }

                            @Override
                            public void surfaceDestroyed(SurfaceHolder holder) {
                                processor.getVideoSurfaceOutput().setSurface(null);
                            }
                        });
    }

    private void startCamera() {
        cameraHelper = new CameraXPreviewHelper();
        cameraHelper.setOnCameraStartedListener(
                surfaceTexture -> {
                    previewFrameTexture = surfaceTexture;
                    // Make the display view visible to start showing the preview. This triggers the
                    // SurfaceHolder.Callback added to (the holder of) previewDisplayView.
                    previewDisplayView.setVisibility(View.VISIBLE);
                });
        cameraHelper.startCamera(this, CAMERA_FACING, /*surfaceTexture=*/ null);
    }



    private class RGBHandler implements FrameProcessor.OnWillAddFrameListener
    {
        @Override
        public void onWillAddFrame(long timestamp){

          red_packet = processor.getPacketCreator().createInt32(red_progress);
          green_packet = processor.getPacketCreator().createInt32(green_progress);
          blue_packet = processor.getPacketCreator().createInt32(blue_progress);
          Log.d(TAG, "onWillAddFrame: adding color" + Integer.toString(red_progress) +"  " + Integer.toString(green_progress) + "  "+ Integer.toString(blue_progress) );
          processor.getGraph().addConsumablePacketToInputStream(RED_INPUT_STREAM,
                                                                red_packet,  timestamp);
          processor.getGraph().addConsumablePacketToInputStream(GREEN_INPUT_STREAM,
                                                                green_packet, timestamp);
          processor.getGraph().addConsumablePacketToInputStream(BLUE_INPUT_STREAM,
                                                                blue_packet,  timestamp);
          red_packet.release();
          green_packet.release();
          blue_packet.release();

        }
      }
}