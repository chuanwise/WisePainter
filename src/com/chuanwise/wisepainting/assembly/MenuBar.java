package com.chuanwise.wisepainting.assembly;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.chuanwise.wisepainting.WisePainting;
import com.chuanwise.wisepainting.icon.StageIcons;
import com.chuanwise.wisepainting.stage.MainStage;

import java.io.*;

public class MenuBar extends javafx.scene.control.MenuBar {
    private Menu fileMenu = new Menu("文件");
    private MenuItem newFile = new MenuItem("新建");
    private MenuItem open = new MenuItem("打开");
    private MenuItem save = new MenuItem("保存");
    private MenuItem saveTo = new MenuItem("另存为");
    private MenuItem close = new MenuItem("关闭");

    private Menu operatorMenu = new Menu("编辑");
    private MenuItem undo = new MenuItem("撤销");
    private MenuItem redo = new MenuItem("重做");

    private Menu helpMenu = new Menu("帮助");
    private MenuItem aboutUs = new MenuItem("关于我们");

    private ControlMenu controlMenu;
    private File currentProjectFile = null;
    private Status status;
    private MainStage mainStage;

    public MenuBar(ControlMenu controlMenu, MainStage mainStage, Status status) {
        this.controlMenu = controlMenu;
        this.status = status;
        this.mainStage = mainStage;
        init();
    }

    private void init() {

        getMenus().addAll(fileMenu, operatorMenu, helpMenu);

        fileMenu.getItems().addAll(newFile, open, save, saveTo, close);

        operatorMenu.getItems().addAll(undo, redo);

        helpMenu.getItems().add(aboutUs);
        setHandler();
    }

    private void setHandler() {

        newFile.setAccelerator(KeyCombination.keyCombination("Ctrl+n"));
        newFile.setOnAction(e -> {
            try {
                new WisePainting().start(new Stage());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        open.setAccelerator(KeyCombination.keyCombination("Ctrl+o"));
        open.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WPP (WisePainter 工程文件)", "*.wpp"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("所有文件", "*"));
            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                openFile(file);
            }
        });

        save.setAccelerator(KeyCombination.keyCombination("Ctrl+s"));
        save.setOnAction(e -> {
            if (currentProjectFile == null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("保存到");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WPP (WisePainter 工程文件)", "*.wpp"));
                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    saveFile(file);
                }
            }
            else {
                saveFile(currentProjectFile);
            }
        });

        saveTo.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+s"));
        saveTo.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WPP (WisePainter 工程文件)", "*.wpp"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                saveFile(file);
            }
        });

        undo.setAccelerator(KeyCombination.keyCombination("Ctrl+z"));
        undo.setOnAction(e -> {
            controlMenu.getPainterPane().undo();
        });

        redo.setAccelerator(KeyCombination.keyCombination("Ctrl+y"));
        redo.setOnAction(e -> {
            controlMenu.getPainterPane().redo();
        });

        aboutUs.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("关于我们");
            alert.setHeaderText("Wise Painter 1.0 正式版（by: Chuanwise）");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(StageIcons.STAGE);

            alert.setContentText("发布时间：" + "2020年12月25日" + "\n" +
                    "作者：强宇；©2020 Chuanwise\n" +
                    "项目已在 GitHub 上开源，给个 Star 好不好 qwq\n" +
                    "https://github.com/Chuanwise/WisePainter");

            alert.getButtonTypes().clear();
            ButtonType buttonType = new ButtonType("前往 GitHub 支持", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().add(buttonType);

            ((Button) alert.getDialogPane().lookupButton(buttonType)).setDefaultButton(true);
            ((Button) alert.getDialogPane().lookupButton(buttonType)).setOnAction(ev -> {
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "https://github.com/Chuanwise/WisePainter");
                }
                catch (Exception exception) {
                }
            });

            alert.show();
        });

        close.setOnAction(e -> {
            mainStage.getStage().close();
        });
    }

    public void setCurrentProjectFile(File currentProjectFile) {
        this.currentProjectFile = currentProjectFile;
        if (currentProjectFile != null) {
            mainStage.getStage().setTitle("WisePainter - " + currentProjectFile.getName());
        }
        else {
            mainStage.getStage().setTitle("WisePainter");
        }
    }

    public void openFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            controlMenu.getPainterPane().getChildren().clear();
            controlMenu.getItemList().getItems().clear();

            while (fileInputStream.available() > 0) {
                Object object = objectInputStream.readObject();
                if (object instanceof Shape) {
                    controlMenu.getPainterPane().addShape((Shape) object);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("读取文件发生错误！");
                    alert.setContentText("可能是文件被损坏，或并非由 WisePainter 生成的 *.wpp 文件");
                    alert.showAndWait();
                    return;
                }
            }
            if (controlMenu.getPainterPane().getChildren().size() > 0) {
                controlMenu.refreshDetails((Shape) controlMenu.getPainterPane().getChildren().get(controlMenu.getPainterPane().getChildren().size() - 1));
            }
            setCurrentProjectFile(file);
            status.setSuccess("成功打开文件：" + file.getAbsoluteFile());
        }
        catch (Exception exception) {
            status.setError("打开文件失败：" + file.getAbsoluteFile());
            exception.printStackTrace();
        }
    }

    public void saveFile(File file) {
        try {
            if (!file.exists() && !file.createNewFile()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("无法保存在指定的位置！");
                alert.setContentText("指定的路径为：" + file.getAbsoluteFile());
                alert.showAndWait();
                return;
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            for (Node node : controlMenu.getPainterPane().getChildren()) {
                objectOutputStream.writeObject(node);
            }
            status.setSuccess("成功保存文件：" + file.getAbsoluteFile());
            setCurrentProjectFile(file);
        }
        catch (Exception exception) {
            status.setError("保存文件失败：" + file.getAbsoluteFile());
            exception.printStackTrace();
        }
    }
}
