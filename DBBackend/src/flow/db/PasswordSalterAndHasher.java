package flow.db;

import sun.misc.BASE64Encoder;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

public class PasswordSalterAndHasher {
    private static final int SALT_LENGTH = 16;
    private static final int HASHED_LENGTH = 44;

    public static String generateNewSalt() {

        SecureRandom rnd = new SecureRandom();
        byte salt[] = new byte[(SALT_LENGTH/4)*3];
        rnd.nextBytes(salt);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(salt);
    }

    public static String hashPassword(String salt, String password){
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, ((HASHED_LENGTH*8)/4)*3);
            return encoder.encode(skf.generateSecret(spec).getEncoded());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
