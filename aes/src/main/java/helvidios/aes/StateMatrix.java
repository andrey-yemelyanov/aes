package helvidios.aes;

import java.util.Objects;

class StateMatrix {

    private final byte[][] m;
    
    StateMatrix(byte[] block){
        Objects.requireNonNull(block, "block must not be null");
        if(block.length != 16) throw new IllegalArgumentException("AES block size must be 16 bytes (128 bits)");

        m = new byte[4][4];
        for(var i = 0; i < block.length; i++){
            m[i / 4][i % 4] = block[i];
        }
    }

    private StateMatrix(byte[][] state){
        m = state;
    }

    StateMatrix shiftRows(){
        return null;
    }

    StateMatrix invShiftRows(){
        return null;
    }

    StateMatrix mixColumn(){
        return null;
    }

    StateMatrix invMixColumn(){
        return null;
    }

    StateMatrix substituteBytes(){
        var state = new byte[4][4];

        for(var i = 0; i < m.length; i++){
            for(var j = 0; j < m[i].length; j++){
                state[i][j] = SBox.substitute(m[i][j]);
            }
        }

        return new StateMatrix(state);
    }

    StateMatrix invSubstituteBytes(){
        var state = new byte[4][4];

        for(var i = 0; i < m.length; i++){
            for(var j = 0; j < m[i].length; j++){
                state[i][j] = SBox.invSubstitute(m[i][j]);
            }
        }

        return new StateMatrix(state);
    }

    StateMatrix xor(byte[] subkey){
        Objects.requireNonNull(subkey, "subkey must not be null");
        if(subkey.length != 16) throw new IllegalArgumentException("AES subkey size must be 128 bits");

        var state = new byte[4][4];

        for(var i = 0; i < m.length; i++){
            for(var j = 0; j < m[i].length; j++){
                state[i][j] = (byte) (m[i][j] ^ subkey[i * 4 + j]);
            }
        }

        return new StateMatrix(state);
    }

    byte[] toByteArray(){
        var block = new byte[16];

        for(var i = 0; i < m.length; i++){
            for(var j = 0; j < m[i].length; j++){
                block[i * 4 + j] = m[i][j];
            }
        }

        return block;
    }
}
