package ka.masato.library.device.grideyelib;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void sate() {
        short value = 100;
        short minusValue = -100;

        String plus = Integer.toBinaryString(value);
        String minus = Integer.toBinaryString(minusValue);
        //最上位が1だったらFFをつける。

        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0x24, (byte) 0x00, (byte) 0x01, (byte) 0xFF, (byte) 0x24}).order(ByteOrder.BIG_ENDIAN);
        buffer.position(0).limit(2);
        byte a = buffer.get();
        byte b = buffer.get();


        String ms = Integer.toBinaryString(minusValue);


    }

}