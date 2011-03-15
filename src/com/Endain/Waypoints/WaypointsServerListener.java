package com.Endain.Waypoints;

import org.bukkit.event.server.PluginEvent;
import org.bukkit.event.server.ServerListener;

public class WaypointsServerListener extends ServerListener {
    private final Waypoints plugin;

    public WaypointsServerListener(final Waypoints plugin) {
        this.plugin = plugin;
    }
    
    public void onPluginEnabled(PluginEvent event) {
		if(this.plugin.getDataManager().isUsingGroupManager())
			this.plugin.getDataManager().enableGroupManager();
    }

    public void onPluginDisabled(PluginEvent event) {
    	if(this.plugin.getDataManager().isUsingGroupManager())
    		this.plugin.getDataManager().disableGroupManager();
    }
}