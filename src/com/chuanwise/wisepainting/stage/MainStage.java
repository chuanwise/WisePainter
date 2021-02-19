package com.chuanwise.wisepainting.stage;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.chuanwise.wisepainting.assembly.*;
import com.chuanwise.wisepainting.icon.StageIcons;

public class MainStage {
    private Stage stage = new Stage();
    private BorderPane globalBorderPane = new BorderPane();
    private Scene scene = new Scene(globalBorderPane);
    private HBox contentHBox = new HBox(10);

    private PainterPane painterPane = new PainterPane();
    private ItemList itemList = new ItemList();
    private VBox detailVBox = new VBox(30);
    private ControlMenu controlMenu = new ControlMenu(painterPane, itemList);
    private Status status = new Status();

    private MenuBar menuBar = new MenuBar(controlMenu, this, status);

    public MainStage() {
        init();
    }

    public void init() {
        stage.setHeight(618 * 1.25);
        stage.setWidth(1000 * 1.25);
        stage.setScene(scene);
        stage.setTitle("Wise Painter");
        stage.getIcons().add(StageIcons.STAGE);

        globalBorderPane.setPadding(new Insets(5));
        globalBorderPane.setTop(menuBar);
        detailVBox.getChildren().addAll(controlMenu.getDetailVBox(), itemList);
        globalBorderPane.setCenter(contentHBox);
        globalBorderPane.setBottom(status);

        contentHBox.setPadding(new Insets(10, 30, 10, 30));
        painterPane.setPrefWidth(800);
        painterPane.setStatus(status);
        painterPane.setItemList(itemList);
        controlMenu.setStatus(status);
        contentHBox.getChildren().addAll(controlMenu.getBrushVBox(), painterPane, detailVBox);

        stage.setResizable(false);
    }

    public Stage getStage() {
        return stage;
    }
}
