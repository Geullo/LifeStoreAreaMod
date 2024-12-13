package com.lifestore.areas.util;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import net.minecraft.util.math.Vec3d;
public class Utils {
    public static boolean mouseBetweenIcon(double mouseX,double mouseY,double x, double y, double width, double height){
        return (mouseX >= x&&mouseY >= y&&mouseX <= x + width&&mouseY <= y + height);
    }
    public static boolean betweenLocation(Vec3d firstLoc, Vec3d secondLoc, Vec3d compareLoc1, Vec3d compareLoc2) {
        if (firstLoc == null || secondLoc == null || compareLoc1 == null || compareLoc2 == null) return false;
        double x1 = Math.min(firstLoc.x, secondLoc.x);
        double y1 = Math.min(firstLoc.y, secondLoc.y);
        double z1 = Math.min(firstLoc.z, secondLoc.z);
        double x2 = Math.max(firstLoc.x + 0.9999999999999999D, secondLoc.x + 0.9999999999999999D);
        double y2 = Math.max(firstLoc.y+ 0.9999999999999999D, secondLoc.y+ 0.9999999999999999D);
        double z2 = Math.max(firstLoc.z + 0.9999999999999999D, secondLoc.z + 0.9999999999999999D);

        double x3 = Math.max(compareLoc1.x /*+ 0.9999999999999999D*/, compareLoc2.x /*+ 0.9999999999999999D*/);
        double y3 = Math.max(compareLoc1.y/* + 0.9999999999999999D*/, compareLoc2.y /*+ 0.9999999999999999D*/);
        double z3 = Math.max(compareLoc1.z /*+ 0.9999999999999999D*/, compareLoc2.z /*+ 0.9999999999999999D*/);
        double x4 = Math.min(compareLoc1.x, compareLoc2.x);
        double y4 = Math.min(compareLoc1.y, compareLoc2.y);
        double z4 = Math.min(compareLoc1.z, compareLoc2.z);
        return x3 >= x1 && y3 >= y1 && z3 >= z1 && x4 <= x2 && y4 <= y2 && z4 <= z2;
    }

    public static boolean betweenLocation(Vec3d firstLoc, Vec3d secondLoc, Vec3d compareLoc) {
        if (firstLoc == null || secondLoc == null || compareLoc == null) return false;
        double x1 = Math.min(firstLoc.x, secondLoc.x);
        double y1 = Math.min(firstLoc.y, secondLoc.y);
        double z1 = Math.min(firstLoc.z, secondLoc.z);
        double x2 = Math.max(firstLoc.x + 0.9999999999999999D, secondLoc.x + 0.9999999999999999D);
        double y2 = Math.max(firstLoc.y, secondLoc.y);
        double z2 = Math.max(firstLoc.z + 0.9999999999999999D, secondLoc.z + 0.9999999999999999D);
        return compareLoc.x >= x1 && compareLoc.y>= y1 && compareLoc.z >= z1 && compareLoc.x <= x2 && compareLoc.y <= y2 && compareLoc.z <= z2;
    }
}
