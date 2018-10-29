package ka.masato.grideyelib.driver;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManager;
import ka.masato.grideyelib.driver.enums.FrameRate;
import ka.masato.grideyelib.driver.enums.IntruptMode;
import ka.masato.grideyelib.driver.exception.GridEyeDriverErrorException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

    public void close() throws IOException, GridEyeDriverErrorException {
        if (i2cDevice == null) {
            throw new GridEyeDriverErrorException("GridEyeDevice can not close before device open.");
        }
        i2cDevice.close();
        mManager = null;
        i2cDevice = null;
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
        if (FrameRate.FRAMERATE_ONE.getValue() != result && FrameRate.FRAMERATE_TEN.getValue() != result) {
            throw new GridEyeDriverErrorException("GridEye returned illigal parameter. frameRate is " + result);
        }
        return FrameRate.FRAMERATE_TEN.getValue() == (int) result ?
                FrameRate.FRAMERATE_TEN : FrameRate.FRAMERATE_ONE;
    }

    public void setInterruptMode(IntruptMode intruptMode) throws IOException {
        i2cDevice.writeRegByte(0x03, intruptMode.getValue());
    }

    public IntruptMode getInterruptMode() throws IOException {

        byte ret = i2cDevice.readRegByte(0x03);
        IntruptMode[] types = IntruptMode.values();
        IntruptMode result = null;
        for (IntruptMode type : types) {
            if (type.getValue() == ret) {
                result = type;
            }
        }
        if (result == null) {
            throw new GridEyeDriverErrorException("GridEye returned illigal parameter. frameRate is " + ret);
        }
        return result;
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


    public void enableTwiceMovingAverageMode() throws IOException {
        setAverageMode(true);
    }

    public void disableTwiceMovingAverageMode() throws IOException {
        setAverageMode(false);
    }

    private void setAverageMode(boolean toggle) throws IOException {
        byte value = toggle ? (byte) 0x20 : 0x00;
        i2cDevice.writeRegByte((byte) 0x1F, (byte) 0x50);
        i2cDevice.writeRegByte((byte) 0x1F, (byte) 0x45);
        i2cDevice.writeRegByte((byte) 0x1F, (byte) 0x57);
        i2cDevice.writeRegByte(0x07, value);
        i2cDevice.writeRegByte((byte) 0x1F, (byte) 0x00);
    }

    public boolean isTwiceMovingAverageModeEnable() throws IOException {
        byte ret = i2cDevice.readRegByte(0x07);
        if (ret != (byte) 0x20 && ret != (byte) 0x00) {
            throw new GridEyeDriverErrorException("Illigal TwiceMovingAverageMode Register value.");
        }
        return ret == (byte) 0x20;
    }

    public void setInterruptLevel(double intHighLevel, double intLowLevel, double hstLevel)
            throws GridEyeDriverErrorException, IOException {

        if (validateTempratureLimit(intHighLevel)) {
            throw new GridEyeDriverErrorException("INT_LEVEL_HIGHT is sensor limit over.");
        }

        if (validateTempratureLimit(intLowLevel)) {
            throw new GridEyeDriverErrorException("INT_LEVEL_LOW is sensor limit over.");
        }

        if (validateTempratureLimit(hstLevel)) {
            throw new GridEyeDriverErrorException("HYSTERISYS_LEVEL is sensor limit over.");
        }

        ByteBuffer payload = ByteBuffer.allocate(6);
        payload.put(getTwelveBitFromShort((short) (intHighLevel / 0.25)));
        payload.put(getTwelveBitFromShort((short) (intLowLevel / 0.25)));
        payload.put(getTwelveBitFromShort((short) (hstLevel / 0.25)));

        i2cDevice.writeRegBuffer(0x08, payload.array(), payload.array().length);

    }

    public double getIntHightLevel() throws IOException {
        byte[] result = new byte[2];
        i2cDevice.readRegBuffer(0x08, result, result.length);
        return (double) getShortFromTwelveBit(result) * 0.25;
    }

    public double getIntLowLevel() throws IOException {
        byte[] result = new byte[2];
        i2cDevice.readRegBuffer(0x0A, result, result.length);
        return (double) getShortFromTwelveBit(result) * 0.25;
    }

    public double getHstLevel() throws IOException {
        byte[] result = new byte[2];
        i2cDevice.readRegBuffer(0x0C, result, result.length);
        return (double) getShortFromTwelveBit(result) * 0.25;
    }

    public double getThermisterTemplature() throws IOException {
        byte[] result = new byte[2];
        i2cDevice.readRegBuffer(0x0E, result, result.length);
        return (double) getShortFromTwelveBit(result) * 0.0625;
    }


    public byte[] getInterruptedPixels() throws IOException {
        byte[] result = new byte[8];
        i2cDevice.readRegBuffer(0x10, result, result.length);
        return result;
    }

    public float[] getTemperatures() throws IOException {

        ByteBuffer bb = ByteBuffer.allocate(8 * 8 * 2).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < 4; i++) {
            byte[] buffer = new byte[32];
            i2cDevice.readRegBuffer(0x80 + (32 * i), buffer, 32);
            bb.put(buffer);
        }

        float[] results = new float[64];
        byte[] twoByteBuffer = new byte[2];
        for (int i = 0; i < 64; i++) {
            bb.position(i * 2).limit((i * 2) + 2);
            bb.get(twoByteBuffer);
            results[i] = (float) ((float) getShortFromTwelveBit(twoByteBuffer) * 0.25);
        }

        return results;
    }


    private boolean validateTempratureLimit(double value) {
        //AMG8833 is below temprature range.
        return 0.000 > value || value > 80.000;

    }

    private byte[] getTwelveBitFromShort(short value) {
        ByteBuffer bb = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(value);
        byte[] result = bb.array();
        result[1] &= 0x0F;
        return result;
    }

    private short getShortFromTwelveBit(byte[] value) {
        if (value.length != 2) {
            throw new GridEyeDriverErrorException("Value should be 2 byte length.");
        }

        if ((value[1] & 0x08) == 0x08) {
            value[1] |= 0xF0;
        }
        return ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }



}
