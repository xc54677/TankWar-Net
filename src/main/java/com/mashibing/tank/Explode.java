package com.mashibing.tank;

import java.awt.*;

/**
 * 子弹类
 */
public class Explode {
    public static final int WIDTH = ResourceMgr.explodes[0].getWidth();
    public static final int HEIGHT = ResourceMgr.explodes[0].getHeight();

    private int x;
    private int y;

    private TankFrame tf;
    private boolean living = true;

    // 爆炸有16张图片合成显示，0代表从第一张图片开始绘图
    private int step = 0;

    public Explode(int x, int y, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.tf = tf;
//        new Audio("H:\\IDEAWorkSpace\\Tank\\src\\audio\\explode.wav").loop();
    }

    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.explodes[step++], x, y, null);
        if (step >= ResourceMgr.explodes.length){
            tf.explodes.remove(this);
        }

    }
}
