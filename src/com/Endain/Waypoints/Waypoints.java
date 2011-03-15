package com.Endain.Waypoints;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
* This plugin will allow players to spawn at 'Waypoint' markers
* that have either been randomly generated within a world or have
* been placed by an admin.
* 
* Now on Github.
* 
* Alpha version - v0.2.2
* @author Endain
*/
public class Waypoints extends JavaPlugin {
    private final WaypointsPlayerListener playerListener = new WaypointsPlayerListener(this);
    private final WaypointsBlockListener blockListener = new WaypointsBlockListener(this);
    private final WaypointsEntityListener entityListener = new WaypointsEntityListener(this);
    private final WaypointsServerListener serverListener = new WaypointsServerListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    //Waypoints Variables
    private DataManager _dataManager;
    PluginDescriptionFile pdfFile;

    public void onDisable() {
    	//getDataManager().cleanPlayerPoints();
    	
    	if(getDataManager() != null)
    		getDataManager().trySave(false);
        sendConsoleMsg("DISABLED!");
    }

    public void onEnable() {
    	pdfFile = this.getDescription();
    	sendConsoleMsg("Attempting to start...");
    	
    	//Init data manager
    	_dataManager = new DataManager(this);
    	
    	//Load config and needed data
    	if(_dataManager.load()) {
	        PluginManager pm = getServer().getPluginManager();
	        //Register player events
	        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Priority.Normal, this);
	        //Register entity events
	        pm.registerEvent(Event.Type.ENTITY_DAMAGED, entityListener, Priority.Normal, this);
	        //Register block events
	        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.BLOCK_PLACED, blockListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
	        //Register server events
	        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverListener, Priority.Normal, this);
	        
	        //Everything successful, plugin enabled
	        sendConsoleMsg("ENABLED!");
    	}
    	else
    		this.getPluginLoader().disablePlugin(this);
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        }
        else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
    
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String commandName = command.getName().toLowerCase();

        if(commandName.equalsIgnoreCase("wpbind")) {
        	if(sender instanceof Player)
        		getDataManager().tryBind((Player)sender);
        	sender.sendMessage("You arent a human player!");
        	return true;
        }
        else if(commandName.equalsIgnoreCase("wpadd")) {
        	if(sender instanceof Player) {
        		if(getDataManager().isUsingGroupManager() && getDataManager().gmWorking()) {
        			if(getDataManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.addpoint"))
        				getDataManager().tryAdd((Player)sender);
        		}
        		else if(((Player)sender).isOp())
        			getDataManager().tryAdd((Player)sender);
        		else
        			sender.sendMessage(getDataManager().wpMessage("You do not have permission to do that!"));
        	}
        	else
        		sender.sendMessage("You arent a human player!");
        	return true;
        }
        else if(commandName.equalsIgnoreCase("wpdel")) {
        	if(sender instanceof Player) {
        		if(getDataManager().isUsingGroupManager() && getDataManager().gmWorking()) {
        			if(getDataManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.delpoint"))
        				getDataManager().tryDel((Player)sender);
        		}
        		else if(((Player)sender).isOp())
        			getDataManager().tryDel((Player)sender);
        		else
        			sender.sendMessage(getDataManager().wpMessage("You do not have permission to do that!"));
        	}
        	else
        		sender.sendMessage("You arent a human player!");
        	return true;
        }
        else if(commandName.equalsIgnoreCase("wpsave")) {
        	if(sender instanceof Player) {
        		if(getDataManager().isUsingGroupManager() && getDataManager().gmWorking()) {
        			if(getDataManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.savepoint"))
        				getDataManager().trySave(true);
        		}
        		else if(((Player)sender).isOp())
        			getDataManager().trySave(true);
        		else
        			sender.sendMessage(getDataManager().wpMessage("You do not have permission to do that!"));
        	}
        	else
        		getDataManager().trySave(true);
        	return true;
        }
        
        return false;
    }
    
    public void sendConsoleMsg(String msg) {
    	if(pdfFile != null)
    		System.out.println("[WAYPOINTS(" + pdfFile.getVersion() + ")] " + msg);
    	else
    		System.out.println("[WAYPOINTS(Unknown Version)] " + msg);
    }
    
    public DataManager getDataManager() {
    	return _dataManager;
    }
}
