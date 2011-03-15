package com.Endain.Waypoints;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class WaypointsPlayerListener extends PlayerListener {
    private final Waypoints plugin;

    public WaypointsPlayerListener(Waypoints instance) {
        plugin = instance;
    }

    public void onPlayerJoin(PlayerEvent event) {
    	plugin.getDataManager().onLogin(event.getPlayer().getEntityId(), event.getPlayer());
    }
    
    public void onPlayerQuit(PlayerEvent event) {
    	plugin.getDataManager().onLogout(event.getPlayer().getEntityId());
    }
    
    public void onPlayerMove(PlayerMoveEvent event) {
    	if(!plugin.getDataManager().hasZeroPoints()) {
	    	Region r = plugin.getDataManager().playerIsSaved(event.getPlayer());
	    	if(r != null)
	    		plugin.getDataManager().checkIsUnsaved(event.getPlayer(), r, event.getTo().getBlockX(), event.getTo().getBlockZ());
	    	else
	    		plugin.getDataManager().checkIsSaved(event.getPlayer(), event.getTo().getBlockX(), event.getTo().getBlockZ());	
	    	
	    	r = plugin.getDataManager().playerIsProtected(event.getPlayer());
	    	if(r != null)
	    		plugin.getDataManager().checkIsUnprotected(event.getPlayer(), r, event.getTo().getBlockX(), event.getTo().getBlockZ());
	    	else
	    		plugin.getDataManager().checkIsProtected(event.getPlayer(), event.getTo().getBlockX(), event.getTo().getBlockZ());
    	}
    }
    
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	Point respawn = plugin.getDataManager().getPointByPlayerId(event.getPlayer().getEntityId());
    	
    	if(respawn != null)
    		event.setRespawnLocation(new Location(plugin.getServer().getWorld(respawn.getWorld()), respawn.getX() + 1.5, respawn.getY() + 1.5, respawn.getZ() + .5));
    }
}