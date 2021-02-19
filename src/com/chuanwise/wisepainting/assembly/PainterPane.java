package com.chuanwise.wisepainting.assembly;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import com.chuanwise.wisepainting.shape.Ellipse;
import com.chuanwise.wisepainting.shape.Line;
import com.chuanwise.wisepainting.shape.Rectangle;
import com.chuanwise.wisepainting.shape.WiseShape;

import java.util.Stack;

public class PainterPane extends Pane {
    private boolean drawAble = false;
    private Stack<Shape> undoList = new Stack<>();
    private Status status;
    private WiseShape wiseShape;
    private ItemList itemList;
    private Color border, fill;
    private double rotate = 0;
    private double borderSize;
    private Effect glowEffect = new Glow(0.5);

    private Shape newShape = null;

    private WiseShape chosenShape;
    private ControlMenu controlMenu;

    private double x1, x2, y1, y2;
    private double deltaX, deltaY;

    public PainterPane() {
//        setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(2), new Insets(0))));
        init();
    }

    public WiseShape getChosenShape() {
        return chosenShape;
    }

    private void init() {
        setHandler();
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

    private void setHandler() {
        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (!isDrawAble()) {
                return;
            }
            x1 = e.getX();
            y1 = e.getY();

            if (wiseShape instanceof Ellipse) {
                newShape = new Ellipse();
            }
            if (wiseShape instanceof Rectangle) {
                newShape = new Rectangle();
            }
            if (wiseShape instanceof Line) {
                newShape = new Line();
            }
            getChildren().add(newShape);
            status.setSuccess("正在绘制图形");

            newShape.setFill(fill);
            newShape.setStroke(border);
            newShape.setStrokeWidth(borderSize);
            newShape.setRotate(rotate);
        });

        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (!isDrawAble()) {
                return;
            }
            chosenShape = (WiseShape) newShape;
            itemList.append((WiseShape) newShape);
            status.setSuccess("成功绘制图形");

            setShapeHandler(newShape);
        });

        addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!isDrawAble()) {
                return;
            }
            x2 = e.getX();
            y2 = e.getY();

            deltaX = x2 - x1;
            deltaY = y2 - y1;

            if (newShape == null) {
                status.setError("意外地发现 shape 的内容为 null");
                return;
            }

            if (newShape instanceof Ellipse) {
                if (e.isShiftDown()) {
                    deltaX = deltaY = Math.min(deltaY, deltaX);
                }
                ((Ellipse) newShape).setRadiusX(Math.abs(deltaX) / 2);
                ((Ellipse) newShape).setRadiusY(Math.abs(deltaY) / 2);
                if (e.isAltDown()) {
                    ((Ellipse) newShape).setCenterX(x1);
                    ((Ellipse) newShape).setCenterY(y1);
                }
                else {
                    ((Ellipse) newShape).setCenterX(x1 + deltaX / 2);
                    ((Ellipse) newShape).setCenterY(y1 + deltaY / 2);
                }
            }
            if (newShape instanceof Rectangle) {
                if (e.isShiftDown()) {
                    deltaX = deltaY = Math.min(deltaY, deltaX);
                }
                if (e.isAltDown()) {
                    ((Rectangle) newShape).setHeight(Math.abs(deltaY) * 2);
                    ((Rectangle) newShape).setWidth(Math.abs(deltaX) * 2);
                    ((Rectangle) newShape).setX(x1 - deltaX);
                    ((Rectangle) newShape).setY(y1 - deltaY);
                }
                else {
                    ((Rectangle) newShape).setHeight(Math.abs(deltaY));
                    ((Rectangle) newShape).setWidth(Math.abs(deltaX));

                    if (deltaX < 0) {
                        ((Rectangle) newShape).setX(x2);
                    }
                    else {
                        ((Rectangle) newShape).setX(x1);
                    }
                    if (deltaY < 0) {
                        ((Rectangle) newShape).setY(y2);
                    }
                    else {
                        ((Rectangle) newShape).setY(y1);
                    }
                }
            }
            if (newShape instanceof Line) {
                if (e.isShiftDown() && deltaX != 0) {
                    boolean overX = deltaY > 0;
                    boolean rightY = deltaX > 0;
                    deltaX = Math.abs(deltaX);
                    deltaY = Math.abs(deltaY);

                    double absK = deltaY / deltaX;
                    if (absK < 0.25) {
                        deltaY = 0;
                    }
                    else if (absK < 2) {
                        deltaY = deltaX = Math.min(deltaX, deltaY);
                    }
                    else {
                        deltaX = 0;
                    }
                    deltaY *= overX ? 1 : -1;
                    deltaX *= rightY ? 1 : -1;
                }
                if (e.isAltDown()) {
                    ((Line) newShape).setStartX(x1 - deltaX);
                    ((Line) newShape).setStartY(y1 - deltaY);
                } else {
                    ((Line) newShape).setStartX(x1);
                    ((Line) newShape).setStartY(y1);
                }
                ((Line) newShape).setEndX(x1 + deltaX);
                ((Line) newShape).setEndY(y1 + deltaY);
            }
        });
    }

    public void setShapeHandler(Shape newShape) {
        // 提示用户该结点可点击
//            System.out.println("当前绑定对象：" + newShape.hashCode());
        newShape.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                if (isDrawAble()) {
                    return;
                }

                newShape.setCursor(Cursor.HAND);
                newShape.setEffect(glowEffect);
        });

        newShape.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (isDrawAble()) {
                return;
            }
            newShape.setCursor(Cursor.DEFAULT);
            newShape.setEffect(null);
        });

        // 提示用户该结点可拖拽
        newShape.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (isDrawAble()) {
                return;
            }
            newShape.setCursor(Cursor.MOVE);

            // 当按压事件发生时，缓存事件发生的位置坐标
            x1 = event.getX();
            y1 = event.getY();
            chosenShape = (WiseShape) newShape;
            refreshSetting();
        });

        newShape.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            if (isDrawAble()) {
                return;
            }
            newShape.setCursor(Cursor.DEFAULT);
        });

        // 实现拖拽功能
        newShape.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (isDrawAble()) {
                return;
            }
            double distanceX = event.getX() - x1;
            double distanceY = event.getY() - y1;

            double x = newShape.getLayoutX() + distanceX;
            double y = newShape.getLayoutY() + distanceY;

