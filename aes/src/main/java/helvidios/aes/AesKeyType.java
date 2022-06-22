package helvidios.aes;

enum AesKeyType{
    _128_bit(16),
    _192_bit(24),
    _256_bit(32);

    private final int keyLen;

    AesKeyType(int keyLen){
        this.keyLen = keyLen;
    }

    int keyLen(){
        return keyLen;
    }
}
