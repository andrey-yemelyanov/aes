package helvidios.aes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilTest {
    @Test
    public void shouldConvertHexToByteArray(){
        var hex = "2b7e151628aed2a6abf7158809cf4f3c";
        var expected = new byte[]{
            0x2b, 0x7e, 0x15, 0x16, 
            0x28, (byte)0xae, (byte)0xd2, (byte)0xa6, 
            (byte)0xab, (byte)0xf7, 0x15, (byte)0x88, 
            0x09, (byte)0xcf, 0x4f, 0x3c
        };

        assertArrayEquals(expected, Util.toByteArray(hex));
    }   

    @Test
    public void shouldConvertByteArrayToHexString(){
        var bytes = new byte[]{
            0x2b, 0x7e, 0x15, 0x16, 
            0x28, (byte)0xae, (byte)0xd2, (byte)0xa6, 
            (byte)0xab, (byte)0xf7, 0x15, (byte)0x88, 
            0x09, (byte)0xcf, 0x4f, 0x3c
        };

        var expected = "2b7e151628aed2a6abf7158809cf4f3c";
        var actual = Util.toHexString(bytes);

        assertEquals(expected, actual);
    }
}
