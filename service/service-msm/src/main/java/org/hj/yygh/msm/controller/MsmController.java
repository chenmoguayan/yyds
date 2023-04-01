package org.hj.yygh.msm.controller;

import org.hj.yygh.common.result.Result;
import org.hj.yygh.common.utils.RandomUtil;
import org.hj.yygh.msm.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author hj
 * @data 2023/3/31 14:33
 */
@RestController
@RequestMapping("/api/msm")
public class MsmController {
    @Autowired
    private MsmService msmServiceImpl;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if (StringUtils.hasLength(code)) {
            return Result.ok();
        }
        code = RandomUtil.getSixBitRandom();
        boolean isSend = msmServiceImpl.send(phone, code);

        if (isSend) {
            redisTemplate.opsForValue().set(phone, code,3, TimeUnit.MINUTES);
            return Result.ok();
        }else {
            return Result.fail().message("发送短信失败");
        }
    }
}
