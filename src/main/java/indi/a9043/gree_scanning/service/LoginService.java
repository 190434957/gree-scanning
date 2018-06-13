package indi.a9043.gree_scanning.service;

import indi.a9043.gree_scanning.mapper.GreeUserMapper;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.util.UserPasswordEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        if(standardGreeUser == null) {
            return null;
        }
        if(!standardGreeUser.getUsrPwd().equals(userPasswordEncrypt.encrypt(greeUser.getUsrPwd()))) {
            return null;
        }
        standardGreeUser.setUsrPwd(null);
        return standardGreeUser;
    }
}
