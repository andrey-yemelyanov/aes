package helvidios.aes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Test;

public class BlockIteratorTest {

    @Test
    public void oneBlockWithPadding() throws IOException{
        var oneBlockWithPadding = new byte[] {
            1, 2, 3, 4, 5
        };

        try(var input = new ByteArrayInputStream(oneBlockWithPadding)){
            var it = new PKCS5BlockIterator(input);
            assertTrue(it.hasNext());
            var block = it.next();
            assertEquals(
                "01020304050b0b0b0b0b0b0b0b0b0b0b", 
                Util.toHexString(block));
            assertFalse(it.hasNext());
        }
    }

    @Test
    public void oneBlockNoPadding() throws IOException{
        var oneBlockNoPadding = new byte[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
        };

        try(var input = new ByteArrayInputStream(oneBlockNoPadding)){
            var it = new PKCS5BlockIterator(input);
            assertTrue(it.hasNext());
            var block1 = it.next();
            assertEquals(
                "0102030405060708090a0b0c0d0e0f10", 
                Util.toHexString(block1));
            assertTrue(it.hasNext());
            var block2 = it.next();
            assertEquals(
                "10101010101010101010101010101010", 
                Util.toHexString(block2));
            assertFalse(it.hasNext());
        }
    }

    @Test
    public void twoBlocksWithPadding() throws IOException{
        var twoBlocksWithPadding = new byte[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 
            17, 18, 19, 20
        };

        try(var input = new ByteArrayInputStream(twoBlocksWithPadding)){
            var it = new PKCS5BlockIterator(input);
            assertTrue(it.hasNext());
            var block1 = it.next();
            assertEquals(
                "0102030405060708090a0b0c0d0e0f10", 
                Util.toHexString(block1));
            assertTrue(it.hasNext());
            var block2 = it.next();
            assertEquals(
                "111213140c0c0c0c0c0c0c0c0c0c0c0c", 
                Util.toHexString(block2));
            assertFalse(it.hasNext());
        }
    }
    
    @Test
    public void twoBlocksNoPadding() throws IOException{

        var twoBlocksNoPadding = new byte[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 
            17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32
        };

        try(var input = new ByteArrayInputStream(twoBlocksNoPadding)){
            var it = new PKCS5BlockIterator(input);
            assertTrue(it.hasNext());
            var block1 = it.next();
            assertEquals(
                "0102030405060708090a0b0c0d0e0f10", 
                Util.toHexString(block1));
            assertTrue(it.hasNext());
            var block2 = it.next();
            assertEquals(
                "1112131415161718191a1b1c1d1e1f20", 
                Util.toHexString(block2));
            assertTrue(it.hasNext());
            var block3 = it.next();
            assertEquals(
                "10101010101010101010101010101010", 
                Util.toHexString(block3));
            assertFalse(it.hasNext());
        }
    }
}