//                newShape.relocate(x, y);
            newShape.setLayoutX(x);
            newShape.setLayoutY(y);
        });
    }

    public void addShape(Shape shape) {
        getChildren().add(shape);
        setShapeHandler(shape);
        itemList.getItems().add(new ListNode((WiseShape) shape));
    }

    public void removeShape(Shape shape) {
        for (Node node: getChildren()) {
            if (node == shape) {
                getChildren().remove(shape);
                break;
            }
        }
        itemList.remove(shape);
    }

    private void refreshSetting() {
        controlMenu.refreshDetails((Shape) chosenShape);
    }

    public void setControlMenu(ControlMenu controlMenu) {
        this.controlMenu = controlMenu;
    }

    public void setRotateVal(double rotate) {
        this.rotate = rotate;
        if (chosenShape != null) {
            ((Shape) chosenShape).setRotate(rotate);
        }
    }

    public void setWiseShape(WiseShape wiseShape) {
        this.wiseShape = wiseShape;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setChosenShape(WiseShape chosenShape) {
        this.chosenShape = chosenShape;
    }

    public void undo() {
        if (getChildren().isEmpty()) {
            status.setError("没有需要撤销的操作");
            return;
        }
        chosenShape = null;
        undoList.push((Shape) getChildren().get(getChildren().size() - 1));
        itemList.pop();
        getChildren().remove(getChildren().size() - 1);
        status.setSuccess("撤销成功");
    }

    public void redo() {
        if (undoList.isEmpty()) {
            status.setError("没有需要重做的操作");
            return;
        }
        chosenShape = null;
        getChildren().add(undoList.peek());
        itemList.append((WiseShape) undoList.peek());
        undoList.pop();
        status.setSuccess("重做成功");
    }

    public boolean isDrawAble() {
        return drawAble;
    }

    public void setDrawAble(boolean drawAble) {
        this.drawAble = drawAble;
    }

    public void setStrokeWidth(double borderSize) {
        this.borderSize = borderSize;
        if (chosenShape != null) {
            ((Shape) chosenShape).setStrokeWidth(borderSize);
        }
    }

    public void setFill(Color fill) {
        this.fill = fill;
        if (chosenShape != null) {
            ((Shape) chosenShape).setFill(fill);
        }
    }

    public void setBorder(Color border) {
        this.border = border;
        if (chosenShape != null) {
            ((Shape) chosenShape).setStroke(border);
        }
    }
}
