package org.hj.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hj
 * @data 2023/3/25 10:39
 */
@SpringBootApplication
@ComponentScan("org.hj")
@EnableDiscoveryClient
public class ServiceCarMain {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCarMain.class, args);
    }
}
