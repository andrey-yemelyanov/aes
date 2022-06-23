package helvidios.aes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

class PKCS5PaddingBlockIterator implements Iterator<byte[]> {

    private final Iterator<byte[]> source;
    private byte[] dummyBlock;

    PKCS5PaddingBlockIterator(Iterator<byte[]> source){
        this.source = Objects.requireNonNull(source, "source iterator must not be null");
    }

    @Override
    public boolean hasNext() {
        return dummyBlock != null || source.hasNext();
    }

    @Override
    public byte[] next() {
        if(dummyBlock != null){
            var block = Arrays.copyOf(dummyBlock, 16);
            dummyBlock = null;
            return block;
        }
        
        if(!source.hasNext()) throw new NoSuchElementException("No more blocks available");
        
        var currentBlock = source.next();

        var isLastBlock = !source.hasNext();
        if(isLastBlock){
            if(currentBlock.length == 16){ // pad with dummy block
                dummyBlock = new byte[16];
                Arrays.fill(dummyBlock, (byte) 16);
            }else{ // pad with missing bytes until length of 16
                var paddedBlock = new byte[16];
                System.arraycopy(currentBlock, 0, paddedBlock, 0, currentBlock.length);
                Arrays.fill(paddedBlock, currentBlock.length, paddedBlock.length, (byte) (16 - currentBlock.length));
                return paddedBlock;
            }
        }
     
        return currentBlock;
    }
    
}
