package yt.bam.bamradio.radiomanager.listener.worldguard;

import com.sk89q.worldguard.protection.regions.*;
import org.bukkit.entity.*;
import yt.bam.bamradio.radiomanager.listener.*;
import yt.bam.bamradio.radiomanager.listener.enums.MovementType;

public class RegionLeftEvent extends RegionEvent {
	
    public RegionLeftEvent(ProtectedRegion region, Player player, MovementType movement) {
        super(region, player, movement);
    }
}
