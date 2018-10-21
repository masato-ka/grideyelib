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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Arrays;

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
        target.setmPeripheralManager(mockPeripheralManager);
        doReturn(mockI2cDevice).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
    }


    @After
    public void tearDown() throws Exception {
        try {
            target.close();
        } catch (GridEyeDriverErrorException e) {

        } catch (IOException e) {

        }
    }


    @Test(expected = IOException.class)
    public void openTestAbnormal01() throws IOException {
        target.close();
        target.setmPeripheralManager(mockPeripheralManager);
        doThrow(new IOException()).when(mockPeripheralManager).openI2cDevice("I2C1", 0x68);
        target.open("I2C1", 0x68);
    }

    @Test(expected = GridEyeDriverErrorException.class)
    public void closeTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        target.close();
        target.close();
        target.setmPeripheralManager(mockPeripheralManager);
        target.open("I2C1", 0x68);
    }

    @Test(expected = IOException.class)
    public void closeTestAbnormal02() throws IOException, GridEyeDriverErrorException {
        doThrow(new IOException()).when(mockI2cDevice).close();
        target.close();
    }


    @Test
    public void flagRestTest01() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte((byte) 0x01, (byte) 0x30);
        target.flagRest();
        verify(mockI2cDevice, times(1)).writeRegByte(0x01, (byte) 0x30);
    }


    @Test(expected = IOException.class)
    public void flagRestTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte((byte) 0x01, (byte) 0x30);
        target.flagRest();
    }

    @Test
    public void initialRestTest01() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte((byte) 0x01, (byte) 0x3F);
        target.initialRest();
        verify(mockI2cDevice, times(1)).writeRegByte(0x01, (byte) 0x3F);
    }

    @Test(expected = IOException.class)
    public void initialRestTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte((byte) 0x01, (byte) 0x3F);
        target.initialRest();
        verify(mockI2cDevice, times(1)).writeRegByte(0x01, (byte) 0x3F);
    }

    @Test
    public void setFrameRateTest01() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x02, (byte) 0x01);
        target.setFrameRate(FrameRate.FRAMERATE_TEN);
        verify(mockI2cDevice, times(1)).writeRegByte(0x02, (byte) 0x00);
    }

    @Test
    public void setFrameRateTest02() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x02, (byte) 0x01);
        target.setFrameRate(FrameRate.FRAMERATE_ONE);
        verify(mockI2cDevice, times(1)).writeRegByte(0x02, (byte) 0x01);
    }

    @Test(expected = IOException.class)
    public void setFrameRateTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte(0x02, (byte) 0x01);
        target.setFrameRate(FrameRate.FRAMERATE_ONE);
    }

    @Test
    public void getFrameRateTest01() throws IOException {
        doReturn((byte) 0x00).when(mockI2cDevice).readRegByte(0x02);
        FrameRate actual = target.getFrameRate();
        assertThat(actual.getValue(), is(FrameRate.FRAMERATE_TEN.getValue()));
    }

    @Test
    public void getFrameRateTest02() throws IOException {
        doReturn((byte) 0x01).when(mockI2cDevice).readRegByte(0x02);
        FrameRate actual = target.getFrameRate();
        assertThat(actual.getValue(), is(FrameRate.FRAMERATE_ONE.getValue()));
    }

    /* GridEye return illigal value.*/
    @Test(expected = GridEyeDriverErrorException.class)
    public void getFrameRateTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        //0x03 is illigal value.
        doReturn((byte) 0x03).when(mockI2cDevice).readRegByte(0x02);
        FrameRate actual = target.getFrameRate();
    }

    /* GridEye return illigal value.*/
    @Test(expected = IOException.class)
    public void getFrameRateTestAbnormal02() throws IOException, GridEyeDriverErrorException {
        //0x03 is illigal value.
        doThrow(new IOException()).when(mockI2cDevice).readRegByte(0x02);
        FrameRate actual = target.getFrameRate();
    }

    @Test
    public void setInterruptModeTest01() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x03, (byte) 0x00);
        target.setInterruptMode(IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE);
        verify(mockI2cDevice, times(1))
                .writeRegByte(0x03, IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE.getValue());
    }

    @Test(expected = IOException.class)
    public void setInterruptModeAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice)
                .writeRegByte(0x03, (byte) IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE.getValue());
        target.setInterruptMode(IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE);
        fail();
    }

    @Test
    public void getInterruptModeTest01() throws IOException {
        doReturn((byte) 0x03).when(mockI2cDevice).readRegByte(0x03);
        IntruptMode actual = target.getInterruptMode();
        assertThat(actual, is(IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE));
    }

    /* device returned illigal value*/
    @Test(expected = GridEyeDriverErrorException.class)
    public void getInterruptModeTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        doReturn((byte) 0xFF).when(mockI2cDevice).readRegByte(0x03);
        IntruptMode actual = target.getInterruptMode();
    }

    /* device returned illigal value*/
    @Test(expected = IOException.class)
    public void getInterruptModeTestAbnormal02() throws IOException, GridEyeDriverErrorException {
        doThrow(new IOException()).when(mockI2cDevice).readRegByte(0x03);
        IntruptMode actual = target.getInterruptMode();
    }

    @Test
    public void getStatusRegisterTest01() throws IOException {
        doReturn((byte) 0x00).when(mockI2cDevice).readRegByte(0x04);
        byte value = target.getStatusRegister();
        assertEquals(0x00, value);
    }

    @Test(expected = IOException.class)
    public void getStatusRegisterTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).readRegByte(0x04);
        byte value = target.getStatusRegister();
        assertEquals(0x00, value);
    }

    @Test
    public void clearRegisterTest01() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x00);
        target.clearRegister(false, false, false);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x00);
    }

    @Test
    public void clearRegisterTest02() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(true, false, false);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
    }

    @Test
    public void clearRegisterTest03() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(false, true, false);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x02);
    }

    @Test
    public void clearRegisterTest04() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(true, true, false);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x06);
    }

    @Test
    public void clearRegisterTest05() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(false, false, true);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x01);
    }

    @Test
    public void clearRegisterTest06() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(false, true, true);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x03);
    }

    @Test
    public void clearRegisterTest07() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x04);
        target.clearRegister(true, true, true);
        verify(mockI2cDevice).writeRegByte(0x05, (byte) 0x07);
    }

    @Test(expected = IOException.class)
    public void clearRegisterTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte(0x05, (byte) 0x07);
        target.clearRegister(true, true, true);
    }

    @Test
    public void enableTwiceMovingAverageModeTest01() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x07);
        target.enableTwiceMovingAverageMode();
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x50);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x45);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x57);
        verify(mockI2cDevice, times(1)).writeRegByte(0x07, (byte) 0x20);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x00);
    }

    @Test(expected = IOException.class)
    public void enableTwiceMovingAverageModeTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte(0x07, (byte) 0x20);
        target.enableTwiceMovingAverageMode();
    }

    @Test
    public void disableTwiceMovingAverageModeTest01() throws IOException {
        doNothing().when(mockI2cDevice).writeRegByte(0x05, (byte) 0x07);
        target.disableTwiceMovingAverageMode();
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x50);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x45);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x57);
        verify(mockI2cDevice, times(1)).writeRegByte(0x07, (byte) 0x00);
        verify(mockI2cDevice, times(1)).writeRegByte(0x1F, (byte) 0x00);
    }

    @Test(expected = IOException.class)
    public void disableTwiceMovingAverageModeTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).writeRegByte(0x07, (byte) 0x00);
        target.disableTwiceMovingAverageMode();
    }

    @Test
    public void isTwiceMovingAverageModeEnableTest01() throws IOException {
        doReturn((byte) 0x20).when(mockI2cDevice).readRegByte(0x07);
        boolean result = target.isTwiceMovingAverageModeEnable();
        assertTrue(result);
    }

    @Test
    public void isTwiceMovingAverageModeEnableTest02() throws IOException {
        doReturn((byte) 0x00).when(mockI2cDevice).readRegByte(0x07);
        boolean result = target.isTwiceMovingAverageModeEnable();
        assertTrue(!result);
    }

    @Test(expected = GridEyeDriverErrorException.class)
    public void isTwiceMovingAverageModeEnableTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        doReturn((byte) 0x01).when(mockI2cDevice).readRegByte(0x07);
        boolean result = target.isTwiceMovingAverageModeEnable();
        fail();
    }

    @Test(expected = IOException.class)
    public void isTwiceMovingAverageModeEnableTestAbnormal02() throws IOException, GridEyeDriverErrorException {
        doThrow(new IOException()).when(mockI2cDevice).readRegByte(0x07);
        boolean result = target.isTwiceMovingAverageModeEnable();
        fail();
    }

    @Test
    public void setInterruptLevelTest01() throws IOException {
        target.setInterruptLevel(80, 80, 80);
        byte[] expected = {0x40, 0x01, 0x40, 0x01, 0x40, 0x01};
        verify(mockI2cDevice).writeRegBuffer(0x08, expected, expected.length);
    }

    @Test
    public void setInterruptLevelTest02() throws IOException {
        target.setInterruptLevel(0, 0, 0);
        byte[] expected = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        verify(mockI2cDevice).writeRegBuffer(0x08, expected, expected.length);
    }

    @Test(expected = GridEyeDriverErrorException.class)
    public void setInterruptLevelTestAbnormal01() throws IOException, GridEyeDriverErrorException {
        target.setInterruptLevel(100, 0, 0);
    }

    @Test(expected = GridEyeDriverErrorException.class)
    public void setInterruptLevelTestAbnormal02() throws IOException {
        target.setInterruptLevel(0, 100, 0);
    }

    @Test(expected = GridEyeDriverErrorException.class)
    public void setInterruptLevelTestAbnormal03() throws IOException {
        target.setInterruptLevel(0, 0, 100);
    }

    @Test(expected = IOException.class)
    public void setInterruptLevelTestAbnormal04() throws IOException {
        byte[] payload = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        doThrow(new IOException()).when(mockI2cDevice).writeRegBuffer(0x08, payload, payload.length);
        target.setInterruptLevel(0, 0, 0);
    }

    @Test
    public void getIntHightLevelTest01() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[0] = 0x00;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x08, expect, 2);
        double result = target.getIntHightLevel();
        assertThat(result, is(0.0));
    }

    @Test
    public void getIntHightLevelTest02() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[1] = 0x0F;
                    result[0] = (byte) 0xFF;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x08, expect, 2);
        double result = target.getIntHightLevel();
        assertThat(result, is(-0.25));
    }

    @Test
    public void getIntHightLevelTest03() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[1] = 0x00;
                    result[0] = (byte) 0x64;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x08, expect, 2);
        double result = target.getIntHightLevel();
        assertThat(result, is(25.0));
    }

    @Test(expected = IOException.class)
    public void getIntHightLevelTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).readRegBuffer(0x08, new byte[]{0x00, 0x00}, 2);
        double result = target.getIntHightLevel();
    }

    @Test
    public void getIntLowLevelTest01() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[0] = 0x00;
                    result[1] = (byte) 0x00;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0A, expect, 2);
        double result = target.getIntLowLevel();
        assertThat(result, is(0.0));
    }

    @Test
    public void getIntLowLevelTest02() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[1] = 0x0F;
                    result[0] = (byte) 0xFF;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0A, expect, 2);
        double result = target.getIntLowLevel();
        assertThat(result, is(-0.25));
    }

    @Test
    public void getIntLowLevelTest03() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[1] = 0x00;
                    result[0] = (byte) 0x64;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0A, expect, 2);
        double result = target.getIntLowLevel();
        assertThat(result, is(25.0));
    }

    @Test(expected = IOException.class)
    public void getIntLowLevelTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).readRegBuffer(0x0A, new byte[]{0x00, 0x00}, 2);
        double result = target.getIntLowLevel();
    }

    @Test
    public void getHstLevelTest01() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[0] = 0x00;
                    result[1] = (byte) 0x00;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0C, expect, 2);
        double result = target.getHstLevel();
        assertThat(result, is(0.0));
    }

    @Test
    public void getHstLevelTest02() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[1] = 0x0F;
                    result[0] = (byte) 0xFF;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0C, expect, 2);
        double result = target.getHstLevel();
        assertThat(result, is(-0.25));
    }

    @Test
    public void getHstLevelTest03() throws IOException {
        byte[] expect = {0x00, 0x00};

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[1] = 0x00;
                    result[0] = (byte) 0x64;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0C, expect, 2);
        double result = target.getHstLevel();
        assertThat(result, is(25.0));
    }

    @Test(expected = IOException.class)
    public void getHstLevelTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).readRegBuffer(0x0C, new byte[]{0x00, 0x00}, 2);
        double result = target.getHstLevel();
    }

    @Test
    public void getThermisterTemplatureTest01() throws IOException {

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[0] = 0x00;
                    result[1] = (byte) 0x00;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0E, new byte[]{0x00, 0x00}, 2);

        double result = target.getThermisterTemplature();
        assertThat(result, is(0.0));
    }

    @Test
    public void getThermisterTemplatureTest02() throws IOException {

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[1] = (byte) 0x07;
                    result[0] = (byte) 0xFF;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0E, new byte[]{0x00, 0x00}, 2);

        double result = target.getThermisterTemplature();
        assertThat(result, is(127.9375));
    }

    @Test
    public void getThermisterTemplatureTest03() throws IOException {

        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[1] = (byte) 0x0F;
                    result[0] = (byte) 0xFC;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x0E, new byte[]{0x00, 0x00}, 2);

        double result = target.getThermisterTemplature();
        assertThat(result, is(-0.25));
    }

    @Test(expected = IOException.class)
    public void getThermisterTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).readRegBuffer(0x0E, new byte[]{0x00, 0x00}, 2);
        double result = target.getThermisterTemplature();
    }

    @Test
    public void getInterruptedPixelsTest01() throws IOException {
        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    result[0] = (byte) 0xFF;
                    result[1] = (byte) 0xFF;
                    result[2] = (byte) 0xFF;
                    result[3] = (byte) 0xFF;
                    result[4] = (byte) 0xFF;
                    result[5] = (byte) 0xFF;
                    result[6] = (byte) 0xFF;
                    result[7] = (byte) 0xFF;
                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x10, new byte[8], 8);

        byte[] result = target.getInterruptedPixels();
        Arrays.equals(result, new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,});
    }

    @Test(expected = IOException.class)
    public void getInterruptedPixelsTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).readRegBuffer(0x10, new byte[8], 8);

        byte[] result = target.getInterruptedPixels();
    }

    @Test
    public void getTemperaturesTest01() throws IOException {
        doAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args[1] instanceof byte[]) {
                    byte[] result = (byte[]) args[1];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = 0x00;
                    }

                }
                return null;
            }
        }).when(mockI2cDevice).readRegBuffer(0x10, new byte[32], 32);

        float[] result = target.getTemperatures();

        float[] expect = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Arrays.equals(result, expect);
    }

    @Test(expected = IOException.class)
    public void getTemperaturesTestAbnormal01() throws IOException {
        doThrow(new IOException()).when(mockI2cDevice).readRegBuffer(0x80, new byte[32], 32);
        target.getTemperatures();
    }

}