package helvidios.aes;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class CbcCipher implements AesCipher {

    private final BlockCipher cipher = new BlockCipher();

    private static byte[] randomIV(){
        var IV = new byte[16];
        for(var i = 0; i < IV.length; i++){
            IV[i] = (byte) ThreadLocalRandom.current().nextInt();
        }
        return IV;
    }

    private List<byte[]> getBlocks(byte[] data){
        var blocks = new ArrayList<byte[]>();

        return blocks;
    }

    private byte[] xor(byte[] b1, byte[] b2){
        if(b1.length != b2.length) throw new IllegalArgumentException(
            "Byte array lengths must be equal"
        );

        var xorResult = new byte[b1.length];
        for(var i = 0; i < b1.length; i++){
            xorResult[i] = (byte) (b1[i] ^ b2[i]);
        }

        return xorResult;
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        var blocks = getBlocks(data);
        if(blocks.isEmpty()) throw new IllegalArgumentException("data is empty, nothing to encrypt");

        var iv = randomIV();
        var output = new byte[(blocks.size() + 1) * 16];
        System.arraycopy(iv, 0, output, 0, iv.length);
        var offset = iv.length;
        var prevBlock = iv;

        for(var block : blocks){
            block = xor(block, prevBlock);
            var cipherBytes = cipher.encrypt(block, key);
            System.arraycopy(cipherBytes, 0, output, offset, cipherBytes.length);
            offset += cipherBytes.length;
            prevBlock = cipherBytes;
        }

        return output;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void encrypt(InputStream input, OutputStream output, byte[] key) {
        throw new IllegalStateException("Encryption with I/O streams not supported in CBC mode.");
    }

    @Override
    public void decrypt(InputStream input, OutputStream output, byte[] key) {
        throw new IllegalStateException("Decryption with I/O streams not supported in CBC mode.");
    }
    
}
