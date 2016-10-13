package camera.test.com.testcamera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;

import java.io.IOException;

/**
 * Created by liyuanqing on 2016/10/12.
 */

public class CameraPreview extends TextureView implements
        TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;
    private float density;
    private CaptureRequest.Builder mCaptureBuilder;
    private CameraCaptureSession mSession;
    public CameraPreview(Context context , Camera camera ,float density) {
        super(context);
        mCamera = camera;
        this.density = density;
        this.setSurfaceTextureListener(this);
        // TODO Auto-generated constructor stub
    }


    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                          int height) {
//        mCamera = Camera.open();


       // mCamera = Camera.open();
        Camera.Parameters objParam = mCamera.getParameters();
//设置对焦模式为持续对焦，（最好先判断一下手机是否有这个对焦模式，有些手机没有会报错的）
        objParam.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
       // m_objCamera.setDisplayOrientation(90);
        mCamera.setParameters(objParam);
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        this.setLayoutParams(new FrameLayout.LayoutParams(
                (int) (previewSize.width*density),(int) (previewSize.height*density), Gravity.CENTER));
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                            int height) {
        //实现自动对焦
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    //initCamera();//实现相机的参数初始化
                    camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                }
            }

        });
        // Ignored, Camera does all the work for us
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

   /* //相机参数的初始化设置
    private void initCamera()
    {
        parameters=camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        //parameters.setPictureSize(surfaceView.getWidth(), surfaceView.getHeight());  // 部分定制手机，无法正常识别该方法。
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        setDispaly(parameters,camera);
        camera.setParameters(parameters);
        camera.startPreview();
        camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

    }
*/
}