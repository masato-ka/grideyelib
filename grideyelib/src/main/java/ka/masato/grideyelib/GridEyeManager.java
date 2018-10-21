package ka.masato.grideyelib;

import android.hardware.Sensor;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.userdriver.sensor.UserSensor;
import ka.masato.grideyelib.driver.GridEyeDriver;

import java.io.IOException;

public class GridEyeManager {

    private static final String DRIVER_NAME = "GridEYESensorDriver";
    private static final String VENDOR_NAME = "masato-ka";
    private static final String STRING_TYPE = "ka.masato.grideye";

    private GridEyeDriver gridEyeDriver;
    private GridEyeUserSensorDriver customUserSensorDriver;

    private final PeripheralManager mPeripheralManager;
    private final String i2CDevice;
    private final int gridEyeAddress;

    public GridEyeManager(PeripheralManager mPeripheralManager, String i2CDevice, int gridEyeAddress) {
        this.mPeripheralManager = mPeripheralManager;
        this.i2CDevice = i2CDevice;
        this.gridEyeAddress = gridEyeAddress;
    }


    public UserSensor getUserSensor() throws IOException {

        gridEyeDriver = GridEyeDriver.getInstance();
        gridEyeDriver.setmPeripheralManager(mPeripheralManager);
        gridEyeDriver.open(i2CDevice, gridEyeAddress);

        customUserSensorDriver = new GridEyeUserSensorDriver(gridEyeDriver);

        return new UserSensor.Builder().setName(DRIVER_NAME)
                .setVendor(VENDOR_NAME)
                .setVersion(1)
                .setCustomType(Sensor.TYPE_DEVICE_PRIVATE_BASE,
                        STRING_TYPE,
                        Sensor.REPORTING_MODE_CONTINUOUS)
                .setDriver(customUserSensorDriver)
                .build();
    }

}


