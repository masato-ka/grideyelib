package ka.masato.library.device.grideyelib;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.google.android.things.pio.PeripheralManager;
import ka.masato.grideyelib.driver.GridEyeDriver;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LowLevelAPIInstrumentedTest {

    private static final GridEyeDriver target = GridEyeDriver.getInstance();
    private static final String I2C_PORT = "I2C1"; //This test ran on Raspberry Pi 3B.
    private static final int ADDRESS = 0x68;

    @Test
    public void getTempratures() throws IOException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        PeripheralManager mPeripheralManager = PeripheralManager.getInstance();
        target.setmPeripheralManager(mPeripheralManager);

        target.open(I2C_PORT, ADDRESS);

        float[] tempratures = target.getTemperatures();
        assertTrue(tempratures.length == 64);

        target.close();

    }
}
