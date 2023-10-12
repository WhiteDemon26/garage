package com.example.demo;

import com.example.demo.raz.excercises.Cuscino;
import com.example.demo.raz.excercises.Divano;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        //Garage garage = new Garage();
        //garage.newMain();

        //NewMLMain newMain = new NewMLMain();
        //newMain.newMain();

        //TryCatch tryCatch = new TryCatch();
        //tryCatch.betterVersion();

        //NewThreadTestForExercise newThreadTestForExercise = new NewThreadTestForExercise();
        //newThreadTestForExercise.newMain();


        Cuscino cuscino = new Cuscino("viola", "quadrata", "tela");
        Cuscino cuscino2 = new Cuscino("giallo scuro", "quadrata", "tela");
        Cuscino cuscino3 = new Cuscino("giallo beige", "quadrata", "tela");
        Cuscino cuscino4 = new Cuscino("giallo beige", "quadrata", "tela");
        Cuscino[] cuscini = new Cuscino[]{cuscino, cuscino2, cuscino3, cuscino4};

        Divano ilMioDivano = new Divano("plastica", cuscini, "seta", "per lo più giallo");

        ilMioDivano.colore = "verde";


    }

}
