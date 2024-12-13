package com.lifestore.areas.Packet;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import com.lifestore.areas.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LifePacket implements IMessage {
	
	public String data;
	
	public LifePacket() {}
	
	public LifePacket(String data) {
		this.data = data;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int len = buf.readInt();
		data = buf.toString(buf.readerIndex(), len, StandardCharsets.UTF_8);
		buf.readerIndex(buf.readerIndex() + len);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}
	
	public static class Handle implements IMessageHandler<LifePacket, IMessage> {
		@Override
		public IMessage onMessage(LifePacket message, MessageContext ctx) {
			try {
				LifeMessage.getInstance().handle(message);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return null;
		}
	}
	public static void sendMessage(String msg){
		StringBuilder builder = new StringBuilder(msg);
		ClientProxy.AREAPACKET.sendToServer(new LifePacket(builder.toString()));
	}
}
