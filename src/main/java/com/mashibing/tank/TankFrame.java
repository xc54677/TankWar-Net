package com.mashibing.tank;

import com.mashibing.tank.net.Client;
import com.mashibing.tank.net.TankDirChangedMsg;
import com.mashibing.tank.net.TankStartMovingMsg;
import com.mashibing.tank.net.TankStopMsg;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

/**
 * @author Xiao
 */
public class TankFrame extends Frame {

    public static final TankFrame INSTANCE = new TankFrame();

    Random r = new Random();

    Tank myTank = new Tank(r.nextInt(GAME_WIDTH), r.nextInt(GAME_HEIGHT), Dir.UP, Group.GOOD,this);
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    Map<UUID, Tank> tanks = new HashMap<UUID, Tank>();
    ArrayList<Explode> explodes = new ArrayList<Explode>();

    static final int GAME_WIDTH = 1080, GAME_HEIGHT = 960;

    public TankFrame() throws HeadlessException {
        // 设置窗口大小
        setSize(GAME_WIDTH, GAME_HEIGHT);
        //设置窗口大小可调整性-flase
        setResizable(false);
        //设置窗口的名字
        setTitle("The War fo Tank");
        //设置窗口的可见性- true
        setVisible(true);

        //设置窗口的监听器 - 关闭窗口程序自动退出
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(new MyKeyListener());
    }

    /**
     * 解决窗口闪烁问题
     */
    Image offScreenImage = null;
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null){
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLUE);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.black);
        g.drawString("子弹的数量：" + bullets.size(), 10, 60);
        g.drawString("敌方坦克的数量：" + tanks.size(), 10, 80);
        g.drawString("爆炸的数量：" + explodes.size(), 10, 100);
        g.setColor(c);

        myTank.paint(g);
        for (int i = 0; i < bullets.size(); i++){
            bullets.get(i).paint(g);
        }

        //画敌方坦克
        tanks.values().stream().forEach(tank -> tank.paint(g));

        //画出爆炸图片
        for (int i = 0; i < explodes.size(); i++){
            explodes.get(i).paint(g);
        }

        Collection<Tank> values = tanks.values();
        //子弹和坦克的碰撞检测
        for (int i = 0; i < bullets.size(); i++){
            for (Tank t : values){
                bullets.get(i).collideWith(t);
            }
        }
    }

    public void addTank(Tank t) {
        tanks.put(t.getId(), t);
    }

    class MyKeyListener extends KeyAdapter {
        boolean bL = false;
        boolean bR = false;
        boolean bU = false;
        boolean bD = false;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = true;
                    setMainTankDir();
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    setMainTankDir();
                    break;
                case KeyEvent.VK_UP:
                    bU = true;
                    setMainTankDir();
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    setMainTankDir();
                    break;
                default:
                    break;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = false;
                    setMainTankDir();
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    setMainTankDir();
                    break;
                case KeyEvent.VK_UP:
                    bU = false;
                    setMainTankDir();
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    setMainTankDir();
                    break;
                case KeyEvent.VK_CONTROL:
                    myTank.fire();
                    break;
                default:
                    break;
            }
            setMainTankDir();
        }

        private void setMainTankDir() {
            // save old dir status
            Dir dir = myTank.getDir();

            if (!bL && !bR && !bU && !bD){
                myTank.setMoving(false);
                //发出坦克停止的消息
                Client.INSTANCE.send(new TankStopMsg(getMainTank()));
            }else{

                if (bL) myTank.setDir(Dir.LEFT);
                if (bR) myTank.setDir(Dir.RIGHT);
                if (bU) myTank.setDir(Dir.UP);
                if (bD) myTank.setDir(Dir.DOWN);

                //发出坦克移动的消息
                if (!myTank.isMoving()){
                    Client.INSTANCE.send(new TankStartMovingMsg(getMainTank()));
                }

                myTank.setMoving(true);

                if (dir != myTank.getDir()){
                    Client.INSTANCE.send(new TankDirChangedMsg(getMainTank()));
                }

            }
        }
    }

    public Tank findTankByUUID(UUID id){
        return tanks.get(id);
    }

    public Bullet findBulletByUUID(UUID id) {
        for(int i=0; i<bullets.size(); i++) {
            if(bullets.get(i).getId().equals(id))
                return bullets.get(i);
        }

        return null;
    }

    public void addBullet(Bullet b) {
        bullets.add(b);
    }



    public Tank getMainTank(){
        return this.myTank;
    }
}
