package com.pingao.model;

import com.pingao.enums.Operate;


/**
 * Created by pingao on 2018/7/21.
 */
public class MarkDownUnit {
    private String id;
    private Operate operate;
    private String content;
    private int isMathJax;

    public MarkDownUnit(String id, Operate operate, String content, int isMathJax) {
        this.id = id;
        this.operate = operate;
        this.content = content;
        this.isMathJax = isMathJax;
    }
}
