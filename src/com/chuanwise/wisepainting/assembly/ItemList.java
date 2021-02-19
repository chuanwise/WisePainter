package com.chuanwise.wisepainting.assembly;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Shape;
import com.chuanwise.wisepainting.shape.WiseShape;

class ListNode {
    private WiseShape body;
    private SimpleStringProperty type = new SimpleStringProperty();
    private SimpleStringProperty fillColor = new SimpleStringProperty();
    private SimpleStringProperty strokeColor = new SimpleStringProperty();
    private SimpleStringProperty strokeWidth = new SimpleStringProperty();
    private SimpleStringProperty area = new SimpleStringProperty();

    public ListNode (WiseShape what) {
        setBody(what);
        setType(body.getType());
        setArea(String.valueOf(what.getArea()));
        if (((Shape) what).getFill() == null) {
            setFillColor("（未启用）");
        }
        else {
            setFillColor(((Shape) what).getFill().toString());
        }
        if (((Shape) what).getStroke() == null) {
            setStrokeColor("（未启用）");
        }
        else {
            setStrokeColor(((Shape) what).getStroke().toString());
        }

        setStrokeWidth(String.valueOf(((Shape) what).getStrokeWidth()));

        ((Shape) what).fillProperty().addListener(e -> {
            if (((Shape) what).getFill() == null) {
                fillColor.set("（无颜色）");
            }
            else {
                fillColor.set(((Shape) what).getFill().toString());
            }
        });
        ((Shape) what).strokeProperty().addListener(e -> {
            if (((Shape) what).getStroke() == null) {
                strokeColor.set("（无颜色）");
            }
            else {
                strokeColor.set(Double.toString(((Shape) what).getStrokeWidth()));
            }
        });
        ((Shape) what).strokeWidthProperty().addListener(e -> {
            strokeWidth.set(String.valueOf(((Shape) what).getStrokeWidth()));
        });
    }

    public void setFillColor(String fillColor) {
        this.fillColor.set(fillColor);
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor.set(strokeColor);
    }

    public void setStrokeWidth(String strokeWidth) {
        this.strokeWidth.set(strokeWidth);
    }

    public void setArea(String area) {
        this.area.set(area);
    }

    public String getArea() {
        return area.get();
    }

    public void setBody(WiseShape body) {
        this.body = body;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public WiseShape getBody() {
        return body;
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public SimpleStringProperty areaProperty() {
        return area;
    }

    public SimpleStringProperty strokeColorProperty() {
        return strokeColor;
    }

    public SimpleStringProperty strokeWidthProperty() {
        return strokeWidth;
    }

    public SimpleStringProperty fillColorProperty() {
        return fillColor;
    }
}

public class ItemList extends TableView<ListNode> {
    private TableColumn<ListNode, String> nameColumn = new TableColumn<>("类型");
    private TableColumn<ListNode, String> areaColumn = new TableColumn<>("面积");
    private TableColumn<ListNode, String> strokeColorColumn = new TableColumn<>("描边颜色");
    private TableColumn<ListNode, String> strokeWidthColumn = new TableColumn<>("描边粗细");
    private TableColumn<ListNode, String> fillColorColumn = new TableColumn<>("填充颜色");

    public ItemList() {
        init();
    }

    private void init() {
        getColumns().addAll(nameColumn, areaColumn, strokeColorColumn, strokeWidthColumn, fillColorColumn);
        nameColumn.setCellValueFactory(e -> e.getValue().typeProperty());
        areaColumn.setCellValueFactory(e -> e.getValue().areaProperty());
        strokeColorColumn.setCellValueFactory(e -> e.getValue().strokeColorProperty());
        strokeWidthColumn.setCellValueFactory(e -> e.getValue().strokeWidthProperty());
        fillColorColumn.setCellValueFactory(e -> e.getValue().fillColorProperty());
    }

    public void append(WiseShape shape) {
        getItems().add(new ListNode(shape));
    }

    public void pop() {
        if (getItems().isEmpty()) {
            return;
        }
        getItems().remove(getItems().size() - 1);
    }

    public ListNode peek() {
        return getItems().get(getItems().size() - 1);
    }

    public void remove(Shape shape) {
        for (ListNode listNode: getItems()) {
            if (listNode.getBody() == (WiseShape) shape) {
                getItems().remove(shape);
                break;
            }
        }
    }
}
