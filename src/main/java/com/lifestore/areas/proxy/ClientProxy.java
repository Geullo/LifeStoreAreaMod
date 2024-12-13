package com.lifestore.areas.proxy;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import com.lifestore.areas.Packet.LifePacket;
import com.lifestore.areas.SelectArea.Events.SelectArea;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy{
    public static final SimpleNetworkWrapper AREAPACKET = NetworkRegistry.INSTANCE.newSimpleChannel("areaPacket");

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        FMLCommonHandler.instance().bus().register(new SelectArea());
        AREAPACKET.registerMessage(LifePacket.Handle.class, LifePacket.class, 0, Side.CLIENT);
    }

    @Override
    public void postInit() {
    }

}
