package helvidios.aes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class AesCipherTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgExcOnUnsupportedMode(){
        AesCipher.forMode(AesCipher.Mode.GaloisCounter);
    }

    @Test
    public void encryptCbcWithPadding() throws Exception{
        var oneBlockWithPadding = new byte[] {
            1, 2, 3, 4, 5
        };

        var cipher = AesCipher.forMode(AesCipher.Mode.CipherBlockChaining);
        var key = AesKey.fromPassword("qwerty", AesKeyType._128_bit)
                        .toByteArray();

        var expected = Util.toHexString(oneBlockWithPadding);
        var actual = Util.toHexString(cipher.decrypt(cipher.encrypt(oneBlockWithPadding, key), key));

        assertEquals(expected, actual);
    }

    @Test
    public void shouldEncryptWithCBCMode() throws Exception {
        var cipher = AesCipher.forMode(AesCipher.Mode.CipherBlockChaining);
        assertTrue("Cipher must be CBC cipher", cipher instanceof CbcCipher);

        var plaintext = "A random number generator isolated to the current thread. Like the global Random generator used by the Math class, a ThreadLocalRandom is initialized with an internally generated seed that may not otherwise be modified. When applicable, use of ThreadLocalRandom rather than shared Random objects in concurrent programs will typically encounter much less overhead and contention. Use of ThreadLocalRandom is particularly appropriate when multiple tasks (for example, each a ForkJoinTask) use random numbers in parallel in thread pools.Usages of this class should typically be of the form: ThreadLocalRandom.current().nextX(...) (where X is Int, Long, etc). When all usages are of this form, it is never possible to accidently share a ThreadLocalRandom across multiple threads. This class also provides additional commonly used bounded random generation methods.";
        var plaintextBytes = plaintext.getBytes("ASCII");

        var key = AesKey.fromPassword("glksjdg3j4,';,_f4", AesKeyType._128_bit)
                        .toByteArray();

        var expected = plaintext;
        var actual = new String(cipher.decrypt(cipher.encrypt(plaintextBytes, key), key), "ASCII");

        //System.out.println(expected);
        //System.out.println(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void decryptCoursera() throws IOException{

        var cipher = AesCipher.forMode(AesCipher.Mode.CipherBlockChaining);

        var key = "140b41b22a29beb4061bda66b6747e14";
        var ciphertext = "4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81";

        System.out.println(new String(cipher.decrypt(
            Util.toByteArray(ciphertext), 
            Util.toByteArray(key))));

        key = "140b41b22a29beb4061bda66b6747e14";
        ciphertext = "5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253";

        System.out.println(new String(cipher.decrypt(
            Util.toByteArray(ciphertext), 
            Util.toByteArray(key))));
    }
}
