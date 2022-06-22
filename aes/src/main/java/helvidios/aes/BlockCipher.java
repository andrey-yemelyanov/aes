package helvidios.aes;

/**
 * AES cipher for encryption and decryption of 128 bit blocks of data.
 */
class BlockCipher {
    
    /**
     * Encrypts a block of data with a symmetric key and produces a cipher text of the same length.
     * @param block 128 bit block of data
     * @param key AES key of length 128, 192 or 256 bits
     * @return cipher text
     */
    byte[] encrypt(byte[] block, byte[] key){
        
        var aesKey = AesKey.from(key);
        
        var state = new StateMatrix(block)
            .xor(aesKey.getSubkey(0));

        for(var round = 1; round < aesKey.nRounds(); round++){
            state = state.substituteBytes()
                         .shiftRows()
                         .mixColumns()
                         .xor(aesKey.getSubkey(round));
        }

        return state.substituteBytes()
                    .shiftRows()
                    .xor(aesKey.getSubkey(aesKey.nRounds()))
                    .toByteArray();
    }

    /**
     * Decrypts a block of data with a symmetric key and produces plaintext.
     * @param block 128 bit block of cipher text
     * @param key AES key of length 128, 192 or 256 bits
     * @return plaintext
     */
    byte[] decrypt(byte[] block, byte[] key){
        
        var aesKey = AesKey.from(key);
        
        var state = new StateMatrix(block)
            .xor(aesKey.getSubkey(aesKey.nRounds()));

        for(var round = aesKey.nRounds() - 1; round > 0; round--){
            state = state.invShiftRows()
                         .invSubstituteBytes()
                         .xor(aesKey.getSubkey(round))
                         .invMixColumns();
        }

        return state.invShiftRows()
                    .invSubstituteBytes()
                    .xor(aesKey.getSubkey(0))
                    .toByteArray();
    }
}
