package com.mashibing.tank.net;

import java.util.List;
import java.util.UUID;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Group;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TankJoinMsgDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() < 8) return; //TCP 拆包 粘包的问题

		in.markReaderIndex(); //给ByteBuf做标记，标记从哪里

		MsgType msgType = MsgType.values()[in.readInt()];
		int length = in.readInt();

		if (in.readableBytes() < length){
			in.resetReaderIndex();
			return;
		}

		byte[] bytes = new byte[length];
		in.readBytes(bytes);

		switch (msgType){
			case TankJoin:
				TankJoinMsg joinMsg = new TankJoinMsg();
				joinMsg.parse(bytes);
				out.add(joinMsg);
				break;
			default:
				break;
		}
		

	}

}
