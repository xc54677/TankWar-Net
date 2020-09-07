package com.mashibing.tank.net;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Tank;
import com.mashibing.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankStopMsg extends Msg{
	public int x, y;
	public UUID id;

	public TankStopMsg() {
	}

	public TankStopMsg(Tank t) {
		this.x = t.getX();
		this.y = t.getY();
		this.id = t.getId();
	}

	public TankStopMsg(int x, int y, Dir dir) {
		this.x = x;
		this.y = y;
		this.id = id;
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
		return MsgType.TankStop;
	}

	@Override
	public String toString() {
		return "TankJoinMsg{" +
				"x=" + x +
				", y=" + y +
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
			t.setMoving(false);
			t.setX(this.x);
			t.setY(this.y);
		}
	}
}
