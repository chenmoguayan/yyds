package org.hj.yygh.controller;

import org.hj.yygh.common.exception.YyghException;
import org.hj.yygh.common.helper.HttpRequestHelper;
import org.hj.yygh.common.result.ResultCodeEnum;
import org.hj.yygh.common.utils.MD5;
import org.hj.yygh.service.HospitalService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/28 9:21
 */
public class BaseController {
    /**
     * 获取请求参数
     */
    public Map<String, Object> getParamMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap =
                HttpRequestHelper.switchMap(parameterMap);
        return paramMap;
    }
    /**
     * 签名校验
     */
    public void checkSignKey(Map<String, Object> paramMap, HospitalService
            hospitalServiceImpl) {
        String hoscode = (String)paramMap.get("hoscode");
        if(!StringUtils.hasLength(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String signKey = (String)paramMap.get("sign");
        String utfSignKey = hospitalServiceImpl.getSignKey(hoscode);
        try {
            byte[] bytes = utfSignKey.getBytes("UTF-8");
            utfSignKey = new String(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String signKeyMD5 = MD5.encrypt(utfSignKey);
        if(!signKey.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }

}
