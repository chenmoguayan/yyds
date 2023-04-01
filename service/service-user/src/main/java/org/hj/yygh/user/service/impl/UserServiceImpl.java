package org.hj.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.yygh.common.exception.YyghException;
import org.hj.yygh.common.helper.JwtHelper;
import org.hj.yygh.common.result.ResultCodeEnum;
import org.hj.yygh.model.user.UserInfo;
import org.hj.yygh.user.mapper.UserInfoMapper;
import org.hj.yygh.user.service.UserService;
import org.hj.yygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/31 10:11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        // 校验验证码
        String mobleCode = redisTemplate.opsForValue().get(phone);
        if(!code.equals(mobleCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }


        if (!StringUtils.hasLength(phone) || !StringUtils.hasLength(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);

        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if (null == userInfo) {
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            this.save(userInfo);
        }
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);

        }
        Map<String,Object> map = new HashMap<String,Object>();
        String name = userInfo.getName();
        if (!StringUtils.hasLength(name)) {
            name = userInfo.getName();
        }
        if (!StringUtils.hasLength(name)) {
            name = userInfo.getPhone();
        }

        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("name", name);
        map.put("token", token);
        return map;
    }

}
