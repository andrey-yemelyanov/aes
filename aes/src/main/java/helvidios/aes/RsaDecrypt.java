import java.math.BigInteger;
import java.util.HexFormat;

public class RsaDecrypt {

    public static void main(String... args){
        // public key
        final BigInteger N = new BigInteger("179769313486231590772930519078902473361797697894230657273430081157732675805505620686985379449212982959585501387537164015710139858647833778606925583497541085196591615128057575940752635007475935288710823649949940771895617054361149474865046711015101563940680527540071584560878577663743040086340742855278549092581");
        final BigInteger e = BigInteger.valueOf(65537);

        var factors = factor(N);
        if(factors == null) throw new RuntimeException("Unable to factor N");

        var p = factors[0]; var q = factors[1];
        var phiOfN = p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));

        // private key
        var d = e.modInverse(phiOfN);
        System.out.println("d=" + d);

        final BigInteger y = new BigInteger("22096451867410381776306561134883418017410069787892831071731839143676135600120538004282329650473509424343946219751512256465839967942889460764542040581564748988013734864120452325229320176487916666402997509188729971690526083222067771600019329260870009579993724077458967773697817571267229951148662959627934791540");
        var x = y.modPow(d, N);
        var hex = x.toString(16);

        System.out.println("x=0x" + x.toString(16));
        
        var separatorIndex = hex.indexOf("00");
        if(separatorIndex < 0) throw new RuntimeException("Invalid PKCS1 encoding");

        System.out.println(hexToString(hex.substring(separatorIndex + 2)));   
    }

    static String hexToString(String hex){
        var hexFormat = HexFormat.of();
        return new String(hexFormat.parseHex(hex));
    }

    static BigInteger[] factor(BigInteger N){
        
        var A = N.sqrt();
        if(!A.pow(2).equals(N)){
            A = A.add(BigInteger.valueOf(1));
        }

        var x = A.pow(2).subtract(N).sqrt();

        var p = A.subtract(x);
        var q = A.add(x);

        if(q.multiply(p).equals(N)){
            return new BigInteger[]{ p, q };
        }
        
        return null;
    }
}
