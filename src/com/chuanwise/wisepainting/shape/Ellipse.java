package com.chuanwise.wisepainting.shape;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Ellipse extends javafx.scene.shape.Ellipse implements WiseShape {

    private static final long serialVersionUID = -8080559575646645303L;

    @Override
    public double getArea() {
        return getRadiusX() * getCenterY() * Math.PI;
    }

    @Override
    public String getType() {
        return ((Ellipse) this).getRadiusX() == ((Ellipse) this).getRadiusY() ? "圆" : "椭圆";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(getCenterX());
        out.writeDouble(getCenterY());
        out.writeDouble(getRadiusX());
        out.writeDouble(getRadiusY());

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
        setCenterX(in.readDouble());
        setCenterY(in.readDouble());
        setRadiusX(in.readDouble());
        setRadiusY(in.readDouble());

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
