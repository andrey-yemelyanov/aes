package helvidios.aes;

import java.util.Objects;
import static helvidios.aes.Util.GMul;

class StateMatrix {

    private final byte[][] m;
    
    StateMatrix(byte[] block){
        Objects.requireNonNull(block, "block must not be null");
        if(block.length != 16) throw new IllegalArgumentException("AES block size must be 16 bytes (128 bits)");

        m = new byte[4][4];
        for(var i = 0; i < block.length; i++){
            m[i % 4][i / 4] = block[i];
        }
    }

    private StateMatrix(byte[][] state){
        m = state;
    }

    StateMatrix shiftRows(){
        return new StateMatrix(
            new byte[][]{
                {m[0][0], m[0][1], m[0][2], m[0][3]},
                {m[1][1], m[1][2], m[1][3], m[1][0]},
                {m[2][2], m[2][3], m[2][0], m[2][1]},
                {m[3][3], m[3][0], m[3][1], m[3][2]}
            });
    }

    StateMatrix invShiftRows(){
        return new StateMatrix(
            new byte[][]{
                {m[0][0], m[0][1], m[0][2], m[0][3]},
                {m[1][3], m[1][0], m[1][1], m[1][2]},
                {m[2][2], m[2][3], m[2][0], m[2][1]},
                {m[3][1], m[3][2], m[3][3], m[3][0]}
            });
    }

    StateMatrix mixColumns(){
        final byte[][] c = {
            {02, 03, 01, 01},
            {01, 02, 03, 01},
            {01, 01, 02, 03},
            {03, 01, 01, 02}
        };

        var state = new byte[4][4];

        for(var col = 0; col < 4; col++){

            state[0][col] = (byte) (
                GMul(c[0][0], m[0][col]) ^ 
                GMul(c[0][1], m[1][col]) ^ 
                GMul(c[0][2], m[2][col]) ^ 
                GMul(c[0][3], m[3][col])
            );

            state[1][col] = (byte) (
                GMul(c[1][0], m[0][col]) ^ 
                GMul(c[1][1], m[1][col]) ^ 
                GMul(c[1][2], m[2][col]) ^ 
                GMul(c[1][3], m[3][col])
            );

            state[2][col] = (byte) (
                GMul(c[2][0], m[0][col]) ^ 
                GMul(c[2][1], m[1][col]) ^ 
                GMul(c[2][2], m[2][col]) ^ 
                GMul(c[2][3], m[3][col])
            );

            state[3][col] = (byte) (
                GMul(c[3][0], m[0][col]) ^ 
                GMul(c[3][1], m[1][col]) ^ 
                GMul(c[3][2], m[2][col]) ^ 
                GMul(c[3][3], m[3][col])
            );
        }

        return new StateMatrix(state);
    }

    StateMatrix invMixColumns(){
        final byte[][] c = {
            {0x0e, 0x0b, 0x0d, 0x09},
            {0x09, 0x0e, 0x0b, 0x0d},
            {0x0d, 0x09, 0x0e, 0x0b},
            {0x0b, 0x0d, 0x09, 0x0e}
        };

        var state = new byte[4][4];

        for(var col = 0; col < 4; col++){

            state[0][col] = (byte) (
                GMul(c[0][0], m[0][col]) ^ 
                GMul(c[0][1], m[1][col]) ^ 
                GMul(c[0][2], m[2][col]) ^ 
                GMul(c[0][3], m[3][col])
            );

            state[1][col] = (byte) (
                GMul(c[1][0], m[0][col]) ^ 
                GMul(c[1][1], m[1][col]) ^ 
                GMul(c[1][2], m[2][col]) ^ 
                GMul(c[1][3], m[3][col])
            );

            state[2][col] = (byte) (
                GMul(c[2][0], m[0][col]) ^ 
                GMul(c[2][1], m[1][col]) ^ 
                GMul(c[2][2], m[2][col]) ^ 
                GMul(c[2][3], m[3][col])
            );

            state[3][col] = (byte) (
                GMul(c[3][0], m[0][col]) ^ 
                GMul(c[3][1], m[1][col]) ^ 
                GMul(c[3][2], m[2][col]) ^ 
                GMul(c[3][3], m[3][col])
            );
        }

        return new StateMatrix(state);
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

        var k = 0;
        for(var j = 0; j < 4; j++){
            for(var i = 0; i < 4; i++){
                state[i][j] = (byte) (m[i][j] ^ subkey[k++]);
            }
        }

        return new StateMatrix(state);
    }

    byte[] toByteArray(){
        var block = new byte[16];

        var k = 0;
        for(var j = 0; j < 4; j++){
            for(var i = 0; i < 4; i++){
                block[k++] = m[i][j];
            }
        }

        return block;
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();
        for(var i = 0; i < 4; i++){
            sb.append(Util.toHexString(m[i]));
            sb.append("\n");
        }
        return sb.toString();
    }
}
