package com.Endain.Waypoints;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class WaypointsEntityListener extends EntityListener {
    private final Waypoints plugin;

    public WaypointsEntityListener(final Waypoints plugin) {
        this.plugin = plugin;
    }
    
    public void onEntityDamage(EntityDamageEvent event) {
    	if(plugin.getDataManager().playersAreProtected()) {
	    	if(event instanceof EntityDamageByEntityEvent) {
	    		EntityDamageByEntityEvent devent = ((EntityDamageByEntityEvent)event);
	    		if(devent.getDamager() instanceof Player) {
	    			if(plugin.getDataManager().playerIsProtected(((Player)devent.getDamager())) != null)
	    				event.setCancelled(true);
	    		}
	    		else if(devent.getEntity() instanceof Player) {
	    			if(plugin.getDataManager().playerIsProtected(((Player)devent.getEntity())) != null)
	    				event.setCancelled(true);
	    		}
	    	}
    	}
    }
}