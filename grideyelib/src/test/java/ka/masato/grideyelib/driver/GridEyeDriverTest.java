package ka.masato.grideyelib.driver;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManager;
import ka.masato.grideyelib.driver.exception.GridEyeDriverErrorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class GridEyeDriverTest {

    @Mock
    private PeripheralManager mockPeripheralManager;
    @Mock
    private I2cDevice mockI2cDevice;

    private GridEyeDriver target;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        target = GridEyeDriver.getInstance();
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getInstance() {
    }

    @Test
    public void setmPeripheralManager() {
    }

    @Test
    public void openTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        try {
            target.open("I2C1", 0x68);
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = IOException.class)
    public void openTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doThrow(new IOException()).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);

    }

    @Test
    public void closeTest01() throws IOException, GridEyeDriverErrorException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        try {
            target.open("I2C1", 0x68);
        } catch (IOException e) {
            fail();
        }
        doNothing().when(mockI2cDevice).close();
        target.close();
    }

    @Test(expected = GridEyeDriverErrorException.class)
    public void closeTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        doNothing().when(mockI2cDevice).close();
        target.close();
    }

    @Test(expected = IOException.class)
    public void closeTestAbnormal02() throws IOException, GridEyeDriverErrorException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        try {
            target.open("I2C1", 0x68);
        } catch (IOException e) {
            fail();
        }
        doThrow(new IOException()).when(mockI2cDevice).close();
        target.close();
    }


    @Test
    public void flagRest() {
    }

    @Test
    public void initialRest() {
    }

    @Test
    public void setFrameRate() {
    }

    @Test
    public void getFrameRate() {
    }

    @Test
    public void setInterruptMode() {
    }

    @Test
    public void getInterruptMode() {
    }

    @Test
    public void getStatusRegister() {
    }

    @Test
    public void clearRegister() {
    }

    @Test
    public void enableTwiveMovingAverageMode() {
    }

    @Test
    public void disableTwiceMovingAverageMode() {
    }

    @Test
    public void setAverageMode() {
    }

    @Test
    public void isTwiceMovingAverageModeEnable() {
    }

    @Test
    public void setInterruptLevel() {
    }

    @Test
    public void getIntHightLevel() {
    }

    @Test
    public void getIntLowLevel() {
    }

    @Test
    public void getHstLevel() {
    }

    @Test
    public void getThermisterTemplature() {
    }

    @Test
    public void getInterruptedPixels() {
    }

    @Test
    public void getTemperatures() {
    }
}