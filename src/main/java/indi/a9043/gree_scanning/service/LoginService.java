package indi.a9043.gree_scanning.service;

import indi.a9043.gree_scanning.mapper.GreeUserMapper;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.util.UserPasswordEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Service("loginService")
public class LoginService {
    @Resource
    private GreeUserMapper greeUserMapper;
    private UserPasswordEncrypt userPasswordEncrypt;

    @Autowired
    public LoginService(UserPasswordEncrypt userPasswordEncrypt) {
        this.userPasswordEncrypt = userPasswordEncrypt;
    }

    public GreeUser doLogin(GreeUser greeUser) {
        GreeUser standardGreeUser = greeUserMapper.selectByPrimaryKey(greeUser.getUsrId());
        if (standardGreeUser == null) {
            return null;
        }
        if (!standardGreeUser.getUsrPwd().equals(userPasswordEncrypt.encrypt(greeUser.getUsrPwd()))) {
            return null;
        }
        standardGreeUser.setUsrPwd(null);
        return standardGreeUser;
    }

    public boolean changePassword(GreeUser greeUser, String oldPassword, String newPassword) {
        GreeUser standardGreeUser = greeUserMapper.selectByPrimaryKey(greeUser.getUsrId());
        if (standardGreeUser == null) {
            return false;
        }
        if (!standardGreeUser.getUsrPwd().equals(userPasswordEncrypt.encrypt(oldPassword))) {
            return false;
        }
        standardGreeUser.setUsrPwd(userPasswordEncrypt.encrypt(newPassword));
        standardGreeUser.setUsrName(null);
        standardGreeUser.setUsrPower(null);
        return greeUserMapper.updateByPrimaryKeySelective(standardGreeUser) > 0;
    }
}
