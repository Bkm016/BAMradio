package yt.bam.bamradio.radiomanager.listener;

import yt.bam.bamradio.*;
import org.bukkit.event.*;
import yt.bam.bamradio.radiomanager.listener.worldguard.*;

public class ListenerRegion implements Listener {
	
    @EventHandler
    public void onRegionEnter(RegionEnterEvent e) {
    	// 无区域?
    	if (e.getRegion().getId() == null || e.getRegion().getId().isEmpty()) {
    		return;
    	}
    	// 所在区域?
    	if (BAMradio.getInst().getConfig().getStringList("enable_world").contains(e.getPlayer().getWorld().getName()) 
    			&& BAMradio.getInst().getConfig().getStringList("enable_region").contains(e.getRegion().getId())) {
    		
        	BAMradio.getRadioManager().tuneOut(e.getPlayer());
            BAMradio.getRadioManager().tuneIn(e.getPlayer());
        }
    }
    
    @EventHandler
    public void onRegionLeave(RegionLeaveEvent e) {
    	// 无区域?
    	if (e.getRegion().getId() == null || e.getRegion().getId().isEmpty()) {
    		return;
    	}
    	// 所在区域?
    	if (BAMradio.getInst().getConfig().getStringList("enable_world").contains(e.getPlayer().getWorld().getName()) 
    			&& BAMradio.getInst().getConfig().getStringList("enable_region").contains(e.getRegion().getId())) {
    		
        	BAMradio.getRadioManager().tuneOut(e.getPlayer());
        }
    }
}
