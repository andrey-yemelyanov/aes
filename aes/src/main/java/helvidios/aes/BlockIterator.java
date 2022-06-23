package helvidios.aes;

import java.io.*;
import java.util.*;

public class BlockIterator implements Iterator<byte[]> {

    private final InputStream input;
    private final byte[] buffer;
    private boolean blockAvailable;
    private int blockLen;

    BlockIterator(InputStream input){
        this.input = Objects.requireNonNull(input, "input stream must not be null");
        this.buffer = new byte[16];
        blockAvailable = readNextBlock();
    }

    private boolean readNextBlock(){
        try {
            blockLen = input.read(buffer, 0, 16);
            return blockLen > 0;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean hasNext() {
        return blockAvailable;
    }

    @Override
    public byte[] next() {
        if(!blockAvailable) throw new NoSuchElementException("No more blocks available");
        var currentBlock = Arrays.copyOf(buffer, blockLen);
        blockAvailable = readNextBlock();
        return currentBlock;
    }
    
}
