package com.Endain.Waypoints;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.Endain.Waypoints.Managers.ConfigManager;
import com.Endain.Waypoints.Managers.DataManager;
import com.Endain.Waypoints.Managers.PermissionManager;
import com.Endain.Waypoints.Managers.WaypointManager;

/**
* This plugin will allow players to spawn at 'Waypoint' markers
* that have either been randomly generated within a world or have
* been placed by an admin. Refer to the README file for more details
* on how the plugin works. Feel free to use and distribute this code,
* but please be sure to give me credit if you use it as a base or
* redistribute it. I have commented up the code a fair bit to try to
* make this software easy to understand and hopefully learn from.
* 
* Now on Github.
* 
* Public Release version - v0.3.0
* @author Endain
*/
public class Waypoints extends JavaPlugin {
    private final WaypointsPlayerListener playerListener = new WaypointsPlayerListener(this);
    private final WaypointsBlockListener blockListener = new WaypointsBlockListener(this);
    private final WaypointsEntityListener entityListener = new WaypointsEntityListener(this);
    private final WaypointsServerListener serverListener = new WaypointsServerListener(this);
    private final WaypointsWorldListener worldListener = new WaypointsWorldListener(this);
    
    //Waypoints Variables
    private ConfigManager _configManager;
    private DataManager _dataManager;
    private PermissionManager _permissionManager;
    private WaypointManager _waypointManager;
    // OLD private DataManager _dataManager;
    PluginDescriptionFile pdfFile;

    public void onDisable() {
    	if(this._dataManager != null)
    		this._dataManager.write();
    	this._configManager = null;
    	this._dataManager = null;
    	this._permissionManager = null;
    	this._waypointManager = null;
        sendConsoleMsg("DISABLED!");
    }

