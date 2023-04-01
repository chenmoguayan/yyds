package org.hj.yygh.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.hj.yygh.msm.config.ConstantPropertiesUtils;
import org.hj.yygh.msm.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/31 14:10
 */
@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(String phone, String code) {
// 判断手机号是否为空
        if(!StringUtils.hasLength(phone)) {
            return false;
        }
// 整合阿里云短信服务
// 设置参数
        DefaultProfile profile = DefaultProfile.getProfile(
                ConstantPropertiesUtils.RESION_ID,
                ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.SECRET
        );
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
// 手机号
        request.putQueryParameter("PhoneNumbers",phone);
// 签名名称
        request.putQueryParameter("SignName","沉默寡言lhj");
// 模板code: 注意，这个模板code是用阿里云短信服务管理中的模板code
        request.putQueryParameter("TemplateCode","SMS_275430160");
// 验证码 使用json格式 {"code":"123456"}
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);
        request.putQueryParameter("TemplateParam",
                JSONObject.toJSONString(param));

// 发送短信
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
