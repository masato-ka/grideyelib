package ka.masato.sample;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SensorManager sensorManager;
    private SensorManager.DynamicSensorCallback mCallback = new SensorManager.DynamicSensorCallback() {
        @Override
        public void onDynamicSensorConnected(Sensor sensor) {
            super.onDynamicSensorConnected(sensor);
            if (sensor.getName() == "GridEYESensorDriver") {
                sensorManager.registerListener(MainActivity.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }

        @Override
        public void onDynamicSensorDisconnected(Sensor sensor) {
            super.onDynamicSensorDisconnected(sensor);
            sensorManager.unregisterListener(MainActivity.this);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, SensorDrierService.class));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerDynamicSensorCallback(mCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterDynamicSensorCallback(mCallback);
        stopService(new Intent(this, SensorDrierService.class));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "RESULT :" + sensorEvent.values.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
