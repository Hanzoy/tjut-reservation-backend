package com.hanzoy.tjutreservation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.hanzoy.tjutreservation.mapper")
public class TjutReservationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TjutReservationApplication.class, args);

    }

}
