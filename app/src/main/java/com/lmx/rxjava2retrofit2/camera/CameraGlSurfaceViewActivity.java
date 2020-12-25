package com.lmx.rxjava2retrofit2.camera;

import android.hardware.Camera;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;

import com.faceunity.FURenderer;
import com.faceunity.gles.core.GlUtil;
import com.faceunity.utils.Constant;
import com.faceunity.utils.FileUtils;
import com.lmx.rxjava2retrofit2.R;
import com.lmx.rxjava2retrofit2.camera.renderer.BaseCameraRenderer;
import com.lmx.rxjava2retrofit2.camera.renderer.Camera2Renderer;
import com.lmx.rxjava2retrofit2.camera.renderer.OnRendererStatusListener;
import com.lmx.rxjava2retrofit2.camera.utils.CameraUtils;
import com.lmx.rxjava2retrofit2.ui.activity.BaseActivity;
import com.lmx.rxjava2retrofit2.utils.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


/**
 * Created by lmx on 2020/12/24
 * Describe:GlSurfaceView 预览camera
 */
public class CameraGlSurfaceViewActivity extends BaseActivity implements OnRendererStatusListener,
        FURenderer.OnFUDebugListener,
        FURenderer.OnTrackingStatusChangedListener {

    private GLSurfaceView glSurfaceView;
    private SeekBar seekBar_mb;
    private SeekBar seekBar_mp;
    private SeekBar seekBar_hr;
    private SeekBar seekBar_sl;
    private SeekBar seekBar_dy;

    private float colorLevel = 0.5f;
    private float blur = 0.5f;
    private float red = 0.5f;

    private float thin = 0.5f;
    private float eye = 0.5f;

    private boolean mIsDualInput = true;
    private BaseCameraRenderer mCameraRenderer;
    private FURenderer mFURenderer;
    private int mFrontCameraOrientation;

    // only for complete requirement quickly
    private void loadInternalConfigJson() {
        File file = new File(Constant.EXTERNAL_FILE_PATH, "switch_config.json");
        if (!file.exists()) {
            return;
        }
        try {
            String jsonStr = FileUtils.readStringFromFile(file);
            JSONObject jsonObject = new JSONObject(jsonStr);
            int drawLandmarks = jsonObject.optInt("draw_landmarks", 0);
            BaseCameraRenderer.ENABLE_DRAW_LANDMARKS = drawLandmarks == 1;
            int stickerImportFile = jsonObject.optInt("sticker_import_file", 0);
            SwitchConfig.ENABLE_LOAD_EXTERNAL_FILE_TO_EFFECT = stickerImportFile == 1;
            int makeupImportFile = jsonObject.optInt("makeup_import_file", 0);
            SwitchConfig.ENABLE_LOAD_EXTERNAL_FILE_TO_MAKEUP = makeupImportFile == 1;
            int hairImportFile = jsonObject.optInt("hair_import_file", 0);
            SwitchConfig.ENABLE_LOAD_EXTERNAL_FILE_TO_HAIR = hairImportFile == 1;
            int bodyImportFile = jsonObject.optInt("body_import_file", 0);
            SwitchConfig.ENABLE_LOAD_EXTERNAL_FILE_TO_BODY = bodyImportFile == 1;
            int lightMakeupImportFile = jsonObject.optInt("light_makeup_import_file", 0);
            SwitchConfig.ENABLE_LOAD_EXTERNAL_FILE_TO_LIGHT_MAKEUP = lightMakeupImportFile == 1;
            int videoRecordDuration = jsonObject.optInt("video_record_duration", 10_000);
            SwitchConfig.VIDEO_RECORD_DURATION = videoRecordDuration;

        } catch (IOException | JSONException e) {
        }
    }

    @Override
    public int layoutId() {
        return R.layout.activity_camera2_glsurfaceview;
    }

    @Override
    protected void initView() {
        ScreenUtils.fullScreen(this);
        loadInternalConfigJson();
        glSurfaceView = findViewById(R.id.glSurfaceView);
        seekBar_mb = findViewById(R.id.seekBar_mb);
        seekBar_mp = findViewById(R.id.seekBar_mp);
        seekBar_hr = findViewById(R.id.seekBar_hr);
        seekBar_sl = findViewById(R.id.seekBar_sl);
        seekBar_dy = findViewById(R.id.seekBar_dy);

        seekBar_mb.setProgress((int) (colorLevel * 100));
        seekBar_mp.setProgress((int) (blur * 100));
        seekBar_hr.setProgress((int) (red * 100));
        seekBar_sl.setProgress((int) (thin * 100));
        seekBar_dy.setProgress((int) (eye * 100));

        initListener();

        glSurfaceView.setEGLContextClientVersion(GlUtil.getSupportGlVersion(this));
        mCameraRenderer = new Camera2Renderer(this, glSurfaceView, this);
//        mCameraRenderer = new Camera1Renderer(this, mGlSurfaceView, this);
        mFrontCameraOrientation = CameraUtils.getCameraOrientation(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mFURenderer = initFURenderer();
        glSurfaceView.setRenderer(mCameraRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private FURenderer initFURenderer() {
        return new FURenderer
                .Builder(this)
                .maxFaces(4)
                .inputImageOrientation(mFrontCameraOrientation)
                .inputTextureType(FURenderer.FU_ADM_FLAG_EXTERNAL_OES_TEXTURE)
                .setOnFUDebugListener(this)
                .setOnTrackingStatusChangedListener(this)
                .build();
    }

    private void initListener() {
        seekBar_mb.setOnSeekBarChangeListener(new OnSeekBarProgressChangeListener() {
            @Override
            void onProgressChanged(float f) {
                colorLevel = f;
                mFURenderer.onColorLevelSelected(Math.max(0.1f, colorLevel));

            }
        });

        seekBar_mp.setOnSeekBarChangeListener(new OnSeekBarProgressChangeListener() {
            @Override
            void onProgressChanged(float f) {
                blur = f;
                mFURenderer.onBlurLevelSelected(Math.max(0.1f, blur));

            }
        });

        seekBar_hr.setOnSeekBarChangeListener(new OnSeekBarProgressChangeListener() {
            @Override
            void onProgressChanged(float f) {
                red = f;
                mFURenderer.onRedLevelSelected(Math.max(0.1f, red));
            }
        });

        seekBar_sl.setOnSeekBarChangeListener(new OnSeekBarProgressChangeListener() {
            @Override
            void onProgressChanged(float f) {
                thin = f;
                mFURenderer.onCheekThinningSelected(Math.max(0.1f, thin));
            }
        });

        seekBar_dy.setOnSeekBarChangeListener(new OnSeekBarProgressChangeListener() {
            @Override
            void onProgressChanged(float f) {
                eye = f;
                mFURenderer.onEyeEnlargeSelected(Math.max(0.1f, eye));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraRenderer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraRenderer.onPause();
    }

    @Override
    public void onTrackStatusChanged(int type, int status) {

    }

    @Override
    public void onFpsChange(double fps, double renderTime) {

    }

    @Override
    public void onSurfaceCreated() {
        mFURenderer.onSurfaceCreated();
        mFURenderer.setBeautificationOn(true);
    }

    @Override
    public void onSurfaceChanged(int viewWidth, int viewHeight) {

    }

    @Override
    public int onDrawFrame(byte[] cameraNv21Byte, int cameraTextureId, int cameraWidth, int cameraHeight, float[] mvpMatrix, float[] texMatrix, long timeStamp) {
        int fuTexId;
        if (mIsDualInput) {
            fuTexId = mFURenderer.onDrawFrame(cameraNv21Byte, cameraTextureId, cameraWidth, cameraHeight);
        } else {
            fuTexId = mFURenderer.onDrawFrame(cameraNv21Byte, cameraWidth, cameraHeight);
        }
        return fuTexId;
    }

    @Override
    public void onSurfaceDestroy() {

    }

    @Override
    public void onCameraChanged(int cameraFacing, int cameraOrientation) {

    }

    private abstract class OnSeekBarProgressChangeListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            onProgressChanged(progress / 100f);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        abstract void onProgressChanged(float f);
    }

}
