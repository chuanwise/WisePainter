package com.chuanwise.wisepainting.assembly;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Status extends Label {

    public Status() {
        setNormal("就绪");
    }

    private void set(String str) {
        setText(str);
    }

    public void setNormal(String str) {
        setTextFill(Color.BLACK);
        setText("就绪 | " + str);
    }

    public void setSuccess(String str) {
        setTextFill(Color.GREEN);
        setText("成功 | " + str);
    }

    public void setDetail(String str) {
        setTextFill(Color.BLACK);
        set("图层细节 | " + str);
    }

    public void setWarning(String str) {
        setTextFill(Color.ORANGE);
        set("警告 | " + str);
    }

    public void setError(String str) {
        setTextFill(Color.RED);
        set("错误 | " + str);
    }
}
