package org.hj.yygh.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import org.hj.yygh.common.helper.JwtHelper;
import org.hj.yygh.common.result.Result;
import org.hj.yygh.common.result.ResultCodeEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author hj
 * @data 2023/3/31 17:18
 */
public class AuthFilter implements GlobalFilter, Ordered {
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain
            chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
// 内部接口，不允许外部访问
        if(antPathMatcher.match("/**/inner/**",path)) {
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.PERMISSION);
        }
// 获取用户id
        Long userId = this.getUserId(request);
// api接口，异步请求，校验用户必须登录
        if(antPathMatcher.match("/api/**/auth/**",path)) {
            if(!StringUtils.hasLength(userId+"")) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.LOGIN_AUTH);
            }
        }
        return chain.filter(exchange);
    }
    /**
     * api接口鉴权失败，返回数据
     */
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum
            resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits =
                JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
// 指定中文编码，否则浏览器会乱码
        response.getHeaders().add("Content-Type","application/json;charset=UTF8");
        return response.writeWith(Mono.just(buffer));
    }
    /**
     * 获取当前用户id
     * 在网关中如何获取用户信息：
     * 统一从header头信息中获取
     * 如何判断用户信息合法：
     * 登录时我们返回用户token，在服务网关中获取到token后，我在到redis中去查看用户id，如
     何用户id存在，则token合法，否则不合法
     */
    private Long getUserId(ServerHttpRequest request) {
        String token = "";
        List<String> tokenList = request.getHeaders().get("token");
        if(null != tokenList) {
            token = tokenList.get(0);
        }
        if(StringUtils.hasLength(token)) {
            return JwtHelper.getUserId(token);
        }
        return null;
    }
    /**
     * 设置过滤器优先级，值越低优先级越高
     * 也可以使用@Order注解
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
