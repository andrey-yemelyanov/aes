package helvidios.aes;

import static org.junit.Assert.*;
import org.junit.Test;

public class AesKeyTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgExceptionForInvalidKeyLen()
    {
        AesKey.from(new byte[] {1, 2, 3, 4, 5});
    }

    @Test
    public void shouldCreate128BitKey(){
        assertNotNull(AesKey.from(new byte[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgExceptionIfRoundNeg(){
        var cipherKey = "2b7e151628aed2a6abf7158809cf4f3c";
        var key = AesKey.from(Util.toByteArray(cipherKey));
        key.getSubkey(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgExceptionIfRoundExceedsMaxRounds(){
        var cipherKey = "2b7e151628aed2a6abf7158809cf4f3c";
        var key = AesKey.from(Util.toByteArray(cipherKey));
        key.getSubkey(11);
    }

    @Test
    public void shouldExpand128BitKey(){
        var cipherKey = "2b7e151628aed2a6abf7158809cf4f3c";

        var key = AesKey.from(Util.toByteArray(cipherKey));

        var round0SubKey = key.getSubkey(0);
        var expected = Util.toByteArray("2b7e151628aed2a6abf7158809cf4f3c");
        assertArrayEquals(expected, round0SubKey);

        var round1SubKey = key.getSubkey(1);
        expected = Util.toByteArray("a0fafe1788542cb123a339392a6c7605");
        assertArrayEquals(expected, round1SubKey);

        var round2SubKey = key.getSubkey(2);
        expected = Util.toByteArray("f2c295f27a96b9435935807a7359f67f");
        assertArrayEquals(expected, round2SubKey);

        var round3SubKey = key.getSubkey(3);
        expected = Util.toByteArray("3d80477d4716fe3e1e237e446d7a883b");
        assertArrayEquals(expected, round3SubKey);

        var round4SubKey = key.getSubkey(4);
        expected = Util.toByteArray("ef44a541a8525b7fb671253bdb0bad00");
        assertArrayEquals(expected, round4SubKey);

        var round5SubKey = key.getSubkey(5);
        expected = Util.toByteArray("d4d1c6f87c839d87caf2b8bc11f915bc");
        assertArrayEquals(expected, round5SubKey);

        var round6SubKey = key.getSubkey(6);
        expected = Util.toByteArray("6d88a37a110b3efddbf98641ca0093fd");
        assertArrayEquals(expected, round6SubKey);

        var round7SubKey = key.getSubkey(7);
        expected = Util.toByteArray("4e54f70e5f5fc9f384a64fb24ea6dc4f");
        assertArrayEquals(expected, round7SubKey);

        var round8SubKey = key.getSubkey(8);
        expected = Util.toByteArray("ead27321b58dbad2312bf5607f8d292f");
        assertArrayEquals(expected, round8SubKey);

        var round9SubKey = key.getSubkey(9);
        expected = Util.toByteArray("ac7766f319fadc2128d12941575c006e");
        assertArrayEquals(expected, round9SubKey);

        var round10SubKey = key.getSubkey(10);
        expected = Util.toByteArray("d014f9a8c9ee2589e13f0cc8b6630ca6");
        assertArrayEquals(expected, round10SubKey);
    }

    @Test
    public void shouldGenerateAesKey() throws Exception{
        final String password = "sglkjg034j33??4jggg,/,40igkeg23;";
        var _128BitKey = AesKey.fromPassword(password, AesKeyType._128_bit);
        var _192BitKey = AesKey.fromPassword(password, AesKeyType._192_bit);
        var _256BitKey = AesKey.fromPassword(password, AesKeyType._256_bit);

        System.out.println(_128BitKey);
        System.out.println(_192BitKey);
        System.out.println(_256BitKey);

        assertEquals("32542f46146d2c12657043f074c0b4b6", _128BitKey.toString());
        assertEquals("32542f46146d2c12657043f074c0b4b666adf8adc1f6242b", _192BitKey.toString());
        assertEquals("32542f46146d2c12657043f074c0b4b666adf8adc1f6242bc7db1b30b2e54074", _256BitKey.toString());

        assertEquals(16, _128BitKey.length());
        assertEquals(24, _192BitKey.length());
        assertEquals(32, _256BitKey.length());
    }
}
