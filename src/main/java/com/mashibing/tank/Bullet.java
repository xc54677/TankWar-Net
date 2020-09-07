package com.mashibing.tank;

import com.mashibing.tank.net.Client;
import com.mashibing.tank.net.TankDieMsg;

import java.awt.*;
import java.util.UUID;

/**
 * 子弹类
 */
public class Bullet {
    private static final int SPEED = 10;

    public static final int WIDTH = ResourceMgr.bulletD.getWidth();
    public static final int HEIGHT = ResourceMgr.bulletD.getHeight();

    private UUID id = UUID.randomUUID();
    private UUID playerId;

    private int x;
    private int y;
    private Dir dir;

    private TankFrame tf;
    private boolean living = true;
    private Group group = Group.BAD;

    Rectangle rect = new Rectangle();

    public Bullet(UUID playerId, int x, int y, Dir dir, Group group, TankFrame tf) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tf = tf;
        this.group = group;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public void paint(Graphics g) {
        if (!living){
            tf.bullets.remove(this);
        }

        switch (dir){
            case LEFT:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case UP:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
            default:
                break;
        }

        move();
    }

    private void move() {
        switch (dir) {
            case LEFT:
                x -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
            default:
                break;
        }

        //update rect value
        rect.x = this.x;
        rect.y = this.y;

        if (x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT){
            living = false;
        }
    }

    /**
     * 子弹和坦克的碰撞检测
     * @param tank
     */
    public void collideWith(Tank tank) {
        if(this.playerId.equals(tank.getId())) return;

        if (this.living && tank.isLiving() && this.rect.intersects(tank.rect)){
            tank.die();
            this.die();
            Client.INSTANCE.send(new TankDieMsg(this.id, tank.getId()));
        }
    }

    public void die() {
        this.living = false;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
