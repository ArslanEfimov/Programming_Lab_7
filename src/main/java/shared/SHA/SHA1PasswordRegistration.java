package shared.SHA;

import org.apache.commons.lang3.RandomStringUtils;

public class SHA1PasswordRegistration {
    private static final String PEPPER = "7-$D?=9j!K";
    private static final String SALT = generateSalt();
    private static final SHA1 sha1 = new SHA1();


    public static String generateSalt(){
        return RandomStringUtils.random(10,true,true);
    }

    public static String hashPassword(String password){
        String pwdWithSaltAndPepper = PEPPER + password + SALT;
        return sha1.hashPassword(pwdWithSaltAndPepper);
    }


    public static String getSalt(){
        return SALT;

    }

    public static String hashPasswordForLogin(String password, String salt){
        String pwd = password + salt;
        return sha1.hashPassword(pwd);
    }

    public static String hashPasswordForLoginWithPepper(String password){
        return PEPPER + password;
    }


}
