package com.chuanwise.wisepainting.assembly;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.chuanwise.wisepainting.icon.BrushIcon;
import com.chuanwise.wisepainting.shape.Ellipse;
import com.chuanwise.wisepainting.shape.Line;
import com.chuanwise.wisepainting.shape.Rectangle;
import com.chuanwise.wisepainting.shape.WiseShape;

import static javafx.scene.paint.Color.*;

public class ControlMenu {
    private PainterPane painterPane;
    private ItemList itemList;
    private VBox brushVBox = new VBox(10);
    private VBox detailVBox = new VBox(10);

    private Button move = new Button();
    private Button undo = new Button();
    private Button redo = new Button();
    private Button clear = new Button();
    private Button remove = new Button();
    private Button ellipse = new Button();
    private Button rectangle = new Button();
    private Button line = new Button();

    private Label rotateNotice = new Label("旋转角度");
    private Label borderSizeNotice = new Label("描边粗细");
    private Label borderExistNotice = new Label("启用描边");
    private Label borderColorNotice = new Label("描边颜色");
    private Label fillExistNotice = new Label("启用填充");
    private Label fillColorNotice = new Label("填充颜色");

    private Slider rotateSlider = new Slider();
    private Slider borderSizeSlider = new Slider();
    private CheckBox borderExist = new CheckBox();
    private CheckBox fillExist = new CheckBox();
    private ColorPicker borderColorPicker = new ColorPicker(BLACK);
    private ColorPicker fillColorPicker = new ColorPicker(WHITE);

    private Status status;

    public void setDrawing(boolean drawing) {
        painterPane.setDrawAble(drawing);
    }

    public ControlMenu(PainterPane painterPane, ItemList itemList) {
        setItemList(itemList);
        setPainterPane(painterPane);
        init();
    }

