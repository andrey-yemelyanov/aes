package helvidios.aes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AesCipherTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgExcOnUnsupportedMode(){
        AesCipher.forMode(AesCipher.Mode.GaloisCounter);
    }

    @Test
    public void shouldEncryptWithCBCMode() throws Exception {
        var cipher = AesCipher.forMode(AesCipher.Mode.CipherBlockChaining);
        assertTrue("Cipher must be CBC cipher", cipher instanceof CbcCipher);

        var plaintext = "A random number generator isolated to the current thread. Like the global Random generator used by the Math class, a ThreadLocalRandom is initialized with an internally generated seed that may not otherwise be modified. When applicable, use of ThreadLocalRandom rather than shared Random objects in concurrent programs will typically encounter much less overhead and contention. Use of ThreadLocalRandom is particularly appropriate when multiple tasks (for example, each a ForkJoinTask) use random numbers in parallel in thread pools.Usages of this class should typically be of the form: ThreadLocalRandom.current().nextX(...) (where X is Int, Long, etc). When all usages are of this form, it is never possible to accidently share a ThreadLocalRandom across multiple threads. This class also provides additional commonly used bounded random generation methods.";
        var plaintextBytes = plaintext.getBytes("ASCII");

        System.out.println(plaintextBytes.length);

        var key = AesKey
                    .fromPassword("glksjdg3j4,';,_f4", AesKeyType._128_bit)
                    .toByteArray();

        assertEquals(plaintext, cipher.decrypt(cipher.encrypt(plaintextBytes, key), key));
    }
}
