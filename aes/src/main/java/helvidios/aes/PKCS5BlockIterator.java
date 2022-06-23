package helvidios.aes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

class PKCS5BlockIterator implements Iterator<byte[]> {

    private final InputStream input;
    private final byte[] buffer;
    private boolean blockAvailable;
    private boolean paddingPerformed;

    private final boolean useDummyBlock;

    PKCS5BlockIterator(InputStream input){
        this(input, true);
    }

    PKCS5BlockIterator(InputStream input, boolean useDummyBlock){
        this.input = Objects.requireNonNull(input, "input stream must not be null");
        this.buffer = new byte[16];
        this.useDummyBlock = useDummyBlock;

        blockAvailable = readNextBlock();
    }

    private boolean readNextBlock(){
        try {
            var bytesRead = input.read(buffer, 0, 16);
            if(bytesRead == -1){
                if(useDummyBlock){
                    padBuffer(0);
                    return true;
                }
                return false;
            }
            else if(bytesRead < 16) padBuffer(bytesRead);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void padBuffer(int offset){
        paddingPerformed = true;
        var paddingLen = 16 - offset;
        Arrays.fill(buffer, offset, buffer.length, (byte) paddingLen);
    }

    @Override
    public boolean hasNext() {
        return blockAvailable;
    }

    @Override
    public byte[] next() {
        if(!blockAvailable) throw new NoSuchElementException("No more blocks available");
        
        var currentBlock = Arrays.copyOf(buffer, 16);

        if(paddingPerformed){
            blockAvailable = false;
        }else{
            blockAvailable = readNextBlock();
        }
     
        return currentBlock;
    }
    
}
