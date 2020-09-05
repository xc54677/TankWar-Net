package com.mashibing.tank.net;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Group;
import com.mashibing.tank.Tank;
import com.mashibing.tank.TankFrame;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class TankJoinMsg extends Msg{
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
	public byte[] toBytes(){
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		byte[] bytes = null;
		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
			dos.writeBoolean(moving);
			dos.writeInt(group.ordinal());
			dos.writeLong(id.getMostSignificantBits());
			dos.writeLong(id.getLeastSignificantBits());
			dos.flush();
			bytes = baos.toByteArray();

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (baos != null){
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (dos != null){
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bytes;
	}

	@Override
	public String toString() {
		return "TankJoinMsg{" +
				"x=" + x +
				", y=" + y +
				", dir=" + dir +
				", moving=" + moving +
				", group=" + group +
				", id=" + id +
				'}';
	}

	@Override
	public void handle() {
		if (this.id.equals(TankFrame.INSTANCE.getMainTank().getId())
				|| TankFrame.INSTANCE.findByUUID(this.id) != null){
			return;
		}

		System.out.println(this);

		Tank t = new Tank(this);
		TankFrame.INSTANCE.addTank(t);

		Client.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
	}
}
