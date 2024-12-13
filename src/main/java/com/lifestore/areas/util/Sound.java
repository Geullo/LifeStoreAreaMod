package com.lifestore.areas.util;
/**
 * 본 모드는 양띵티비 인생상회 프로젝트에서만 사용되며,
 * 프로젝트내 패치기를 제작자의 허락없이 뜯어보는 행위는 저작권법 위반에 해당되며,
 * 법적인 고소 조치로 이어집니다.
 * */
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class Sound {
    public static ISound getSound(SoundEvent soundEvent,SoundCategory soundCategory){
        return getSound(soundEvent,soundCategory,1.0f);
    }
    public static ISound getSound(SoundEvent soundEvent,SoundCategory soundCategory,float volume) {
        return getSound(soundEvent,soundCategory,volume,1.0f);
    }
    public static ISound getSound(SoundEvent soundEvent,SoundCategory soundCategory,float volume,float pitch){
        return new PositionedSoundRecord(soundEvent.getSoundName(), soundCategory, volume, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static ISound getSound(SoundEvent soundEvent,SoundCategory soundCategory,float volume,float pitch,float x,float y,float z){
        return new PositionedSoundRecord(soundEvent.getSoundName(), soundCategory, volume, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }
}
