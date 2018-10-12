package ka.masato.irarraysensorlib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManager;
import ka.masato.library.device.camera.CameraController;
import ka.masato.library.device.camera.ImagePreprocessor;
import ka.masato.library.device.camera.exception.FailedCaptureImageException;
import ka.masato.library.device.camera.exception.NoCameraFoundException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class IRArrayActivity extends Activity {

    private static final String LOG_TAG = "IRArrayActivity";
    private static final String I2C_NAME = "I2C1";
    private static final int I2C_ADDRESS = 0x68;
    private static final int IMAGE_WIDTH = 640;
    private static final int IMAGE_HEIGHT = 480;

    private CameraController cameraController = null;
    private ImagePreprocessor mImagePreprocessor = null;
    private Handler cameraHandler;
    private Bitmap bitmap;

    private I2cDevice i2cDevice;
    private Handler handler;
    private Handler mainHandler;
    private HeatmapView heatmapView;
    private List<SensingData> sensingDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManager peripheralManager = PeripheralManager.getInstance();
        try {
            i2cDevice = peripheralManager.openI2cDevice(I2C_NAME, I2C_ADDRESS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HandlerThread handlerThread = new HandlerThread("SENSOR_HANDLER");
        handlerThread.start();
        HandlerThread cameraHandlerThread = new HandlerThread("CAMERA_THREAD");
        cameraHandlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler();
        handler.post(runnable);

        cameraHandler = new Handler(cameraHandlerThread.getLooper());
        cameraController = CameraController.getInstance();
        try {

            cameraController.initializeCameraDevice(this, IMAGE_WIDTH, IMAGE_HEIGHT, cameraHandler, imageListener);
        } catch (NoCameraFoundException e) {
            Log.e(LOG_TAG, "Not found camera device");
        } catch (CameraAccessException e) {
            Log.e(LOG_TAG, "Illigal camera access exception.");
        }

        mImagePreprocessor = new ImagePreprocessor(IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
        cameraHandler.post(cameraRunnable);
        heatmapView = new HeatmapView(this);
        setContentView(heatmapView);

        //setContentView(R.layout.activity_irarray);
    }


    private ImageReader.OnImageAvailableListener imageListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader imageReader) {
            Image image = imageReader.acquireLatestImage();
            bitmap = mImagePreprocessor.preprocessImage(image);

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    heatmapView.setBitmap(bitmap);
                    heatmapView.invalidate();

                }
            });
        }
    };

    Runnable cameraRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                cameraController.takePicture();
            } catch (FailedCaptureImageException e) {
                e.printStackTrace();
            }
            cameraHandler.postDelayed(this, 100);
        }
    };

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            sensingDataList = new ArrayList<>();
            byte[][] values = new byte[8][16];
            double[][] result = new double[8][8];
            for(int i = 0; i < 8; i++){
                try {
                    i2cDevice.readRegBuffer(0x80+(0x10*i),values[i], 16);
                    result[i] = decodeThermalValues(values[i]);
                    for(int j=0; j < 8; j ++){
                        SensingData sensingData = new SensingData(7-j,7-i, result[i][j]);
                        sensingDataList.add(sensingData);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                        heatmapView.setSensingDataList(sensingDataList);
                        heatmapView.invalidate();
                    }
            });
            handler.postDelayed(this, 100);
        }


    };


    private double[] decodeThermalValues(byte[] values){
        double[] result = new double[8];
        ByteBuffer bb = ByteBuffer.wrap(values).order(ByteOrder.LITTLE_ENDIAN);
        for(int i = 0; i < 8; i++) {
            bb.position(2 * i);
            byte[] buffer = new byte[2];
            bb.get(buffer, 0, 2);
            byte msb = (byte)((byte)buffer[1] & 0x07);
            result[i] = ByteBuffer.wrap(new byte[]{msb, buffer[0]}).getShort() * 0.25;
            if((buffer[1] & 0x08) == 0x08){
                result[i] *= -1;
            }
        }

        return result;
    }

}
