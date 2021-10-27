import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.util.Date;

public class MainClass {
    public static void main(String[] args) throws Exception {
        //keytool -genkey -alias hanbotest -keyalg RSA -keystore hanbotest.jks -keysize 2048
        // The password for the keystore, I use "123test321".
        //  Again for the same password.
        //  First name and last name, I entered "Aqib Javed".
        // Name of the organization unit, I entered "Aqib Enterprise".
        // Name of the organization, I entered "Aqib Enterprise" again.
        // Name of the city, I entered "Chicago".
        // Name of the state or province, I entered "IL".
        // Two characters of the country, I entered "US".
        // Last one is just "[no]", I type in "yes" and hit enter.
        // The last last thing is the password of the certificate and private key. I keep it the same as the password of the keystore. Just hit enter.
        // Look into the destination directory, you will see the KeyStore file.
        String jksPassword = "123test321"; // use password which you use while creating jks

        KeyStore ks  = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream("C:\\DevJunk\\jwt\\keystore\\hanbotest.jks"), jksPassword.toCharArray());
        Key key = ks.getKey("hanbotest", jksPassword.toCharArray());


        String compactJws = Jwts.builder()
                .setSubject("Joe")
                .setAudience("testAudienceId")
                .setExpiration(new Date(System.currentTimeMillis()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setId("testuserid")
                .signWith(SignatureAlgorithm.RS512, key)
                .compact();

        System.out.println(compactJws);

        PublicKey publicKey = loadPublicKey("C:\\DevJunk\\jwt\\keystore\\hanbotest.cer");

        Jws<Claims> x = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(compactJws);

        String id = x.getBody().getId();
        System.out.println("id: " + id);
        System.out.println("audience: " + x.getBody().getAudience());
        System.out.println("audience: " + x.getBody().getSubject());
    }

    public static PublicKey loadPublicKey(String filename)
            throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = (Certificate) cf.generateCertificate(new FileInputStream(filename));
        return cert.getPublicKey();
    }
}
