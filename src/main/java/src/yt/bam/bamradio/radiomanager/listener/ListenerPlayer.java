package yt.bam.bamradio.radiomanager.listener;

import yt.bam.bamradio.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class ListenerPlayer implements Listener {
    
    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent event) {
        if (BAMradio.getInst() != null && BAMradio.getRadioManager() != null) {
            BAMradio.getRadioManager().tuneOut(event.getPlayer());
        }
    }
    
    @EventHandler
    public static void onPlayerJoin(final PlayerJoinEvent event) {
        if (BAMradio.getInst() != null && BAMradio.getRadioManager() != null) {
            BAMradio.getRadioManager().tuneIn(event.getPlayer());
        }
    }
}
