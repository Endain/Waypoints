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
    //Used to prevent block breaking in the 'protection zone'.
    public void onBlockBreak(BlockBreakEvent event)
    {
    	if(this.plugin.getDataManager().playerIsProtected(event.getPlayer()) != null) {
    		event.setCancelled(true);
    		event.getBlock().setType(event.getBlock().getType());
    	}
    }
    //Used to prevent block damaging in the 'protection zone'.
    public void onBlockDamage(BlockDamageEvent event) {
    	if(this.plugin.getDataManager().playerIsProtected(event.getPlayer()) != null)
    		event.setCancelled(true);
    }
    //Used to prevent block placing in the 'protection zone'.
    public void onBlockPlace(BlockPlaceEvent event) {
    	if(this.plugin.getDataManager().playerIsProtected(event.getPlayer()) != null) {
    		event.setBuild(false);
    		event.setCancelled(true);
    	}
    }
    //Used to prevent block ignition in the 'protection zone'.
    public void onBlockIgnite(BlockIgniteEvent event) {
    	if(event.getPlayer() != null)
    		if(this.plugin.getDataManager().playerIsProtected(event.getPlayer()) != null)
    			event.setCancelled(true);
    }
}