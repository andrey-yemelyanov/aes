package helvidios.aes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

class CtrCipher implements AesCipher {

    private final BlockCipher blockCipher;

    CtrCipher(BlockCipher blockCipher){
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
        Objects.requireNonNull(input, "input stream must not be null");
        Objects.requireNonNull(output, "output stream must not be null");
        
        var counter = IVCounter.generateRandom();
        output.write(counter.toByteArray());

        for(var blockIterator = new BlockIterator(input); blockIterator.hasNext();){
            var block = blockIterator.next();
            var streamKey = blockCipher.encrypt(counter.toByteArray(), key);
            output.write(Util.xor(block, streamKey));
            counter = counter.incrementIV();
        }

        output.flush();
    }

    @Override
    public void decrypt(InputStream input, OutputStream output, byte[] key) throws IOException {
        Objects.requireNonNull(input, "input stream must not be null");
        Objects.requireNonNull(output, "output stream must not be null");

        var blockIterator = new BlockIterator(input);

        if(!blockIterator.hasNext()) throw new IllegalStateException(
            "Invalid input stream"
        );

        var counter = IVCounter.from(blockIterator.next());

        while(blockIterator.hasNext()){
            var block = blockIterator.next();
            var streamKey = blockCipher.encrypt(counter.toByteArray(), key);
            output.write(Util.xor(block, streamKey));
            counter = counter.incrementIV();
        }

        output.flush();
    }
    
}
