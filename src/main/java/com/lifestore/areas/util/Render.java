package com.lifestore.areas.util;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import com.lifestore.areas.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class Render {
    public static double zDepth = 0.0D;
    private static BufferedImage playerHead;
    private static HashMap<String,BufferedImage> skins = new HashMap<>();
    public static BufferedImage setPlayerSkinHead(EntityPlayer player){
        try {
            if (playerHead == null) playerHead = getPlayerHead(player.getName());
        }catch (IOException e){
            e.printStackTrace();
        }
        return playerHead;
    }
    public static BufferedImage getPlayerSkinHead(){
        try {
            if (playerHead == null) playerHead = getPlayerHead(Minecraft.getMinecraft().player.getName());
        }catch (IOException e){
            e.printStackTrace();
        }
        return playerHead;
    }

    public static BufferedImage getPlayerHead(String player) throws IOException {
        if (!skins.containsKey(player)) skins.put(player,ImageIO.read(new URL("https://minotar.net/helm/" + player + "/100.png")));
        return skins.get(player);
    }
    public static void bindLiveTexture(BufferedImage image){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,new DynamicTexture(image).getGlTextureId());
    }

    public static void setColor(int color) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(((color >> 16) & 0xff) / 255.0f, ((color >> 8) & 0xff) / 255.0f, ((color) & 0xff) / 255.0f, ((color >> 24) & 0xff) / 255.0f);
        GlStateManager.disableBlend();
    }

    public static void drawTexturedRect(double x, double y, double w, double h) {
        drawTexturedRect(x, y, w, h, 0.0D, 0.0D, 1.0D, 1.0D);
    }

    public static void drawTexturedRect(double x, double y, double w, double h, double u1, double v1, double u2, double v2) {
        try {
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS , DefaultVertexFormats.POSITION_TEX);
            buffer.pos(x + w, y, zDepth).tex(u2, v1).endVertex();
            buffer.pos(x, y, zDepth).tex(u1, v1).endVertex();
            buffer.pos(x, y + h, zDepth).tex(u1, v2).endVertex();
            buffer.pos(x + w, y + h, zDepth).tex(u2, v2).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
        } catch(NullPointerException e) {
            Main.logger.info("Render.drawTexturedRect : Null Pointer Exception");
        }
    }
    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0f;
        float f1 = (float)(startColor >> 16 & 255) / 255.0f;
        float f2 = (float)(startColor >> 8 & 255) / 255.0f;
        float f3 = (float)(startColor & 255) / 255.0f;
        float f4 = (float)(endColor >> 24 & 255) / 255.0f;
        float f5 = (float)(endColor >> 16 & 255) / 255.0f;
        float f6 = (float)(endColor >> 8 & 255) / 255.0f;
        float f7 = (float)(endColor & 255) / 255.0f;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, zDepth).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)top, zDepth).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, zDepth).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, zDepth).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawString(String s, float x, float y) {
        drawString(s, x, y, 0);
    }


    public static void drawString(String s, float x, float y,int width,int height, int align,int  color) { // 0xffffff
        GlStateManager.pushMatrix();
        float scaleX = width/16.0f,scaleY = height/16.0f;
        GL11.glScalef(scaleX,scaleY,1.0f);
        drawString(s,x/scaleX, y/scaleY,align,color);
        GlStateManager.popMatrix();
    }

    public static void drawString(String s, float x, float y, int align) { // 0xffffff
        drawString(s,x,y,align,0xffffff);
    }
    public static int drawString(String s, float x, float y, int align,int  color) {
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        if(align == 0) { // LEFT
            fr.drawString(s, x, y, color,false);
        } else if(align == 1) { // CENTER
            fr.drawString(s, x - fr.getStringWidth(s) / 2f, y, color,false);
        } else { // RIGHT
            fr.drawString(s, x - fr.getStringWidth(s), y, color,false);
        }
        return fr.getStringWidth(s);
    }

    public static void drawTooltip(List<String> info, int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fr = mc.fontRenderer;

        // getMaxStringWidth
        int width = 0;
        for(String s : info) {
            int w = fr.getStringWidth(s);
            if(width < w)
                width = w;
        }

        // getHeight
        int height = 8;
        if(info.size() > 1) {
            height += 2 + (info.size() - 1) * (fr.FONT_HEIGHT + 1);
        }

        int posX = x + 12;
        int posY = y - 12;

        if(posX + width > mc.displayWidth)
            posX -= 28 + width;
        if(posY + height + 6 > mc.displayHeight)
            posY = mc.displayHeight - height - 6;

        drawGradientRect(posX - 3, posY - 4, posX + width + 3, posY - 3, -267386864, -267386864);
        drawGradientRect(posX - 3, posY + height + 3, posX + width + 3, posY + height + 4, -267386864, -267386864);
        drawGradientRect(posX - 4, posY - 3, posX + width + 4, posY + height + 3, -267386864, -267386864);

        drawGradientRect(posX - 3, posY - 3 + 1, posX - 3 + 1, posY + height + 3 - 1, 1347420415, 1344798847);
        drawGradientRect(posX + width + 2, posY - 3 + 1, posX + width + 3, posY + height + 3 - 1, 1347420415, 1344798847);
        drawGradientRect(posX - 3, posY - 3, posX + width + 3, posY - 3 + 1, 1347420415, 1347420415);
        drawGradientRect(posX - 3, posY + height + 2, posX + width + 3, posY + height + 3, 1344798847, 1344798847);

        int size = info.size();
        for(int i = 0; i < size; i ++) {
            fr.drawStringWithShadow(info.get(i), (float) posX, (float) posY, 0xffffff);
            if(i == 0)
                posY += 2;
            posY += 10;
        }
    }
    public static void bindTexture(ResourceLocation resource) {
        ITextureObject textureObj = Minecraft.getMinecraft().getTextureManager().getTexture(resource);
        if(textureObj == null) {
            textureObj = new BlurTexture(resource);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resource, textureObj);
        }
        GlStateManager.bindTexture(textureObj.getGlTextureId());
    }

    public static void deleteTexture(ResourceLocation resource) {
        Minecraft.getMinecraft().getTextureManager().deleteTexture(resource);
    }

    @SideOnly(Side.CLIENT)
    public static class BlurTexture extends AbstractTexture {
        protected final ResourceLocation textureLocation;

        public BlurTexture(ResourceLocation textureResourceLocation) {
            textureLocation = textureResourceLocation;
        }

        public void loadTexture(IResourceManager resourceManager) throws IOException {
            this.deleteGlTexture();
            IResource iresource = null;

            try {
                iresource = resourceManager.getResource(textureLocation);
                BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());

                TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, true, true);
            } finally {
                IOUtils.closeQuietly((Closeable)iresource);
            }
        }
    }
}


