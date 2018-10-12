package ka.masato.library.device;

import android.util.Log;
import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.userdriver.sensor.UserSensorDriver;
import com.google.android.things.userdriver.sensor.UserSensorReading;

import java.io.IOException;

public class GridEyeSensorDriver implements UserSensorDriver {

    private static final String LOG_TAG = "GridEyeSensorDriver";
    private static final String I2C_NAME = "I2C1";
    private static final int ADDRESS = 0x68;

    private I2cDevice mI2cDevice = null;

    public GridEyeSensorDriver(){

        PeripheralManager mManager = PeripheralManager.getInstance();
        try {
            mI2cDevice = mManager.openI2cDevice(I2C_NAME, ADDRESS);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }


    @Override
    public UserSensorReading read() throws IOException {
        byte[] data = new byte[64];
        mI2cDevice.readRegBuffer(0x80, data, 64);


        return null;
    }

    @Override
    public void setEnabled(boolean enabled) throws IOException {
    }
}
