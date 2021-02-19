package com.chuanwise.wisepainting;

import javafx.application.Application;
import javafx.stage.Stage;
import com.chuanwise.wisepainting.stage.MainStage;

public class WisePainting extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        new MainStage().getStage().show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
