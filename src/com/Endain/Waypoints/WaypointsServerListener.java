package com.Endain.Waypoints;

import org.bukkit.event.server.PluginEvent;
import org.bukkit.event.server.ServerListener;

public class WaypointsServerListener extends ServerListener {
    private final Waypoints plugin;

    public WaypointsServerListener(final Waypoints plugin) {
        this.plugin = plugin;
    }
    //Used to detect if GroupManager becomes enabled.
    public void onPluginEnabled(PluginEvent event) {
		if(this.plugin.getDataManager().isUsingGroupManager())
			this.plugin.getDataManager().enableGroupManager();
    }
    //Used to detect if GroupManager becomes disabled.
    public void onPluginDisabled(PluginEvent event) {
    	if(this.plugin.getDataManager().isUsingGroupManager())
    		this.plugin.getDataManager().disableGroupManager();
    }
}