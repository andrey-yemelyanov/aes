package helvidios.aes;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;

interface AesKey {

    /**
     * Creates an AES key from key bytes. Supported key length is 128, 192 or 256 bits.
     * @param data key data
     * @return AES key implementation
     */
    static AesKey from(byte[] data){
        Objects.requireNonNull(data, "Key data must not be null");
        if(data.length == 16) return new _128BitAesKey(data);
        if(data.length == 24) return new _192BitAesKey(data);
        if(data.length == 32) return new _256BitAesKey(data);
        throw new IllegalArgumentException(String.format("Key of length %d bits is not supported.", data.length * 8));
    }

    /**
     * Creates an AES key from a specified password.
     * @param password from which key bytes will be generated
     * @param keyType 128, 192 or 256 bit key type
     * @return AES key of specific type
     * @throws Exception if password cannot be converted to a vector of bits
     */
    static AesKey fromPassword(String password, AesKeyType keyType) throws Exception {
        var md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        var digest = md.digest();
        return from(Arrays.copyOf(digest, keyType.keyLen()));
    }

    /**
     * Generates a cryptographically secure random AES key of specified length.
     */
    static AesKey randomKey(AesKeyType keyType){
        return from(Util.randomBytes(keyType.keyLen()));
    }

    /**
     * Returns a 128-bit subkey for a specific encryption/decryption round.
     * @param round current round
     * @return 128-bit subkey
     */
    byte[] getSubkey(int round);

    int nRounds();

    byte[] toByteArray();

    int length();
}

abstract class AbstractAesKey implements AesKey {

    private final int[] W; // key expansion array
    private final byte[] key;

    AbstractAesKey(byte[] key) {
        this.key = Objects.requireNonNull(key, "key must not be null");
        this.W = expandKey(key);
    }

    @Override
    public String toString(){
        return Util.toHexString(key);
    }

    @Override
    public int length(){
        return key.length;
    }

    @Override
    public byte[] toByteArray(){
        return key;
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
            var bytes = Util.getBytes(W[i]);

            var j = (i - 4 * round) * 4;

            subkey[j]     = bytes[0];
            subkey[j + 1] = bytes[1];
            subkey[j + 2] = bytes[2];
            subkey[j + 3] = bytes[3];
        }

        return subkey;
    }

    int rotateWord(int w){
        var bytes = Util.getBytes(w);
        return Util.toInt(bytes[1], bytes[2], bytes[3], bytes[0]);
    }

    int subWord(int w){
        var bytes = Util.getBytes(w);
        return Util.toInt(
            SBox.substitute(bytes[0]), 
            SBox.substitute(bytes[1]), 
            SBox.substitute(bytes[2]), 
            SBox.substitute(bytes[3])
        );
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

        var w = new int[44];

        // initial 128 bit subkey for round 0 is the same as the main cipher key
        w[0] = Util.toInt(key[0], key[1], key[2], key[3]);
        w[1] = Util.toInt(key[4], key[5], key[6], key[7]);
        w[2] = Util.toInt(key[8], key[9], key[10], key[11]);
        w[3] = Util.toInt(key[12], key[13], key[14], key[15]);

        // compute 128 bit subkeys for the remaining 10 rounds
        final int[] rcon = {
            0x01000000, 
            0x02000000, 
            0x04000000, 
            0x08000000, 
            0x10000000, 
            0x20000000, 
            0x40000000, 
            0x80000000, 
            0x1B000000, 
            0x36000000
        };

        for(var i = 4; i < w.length; i++){
            var temp = w[i - 1];
            if(i % 4 == 0){
                temp = rotateWord(temp);
                temp = subWord(temp);
                temp ^= rcon[i / 4 - 1];
            }
            w[i] = w[i - 4] ^ temp;
        }

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
        return null;
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
        return null;
    }

}

