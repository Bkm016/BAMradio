package yt.bam.bamradio.radiomanager.listener;

import org.bukkit.entity.*;
import com.sk89q.worldguard.protection.regions.*;
import yt.bam.bamradio.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import java.util.*;

import yt.bam.bamradio.radiomanager.listener.enums.MovementType;
import yt.bam.bamradio.radiomanager.listener.worldguard.*;
import com.sk89q.worldguard.protection.managers.*;
import com.sk89q.worldguard.protection.*;
import java.lang.reflect.*;

public class ListenerWorldGuard implements Listener {
	
    private Map<Player, Set<ProtectedRegion>> playerRegions = new HashMap<Player, Set<ProtectedRegion>>();
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Set<ProtectedRegion> regions = playerRegions.remove(e.getPlayer());
        if (regions != null) {
            for (ProtectedRegion region : regions) {
                Bukkit.getPluginManager().callEvent(new RegionLeaveEvent(region, e.getPlayer(), MovementType.DISCONNECT));
                Bukkit.getPluginManager().callEvent(new RegionLeftEvent(region, e.getPlayer(), MovementType.DISCONNECT));
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Set<ProtectedRegion> regions = playerRegions.remove(e.getPlayer());
        if (regions != null) {
            for (ProtectedRegion region : regions) {
                Bukkit.getPluginManager().callEvent(new RegionLeaveEvent(region, e.getPlayer(), MovementType.DISCONNECT));
                Bukkit.getPluginManager().callEvent(new RegionLeftEvent(region, e.getPlayer(), MovementType.DISCONNECT));
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
    	if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
    		e.setCancelled(updateRegions(e.getPlayer(), MovementType.MOVE, e.getTo()));
    	}
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        e.setCancelled(updateRegions(e.getPlayer(), MovementType.TELEPORT, e.getTo()));
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        updateRegions(e.getPlayer(), MovementType.SPAWN, e.getPlayer().getLocation());
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        updateRegions(e.getPlayer(), MovementType.SPAWN, e.getRespawnLocation());
    }
    
    @SuppressWarnings("unchecked")
	private synchronized boolean updateRegions(Player player, MovementType movement, Location to) {
        Set<ProtectedRegion> regions;
        if (playerRegions.get(player) == null) {
            regions = new HashSet<ProtectedRegion>();
        }
        else {
            regions = new HashSet<ProtectedRegion>(this.playerRegions.get(player));
        }
        Set<ProtectedRegion> oldRegions = new HashSet<ProtectedRegion>(regions);
        RegionManager rm = BAMradio.getWorldGuardPlugin().getRegionManager(to.getWorld());
        if (rm == null) {
            return false;
        }
        ApplicableRegionSet appRegions = rm.getApplicableRegions(to);
        for (ProtectedRegion region : appRegions) {
            if (!regions.contains(region)) {
                RegionEnterEvent e = new RegionEnterEvent(region, player, movement);
                Bukkit.getPluginManager().callEvent(e);
                if (e.isCancelled()) {
                    regions.clear();
                    regions.addAll(oldRegions);
                    return true;
                }
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(50L);
                        }
                        catch (InterruptedException ex) {}
                        RegionEnteredEvent e = new RegionEnteredEvent(region, player, movement);
                        Bukkit.getPluginManager().callEvent(e);
                    }
                }.start();
                regions.add(region);
            }
        }
        Collection<ProtectedRegion> app = (Collection<ProtectedRegion>) getPrivateValue(appRegions, "applicable");
        Iterator<ProtectedRegion> itr = regions.iterator();
        while (itr.hasNext()) {
            ProtectedRegion region2 = itr.next();
            if (!app.contains(region2)) {
                if (rm.getRegion(region2.getId()) != region2) {
                    itr.remove();
                }
                else {
                    RegionLeaveEvent e2 = new RegionLeaveEvent(region2, player, movement);
                    Bukkit.getPluginManager().callEvent(e2);
                    if (e2.isCancelled()) {
                        regions.clear();
                        regions.addAll(oldRegions);
                        return true;
                    }
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(50L);
                            }
                            catch (InterruptedException ex) {
                            	//
                            }
                            Bukkit.getPluginManager().callEvent(new RegionLeftEvent(region2, player, movement));
                        }
                    }.start();
                    itr.remove();
                }
            }
        }
        playerRegions.put(player, regions);
        return false;
    }
    
    private Object getPrivateValue(Object obj, String name) {
        try {
            Field f = obj.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return f.get(obj);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
