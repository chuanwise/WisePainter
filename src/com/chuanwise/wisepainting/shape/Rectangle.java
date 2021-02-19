package com.chuanwise.wisepainting.shape;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Rectangle extends javafx.scene.shape.Rectangle implements WiseShape {

    private static final long serialVersionUID = -2980591594052751176L;

    @Override
    public double getArea() {
        return getWidth() * getHeight();
    }

    @Override
    public String getType() {
        return getWidth() == getHeight() ? "正方形" : "矩形";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(getX());
        out.writeDouble(getY());
        out.writeDouble(getWidth());
        out.writeDouble(getHeight());

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
        setX(in.readDouble());
        setY(in.readDouble());
        setWidth(in.readDouble());
        setHeight(in.readDouble());

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
