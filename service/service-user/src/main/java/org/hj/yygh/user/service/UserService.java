package org.hj.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.yygh.model.user.UserInfo;
import org.hj.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @author hj
 * @data 2023/3/31 10:10
 */
public interface UserService extends IService<UserInfo> {

    Map<String,Object> login(LoginVo loginVo);
}
