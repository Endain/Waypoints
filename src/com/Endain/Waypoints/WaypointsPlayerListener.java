package com.Endain.Waypoints;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class WaypointsPlayerListener extends PlayerListener {
    private final Waypoints plugin;

    public WaypointsPlayerListener(Waypoints instance) {
    	this.plugin = instance;
    }
    //Notify the plugin that a player joined.
    public void onPlayerJoin(PlayerEvent event) {
    	this.plugin.getDataManager().onLogin(event.getPlayer().getEntityId(), event.getPlayer());
    }
    //Notify the plugin that a player left teh server.
    public void onPlayerQuit(PlayerEvent event) {
    	this.plugin.getDataManager().onLogout(event.getPlayer().getEntityId());
    }
    //Check if we are are entering or exiting a zone every time the player moves.
    public void onPlayerMove(PlayerMoveEvent event) {
    	if(!this.plugin.getDataManager().hasZeroPoints()) {
    		//Checks related to 'save zones'
	    	Region r = this.plugin.getDataManager().playerIsSaved(event.getPlayer());
	    	if(r != null)
	    		this.plugin.getDataManager().checkIsUnsaved(event.getPlayer(), r, event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());
	    	else
	    		this.plugin.getDataManager().checkIsSaved(event.getPlayer(), event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());	
	    	//Checks related to 'protection zones'
	    	r = this.plugin.getDataManager().playerIsProtected(event.getPlayer());
	    	if(r != null)
	    		this.plugin.getDataManager().checkIsUnprotected(event.getPlayer(), r, event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());
	    	else
	    		this.plugin.getDataManager().checkIsProtected(event.getPlayer(), event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());
    	}
    }
    //When the player needs to respawn, send them to their bound Waypoint.
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	Point respawn = this.plugin.getDataManager().getPointByPlayerId(event.getPlayer().getEntityId());
    	
    	if(respawn != null)
    		event.setRespawnLocation(new Location(this.plugin.getServer().getWorld(respawn.getWorld()), respawn.getX() + 1.5, respawn.getY() + 1.5, respawn.getZ() + .5));
    }
}