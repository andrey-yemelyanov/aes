package helvidios.aes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

class CbcCipher implements AesCipher {

    private final BlockCipher blockCipher;

    CbcCipher(BlockCipher blockCipher){
        this.blockCipher = Objects.requireNonNull(blockCipher, "blockCipher must not be null");
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) throws IOException {
        try(var input = new ByteArrayInputStream(data);
            var output = new ByteArrayOutputStream()){
            encrypt(input, output, key);
            return output.toByteArray();
        }
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) throws IOException {
        try(var input = new ByteArrayInputStream(data);
            var output = new ByteArrayOutputStream()){
            decrypt(input, output, key);
            return output.toByteArray();
        }
    }

    @Override
    public void encrypt(InputStream input, OutputStream output, byte[] key) throws IOException {
        Objects.requireNonNull(output, "output stream must not be null");
        
        var prevBlock = blockCipher.encrypt(Util.randomBytes(16), key); // random IV vector
        // write IV vector to the output stream as prefix of cipher text
        output.write(prevBlock);
        
        for(var blockIterator = new PKCS5BlockIterator(input); blockIterator.hasNext();){
            var block = blockIterator.next();
            block = blockCipher.encrypt(Util.xor(block, prevBlock), key);
            output.write(block);
            prevBlock = block;
        }

        output.flush();
    }

    @Override
    public void decrypt(InputStream input, OutputStream output, byte[] key) throws IOException {
        Objects.requireNonNull(output, "output stream must not be null");

        var blockIterator = new PKCS5BlockIterator(input, false);

        if(!blockIterator.hasNext()) throw new IllegalStateException(
            "Invalid input stream"
        );

        var prevBlock = blockIterator.next(); // get encrypted IV

        while(blockIterator.hasNext()){
            var block = blockIterator.next();
            var decryptedBlock = Util.xor(blockCipher.decrypt(block, key), prevBlock);
            prevBlock = block;

            var lastBlock = !blockIterator.hasNext();
            if(lastBlock){
                var lastByte = decryptedBlock[decryptedBlock.length - 1];
                if(lastByte == 16) break;
                output.write(Arrays.copyOfRange(decryptedBlock, 0, 16 - lastByte));
                break;
            }

            output.write(decryptedBlock);
        }

        output.flush();
    }
    
}
