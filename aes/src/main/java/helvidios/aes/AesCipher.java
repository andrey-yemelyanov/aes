package helvidios.aes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface AesCipher {
    
    public static enum Mode{
        CipherBlockChaining,
        Counter,
        ElectronicCodeBook,
        GaloisCounter
    }

    static AesCipher forMode(Mode mode){
        var blockCipher = new BlockCipherImpl();
        if(mode == Mode.CipherBlockChaining) return new CbcCipher(blockCipher);
        if(mode == Mode.Counter) return new CtrCipher(blockCipher);
        throw new IllegalArgumentException(
            String.format("Unsupported AES cipher mode: %s", mode.toString())
        );
    }

    byte[] encrypt(byte[] data, byte[] key) throws IOException;
    byte[] decrypt(byte[] data, byte[] key) throws IOException;
    
    void encrypt(InputStream input, OutputStream output, byte[] key) throws IOException;
    void decrypt(InputStream input, OutputStream output, byte[] key) throws IOException;
}