    private void init() {
        initBrushVBox();
        initDetailVBox();
        painterPane.setControlMenu(this);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private void initBrushVBox() {
        brushVBox.setSpacing(10);
        brushVBox.getChildren().addAll(move, remove, undo, redo, new Separator(), ellipse, rectangle, line, new Separator(), clear);

        move.setGraphic(new ImageView(BrushIcon.SELECTED_MOVE));
        remove.setGraphic(new ImageView(BrushIcon.UNSELECTED_REMOVE));
        undo.setGraphic(new ImageView(BrushIcon.UNSELECTED_UNDO));
        redo.setGraphic(new ImageView(BrushIcon.UNSELECTED_REDO));

        ellipse.setGraphic(new ImageView(BrushIcon.UNSELECTED_ELLIPSE));
        line.setGraphic(new ImageView(BrushIcon.UNSELECTED_LINE));
        rectangle.setGraphic(new ImageView(BrushIcon.UNSELECTED_RECTANGLE));

        clear.setGraphic(new ImageView(BrushIcon.UNSELECTED_CLEAR));

        move.setOnAction(e -> {
            cancelOtherButton();
            setDrawing(false);
            move.setGraphic(new ImageView(BrushIcon.SELECTED_MOVE));
            status.setSuccess("移动模式");
        });

        undo.setOnAction(e -> {
            painterPane.undo();
        });

        redo.setOnAction(e -> {
            painterPane.redo();
        });

        remove.setOnAction(e -> {
            Shape chooseShape = (Shape) painterPane.getChosenShape();
            if (chooseShape == null) {
                status.setError("删除图形前必须先选择一个图形！");
                return;
            }
            status.setSuccess("成功清除一个" + ((WiseShape) chooseShape).getType());
            painterPane.removeShape(chooseShape);
            painterPane.setChosenShape(null);
        });

        rectangle.setOnAction(e -> {
            setDrawing(true);
            cancelOtherButton();
            rectangle.setGraphic(new ImageView(BrushIcon.SELECTED_RECTANGLE));
            painterPane.setChosenShape(null);
            painterPane.setWiseShape(new Rectangle());
            status.setSuccess("矩形模式（按下 Shift 时绘制正方形，按下 Alt 时以初始位置为中心绘制）");
        });

        ellipse.setOnAction(e -> {
            setDrawing(true);
            cancelOtherButton();
            ellipse.setGraphic(new ImageView(BrushIcon.SELECTED_ELLIPSE));
            painterPane.setChosenShape(null);
            painterPane.setWiseShape(new Ellipse());
            status.setSuccess("椭圆模式（按下 Shift 时绘制圆形，按下 Alt 时以初始位置为中心绘制）");
        });

        line.setOnAction(e -> {
            setDrawing(true);
            cancelOtherButton();
            line.setGraphic(new ImageView(BrushIcon.SELECTED_LINE));
            painterPane.setChosenShape(null);
            painterPane.setWiseShape(new Line());
            status.setSuccess("线段模式（按下 Shift 时线条对准正八个方位，按下 Alt 时以初始位置为中心绘制）");
        });

        clear.setOnAction(e -> {
            Alert certain = new Alert(Alert.AlertType.CONFIRMATION);
            certain.setHeaderText("该操作将会清空画板，确定要这么做吗？");
            certain.setContentText("该操作不可撤销！");

            certain.showAndWait();
            if (certain.getResult().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                painterPane.getChildren().clear();
                itemList.getItems().clear();
                status.setSuccess("成功清空画板");
            }
        });

    }

    private void initDetailVBox() {
        GridPane gridPane = new GridPane();
        Label title = new Label("属性设置");
        title.setFont(Font.font(null, FontWeight.BOLD, -1));
        detailVBox.getChildren().addAll(title, gridPane);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(rotateNotice, 0, 0);
        gridPane.add(rotateSlider, 1, 0);
        rotateSlider.setMajorTickUnit(45);
        rotateSlider.setMax(360);
        rotateSlider.setShowTickMarks(true);
        rotateSlider.setShowTickLabels(true);
        rotateSlider.valueProperty().addListener(e -> {
            if (painterPane.getChildren().size() > 0) {
                painterPane.setRotateVal(rotateSlider.getValue());
            }
            else {
                status.setError("没有需要可用旋转的形状");
            }
        });

        borderExist.setTextFill(RED);
        borderExist.setSelected(true);
        borderExist.setOnAction(e -> {
            if (borderExist.isSelected()) {
                fillExist.setText(null);
                borderExist.setText(null);
                borderColorNotice.setTextFill(BLACK);
                borderSizeNotice.setTextFill(BLACK);
                borderColorPicker.setDisable(false);
                borderSizeSlider.setDisable(false);
                painterPane.setStrokeWidth(borderSizeSlider.getValue());
                painterPane.setBorder(borderColorPicker.getValue());
            }
            else {
                if (!fillExist.isSelected()) {
                    borderExist.setText("描边和填充应至少启用一项");
                    borderExist.setSelected(true);
                    return;
                }
                else {
                    fillExist.setText(null);
                    borderExist.setText(null);
                }
                borderColorNotice.setTextFill(GREY);
                borderSizeNotice.setTextFill(GREY);
                borderColorPicker.setDisable(true);
                borderSizeSlider.setDisable(true);
                painterPane.setStrokeWidth(0);
                painterPane.setBorder((Color) null);
            }
        });

        gridPane.add(borderExistNotice, 0, 1);
        gridPane.add(borderExist, 1, 1);

        gridPane.add(borderSizeNotice, 0, 2);
        gridPane.add(borderSizeSlider, 1, 2);

        borderSizeSlider.setShowTickLabels(true);
        borderSizeSlider.setShowTickMarks(true);
        borderSizeSlider.setMajorTickUnit(10);
        borderSizeSlider.setMax(50);
        borderSizeSlider.setValue(10);
        painterPane.setStrokeWidth(borderSizeSlider.getValue());
        borderSizeSlider.valueProperty().addListener(e -> {
            painterPane.setStrokeWidth(borderSizeSlider.getValue());
        });

        gridPane.add(borderColorNotice, 0, 3);
        gridPane.add(borderColorPicker, 1, 3);
        borderColorPicker.setValue(Color.color(82 / 255.0, 2 / 255.0, 179 / 255.0));    // #5202b3
        painterPane.setBorder(borderColorPicker.getValue());
        borderColorPicker.setOnAction(e -> {
            painterPane.setBorder(borderColorPicker.getValue());
        });

        gridPane.add(fillExistNotice, 0, 4);
        gridPane.add(fillExist, 1, 4);

        fillExist.setTextFill(RED);
        fillColorPicker.setValue(borderColorPicker.getValue().brighter());
        fillExist.setSelected(true);
        painterPane.setFill(fillColorPicker.getValue());
        fillExist.setOnAction(e -> {
            if (fillExist.isSelected()) {
                fillExist.setText(null);
                borderExist.setText(null);
                fillColorPicker.setDisable(false);
                fillColorNotice.setTextFill(BLACK);
                painterPane.setFill(fillColorPicker.getValue());
            }
            else {
                if (!borderExist.isSelected()) {
                    fillExist.setText("描边和填充应至少启用一项");
                    fillExist.setSelected(true);
                    return;
                }
                else {
                    fillExist.setText(null);
                    borderExist.setText(null);
                }
                fillColorNotice.setTextFill(GREY);
                fillColorPicker.setDisable(true);
                painterPane.setFill(null);
            }
        });

        gridPane.add(fillColorNotice, 0, 5);
        gridPane.add(fillColorPicker, 1, 5);
        fillColorPicker.setOnAction(e -> {
            painterPane.setFill(fillColorPicker.getValue());
        });

        gridPane.getColumnConstraints().addAll(
                new ColumnConstraints(80),
                new ColumnConstraints(325)
        );
        gridPane.getRowConstraints().addAll(
                new RowConstraints(30),
                new RowConstraints(30),
                new RowConstraints(30),
                new RowConstraints(30),
                new RowConstraints(30),
                new RowConstraints(30)
        );
    }

    public void refreshDetails(Shape shape) {
        rotateSlider.setValue(shape.getRotate());
        fillExist.setText(null);
        borderExist.setText(null);

        if (shape.getFill() == null) {
            fillColorPicker.setDisable(true);
            fillColorNotice.setTextFill(GREY);
            fillExist.setSelected(false);
        }
        else {
            fillColorPicker.setValue((Color) shape.getFill());
            fillColorPicker.setDisable(false);
            fillColorNotice.setTextFill(BLACK);
            fillExist.setSelected(true);
        }

        if (shape.getStrokeWidth() == 0) {
            borderExist.setTextFill(GREY);
            borderSizeNotice.setTextFill(GREY);
            borderSizeSlider.setDisable(true);
            borderExist.setSelected(false);
            borderColorPicker.setDisable(true);
            borderColorNotice.setTextFill(GREY);
        }
        else {
            borderColorPicker.setDisable(false);
            borderColorPicker.setValue((Color) shape.getStroke());
            borderColorNotice.setTextFill(BLACK);
            borderExist.setSelected(true);
            borderExist.setTextFill(RED);
            borderSizeNotice.setTextFill(BLACK);
            borderSizeSlider.setDisable(false);
            borderSizeSlider.setValue(shape.getStrokeWidth());
        }
    }

    private void cancelOtherButton() {
        painterPane.setWiseShape(null);
        move.setGraphic(new ImageView(BrushIcon.UNSELECTED_MOVE));
        ellipse.setGraphic(new ImageView(BrushIcon.UNSELECTED_ELLIPSE));
        rectangle.setGraphic(new ImageView(BrushIcon.UNSELECTED_RECTANGLE));
        line.setGraphic(new ImageView(BrushIcon.UNSELECTED_LINE));
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

    public void setPainterPane(PainterPane painterPane) {
        this.painterPane = painterPane;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public PainterPane getPainterPane() {
        return painterPane;
    }

    public VBox getBrushVBox() {
        return brushVBox;
    }

    public VBox getDetailVBox() {
        return detailVBox;
    }
}
