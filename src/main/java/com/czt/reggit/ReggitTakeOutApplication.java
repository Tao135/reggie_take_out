package com.czt.reggit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ReggitTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggitTakeOutApplication.class, args);
        log.info("启动");
    }

}
