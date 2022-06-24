package helvidios.aes;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class IVCounterTest {
    
    @Test
    public void shouldIncrementIV(){
        var counter = IVCounter.generateRandom();
        var nonce = Util.toHexString(counter.getNonce());
        
        assertEquals("0000000000000000", Util.toHexString(counter.getCounter()));
        System.out.println(Util.toHexString(counter.toByteArray()));

        counter = counter.incrementIV();
        assertEquals(nonce, Util.toHexString(counter.getNonce()));
        assertEquals("0000000000000001", Util.toHexString(counter.getCounter()));
        System.out.println(Util.toHexString(counter.toByteArray()));

        counter = counter.incrementIV();
        assertEquals(nonce, Util.toHexString(counter.getNonce()));
        assertEquals("0000000000000002", Util.toHexString(counter.getCounter()));
        System.out.println(Util.toHexString(counter.toByteArray()));

        counter = counter.incrementIV();
        assertEquals(nonce, Util.toHexString(counter.getNonce()));
        assertEquals("0000000000000003", Util.toHexString(counter.getCounter()));
        System.out.println(Util.toHexString(counter.toByteArray()));

        counter = counter.incrementIV();
        assertEquals(nonce, Util.toHexString(counter.getNonce()));
        assertEquals("0000000000000004", Util.toHexString(counter.getCounter()));
        System.out.println(Util.toHexString(counter.toByteArray()));

        counter = counter.incrementIV();
        assertEquals(nonce, Util.toHexString(counter.getNonce()));
        assertEquals("0000000000000005", Util.toHexString(counter.getCounter()));
        System.out.println(Util.toHexString(counter.toByteArray()));
    }
}
