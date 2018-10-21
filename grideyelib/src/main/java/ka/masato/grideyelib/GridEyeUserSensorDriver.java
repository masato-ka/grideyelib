package ka.masato.grideyelib;

import com.google.android.things.userdriver.sensor.UserSensorDriver;
import com.google.android.things.userdriver.sensor.UserSensorReading;
import ka.masato.grideyelib.driver.GridEyeDriver;
import ka.masato.grideyelib.driver.enums.FrameRate;

import java.io.IOException;

public class GridEyeUserSensorDriver implements UserSensorDriver {

    private GridEyeDriver gridEyeDriver;

    public GridEyeUserSensorDriver(GridEyeDriver gridEyeDriver) {
        super();
        this.gridEyeDriver = gridEyeDriver;
    }

    @Override
    public UserSensorReading read() throws IOException {
        float[] temperatures = gridEyeDriver.getTemperatures();
        return new UserSensorReading(temperatures);
    }

    @Override
    public void setEnabled(boolean enabled) throws IOException {
        if (enabled) {
            this.gridEyeDriver.setFrameRate(FrameRate.FRAMERATE_TEN);
        } else {
            this.gridEyeDriver.setFrameRate(FrameRate.FRAMERATE_ONE);
        }
    }
}
