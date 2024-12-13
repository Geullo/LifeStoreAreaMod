package com.lifestore.areas.UI;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import com.lifestore.areas.util.Reference;
import com.lifestore.areas.util.Render;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class OwnerUI extends GuiScreen {
    private double[] bgSize = new double[2],bgPos = new double[2],btnPos = new double[2],btnSize = new double[2];
    private boolean clicked = false;

    private String areaName;
    private BufferedImage ownerSkin;

    public OwnerUI(String areaName, String owner) throws IOException {
        this.areaName = areaName;
        ownerSkin = Render.getPlayerHead(owner);
    }


    @Override
    public void initGui() {
        bgSize[0] = width/1.995d;
        bgSize[1] = height/1.63d;
        bgPos[0] = (width/2d) - (bgSize[0]/2d);
        bgPos[1] = (height/2d) - (bgSize[1]/2d);

        btnSize[0] = bgSize[0]/2.715d;
        btnSize[1] = bgSize[1]/1.935d;
        btnPos[0] = bgPos[0]+((bgSize[0]/2d) - (btnSize[0]/2d));
        btnPos[1] = bgPos[1]+((bgSize[1]/1.74d) - (btnSize[1]/2d));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        Render.bindTexture(new ResourceLocation(Reference.MOD_ID,"black-bg.png"));
        Render.setColor(0xffffffff);
        Render.drawTexturedRect(0,0,width,height);
        Render.bindTexture(new ResourceLocation(Reference.MOD_ID,BuyUI.LandType.convert(areaName).name().toLowerCase()+"_owner.png"));
        Render.setColor(0xffffffff);
        Render.drawTexturedRect(bgPos[0], bgPos[1], bgSize[0], bgSize[1]);
        Render.bindLiveTexture(ownerSkin);
        Render.setColor(0xffffffff);
        Render.drawTexturedRect(btnPos[0], btnPos[1], btnSize[0], btnSize[1]);
        Render.setColor(0x80C6C6C6);
        Render.drawTexturedRect(btnPos[0], btnPos[1], btnSize[0], btnSize[1]);
        GlStateManager.popMatrix();
    }

}
