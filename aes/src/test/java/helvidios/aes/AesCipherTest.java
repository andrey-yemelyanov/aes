package helvidios.aes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;
import helvidios.aes.AesCipher.Mode;

public class AesCipherTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgExcOnUnsupportedMode(){
        AesCipher.forMode(AesCipher.Mode.GaloisCounter);
    }

    @Test
    public void shouldEncryptWithCbcCipher() throws Exception{
        var cbcCipher = AesCipher.forMode(Mode.CipherBlockChaining);
        assertTrue(cbcCipher instanceof CbcCipher);
        encryptLargeText(cbcCipher);
        encryptOneBlockLessThan16Bytes(cbcCipher);
        encryptOneBlockOf16Bytes(cbcCipher);
        encryptTwoBlocks(cbcCipher);
        encryptTwoBlocks32Bytes(cbcCipher);
    }

    @Test
    public void shouldEncryptWithCtrCipher() throws Exception{
        var ctrCipher = AesCipher.forMode(Mode.Counter);
        assertTrue(ctrCipher instanceof CtrCipher);
        encryptLargeText(ctrCipher);
        encryptOneBlockLessThan16Bytes(ctrCipher);
        encryptOneBlockOf16Bytes(ctrCipher);
        encryptTwoBlocks(ctrCipher);
        encryptTwoBlocks32Bytes(ctrCipher);
    }

    public void encryptOneBlockLessThan16Bytes(AesCipher cipher) throws Exception{
        var oneBlockWithPadding = new byte[] {
            1, 2, 3, 4, 5
        };

        var key = AesKey.fromPassword("qwerty", AesKeyType._128_bit)
                        .toByteArray();

        var expected = Util.toHexString(oneBlockWithPadding);
        var actual = Util.toHexString(cipher.decrypt(cipher.encrypt(oneBlockWithPadding, key), key));

        assertEquals(expected, actual);
    }

    public void encryptOneBlockOf16Bytes(AesCipher cipher) throws Exception{
        var oneBlockNoPadding = new byte[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
        };

        var key = AesKey.fromPassword("qwerty", AesKeyType._128_bit)
                        .toByteArray();

        var expected = Util.toHexString(oneBlockNoPadding);
        var actual = Util.toHexString(cipher.decrypt(cipher.encrypt(oneBlockNoPadding, key), key));

        assertEquals(expected, actual);
    }

    public void encryptTwoBlocks(AesCipher cipher) throws Exception{
        var twoBlocksWithPadding = new byte[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 
            17, 18, 19, 20
        };

        var key = AesKey.fromPassword("qwerty", AesKeyType._128_bit)
                        .toByteArray();

        var expected = Util.toHexString(twoBlocksWithPadding);
        var actual = Util.toHexString(cipher.decrypt(cipher.encrypt(twoBlocksWithPadding, key), key));

        assertEquals(expected, actual);
    }

    public void encryptTwoBlocks32Bytes(AesCipher cipher) throws Exception{
        var twoBlocksNoPadding = new byte[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 
            17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32
        };

        var key = AesKey.fromPassword("qwerty", AesKeyType._128_bit)
                        .toByteArray();

        var expected = Util.toHexString(twoBlocksNoPadding);
        var actual = Util.toHexString(cipher.decrypt(cipher.encrypt(twoBlocksNoPadding, key), key));

        assertEquals(expected, actual);
    }

    public void encryptLargeText(AesCipher cipher) throws Exception {

        var plaintext = "A random number generator isolated to the current thread. Like the global Random generator used by the Math class, a ThreadLocalRandom is initialized with an internally generated seed that may not otherwise be modified. When applicable, use of ThreadLocalRandom rather than shared Random objects in concurrent programs will typically encounter much less overhead and contention. Use of ThreadLocalRandom is particularly appropriate when multiple tasks (for example, each a ForkJoinTask) use random numbers in parallel in thread pools.Usages of this class should typically be of the form: ThreadLocalRandom.current().nextX(...) (where X is Int, Long, etc). When all usages are of this form, it is never possible to accidently share a ThreadLocalRandom across multiple threads. This class also provides additional commonly used bounded random generation methods.";
        var plaintextBytes = plaintext.getBytes("ASCII");

        var key = AesKey.fromPassword("mysecretkey", AesKeyType._128_bit)
                        .toByteArray();

        var expected = plaintext;
        var actual = new String(cipher.decrypt(cipher.encrypt(plaintextBytes, key), key), "ASCII");

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

        
        cipher = AesCipher.forMode(AesCipher.Mode.Counter);

        key = "36f18357be4dbd77f050515c73fcf9f2";
        ciphertext = "69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329";

        System.out.println(new String(cipher.decrypt(
            Util.toByteArray(ciphertext), 
            Util.toByteArray(key))));

        key = "36f18357be4dbd77f050515c73fcf9f2";
        ciphertext = "770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451";

        System.out.println(new String(cipher.decrypt(
            Util.toByteArray(ciphertext), 
            Util.toByteArray(key))));
    }
}
