package com.Endain.Waypoints;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class WaypointsEntityListener extends EntityListener {
    private final Waypoints plugin;

    public WaypointsEntityListener(final Waypoints plugin) {
        this.plugin = plugin;
    }
    //Used to protect players from damage if the plugin is configured to.
    public void onEntityDamage(EntityDamageEvent event) {
    	if(this.plugin.getDataManager().playersAreProtected()) {
	    	if(event instanceof EntityDamageByEntityEvent) {
	    		EntityDamageByEntityEvent devent = ((EntityDamageByEntityEvent)event);
	    		if(devent.getDamager() instanceof Player) {
	    			if(this.plugin.getDataManager().playerIsProtected(((Player)devent.getDamager())) != null)
	    				event.setCancelled(true);
	    		}
	    		else if(devent.getEntity() instanceof Player) {
	    			if(this.plugin.getDataManager().playerIsProtected(((Player)devent.getEntity())) != null)
	    				event.setCancelled(true);
	    		}
	    	}
    	}
    }
    //Used to protect the blocks and players in a protected zone from creeper (or other) explosions.
    public void onEntityExplode(EntityExplodeEvent event) {
    	Location loc = event.getLocation();
    	if(this.plugin.getDataManager().inProtectedRegion(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
    		event.blockList().clear();
    		event.setCancelled(true);
    	}
    }
}