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

    private final BigInteger value;

    IVCounterImpl(byte[] data){
        this(new BigInteger(data));
    }

    IVCounterImpl(BigInteger IV){
        this.value = IV;
    }

    @Override
    public IVCounter incrementIV() {
        return new IVCounterImpl(value.add(BigInteger.valueOf(1)));
    }

    @Override
    public byte[] toByteArray() {
        return value.toByteArray();
    }

    @Override
    public byte[] getNonce() {
        return Arrays.copyOfRange(toByteArray(), 0, 8);
    }

    @Override
    public byte[] getCounter() {
        return Arrays.copyOfRange(toByteArray(), 8, 16);
    }

}
