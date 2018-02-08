package yt.bam.bamradio.radiomanager.listener.worldguard;

import org.bukkit.event.player.*;
import org.bukkit.event.*;
import com.sk89q.worldguard.protection.regions.*;
import yt.bam.bamradio.radiomanager.listener.*;
import yt.bam.bamradio.radiomanager.listener.enums.MovementType;

import org.bukkit.entity.*;

public abstract class RegionEvent extends PlayerEvent {
	
    private static HandlerList handlerList;
    private ProtectedRegion region;
    private MovementType movement;
    
    public RegionEvent(ProtectedRegion region, Player player, MovementType movement) {
        super(player);
        this.region = region;
        this.movement = movement;
    }
    
    public HandlerList getHandlers() {
        return RegionEvent.handlerList;
    }
    
    public ProtectedRegion getRegion() {
        return this.region;
    }
    
    public static HandlerList getHandlerList() {
        return RegionEvent.handlerList;
    }
    
    public MovementType getMovementWay() {
        return this.movement;
    }
    
    static {
        handlerList = new HandlerList();
    }
}
