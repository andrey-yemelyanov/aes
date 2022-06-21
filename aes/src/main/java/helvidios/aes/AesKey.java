package helvidios.aes;

import java.util.Objects;

interface AesKey {

    static AesKey from(byte[] data){
        Objects.requireNonNull(data, "Key data must not be null");
        var keyLen = data.length * 8;
        if(keyLen == 128) return new _128BitAesKey(data);
        if(keyLen == 192) return new _192BitAesKey(data);
        if(keyLen == 256) return new _256BitAesKey(data);
        throw new IllegalArgumentException(String.format("Key of length %d bits is not supported.", keyLen));
    }

    byte[] getSubkey(int round);

    int nRounds();
}

abstract class AbstractAesKey implements AesKey {

    private final int[] W; // key expansion array

    AbstractAesKey(byte[] key) {
        Objects.requireNonNull(key, "key must not be null");
        this.W = expandKey(key);
    }

    abstract int[] expandKey(byte[] key);

    @Override
    public byte[] getSubkey(int round) {

        if(round < 0 || round > nRounds()){
            throw new IllegalArgumentException(
                String.format("round must be between 0 and %d", nRounds())
            );
        }

        var subkey = new byte[16]; // 128 bit subkey

        for(var i = 4 * round; i < 4 * (round + 1); i++){
            var byte1 = (byte)((W[i] >> 24) & 0xFF);
            var byte2 = (byte)((W[i] >> 16) & 0xFF);
            var byte3 = (byte)((W[i] >> 8) & 0xFF);
            var byte4 = (byte)( W[i] & 0xFF);

            var j = (i - 4 * round) * 4;

            subkey[j]     = byte1;
            subkey[j + 1] = byte2;
            subkey[j + 2] = byte3;
            subkey[j + 3] = byte4;
        }

        return subkey;
    }

    int rotateWord(int w){
        var b1 = (byte) ((w >> 24) & 0xFF);
        var b2 = (byte) ((w >> 16) & 0xFF);
        var b3 = (byte) ((w >> 8) & 0xFF);
        var b4 = (byte) (w & 0xFF);
        return toInt(b2, b3, b4, b1);
    }

    int subWord(int w){
        var b1 = (byte) ((w >> 24) & 0xFF);
        var b2 = (byte) ((w >> 16) & 0xFF);
        var b3 = (byte) ((w >> 8) & 0xFF);
        var b4 = (byte) (w & 0xFF);
        return toInt(
            SBox.substitute(b1), 
            SBox.substitute(b2), 
            SBox.substitute(b3), 
            SBox.substitute(b4)
        );
    }

    int toInt(byte b1, byte b2, byte b3, byte b4) {
        return  ((b1 & 0xFF) << 24) |
                ((b2 & 0xFF) << 16) |
                ((b3 & 0xFF) << 8) |
                ((b4 & 0xFF) << 0);
    }
}

class _128BitAesKey extends AbstractAesKey {

    _128BitAesKey(byte[] key){
        super(key);
    }

    @Override
    public int nRounds() {
        return 10;
    }

    @Override
    int[] expandKey(byte[] key) {
        if(key.length != 16) throw new IllegalArgumentException("Key length must be 128 bits");

        var w = new int[(nRounds() + 1) * 4];

        // initial 128 bit subkey for round 0 is the same as the main cipher key
        w[0] = toInt(key[0], key[1], key[2], key[3]);
        w[1] = toInt(key[4], key[5], key[6], key[7]);
        w[2] = toInt(key[8], key[9], key[10], key[11]);
        w[3] = toInt(key[12], key[13], key[14], key[15]);

        // compute 128 bit subkeys for the remaining 10 rounds


        return w;
    }
}

class _192BitAesKey extends AbstractAesKey {

    _192BitAesKey(byte[] key) {
        super(key);
    }

    @Override
    public int nRounds() {
        return 12;
    }

    @Override
    int[] expandKey(byte[] key) {
        throw new IllegalStateException("192 bit key support is not yet implemented");
    }
}

class _256BitAesKey extends AbstractAesKey {

    _256BitAesKey(byte[] key) {
        super(key);
    }

    @Override
    public int nRounds() {
        return 14;
    }

    @Override
    int[] expandKey(byte[] key) {
        throw new IllegalStateException("256 bit key support is not yet implemented");
    }

}

