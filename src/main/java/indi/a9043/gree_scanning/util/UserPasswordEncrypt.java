package indi.a9043.gree_scanning.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * UserPasswordEncrypt
 * 密码加密算法
 *
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Component
public class UserPasswordEncrypt {
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
                userEncryptPassword = Base64.encodeBase64String(mDigest.digest(userOriginalPassword.getBytes()));
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            return userEncryptPassword;
        } else {
            return null;
        }
    }
}
