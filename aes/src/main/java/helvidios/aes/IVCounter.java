package helvidios.aes;

import java.math.BigInteger;
import java.util.Arrays;

interface IVCounter {

    static IVCounter generateRandom(){
        // first 64 bits represent nonce
        // the other 64 bits represent counter (starts at 0 for each new counter)
        var IV = new byte[16];
        var randomBytes = Util.randomBytes(8); // random nonce of 64 bits
        System.arraycopy(randomBytes, 0, IV, 0, randomBytes.length);
        Arrays.fill(IV, randomBytes.length, IV.length, (byte) 0); // init counter = 0
        return new IVCounterImpl(IV);
    }

    static IVCounter from(byte[] data){
        return new IVCounterImpl(data);
    }
    
    IVCounter incrementIV();

    byte[] toByteArray();

    byte[] getNonce();

    byte[] getCounter();
}

class IVCounterImpl implements IVCounter {

    private final byte[] IV;

    IVCounterImpl(byte[] IV){
        this.IV = IV;
    }

    @Override
    public IVCounter incrementIV() {
        var counter = Arrays.copyOfRange(IV, 8, 16);
        
        var incCounter = new BigInteger(counter)
            .add(BigInteger.valueOf(1))
            .toByteArray();
        
        var newIV = new byte[16];
        System.arraycopy(IV, 0, newIV, 0, 8);
        System.arraycopy(incCounter, 0, newIV, 16 - incCounter.length, incCounter.length);
        return new IVCounterImpl(newIV);
    }

    @Override
    public byte[] toByteArray() {
        return IV;
    }

    @Override
    public byte[] getNonce() {
        return Arrays.copyOfRange(IV, 0, 8);
    }

    @Override
    public byte[] getCounter() {
        return Arrays.copyOfRange(IV, 8, 16);
    }

}
