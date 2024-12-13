package com.lifestore.areas.Packet;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import com.lifestore.areas.Area;
import com.lifestore.areas.UI.UpgradeUI;
import com.lifestore.areas.util.Render;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LifeMessage {
	
	private static LifeMessage instance;
	public static HashMap<String, Area> areas = new HashMap<>();
	public static List<UUID> owners = new ArrayList<>();
	private Minecraft mc = Minecraft.getMinecraft();
	public static LifeMessage getInstance() {
		if(instance == null) instance = new LifeMessage();
		return instance;
	}
	
	private LifeMessage() {
	}
	public void handle(LifePacket message) throws IOException {
		String code = message.data.substring(0,2);
		System.out.println("Geullo Area :: "+ message.data);
		if (code.equals(LifePacketList.RESET_AREAS.recogCode)) {
			String s = message.data.substring(2);
			if (s==null||s.equals("")) LifePacket.sendMessage(LifePacketList.GET_AREAS.recogCode);
		}
		else if (code.equals(LifePacketList.ADD_AREAS.recogCode)) {
			String s = message.data.substring(2);
			String[] a = s.split("}}");
			String[] b = a[0].split("\\|\\|");
			if (a.length==4) {
				areas.put(a[1], new Area(a[1], Integer.parseInt(a[2]), UUID.fromString(a[3]), Double.parseDouble(b[0]), Double.parseDouble(b[1]), Double.parseDouble(b[2]), Double.parseDouble(b[3]), Double.parseDouble(b[4]), Double.parseDouble(b[5])));
				owners.add(UUID.fromString(a[3]));
//				Render.getPlayerHead(a[3]);
			}
			else {
				areas.put(a[1], new Area(a[1], Integer.parseInt(a[2]), Double.parseDouble(b[0]), Double.parseDouble(b[1]), Double.parseDouble(b[2]), Double.parseDouble(b[3]), Double.parseDouble(b[4]), Double.parseDouble(b[5])));
			}
		}
		else if (code.equals(LifePacketList.OPEN_UPGRADE_UI.recogCode)) {
			String s = message.data.substring(2);
			String[] a = s.split("}}");
			Minecraft.getMinecraft().displayGuiScreen(new UpgradeUI(a[0],Integer.parseInt(a[1])));
		}
	}
}
