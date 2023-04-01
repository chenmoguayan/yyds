package org.hj.yygh.msm.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hj
 * @data 2023/3/31 11:59
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {
    @Value("${aliyun.sms.regionId}")
    private String regionId;
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.secret}")
    private String secret;

    public static String RESION_ID;
    public static String ACCESS_KEY_ID;
    public static String SECRET;
    @Override
    public void afterPropertiesSet() throws Exception {
        RESION_ID = regionId;
        ACCESS_KEY_ID = accessKeyId;
        SECRET = secret;
    }
}
