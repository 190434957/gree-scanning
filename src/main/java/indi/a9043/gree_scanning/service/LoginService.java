package indi.a9043.gree_scanning.service;

import indi.a9043.gree_scanning.mapper.GreeUserMapper;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.pojo.GreeUserExample;
import indi.a9043.gree_scanning.util.UserPasswordEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

    public GreeUser doLogin(GreeUser greeUser) throws CannotGetJdbcConnectionException {
        GreeUser standardGreeUser = null;
        try {
            standardGreeUser = greeUserMapper.selectByPrimaryKey(greeUser.getUsrId());
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof CannotGetJdbcConnectionException) {
                throw (CannotGetJdbcConnectionException) e.getCause().getCause();
            }
            e.printStackTrace();
        }
        if (standardGreeUser == null) {
            return null;
        }
        if (!standardGreeUser.getUsrPwd().equals(userPasswordEncrypt.encrypt(greeUser.getUsrPwd()))) {
            return null;
        }
        standardGreeUser.setUsrPwd(null);
        return standardGreeUser;
    }


    @Transactional
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

    public List<GreeUser> getAllUser() {
        GreeUserExample greeUserExample = new GreeUserExample();
        List<GreeUser> greeUserList = greeUserMapper.selectByExample(greeUserExample);
        for (GreeUser greeUser : greeUserList) {
            greeUser.setUsrPwd(null);
        }
        return greeUserList;
    }

    public void updateUserList(List<GreeUser> greeUserList) {
        for (GreeUser greeUser : greeUserList) {
            if (greeUser.getUsrPwd().equals("")) {
                greeUserMapper.deleteByPrimaryKey(greeUser.getUsrId());
            } else {
                greeUserMapper.updateByPrimaryKeySelective(greeUser);
            }
        }
    }

    public boolean addGreeUser(GreeUser greeUser) {
        if (greeUserMapper.selectByPrimaryKey(greeUser.getUsrId()) != null) {
            return false;
        }
        greeUser.setUsrPwd(userPasswordEncrypt.encrypt(greeUser.getUsrPwd()));
        return greeUserMapper.insert(greeUser) > 0;
    }
}
