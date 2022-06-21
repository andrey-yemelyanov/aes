package helvidios.aes;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StateMatrixTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPtrExcIfBlockIsNull(){
        new StateMatrix(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgExcIfBlockHasInvalidSize(){
        new StateMatrix(new byte[] {1, 2, 3, 4, 5});
    }

    @Test
    public void shouldCreateMatrixColumnMajorOrder(){
        var block = "3243f6a8885a308d313198a2e0370734";
        var state = new StateMatrix(Util.toByteArray(block));
        var actualState = Util.toHexString(state.toByteArray());
        assertEquals(block, actualState);
    }
    
    @Test
    public void shouldSubstituteBytes(){
        var block = "19a09ae93df4c6f8e3e28d48be2b2a08";
        var state = new StateMatrix(Util.toByteArray(block));
        state = state.substituteBytes();
        var expectedState = "d4e0b81e27bfb44111985d52aef1e530";
        var actualState = Util.toHexString(state.toByteArray());
        assertEquals(expectedState, actualState);
    }

    @Test
    public void shouldInvSubstituteBytes(){
        var block = "d4e0b81e27bfb44111985d52aef1e530";
        var state = new StateMatrix(Util.toByteArray(block));
        state = state.invSubstituteBytes();
        var expectedState = "19a09ae93df4c6f8e3e28d48be2b2a08";
        var actualState = Util.toHexString(state.toByteArray());
        assertEquals(expectedState, actualState);
    }

    @Test
    public void shouldXor(){
        var block = "04e0482866cbf8068119d326e59a7a4c";
        var subkey = "a088232afa54a36cfe2c397617b13905";
        var state = new StateMatrix(Util.toByteArray(block));
        state = state.xor(Util.toByteArray(subkey));
        var expectedState = "a4686b029c9f5b6a7f35ea50f22b4349";
        var actualState = Util.toHexString(state.toByteArray());
        assertEquals(expectedState, actualState);
    }

    @Test
    public void shouldShiftRows(){
        var block = "49ded28945db96f17f39871a7702533b";
        var state = new StateMatrix(Util.toByteArray(block));
        state = state.shiftRows();
        var expectedState = "49db873b453953897f02d2f177de961a";
        var actualState = Util.toHexString(state.toByteArray());
        assertEquals(expectedState, actualState);
    }

    @Test
    public void shouldInvShiftRows(){
        var block = "49db873b453953897f02d2f177de961a";
        var state = new StateMatrix(Util.toByteArray(block));
        state = state.invShiftRows();
        var expectedState = "49ded28945db96f17f39871a7702533b";
        var actualState = Util.toHexString(state.toByteArray());
        assertEquals(expectedState, actualState);
    }
}
