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
import static org.junit.Assert.*;
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
        target.close();
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
        target.close();
        fail();
    }

    @Test
    public void getStatusRegisterTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doReturn((byte) 0x00).when(mockI2cDevice).readRegByte(0x04);
        byte value = target.getStatusRegister();
        assertEquals(0x00, value);
        target.close();
    }

    @Test(expected = IOException.class)
    public void getStatusRegisterTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doThrow(new IOException()).when(mockI2cDevice).readRegByte(0x04);
        byte value = target.getStatusRegister();
        assertEquals(0x00, value);
        target.close();
    }

    @Test
    public void clearRegisterTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x00);
        target.clearRegister(false, false, false);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x00);
        target.close();
    }

    @Test
    public void clearRegisterTest02() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(true, false, false);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.close();
    }

    @Test
    public void clearRegisterTest03() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(false, true, false);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x02);
        target.close();
    }

    @Test
    public void clearRegisterTest04() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(true, true, false);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x06);
        target.close();
    }

    @Test
    public void clearRegisterTest05() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(false, false, true);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x01);
        target.close();
    }

    @Test
    public void clearRegisterTest06() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(false, true, true);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x03);
        target.close();
    }

    @Test
    public void clearRegisterTest07() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(true, true, true);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x07);
        target.close();
    }

    @Test(expected = IOException.class)
    public void clearRegisterTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte(0x05, (byte) 0x07);
        target.clearRegister(true, true, true);
        target.close();
    }

    @Test
    public void enableTwiceMovingAverageModeTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x07);
        target.enableTwiceMovingAverageMode();
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x50);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x45);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x57);
        verify(mockI2cDevice, times(1)).writeRegByte(0x07, (byte) 0x20);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x00);
        target.close();
    }

    @Test(expected = IOException.class)
    public void enableTwiceMovingAverageModeTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte(0x07, (byte) 0x20);
        target.enableTwiceMovingAverageMode();
        target.close();

    }

    @Test
    public void disableTwiceMovingAverageModeTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x07);
        target.disableTwiceMovingAverageMode();
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x50);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x45);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x57);
        verify(mockI2cDevice, times(1)).writeRegByte(0x07, (byte) 0x00);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x00);
        target.close();

    }

    @Test(expected = IOException.class)
    public void disableTwiceMovingAverageModeTestAbnormal01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte(0x07, (byte) 0x00);
        target.disableTwiceMovingAverageMode();
        target.close();
    }

    @Test
    public void isTwiceMovingAverageModeEnableTest01() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doReturn((byte) 0x20).when(mockI2cDevice).readRegByte(0x07);
        boolean result = target.isTwiceMovingAverageModeEnable();
        assertTrue(result);
        target.close();
    }

    @Test
    public void isTwiceMovingAverageModeEnableTest02() throws IOException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doReturn((byte) 0x00).when(mockI2cDevice).readRegByte(0x07);
        boolean result = target.isTwiceMovingAverageModeEnable();
        assertTrue(!result);
        target.close();
    }

    @Test(expected = GridEyeDriverErrorException.class)
    public void isTwiceMovingAverageModeEnableTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doReturn((byte) 0x01).when(mockI2cDevice).readRegByte(0x07);
        boolean result = target.isTwiceMovingAverageModeEnable();
        fail();
    }

    @Test(expected = IOException.class)
    public void isTwiceMovingAverageModeEnableTestAbnormal02() throws IOException, GridEyeDriverErrorException {
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
        doThrow(new IOException()).when(mockI2cDevice).readRegByte(0x07);
        boolean result = target.isTwiceMovingAverageModeEnable();
        fail();
    }

    @Test
    public void setInterruptLevel() throws IOException {

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