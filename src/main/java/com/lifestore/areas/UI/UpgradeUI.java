package com.lifestore.areas.UI;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import com.lifestore.areas.Packet.LifePacket;
import com.lifestore.areas.Packet.LifePacketList;
import com.lifestore.areas.util.Reference;
import com.lifestore.areas.util.Render;
import com.lifestore.areas.util.Sound;
import com.lifestore.areas.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class UpgradeUI extends GuiScreen {
    private double[] bgSize = new double[2],bgPos = new double[2],btnPos = new double[2],btnSize = new double[2];
    private boolean clicked = false,grabbed = false;
    private String name;
    private int price;

    public UpgradeUI(String name,int price) {
        this.name = name;
        this.price = price;
    }


    @Override
    public void initGui() {
        bgSize[0] = width/2.218d;
        bgSize[1] = height/1.53d;
        bgPos[0] = (width/2d) - (bgSize[0]/2d);
        bgPos[1] = (height/2d) - (bgSize[1]/2d);

        btnSize[0] = bgSize[0]/2.7d;
        btnSize[1] = bgSize[1]/5.75d/1.382d;
        btnPos[0] = bgPos[0]+((bgSize[0]/2d) - (btnSize[0]/2d));
        btnPos[1] = bgPos[1]+((bgSize[1]/1.22d) - (btnSize[1]/2d));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!grabbed) {
            if (Minecraft.getMinecraft().mouseHelper!=null) Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
            grabbed = true;
        }
        GlStateManager.pushMatrix();
        Render.bindTexture(new ResourceLocation(Reference.MOD_ID,"black-bg.png"));
        Render.setColor(0xffffffff);
        Render.drawTexturedRect(0,0,width,height);
        Render.bindTexture(new ResourceLocation(Reference.MOD_ID,"upgrade_"+(name.contains("번_농장_")?name.split("번_농장_")[1]:1)+".png"));
        Render.setColor(0xffffffff);
        Render.drawTexturedRect(bgPos[0], bgPos[1], bgSize[0], bgSize[1]);
        if (clicked) Render.bindTexture(new ResourceLocation(Reference.MOD_ID,"upgrade_btn_click.png"));
        else if (Utils.mouseBetweenIcon(mouseX,mouseY,btnPos[0], btnPos[1],btnSize[0],btnSize[1])) Render.bindTexture(new ResourceLocation(Reference.MOD_ID,"upgrade_btn_hover.png"));
        else Render.bindTexture(new ResourceLocation(Reference.MOD_ID,"upgrade_btn.png"));
        Render.setColor(0xffffffff);
        Render.drawTexturedRect(btnPos[0], btnPos[1], btnSize[0], btnSize[1]);
        Render.drawString((name.contains("번_농장_")?name.split("번_농장_")[0]:"1"), (float) (bgPos[0]+(bgSize[0]/1.58613)), (float) (bgPos[1]+bgSize[1]/3.3011), (int) (bgSize[0]/9.85),(int) (bgSize[0]/8.85),2,0xffffff);
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.KOREA);
        Render.drawString(nf.format(price).replaceFirst(""+nf.format(price).charAt(0),""), (float) (bgPos[0]+(bgSize[0]/1.38634)), (float) (bgPos[1]+bgSize[1]/2.1173),(int) (bgSize[0]/9.85/1.35),(int) (bgSize[0]/8.85/1.35),2,0xffffff);
        GlStateManager.popMatrix();
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (Utils.mouseBetweenIcon(mouseX,mouseY,btnPos[0], btnPos[1],btnSize[0],btnSize[1])) {
            LifePacket.sendMessage(LifePacketList.UPGRADE_AREA.recogCode+name);
            Minecraft.getMinecraft().getSoundHandler().playSound(Sound.getSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS,1f,1f));
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }
}
