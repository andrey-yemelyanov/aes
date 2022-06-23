package helvidios.aes;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BlockCipherTest {
    
    @Test
    public void shouldEncrypt(){
        var plaintext = "00112233445566778899aabbccddeeff";
        var key = "000102030405060708090a0b0c0d0e0f";

        BlockCipher cipher = new BlockCipherImpl();

        var expectedCipherText = "69c4e0d86a7b0430d8cdb78070b4c55a";
        var actualCipherText = Util.toHexString(cipher.encrypt(
            Util.toByteArray(plaintext), Util.toByteArray(key)));
        assertEquals(expectedCipherText, actualCipherText);
    }

    @Test
    public void shouldDecrypt(){
        var cipherText = "69c4e0d86a7b0430d8cdb78070b4c55a";
        var key = "000102030405060708090a0b0c0d0e0f";

        BlockCipher cipher = new BlockCipherImpl();

        var expectedPlaintext = "00112233445566778899aabbccddeeff";
        var actualPlaintext = Util.toHexString(cipher.decrypt(
            Util.toByteArray(cipherText), Util.toByteArray(key)));
        assertEquals(expectedPlaintext, actualPlaintext);
    }
}