    public void onEnable() {
    	pdfFile = this.getDescription();
    	sendConsoleMsg("Attempting to start...");
    	
    	PluginManager pm = getServer().getPluginManager();
        //Register player events
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Priority.Normal, this);
        //Register entity events
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Priority.Normal, this);
        //Register block events
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Priority.Normal, this);
        //Register server events
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverListener, Priority.Normal, this);
        //Register world events
        pm.registerEvent(Event.Type.WORLD_LOAD, worldListener, Priority.Normal, this);
        
    	//Init managers
        this._configManager = new ConfigManager(this);
        if(!this._configManager.load()) {
        	this.getPluginLoader().disablePlugin(this);
        	return;
        }
    	this._dataManager = new DataManager(this, this._configManager);
    	if(!this._dataManager.load()) {
    		this.getPluginLoader().disablePlugin(this);
    		return;
    	}
    	this._permissionManager = new PermissionManager(this);
    	if(!this._permissionManager.load()) {
    		this.getPluginLoader().disablePlugin(this);
    		return;
    	}
    	this._waypointManager = new WaypointManager(this, this._configManager, this._dataManager, this._permissionManager);
    	if(!this._waypointManager.load()) {
    		this.getPluginLoader().disablePlugin(this);
    		return;
    	}
    	
    	sendConsoleMsg("ENABLED!");
    }
    
    //Handles all plugin commands
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String commandName = command.getName().toLowerCase();
        
        //Handle the /wpbind command
        if(commandName.equalsIgnoreCase("wpbind")) {
        	if(sender instanceof Player) {
        		//Check that the called has permission to use this command
        		if(getConfigManager().getProperty("limit-use").equalsIgnoreCase("true") && getConfigManager().getProperty("use-group-manager").equalsIgnoreCase("true") && getPermissionManager().isWorking()) {
        			if(getPermissionManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.wpuse"))
        				getWaypointManager().tryBind((Player)sender);
        			else
        				sender.sendMessage(getWaypointManager().wpMessage("You do not have permission to do that!"));
        		}
        		else if(getConfigManager().getProperty("limit-use").equalsIgnoreCase("true") && getConfigManager().getProperty("limit-use").equalsIgnoreCase("false")) {
        			if(((Player)sender).isOp())
        				getWaypointManager().tryBind((Player)sender);
        			else
        				sender.sendMessage(getWaypointManager().wpMessage("You do not have permission to do that!"));
        		}
        		else
        			getWaypointManager().tryBind((Player)sender);
        	}
        	else
        		sender.sendMessage("You arent a human player!");
        	return true;
        }
        //Handle the /wpfree command
        else if(commandName.equalsIgnoreCase("wpfree") || commandName.equalsIgnoreCase("wpunbind")) {
        	if(sender instanceof Player) {
        		if(getConfigManager().getProperty("allow-unbind").equalsIgnoreCase("true")) {
	        		//Check that the called has permission to use this command
	        		if(getConfigManager().getProperty("limit-use").equalsIgnoreCase("true") && getConfigManager().getProperty("use-group-manager").equalsIgnoreCase("true") && getPermissionManager().isWorking()) {
	        			if(getPermissionManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.wpuse"))
	        				getWaypointManager().tryUnbind((Player)sender);
	        			else
	        				sender.sendMessage(getWaypointManager().wpMessage("You do not have permission to do that!"));
	        		}
	        		else if(getConfigManager().getProperty("limit-use").equalsIgnoreCase("true") && getConfigManager().getProperty("use-group-manager").equalsIgnoreCase("false")) {
	        			if(((Player)sender).isOp())
	        				getWaypointManager().tryUnbind((Player)sender);
	        			else
	        				sender.sendMessage(getWaypointManager().wpMessage("You do not have permission to do that!"));
	        		}
	        		else
	        			getWaypointManager().tryUnbind((Player)sender);
        		}
        		else
    				sender.sendMessage(getWaypointManager().wpMessage("Command is disabled!"));
        	}
        	else
        		sender.sendMessage("You arent a human player!");
        	return true;
        }
        //Handle the /wpadd command
        else if(commandName.equalsIgnoreCase("wpadd")) {
        	if(sender instanceof Player) {
        		//Check that the called has permission to use this command
        		if(getConfigManager().getProperty("use-group-manager").equalsIgnoreCase("true") && getPermissionManager().isWorking()) {
        			if(getPermissionManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.addpoint"))
        				getWaypointManager().tryAdd((Player)sender);
        		}
        		else if(((Player)sender).isOp())
        			getWaypointManager().tryAdd((Player)sender);
        		else
        			sender.sendMessage(getWaypointManager().wpMessage("You do not have permission to do that!"));
        	}
        	else
        		sender.sendMessage("You arent a human player!");
        	return true;
        }
        //Handle the /wpdel command
        else if(commandName.equalsIgnoreCase("wpdel")) {
        	if(sender instanceof Player) {
        		//Check that the called has permission to use this command
        		if(getConfigManager().getProperty("use-group-manager").equalsIgnoreCase("true") && getPermissionManager().isWorking()) {
        			if(getPermissionManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.delpoint"))
        				getWaypointManager().tryDel((Player)sender);
        		}
        		else if(((Player)sender).isOp())
        			getWaypointManager().tryDel((Player)sender);
        		else
        			sender.sendMessage(getWaypointManager().wpMessage("You do not have permission to do that!"));
        	}
        	else
        		sender.sendMessage("You arent a human player!");
        	return true;
        }
        //handle the /wpclean command
        else if(commandName.equalsIgnoreCase("wpclean")) {
        	if(sender instanceof Player) {
        		//Check that the called has permission to use this command
        		if(getConfigManager().getProperty("use-group-manager").equalsIgnoreCase("true") && getPermissionManager().isWorking()) {
        			if(getPermissionManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.cleanpoint"))
        				getWaypointManager().tryClean((Player)sender);
        		}
        		else if(((Player)sender).isOp())
        			getWaypointManager().tryClean((Player)sender);
        		else
        			sender.sendMessage(getWaypointManager().wpMessage("You do not have permission to do that!"));
        	}
        	else
        		sender.sendMessage("You arent a human player!");
        	return true;
        }
        //Handle the /wpsave command
        else if(commandName.equalsIgnoreCase("wpsave")) {
        	if(sender instanceof Player) {
        		//Check that the called has permission to use this command
        		if(getConfigManager().getProperty("use-group-manager").equalsIgnoreCase("true") && getPermissionManager().isWorking()) {
        			if(getPermissionManager().getPermissions().getWorldsHolder().getWorldPermissions((Player)sender).has((Player)sender, "waypoints.savepoint")) {
        				getWaypointManager().save();
        				sender.sendMessage(getWaypointManager().wpMessage("Data saved!"));
        			}
        		}
        		else if(((Player)sender).isOp()) {
        			getWaypointManager().save();
        			sender.sendMessage(getWaypointManager().wpMessage("Data saved!"));
        		}
        		else
        			sender.sendMessage(getWaypointManager().wpMessage("You do not have permission to do that!"));
        	}
        	else {
        		getWaypointManager().save();
        		sender.sendMessage("Data saved!");
        	}
        	return true;
        }
        
        return false;
    }
    
    //Function to standardize console output .. and make it pretty...
    public void sendConsoleMsg(String msg) {
    	if(pdfFile != null)
    		System.out.println("[WAYPOINTS(" + pdfFile.getVersion() + ")] " + msg);
    	else
    		System.out.println("[WAYPOINTS(Unknown Version)] " + msg);
    }
    
    //ConfigManager accessor
    public ConfigManager getConfigManager() {
    	return _configManager;
    }
    
    //DataManager accessor
    public DataManager getDataManager() {
    	return _dataManager;
    }
    
    //PermissionManager accessor
    public PermissionManager getPermissionManager() {
    	return _permissionManager;
    }
    
    //WaypointManager accessor
    public WaypointManager getWaypointManager() {
    	return _waypointManager;
    }
}
