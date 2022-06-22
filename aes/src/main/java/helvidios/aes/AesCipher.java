package helvidios.aes;

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
        if(mode == Mode.CipherBlockChaining) return new CbcCipher();
        if(mode == Mode.Counter) return new CtrCipher();
        throw new IllegalArgumentException(
            String.format("Unsupported AES cipher mode: %s", mode.toString())
        );
    }

    byte[] encrypt(byte[] data, byte[] key);
    byte[] decrypt(byte[] data, byte[] key);
    
    void encrypt(InputStream input, OutputStream output, byte[] key);
    void decrypt(InputStream input, OutputStream output, byte[] key);
}
