package WJ.games.haha;


import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import WJ.games.haha.commands.HelloCommand;
import WJ.games.haha.listener.WJlistener;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable() {
		System.out.println("This plugin works!");
		this.getCommand("hello").setExecutor((CommandExecutor) new HelloCommand());
		getServer().getPluginManager().registerEvents(new WJlistener(), this);
	}
	
	@Override
	public void onDisable() {
		System.out.println("Plugin shutting down!");
	}
	
	

}
