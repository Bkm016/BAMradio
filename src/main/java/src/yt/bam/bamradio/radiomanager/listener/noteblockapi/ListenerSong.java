package yt.bam.bamradio.radiomanager.listener.noteblockapi;

import com.xxmicloxx.NoteBlockAPI.*;
import yt.bam.bamradio.*;
import org.bukkit.event.*;

public class ListenerSong implements Listener {
	
    @EventHandler
    public static void onSongEnd(SongEndEvent e) {
        if (BAMradio.getRadioManager() != null) {
        	// 结束播放
        	BAMradio.getRadioManager().setNowPlaying(false);
        	// 是否自动下一首
            if (BAMradio.getInst().getConfig().getBoolean("auto-play-next")) {
            	// 下一首
                BAMradio.getRadioManager().playNextSong();
            }
        }
    }
}
