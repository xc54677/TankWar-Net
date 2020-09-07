package com.mashibing.tank.net;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Group;
import com.mashibing.tank.Tank;
import com.mashibing.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankStartMovingMsg extends Msg{
	public int x, y;
	public Dir dir;
	public UUID id;

	public TankStartMovingMsg() {
	}

	public TankStartMovingMsg(Tank t) {
		this.x = t.getX();
		this.y = t.getY();
		this.dir = t.getDir();
		this.id = t.getId();
	}

	public TankStartMovingMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.id = id;
	}

	public Dir getDir() {
		return dir;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public UUID getId() {
		return id;
	}

	@Override
	public byte[] toBytes(){
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		byte[] bytes = null;
		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			dos.writeLong(id.getMostSignificantBits());
			dos.writeLong(id.getLeastSignificantBits());
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
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
	public void parse(byte[] bytes) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			this.id = new UUID(dis.readLong(), dis.readLong());
			this.x = dis.readInt();
			this.y = dis.readInt();
			this.dir = Dir.values()[dis.readInt()];
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public MsgType getMsgType() {
		return MsgType.TankStartMoving;
	}

	@Override
	public String toString() {
		return "TankJoinMsg{" +
				"x=" + x +
				", y=" + y +
				", dir=" + dir +
				", id=" + id +
				'}';
	}

	@Override
	public void handle() {
		if (this.id.equals(TankFrame.INSTANCE.getMainTank().getId())){
			return;
		}

		Tank t = TankFrame.INSTANCE.findTankByUUID(this.id);
		if (t != null){
			t.setMoving(true);
			t.setX(this.x);
			t.setY(this.y);
			t.setDir(this.dir);
		}
	}
}
