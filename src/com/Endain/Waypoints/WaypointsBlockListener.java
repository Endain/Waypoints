package com.Endain.Waypoints;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class WaypointsBlockListener extends BlockListener {
    private final Waypoints plugin;

    public WaypointsBlockListener(final Waypoints plugin) {
        this.plugin = plugin;
    }
    
    public void onBlockBreak(BlockBreakEvent event)
    {
    	if(plugin.getDataManager().playerIsProtected(event.getPlayer()) != null) {
    		event.setCancelled(true);
    		event.getBlock().setType(event.getBlock().getType());
    	}
    }
    
    public void onBlockDamage(BlockDamageEvent event) {
    	if(plugin.getDataManager().playerIsProtected(event.getPlayer()) != null)
    		event.setCancelled(true);
    }
    
    public void onBlockPlace(BlockPlaceEvent event) {
    	if(plugin.getDataManager().playerIsProtected(event.getPlayer()) != null) {
    		event.setBuild(false);
    		event.setCancelled(true);
    	}
    }
    
    public void onBlockIgnite(BlockIgniteEvent event) {
    	if(event.getPlayer() != null)
    		if(plugin.getDataManager().playerIsProtected(event.getPlayer()) != null)
    			event.setCancelled(true);
    }
}