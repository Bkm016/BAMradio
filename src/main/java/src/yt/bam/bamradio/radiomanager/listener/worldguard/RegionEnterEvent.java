package yt.bam.bamradio.radiomanager.listener.worldguard;

import org.bukkit.event.*;
import com.sk89q.worldguard.protection.regions.*;
import org.bukkit.entity.*;
import yt.bam.bamradio.radiomanager.listener.*;
import yt.bam.bamradio.radiomanager.listener.enums.MovementType;

public class RegionEnterEvent extends RegionEvent implements Cancellable {
	
    private boolean cancelled;
    private boolean cancellable;
    
    public RegionEnterEvent(ProtectedRegion region, Player player, MovementType movement) {
        super(region, player, movement);
        this.cancelled = false;
        this.cancellable = true;
        if (movement == MovementType.SPAWN || movement == MovementType.DISCONNECT) {
            this.cancellable = false;
        }
    }
    
    public void setCancelled(boolean cancelled) {
        if (!cancellable) {
            return;
        }
        this.cancelled = cancelled;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public boolean isCancellable() {
        return cancellable;
    }
    
    protected void setCancellable(boolean cancellable) {
        if (!(this.cancellable = cancellable)) {
            this.cancelled = false;
        }
    }
}
