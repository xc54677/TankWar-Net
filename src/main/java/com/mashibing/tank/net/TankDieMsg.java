package com.mashibing.tank.net;

import com.mashibing.tank.*;

import java.io.*;
import java.util.UUID;

public class TankDieMsg extends Msg{
	public UUID bulletId;
	public UUID id;

	public TankDieMsg() {
	}

	public TankDieMsg(UUID playerId, UUID id) {
		this.bulletId = playerId;
		this.id = id;
	}

	public UUID getBulletId() {
		return bulletId;
	}

	public void setBulletId(UUID bulletId) {
		this.bulletId = bulletId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public byte[] toBytes(){
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		byte[] bytes = null;
		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			dos.writeLong(bulletId.getMostSignificantBits());
			dos.writeLong(bulletId.getLeastSignificantBits());
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
	public void parse(byte[] bytes) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			this.bulletId = new UUID(dis.readLong(), dis.readLong());
			this.id = new UUID(dis.readLong(), dis.readLong());
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
		return MsgType.TankDie;
	}

	@Override
	public String toString() {
		return "TankDieMsg{" +
				"bulletId=" + bulletId +
				", id=" + id +
				'}';
	}

	@Override
	public void handle() {
		System.out.println("we got a tank die:" + id);
		System.out.println("and my tank is:" + TankFrame.INSTANCE.getMainTank().getId());
		Tank tt = TankFrame.INSTANCE.findTankByUUID(id);
		System.out.println("i found a tank with this id:" + tt);

		Bullet b = TankFrame.INSTANCE.findBulletByUUID(bulletId);
		if(b != null) {
			b.die();
		}

		if(this.id.equals(TankFrame.INSTANCE.getMainTank().getId())) {
			TankFrame.INSTANCE.getMainTank().die();
		} else {

			Tank t = TankFrame.INSTANCE.findTankByUUID(id);
			if(t != null) {
				t.die();
			}
		}

	}
}
