package yt.bam.bamradio.radiomanager.listener.noteblockapi;

import com.xxmicloxx.NoteBlockAPI.*;
import yt.bam.bamradio.*;
import org.bukkit.event.*;

public class ListenerSong implements Listener {
	
    @EventHandler
    public static void onSongEnd(SongEndEvent e) {
        if (BAMradio.getRadioManager() != null) {
        	// ��������
        	BAMradio.getRadioManager().setNowPlaying(false);
        	// �Ƿ��Զ���һ��
            if (BAMradio.getInst().getConfig().getBoolean("auto-play-next")) {
            	// ��һ��
                BAMradio.getRadioManager().playNextSong();
            }
        }
    }
}
