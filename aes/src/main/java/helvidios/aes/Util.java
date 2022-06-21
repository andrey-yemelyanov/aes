package helvidios.aes;

public class Util {
    public static byte[] toByteArray(String hex){
        var ans = new byte[hex.length() / 2];
        for (int i = 0; i < ans.length; i++) {
            int index = i * 2;
            int val = Integer.parseInt(hex.substring(index, index + 2), 16);
            ans[i] = (byte) val;
        }
        return ans;
    }
}
