package yt.bam.bamradio.radiomanager;

import org.bukkit.event.*;
import org.bukkit.entity.*;
import yt.bam.bamradio.*;
import java.io.*;
import org.bukkit.command.*;
import com.xxmicloxx.NoteBlockAPI.*;

import lombok.Getter;
import me.skymc.taboolib.message.MsgUtils;

import java.util.*;

public class NoteBlockPlayer implements MidiPlayer, Listener {
	
    private RadioManager manager;
    private SongPlayer songPlayer;
    
    public NoteBlockPlayer(RadioManager manager) {
    	this.manager = manager;
    }
    
    public void tuneIn(Player player) {
        if (songPlayer != null) {
        	songPlayer.addPlayer(player);
        }
    }
    
    public void tuneOut(Player player) {
        if (songPlayer != null) {
        	songPlayer.removePlayer(player);
        }
    }
    
    public void stopPlaying() {
        if (songPlayer != null) {
        	songPlayer.setPlaying(false);
        }
        manager.setNowPlaying(false);
    }
    
    public boolean playSong(String fileName) {
        manager.setNowPlayingFile(fileName);
        manager.setNowPlaying(true);
        try {
            Song s = NBSDecoder.parse(new File(BAMradio.getInst().getDataFolder(), fileName + ".nbs"));
            (songPlayer = (SongPlayer) new RadioSongPlayer(s)).setAutoDestroy(false);
            for (Player player : manager.getTunedIn()) {
                songPlayer.addPlayer(player);
                manager.nowPlaying(player, false);
            }
            songPlayer.setPlaying(true);
            return true;
        }
        catch (Exception e) {
            MsgUtils.send(BAMradio.getLanguage().get("RADIO_MANAGER_CORRUPT_NBS") + " " + fileName + " (" + e.getMessage() + ")", BAMradio.getInst());
            return false;
        }
    }
}
