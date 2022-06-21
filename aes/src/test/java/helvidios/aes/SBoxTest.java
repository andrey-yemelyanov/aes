package helvidios.aes;

import static org.junit.Assert.*;
import org.junit.Test;

public class SBoxTest {
   
    @Test
    public void shouldSubstitue53forED()
    {
        assertEquals((byte) 0xED, SBox.substitute((byte) 0x53));
    }

    @Test
    public void shouldInvSubstituteEDfor53(){
        assertEquals((byte) 0x53, SBox.invSubstitute((byte) 0xED));
    }
}
