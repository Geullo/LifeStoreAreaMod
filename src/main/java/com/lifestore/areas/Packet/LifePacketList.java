package com.lifestore.areas.Packet;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
public enum LifePacketList {
    RESET_AREAS("00"),
    ADD_AREAS("01"),
    GET_AREAS("02"),
    BUY_AREA("03"),
    OPEN_UPGRADE_UI("04"),
    OPEN_OWNER("05"),
    UPGRADE_AREA("06"),
    ;
    public String recogCode;
    LifePacketList(String recogCode){
        this.recogCode = recogCode;
    }

    public static LifePacketList convert(String recogCode) {
        LifePacketList[] packets = values();
        for (int i=0;i<packets.length;i+=2) {
            if (packets[i].recogCode.equals(recogCode)) return packets[i];
            if (i+1< packets.length&&packets[i+1].recogCode.equals(recogCode)) return packets[i+1];
        }
        return null;
    }
}
