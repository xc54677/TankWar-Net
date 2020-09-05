package com.mashibing.tank;


import com.mashibing.tank.net.Client;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //新建windows窗口
        TankFrame tf = TankFrame.INSTANCE;
        tf.setVisible(true);

        /*int initTankCount = Integer.parseInt((String) PropertyMgr.getProps().get("initTankCount"));

        for (int i = 0; i < initTankCount; i++){
            tf.tanks.add(new Tank(50+i*80, 200, Dir.DOWN, Group.BAD, tf));
        }*/

        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tf.repaint();
            }
        }).start();

        Client.INSTANCE.connect();

    }

}
