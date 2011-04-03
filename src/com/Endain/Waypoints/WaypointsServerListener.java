package com.Endain.Waypoints;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

public class WaypointsServerListener extends ServerListener {
    private final Waypoints plugin;

    public WaypointsServerListener(final Waypoints plugin) {
        this.plugin = plugin;
    }
    //Used to detect if GroupManager becomes enabled.
    public void onPluginEnable(PluginEnableEvent event) {
    	if(!(event.getPlugin() instanceof Waypoints))
    		this.plugin.getPermissionManager().enable(event.getPlugin());
    }
    //Used to detect if GroupManager becomes disabled.
    public void onPluginDisable(PluginDisableEvent event) {
    	if(!(event.getPlugin() instanceof Waypoints))
    		this.plugin.getPermissionManager().disable(event.getPlugin());
    }
}