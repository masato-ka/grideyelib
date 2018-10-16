package ka.masato.grideyelib.driver;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManager;
import ka.masato.grideyelib.driver.enums.FrameRate;
import ka.masato.grideyelib.driver.enums.IntruptMode;
import ka.masato.grideyelib.driver.exception.GridEyeDriverErrorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
    public void tearDown() throws Exception, GridEyeDriverErrorException {
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
        target.close();
    }

    @Test(expected = IOException.class)
    public void openTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doThrow(new IOException()).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        target.close();
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
    public void flagRestTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);

        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        try {
            target.open("I2C1", 0x68);
        } catch (IOException e) {
            fail();
        }

        doNothing().when(mockI2cDevice).writeRegByte((byte) 0x01, (byte) 0x30);
        target.flagRest();
        verify(mockI2cDevice, times(1)).writeRegByte(0x01, (byte) 0x30);
        target.close();
    }


    @Test(expected = IOException.class)
    public void flagRestTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        try {
            target.open("I2C1", 0x68);
        } catch (IOException e) {
            fail();
        }

        doThrow(new IOException()).when(mockI2cDevice).writeRegByte((byte) 0x01, (byte) 0x30);

        target.flagRest();
        target.close();
    }

    @Test
    public void initialRestTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);

        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        try {
            target.open("I2C1", 0x68);
        } catch (IOException e) {
            fail();
        }

        doNothing().when(mockI2cDevice).writeRegByte((byte) 0x01, (byte) 0x3F);
        target.initialRest();
        verify(mockI2cDevice, times(1)).writeRegByte(0x01, (byte) 0x3F);
        target.close();
    }

    @Test(expected = IOException.class)
    public void initialRestTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);

        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        try {
            target.open("I2C1", 0x68);
        } catch (IOException e) {
            fail();
        }

        doThrow(new IOException()).when(mockI2cDevice).writeRegByte((byte) 0x01, (byte) 0x3F);
        target.initialRest();
        verify(mockI2cDevice, times(1)).writeRegByte(0x01, (byte) 0x3F);
        target.close();
    }

    @Test
    public void setFrameRateTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x02, (byte) 0x01);
        target.setFrameRate(FrameRate.FRAMERATE_TEN);
        verify(mockI2cDevice, times(1)).writeRegByte(0x02, (byte) 0x00);
        target.close();
    }

    @Test
    public void setFrameRateTest02() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x02, (byte) 0x01);
        target.setFrameRate(FrameRate.FRAMERATE_ONE);
        verify(mockI2cDevice, times(1)).writeRegByte(0x02, (byte) 0x01);
        target.close();
    }

    @Test(expected = IOException.class)
    public void setFrameRateTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte(0x02, (byte) 0x01);
        target.setFrameRate(FrameRate.FRAMERATE_ONE);
        target.close();
    }

    @Test
    public void getFrameRateTest01() throws IOException {

        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doReturn((byte) 0x00).when(mockI2cDevice).readRegByte(0x02);
        FrameRate actual = target.getFrameRate();
        assertThat(actual.getValue(), is(FrameRate.FRAMERATE_TEN.getValue()));
        target.close();
    }

    @Test
    public void getFrameRateTest02() throws IOException {

        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doReturn((byte) 0x01).when(mockI2cDevice).readRegByte(0x02);
        FrameRate actual = target.getFrameRate();
        assertThat(actual.getValue(), is(FrameRate.FRAMERATE_ONE.getValue()));
        target.close();
    }

    /* GridEye return illigal value.*/
    @Test(expected = GridEyeDriverErrorException.class)
    public void getFrameRateTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        //0x03 is illigal value.
        doReturn((byte) 0x03).when(mockI2cDevice).readRegByte(0x02);
        FrameRate actual = target.getFrameRate();
        target.close();
    }

    /* GridEye return illigal value.*/
    @Test(expected = IOException.class)
    public void getFrameRateTestAbnormal02() throws IOException, GridEyeDriverErrorException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        //0x03 is illigal value.
        doThrow(new IOException()).when(mockI2cDevice).readRegByte(0x02);
        FrameRate actual = target.getFrameRate();
        target.close();
    }

    @Test
    public void setInterruptModeTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);

        doNothing().when(mockI2cDevice).writeRegByte(0x03, (byte) 0x00);
        target.setInterruptMode(IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE);

        verify(mockI2cDevice, times(1))
                .writeRegByte(0x03, IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE.getValue());

        target.close();
    }

    @Test(expected = IOException.class)
    public void setInterruptModeAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);

        doThrow(new IOException()).when(mockI2cDevice)
                .writeRegByte(0x03, (byte) IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE.getValue());
        target.setInterruptMode(IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE);
        fail();
    }

    @Test
    public void getInterruptModeTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);

        doReturn((byte) 0x03).when(mockI2cDevice).readRegByte(0x03);
        IntruptMode actual = target.getInterruptMode();

        assertThat(actual, is(IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE));
        target.close();
    }

    /* device returned illigal value*/
    @Test(expected = GridEyeDriverErrorException.class)
    public void getInterruptModeTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);

        doReturn((byte) 0xFF).when(mockI2cDevice).readRegByte(0x03);
        IntruptMode actual = target.getInterruptMode();
        fail();
    }

    /* device returned illigal value*/
    @Test(expected = IOException.class)
    public void getInterruptModeTestAbnormal02() throws IOException, GridEyeDriverErrorException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);

        doThrow(new IOException()).when(mockI2cDevice).readRegByte(0x03);
        IntruptMode actual = target.getInterruptMode();
        fail();
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