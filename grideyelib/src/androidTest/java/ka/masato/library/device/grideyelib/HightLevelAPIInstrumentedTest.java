package ka.masato.library.device.grideyelib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.userdriver.UserDriverManager;
import com.google.android.things.userdriver.sensor.UserSensor;
import ka.masato.grideyelib.GridEyeManager;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class HightLevelAPIInstrumentedTest {

    private GridEyeManager gridEyeManager;
    private static final String I2C_PORT = "I2C1";
    private static final int ADDRESS = 0x68;

    private UserDriverManager mUserDriverManager;
    private UserSensor userSensor;
    private SensorEventListener sensorCallback = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Log.d("SENSOR;", sensorEvent.values.toString());
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Before
    public void setUp() {
        PeripheralManager mPeripheralManager = PeripheralManager.getInstance();
        gridEyeManager = new GridEyeManager(mPeripheralManager, I2C_PORT, ADDRESS);
    }

    @Test
    public void registerUserSpaceDriver() throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();

        userSensor = gridEyeManager.getUserSensor();

        mUserDriverManager = UserDriverManager.getInstance();
        mUserDriverManager.registerSensor(userSensor);

    }


    @Test
    public void sensorReadValue() throws InterruptedException, IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();

        userSensor = gridEyeManager.getUserSensor();

        mUserDriverManager = UserDriverManager.getInstance();
        mUserDriverManager.registerSensor(userSensor);

        final SensorManager sensorManager = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
        final SensorManager.DynamicSensorCallback mCallback = new SensorManager.DynamicSensorCallback() {

            @Override
            public void onDynamicSensorConnected(Sensor sensor) {
                super.onDynamicSensorConnected(sensor);
                sensorManager.registerListener(sensorCallback, sensor, SensorManager.SENSOR_DELAY_NORMAL);

            }

            @Override
            public void onDynamicSensorDisconnected(Sensor sensor) {
                super.onDynamicSensorDisconnected(sensor);
                sensorManager.unregisterListener(sensorCallback);
            }
        };
        sensorManager.registerDynamicSensorCallback(mCallback);

        while (true) {
            Thread.sleep(1000);
        }

//        mUserDriverManager.unregisterSensor(userSensor);
    }

    @Test
    public void unregisterUserSpaceDriver() {
        mUserDriverManager.unregisterSensor(userSensor);
    }


}
