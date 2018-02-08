package yt.bam.bamradio.radiomanager;

import java.util.logging.*;
import org.bukkit.*;

import me.skymc.taboolib.sound.SoundUtils;

public class Instrument
{
    public static final Logger logger;
    
    public static Sound getInstrument(final int patch, final int channel) {
        if (channel == 10) {
            return null;
        }
        if (channel == 9) {
            return null;
        }
        if ((patch >= 0 && patch <= 7) || (patch >= 80 && patch <= 103) || (patch >= 64 && patch <= 71)) {
            return Sound.BLOCK_NOTE_HARP;
        }
        if (patch >= 8 && patch <= 15) {
            return Sound.BLOCK_NOTE_PLING;
        }
        if ((patch >= 16 && patch <= 23) || (patch >= 44 && patch <= 46)) {
            //return Sound.NOTE_BASS_GUITAR;
        	return Sound.BLOCK_NOTE_BASS;
        }
        if ((patch >= 28 && patch <= 40) || (patch >= 56 && patch <= 63)) {
            //return Sound.NOTE_BASS;
        	return Sound.BLOCK_NOTE_BASS;
        }
        if (patch >= 113 && patch <= 119) {
            return Sound.BLOCK_NOTE_BASEDRUM;
        }
        if (patch >= 120 && patch <= 127) {
            return Sound.BLOCK_NOTE_SNARE;
        }
        if (patch >= 120 && patch <= 127) {
            return Sound.BLOCK_NOTE_SNARE;
        }
        return Sound.BLOCK_NOTE_PLING;
    }
    
    static {
        logger = Bukkit.getLogger();
    }
}
