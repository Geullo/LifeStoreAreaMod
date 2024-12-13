package com.lifestore.areas.SelectArea.Events;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import com.life.item.item.ItemInit;
import com.lifestore.areas.Area;
import com.lifestore.areas.Packet.LifeMessage;
import com.lifestore.areas.util.Reference;
import com.lifestore.areas.util.Render;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.command.CommandGive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class SelectArea {
    private Minecraft mc = Minecraft.getMinecraft();
    public SelectArea(){}

    @SubscribeEvent
    public void onRender(PlayerInteractEvent.LeftClickEmpty event) throws IOException {
        b();
    }
    @SubscribeEvent
    public void onRender(PlayerInteractEvent.LeftClickBlock event) throws IOException {
        b();
    }
    public void b() throws IOException {
        if (Area.getSelectedArea()!=null&&Area.getSelectedArea().click()) return;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            Area sel = Area.getSelectedArea();
            if (sel == null) return;
            GL11.glPushMatrix();
            ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
            double textWidth = sc.getScaledWidth_double() / 2.13d / 2;
            double textHeight = sc.getScaledHeight_double() / 8.7d / 2.45;
            double textWidth2 = sc.getScaledWidth_double() / 2.13d/1.57;
            double textHeight2 = sc.getScaledHeight_double() / 7.7d / 2.45;
            if (sel.getFirstLevelArea().owner == null)
                Render.bindTexture(new ResourceLocation(Reference.MOD_ID, "anne.png"));
            else if (sel.getFirstLevelArea().owner != null)
                Render.bindTexture(new ResourceLocation(Reference.MOD_ID, "anne_2.png"));
            Render.setColor(0xffffffff);
            if (sel.getFirstLevelArea().owner == null)
                Render.drawTexturedRect((sc.getScaledWidth_double() / 2) - (textWidth / 2), (sc.getScaledHeight_double() / 1.45d) - (textHeight / 2), textWidth, textHeight);
            else
                Render.drawTexturedRect((sc.getScaledWidth_double() / 2) - (textWidth2 / 2), (sc.getScaledHeight_double() / 1.45d) - (textHeight2 / 2), textWidth2, textHeight2);

            double mouseWidth = textWidth / 4.95d / 1.4;
            double mouseHeight = textHeight * 2.65 / 1.4;
            Render.bindTexture(new ResourceLocation(Reference.MOD_ID, "mouse.png"));
            Render.setColor(0xffffffff);
            Render.drawTexturedRect((sc.getScaledWidth_double() / 2) - (mouseWidth / 2), (sc.getScaledHeight_double() / 1.67d) - (mouseHeight / 2), mouseWidth, mouseHeight);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().world != null) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            try {
                ConcurrentHashMap<String, Area> areas = new ConcurrentHashMap<>(LifeMessage.areas==null? new HashMap<>() : LifeMessage.areas);
                Area[] b = areas.values().toArray(new Area[0]);
                if (b.length == 0 && Area.getSelectedArea() != null) Area.setSelectedArea(null);
                if (player.getHeldItemMainhand().getItem().equals(ItemInit.landSelector)) {
                    String heldItemNm = player.getHeldItemMainhand().getDisplayName();
                    if ((heldItemNm.equals("§f땅 문서") || heldItemNm.equals("땅 문서")) && !heldItemNm.contains("§6")) {
                        if (Area.getSelectedArea() != null) Area.setSelectedArea(null);
                        for (Area area : b)
                            if (((area.getName().contains("_1") && area.owner == null) || area.getName().contains("목장")) && !area.checked(event.getPartialTicks())) {
                                if ((area.getName().contains("목장") && area.owner != null)) continue;
                                drawBoundingBox(player.getPositionVector(), player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ, area.getPos1(), area.getPos2(), 3.75F, new Color(120, 120, 120, 0x80), new Color(255, 255, 255, 100));
                            }
                    } else if (heldItemNm.contains("§f 땅 문서")) {
                        if (Area.getSelectedArea() != null) Area.setSelectedArea(null);
                        if (player.getHeldItemMainhand().getDisplayName().contains("농장")) {
                            String item = player.getHeldItemMainhand().getDisplayName().toLowerCase().replace("§f 땅 문서", "").replace(" ", "_").replace("§6", "");
                            Area area = getNowMaxUpgrade(item, new HashMap<>(LifeMessage.areas));
                            if (area != null) {
                                if (!area.checked(event.getPartialTicks()))
                                    drawBoundingBox(player.getPositionVector(), player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ, area.getPos1(), area.getPos2(), 3.75F, new Color(120, 120, 120, 0x80), new Color(255, 255, 255, 100));
                            }
                        }
                    }
                    if (Area.getSelectedArea() != null)
                        drawBoundingBox(player.getPositionVector(), player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ, Area.getSelectedArea().getPos1(), Area.getSelectedArea().getPos2(), 3.75F, new Color(0, 50, 0, 0x80), new Color(0, 150, 0, 100));
                } else if (Area.getSelectedArea() != null) Area.setSelectedArea(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static Area getNowMaxUpgrade(String name,HashMap<String,Area> areas) {
        Area area = null;
        for (int i=1;i<5;i++) {
            if (areas.containsKey(name.concat("_"+i))) {
                area = areas.get(name.concat("_"+i));
                if (area.owner==null) break;
            }
            else {
                area = areas.get(name.concat("_"+(i-1)));
                break;
            }
        }
        return area;
    }

    public static void drawBoundingBox(Vec3d player_pos, double lastx,double lasty, double lastz, Vec3d posA, Vec3d posB, float lineWidth,Color c,Color highlight_color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,GlStateManager.DestFactor.ZERO);
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        double d3 = lastx + (player_pos.x - lastx) * (double)Minecraft.getMinecraft().getRenderPartialTicks();
        double d4 = lasty + (player_pos.y - lasty) * (double)Minecraft.getMinecraft().getRenderPartialTicks();
        double d5 = lastz + (player_pos.z - lastz) * (double)Minecraft.getMinecraft().getRenderPartialTicks();
        GlStateManager.translate(-d3, -d4, -d5);
        double minX = Math.min(posA.x,posB.x);
        double minY = Math.min(posA.y,posB.y);
        double minZ = Math.min(posA.z,posB.z);
        double maxX = Math.max(posA.x+0.999999,posB.x+0.999999);
        double maxY = Math.max(posA.y+0.999999,posB.y+0.999999);
        double maxZ = Math.max(posA.z+0.999999,posB.z+0.999999);
        RenderGlobal.renderFilledBox(maxX,maxY,maxZ,minX,minY,minZ,c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
        RenderGlobal.renderFilledBox(minX,minY,minZ,maxX,maxY,maxZ,c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
        RenderGlobal.renderFilledBox(maxX,maxY,maxZ,minX,minY,minZ,c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
        RenderGlobal.renderFilledBox(minX,minY,minZ,maxX,maxY,maxZ,c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        drawOutsideLine(player_pos,lastx,lasty,lastz,posA,posB,lineWidth,highlight_color);
    }

    private static void drawOutsideLine(Vec3d player_pos, double lastx,double lasty, double lastz, Vec3d posA, Vec3d posB, float lineWidth,Color c) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,GlStateManager.DestFactor.ZERO);
        GlStateManager.disableLighting();
        GlStateManager.glLineWidth(lineWidth);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        double d3 = lastx + (player_pos.x - lastx) * (double)Minecraft.getMinecraft().getRenderPartialTicks();
        double d4 = lasty + (player_pos.y - lasty) * (double)Minecraft.getMinecraft().getRenderPartialTicks();
        double d5 = lastz + (player_pos.z - lastz) * (double)Minecraft.getMinecraft().getRenderPartialTicks();
        GlStateManager.translate(-d3, -d4, -d5);
        double minX = Math.min(posA.x,posB.x);
        double minY = Math.min(posA.y,posB.y);
        double minZ = Math.min(posA.z,posB.z);
        double maxX = Math.max(posA.x+0.999999,posB.x+0.999999);
        double maxY = Math.max(posA.y+0.999999,posB.y+0.999999);
        double maxZ = Math.max(posA.z+0.999999,posB.z+0.999999);
        double dx = Math.abs(maxX-minX);
        double dy = Math.abs(maxY-minY);
        double dz = Math.abs(maxZ-minZ);
        bufferBuilder.pos(minX, minY, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX+dx, minY, minZ+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX+dx, minY, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY+dy, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY+dy, minZ+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX+dx, minY+dy, minZ+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX+dx, minY+dy, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY+dy, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY+dy, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY+dy, minZ+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX+dx, minY, minZ+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX+dx, minY+dy, minZ+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX+dx, minY+dy, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX+dx, minY, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY+dy, minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }


}
