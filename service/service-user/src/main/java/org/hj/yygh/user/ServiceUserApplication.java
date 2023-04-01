package org.hj.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hj
 * @data 2023/3/31 10:01
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.hj")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.hj")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
