package com.Endain.Waypoints;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.Endain.Waypoints.Support.Point;
import com.Endain.Waypoints.Support.Region;

public class WaypointsPlayerListener extends PlayerListener {
    private final Waypoints plugin;
    private boolean reducer;

    public WaypointsPlayerListener(Waypoints instance) {
    	this.plugin = instance;
    	this.reducer = false;
    }
    //Notify the plugin that a player joined.
    public void onPlayerJoin(PlayerJoinEvent event) {
    	this.plugin.getWaypointManager().onLogin(event.getPlayer().getEntityId(), event.getPlayer());
    }
    //Notify the plugin that a player left teh server.
    public void onPlayerQuit(PlayerEvent event) {
    	this.plugin.getWaypointManager().onLogout(event.getPlayer().getEntityId());
    }
    //Check if we are are entering or exiting a zone every time the player moves.
    public void onPlayerMove(PlayerMoveEvent event) {
    	if(this.reducer) {
    		this.reducer = false;
			//Checks related to 'save zones'
	    	Region r = this.plugin.getWaypointManager().playerIsSaved(event.getPlayer());
	    	if(r != null)
	    		this.plugin.getWaypointManager().checkIsUnsaved(event.getPlayer(), r, event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());
	    	else
	    		this.plugin.getWaypointManager().checkIsSaved(event.getPlayer(), event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());	
	    	//Checks related to 'protection zones'
	    	r = this.plugin.getWaypointManager().playerIsProtected(event.getPlayer());
	    	if(r != null)
	    		this.plugin.getWaypointManager().checkIsUnprotected(event.getPlayer(), r, event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());
	    	else
	    		this.plugin.getWaypointManager().checkIsProtected(event.getPlayer(), event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());
	    	}
    	else
    		this.reducer = true;
    }
    //When the player needs to respawn, send them to their bound Waypoint.
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	Point respawn = null;
    	if(this.plugin.getConfigManager().getProperty("seperate-worlds").equalsIgnoreCase("true"))
    		respawn = this.plugin.getDataManager().getRealm(event.getPlayer().getWorld().getName()).getPlayerpoints().get(event.getPlayer().getEntityId()).getPoint(event.getPlayer().getWorld().getName());
    	else {
    		respawn = this.plugin.getDataManager().getRealm(event.getPlayer().getWorld().getName()).getPlayerpoints().get(event.getPlayer().getEntityId()).getRecentPoint();
    		if(this.plugin.getServer().getWorld(respawn.getWorld()) == null)
    			respawn = null;
    	}
    	
    	if(respawn != null) {
    		World w = this.plugin.getServer().getWorld(respawn.getWorld());
    		event.setRespawnLocation(new Location(w, respawn.getX() + 1.5, respawn.getY() + 1.5, respawn.getZ() + .5));
    		//w.loadChunk(respawn.getX(), respawn.getZ());
    	}
    	
    }
    
    /*
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	event.
    }
    */
}