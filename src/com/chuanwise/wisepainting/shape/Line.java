package com.chuanwise.wisepainting.shape;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Line extends javafx.scene.shape.Line implements WiseShape{

    private static final long serialVersionUID = 5067646213408406875L;

    @Override
    public double getArea() {
        return 0;
    }

    @Override
    public String getType() {
        return "线段";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(getStartX());
        out.writeDouble(getStartY());
        out.writeDouble(getEndX());
        out.writeDouble(getEndY());

        if (getStroke() == null) {
            out.writeObject("null");
        }
        else {
            out.writeObject(getStroke().toString());
        }

        out.writeDouble(getRotate());
        out.writeDouble(getStrokeWidth());

        if (getFill() == null) {
            out.writeObject("null");
        }
        else {
            out.writeObject(getFill().toString());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setStartX(in.readDouble());
        setStartY(in.readDouble());
        setEndX(in.readDouble());
        setEndY(in.readDouble());

        String colorString = (String) in.readObject();
        if (colorString.equals("null")) {
            setStroke(null);
        }
        else {
            setStroke(Color.valueOf(colorString));
        }

        setRotate(in.readDouble());
        setStrokeWidth(in.readDouble());

        colorString = (String) in.readObject();
        if (colorString.equals("null")) {
            setFill(null);
        }
        else {
            setFill(Color.valueOf(colorString));
        }
    }
}
