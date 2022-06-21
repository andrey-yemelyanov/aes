package helvidios.aes;

class BlockCipher {
    
    byte[] encrypt(byte[] block, byte[] key){
        var aesKey = AesKey.from(key);
        
        var state = new StateMatrix(block).xor(aesKey.getSubkey(0));

        for(var round = 1; round < aesKey.nRounds(); round++){
            state = state.substituteBytes()
                         .shiftRows()
                         .mixColumn()
                         .xor(aesKey.getSubkey(round));
        }

        return state.substituteBytes()
                    .shiftRows()
                    .xor(aesKey.getSubkey(aesKey.nRounds()))
                    .toByteArray();
    }

    byte[] decrypt(byte[] block, byte[] key){
        var aesKey = AesKey.from(key);
        
        var state = new StateMatrix(block).xor(aesKey.getSubkey(aesKey.nRounds()));

        for(var round = aesKey.nRounds() - 1; round > 0; round--){
            state = state.invShiftRows()
                         .invSubstituteBytes()
                         .xor(aesKey.getSubkey(round))
                         .invMixColumn();
        }

        return state.invShiftRows()
                    .invSubstituteBytes()
                    .xor(aesKey.getSubkey(0))
                    .toByteArray();
    }
}
