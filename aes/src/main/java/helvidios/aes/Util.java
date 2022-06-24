package helvidios.aes;

import java.security.SecureRandom;

class Util {
    
    static byte[] toByteArray(String hex){
        var ans = new byte[hex.length() / 2];
        for (int i = 0; i < ans.length; i++) {
            int index = i * 2;
            int val = Integer.parseInt(hex.substring(index, index + 2), 16);
            ans[i] = (byte) val;
        }
        return ans;
    }

    static String toHexString(byte[] bytes){
        var sb = new StringBuilder();
        for(var b : bytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    static int toInt(byte b1, byte b2, byte b3, byte b4) {
        return  ((b1 & 0xFF) << 24) |
                ((b2 & 0xFF) << 16) |
                ((b3 & 0xFF) << 8) |
                ((b4 & 0xFF) << 0);
    }

    static byte[] getBytes(int w){
        return new byte[] {
            (byte) ((w >> 24) & 0xFF),
            (byte) ((w >> 16) & 0xFF),
            (byte) ((w >> 8) & 0xFF),
            (byte) (w & 0xFF)
        };
    }

    /**
     * Galois Field (256) Multiplication of two Bytes.
     * Borrowed from https://en.wikipedia.org/wiki/Rijndael_MixColumns
     */
    static byte GMul(byte a, byte b) { // 
        byte p = 0;
    
        for (int counter = 0; counter < 8; counter++) {
            if ((b & 1) != 0) {
                p ^= a;
            }
    
            var hi_bit_set = (a & 0x80) != 0;
            a <<= 1;
            if (hi_bit_set) {
                a ^= 0x1B; /* x^8 + x^4 + x^3 + x + 1 */
            }
            b >>= 1;
        }
    
        return p;
    }

    public static byte[] xor(byte[] b1, byte[] b2){
        var xorResult = new byte[Math.min(b1.length, b2.length)];
        for(var i = 0; i < xorResult.length; i++){
            xorResult[i] = (byte) (b1[i] ^ b2[i]);
        }

        return xorResult;
    }

    public static byte[] randomBytes(int len){
        var sr = new SecureRandom();
        var bytes = new byte[len];
        sr.nextBytes(bytes);
        return bytes;
    }
}
