package com.pingao.model;

/**
 * Created by pingao on 2018/7/29.
 */
public class YamlSpan {
    private int start;
    private int end;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isInYaml(int line) {
        return start <= line && line <= end;
    }
}
