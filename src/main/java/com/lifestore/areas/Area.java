package com.lifestore.areas;

/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */

import com.lifestore.areas.Packet.LifeMessage;
import com.lifestore.areas.Packet.LifePacket;
import com.lifestore.areas.Packet.LifePacketList;
import com.lifestore.areas.SelectArea.Events.SelectArea;
import com.lifestore.areas.UI.BuyUI;
import com.lifestore.areas.UI.OwnerUI;
import com.lifestore.areas.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Area {
    private String name;
    private Vec3d pos1, pos2;
    public UUID owner = null;
    public int money = 100000;
    private static Area selectedArea = null;

    public static Area getSelectedArea() {
        return selectedArea;
    }
    public Area getFirstLevelArea() {
        return name.contains("번_농장_")?LifeMessage.areas.get(name.split("번_농장_")[0]+"번_농장_1"):this;
    }

    public static void setSelectedArea(Area selectedArea) {
        Area.selectedArea = selectedArea;
    }

    private boolean isSelected = false,clicked = false;

    public Area(String name ,Integer money,double x1,double y1,double z1,double x2,double y2,double z2) {
        this.name = name;
        pos1 = new Vec3d(x1,y1,z1);
        pos2 = new Vec3d(x2,y2,z2);
        this.money = money;
    }
    public Area(String name,Integer money , UUID owner,double x1, double y1, double z1, double x2, double y2, double z2) {
        this.name = name;
        pos1 = new Vec3d(x1,y1,z1);
        pos2 = new Vec3d(x2,y2,z2);
        this.owner = owner;
        this.money = money;
    }

    public Area(String name,Integer money ,Vec3d pos1,Vec3d pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.money = money;
    }

    public boolean isPlayerOwner() {
        return owner!=null&&owner.equals(Minecraft.getMinecraft().player.getUniqueID());
    }

    public String getName() {
        return name;
    }

    public void setPos1(Vec3d pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Vec3d pos2) {
        this.pos2 = pos2;
    }

    public Vec3d getPos1() {
        return pos1;
    }

    public Vec3d getPos2() {
        return pos2;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean checked(float partialTicks) {
        isSelected = false;
        EntityPlayer player = Minecraft.getMinecraft().player;
        RayTraceResult rt = player.rayTrace(100, partialTicks);
        AxisAlignedBB aabb = getAABB();
        if (owner!=null) return isSelected;
        if (selectedArea!=null&&!LifeMessage.areas.containsKey(selectedArea.name)) {
            selectedArea = null;
        }
        if (rt==null) {
            if (selectedArea==null) selectedArea = this;
            return isSelected;
        }
        Vec3d plVec = player.getPositionVector().addVector(0d,1d,0d);
        RayTraceResult intercept=aabb.calculateIntercept(plVec, rt.hitVec);
        RayTraceResult interceptSelect;
        if (selectedArea!=null&&!selectedArea.name.equals(name)) {
            interceptSelect = selectedArea.getAABB().calculateIntercept(plVec,rt.hitVec);
            isSelected = intercept!=null&&interceptSelect!=null&&Math.abs(intercept.hitVec.distanceTo(rt.hitVec))>Math.abs(interceptSelect.hitVec.distanceTo(rt.hitVec));
            if (isSelected) {selectedArea = this;}

        }
        else {
            isSelected = ((Utils.betweenLocation(pos1, pos2, rt.hitVec)) || intercept != null);
            if (isSelected) {
                if (selectedArea == null) selectedArea = this;
            }
            else selectedArea = null;
        }
        return isSelected;
    }
    public AxisAlignedBB getAABB() {
        double minX = Math.min(pos1.x,pos2.x);
        double minY = Math.min(pos1.y,pos2.y);
        double minZ = Math.min(pos1.z,pos2.z);
        double maxX = Math.max(pos1.x+1,pos2.x+1);
        double maxY = Math.max(pos1.y+0.79999999d,pos2.y+0.79999999d);
        double maxZ = Math.max(pos1.z+1,pos2.z+1);
        return new AxisAlignedBB(minX,minY,minZ,maxX,maxY,maxZ);
    }
    public boolean click() throws IOException {
        if (isSelected) {
            System.out.println(toString());
            HashMap<String,Area> areas  = new HashMap<>(LifeMessage.areas);
            clicked = true;
            if (owner==null&&(name.contains("번_농장_1")||name.contains("번_목장"))) Minecraft.getMinecraft().displayGuiScreen(new BuyUI(BuyUI.LandType.convert(name),this));
            else if ((name.contains("번_농장_")&&!name.contains("번_농장_1"))) {
                if (!(name.substring(0,name.length()-1).concat("4").equals(name)&&owner!=null)) {
                    String[] s = name.split("번_농장_");
                    LifePacket.sendMessage(LifePacketList.OPEN_UPGRADE_UI.recogCode + name);
                }
                else if (owner!=null) Minecraft.getMinecraft().displayGuiScreen(new OwnerUI(name,owner.toString()));
            }
            else if (owner!=null) Minecraft.getMinecraft().displayGuiScreen(new OwnerUI(name,owner.toString()));
            clicked = false;
            return true;
        }
        else clicked = false;
        return false;
    }

    public boolean isClicked() {
        return clicked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public String toString() {
        return "Area{" +
                "name=" + name+
                ", clicked=" + clicked +
                ", selected="+isSelected+
                ", pos1=" + pos1 +
                ", pos2=" + pos2 +
                "}";
    }
}
