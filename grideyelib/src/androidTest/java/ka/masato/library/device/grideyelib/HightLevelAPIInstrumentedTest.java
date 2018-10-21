package ka.masato.library.device.grideyelib;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
    public void unregisterUserSpaceDriver() {
        mUserDriverManager.unregisterSensor(userSensor);
    }


}
