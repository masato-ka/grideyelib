package ka.masato.grideyelib.driver;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManager;
import ka.masato.grideyelib.driver.enums.FrameRate;
import ka.masato.grideyelib.driver.enums.IntruptMode;

import java.io.IOException;

public class GridEyeDriver {

    private PeripheralManager mManager;
    private I2cDevice i2cDevice;

    private GridEyeDriver() {
    }

    private static class InstanceHolder {
        public static final GridEyeDriver hasInstance = new GridEyeDriver();
    }

    public static GridEyeDriver getInstance() {
        return InstanceHolder.hasInstance;
    }

    public void setmPeripheralManager(PeripheralManager mManager) {
        this.mManager = mManager;
    }

    public void open(String i2CName, int address) throws IOException {
        i2cDevice = mManager.openI2cDevice(i2CName, address);
    }

    public void close() throws IOException {
        i2cDevice.close();
    }

    public void flagRest() throws IOException {
        resetRegister((byte) 0x30);
    }

    public void initialRest() throws IOException {
        resetRegister((byte) 0x3F);
    }

    private void resetRegister(byte value) throws IOException {
        i2cDevice.writeRegByte(0x01, value);
    }

    public void setFrameRate(FrameRate frameRate) throws IOException {
        i2cDevice.writeRegByte(0x02, (byte) frameRate.getValue());
    }

    public FrameRate getFrameRate() throws IOException {
        byte result = i2cDevice.readRegByte(0x02);
        return FrameRate.FRAMERATE_TEN.getValue() == (int) result ?
                FrameRate.FRAMERATE_TEN : FrameRate.FRAMERATE_ONE;
    }

    public void setInterruptMode(IntruptMode intruptMode) throws IOException {
        i2cDevice.writeRegByte(0x03, intruptMode.getValue());
    }

    public IntruptMode getInterruptMode() throws IOException {
        byte result = i2cDevice.readRegByte(0x03);
        if (IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE.getValue() == result) {
            return IntruptMode.ABSOLUTE_VALUE_INT_ACTIVE;
        }
        if (IntruptMode.ABSOLUTE_VALUE_INT_DEACTIVE.getValue() == result) {
            return IntruptMode.ABSOLUTE_VALUE_INT_DEACTIVE;
        }
        if (IntruptMode.DIFFERENT_INT_ACTIVE.getValue() == result) {
            return IntruptMode.DIFFERENT_INT_ACTIVE;
        }
        return IntruptMode.DIFFERENT_INT_DEACTIVE;

    }


    public byte getStatusRegister() throws IOException {
        byte result = i2cDevice.readRegByte(0x04);
        return result;
    }

    public void clearRegister(boolean OVT_CLR, boolean OVS_CLR, boolean INT_CLR) throws IOException {
        byte value = 0x00;
        value |= OVT_CLR ? 0x04 : 0x00;
        value |= OVS_CLR ? 0x02 : 0x00;
        value |= INT_CLR ? 0x01 : 0x00;

        i2cDevice.writeRegByte(0x05, value);

    }


    public void enableTwiveMovingAverageMode() throws IOException {
        setAverageMode(true);
    }

    public void disableTwiceMovingAverageMode() throws IOException {
        setAverageMode(false);
    }

    public void setAverageMode(boolean toggle) throws IOException {
        byte value = toggle ? (byte) 0x20 : 0x00;
        i2cDevice.writeRegByte((byte) 0x1F, (byte) 0x50);
        i2cDevice.writeRegByte((byte) 0x1F, (byte) 0x45);
        i2cDevice.writeRegByte((byte) 0x1F, (byte) 0x57);
        i2cDevice.writeRegByte(0x07, value);
        i2cDevice.writeRegByte((byte) 0x1F, (byte) 0x00);
    }

    public boolean isTwiceMovingAverageModeEnable() throws IOException {
        byte ret = i2cDevice.readRegByte(0x07);
        return ret == (byte) 0x20;
    }

    public void setInterruptLevel(short intHighLevel, short intLowLevel, short hstLevel) {

        if (127.9375 >= intHighLevel && intHighLevel >= -59.6875) {

        }

        if (127.9375 >= intLowLevel && intLowLevel >= -59.6875) {

        }

        if (127.9375 >= hstLevel && hstLevel >= -59.6875) {

        }

    }

    private byte[] secondComplement(short value) {


        return new byte[2];
    }

}
