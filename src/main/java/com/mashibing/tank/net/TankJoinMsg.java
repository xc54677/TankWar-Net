package com.mashibing.tank.net;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Group;
import com.mashibing.tank.Tank;

import java.util.UUID;

public class TankJoinMsg {
	public int x, y;
	public Dir dir;
	public boolean moving;
	public Group group;
	public UUID id;

	public TankJoinMsg(Tank t) {
		this.x = t.getX();
		this.y = t.getY();
		this.dir = t.getDir();
		this.moving = t.isMoving();
		this.group = t.getGroup();
		this.id = t.getId();
	}

	public TankJoinMsg(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public TankJoinMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.moving = moving;
		this.group = group;
		this.id = id;
	}

	public TankJoinMsg() {
	}

	@Override
	public String toString() {
		return "TankMsg:" + x + "," + y; 
	}
}
