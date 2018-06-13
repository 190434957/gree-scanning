package indi.a9043.gree_scanning.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * UserPasswordEncrypt
 * 密码加密算法
 */
@Component
public class UserPasswordEncrypt {

    private static final Base64.Encoder base64Encoder = Base64.getEncoder();
    /**
     * 加密
     *
     * @param userOriginalPassword 原始密码
     * @return 加密密码
     */
    public String encrypt(String userOriginalPassword) {
        if (userOriginalPassword != null) {
            String userEncryptPassword = "";
            try {
                MessageDigest mDigest = MessageDigest.getInstance("MD5");
                userEncryptPassword = base64Encoder.encodeToString(mDigest.digest(userOriginalPassword.getBytes()));
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            return userEncryptPassword;
        } else {
            return null;
        }
    }
}
