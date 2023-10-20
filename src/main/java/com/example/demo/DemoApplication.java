package com.example.demo;

import com.example.demo.garage.model.Access;
import com.example.demo.raz.excercises.Cuscino;
import com.example.demo.raz.excercises.Divano;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.Scanner;

import static java.lang.Thread.sleep;
import static java.time.temporal.ChronoUnit.MINUTES;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }



}
