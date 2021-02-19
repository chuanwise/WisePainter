package com.chuanwise.wisepainting.shape;

import java.io.Externalizable;

public interface WiseShape extends Externalizable {
    double getArea();
    String getType();
}
