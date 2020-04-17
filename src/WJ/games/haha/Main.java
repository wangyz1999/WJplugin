package WJ.games.haha;


import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

//import WJ.games.haha.commands.HelloCommand;
//import WJ.games.haha.listener.WJlistener;
import WJ.games.haha.TicTacToe;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable() {
		System.out.println("This plugin works!");
//		this.getCommand("hello").setExecutor((CommandExecutor) new HelloCommand());
//		getServer().getPluginManager().registerEvents(new WJlistener(), this);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
//		this.getCommand("tictactoe").setExecutor((CommandExecutor) this);
//		getServer().getPluginManager().registerEvents(this, this);
		
		this.getCommand("tictactoe").setExecutor((CommandExecutor) new TicTacToe(this));
		getServer().getPluginManager().registerEvents(new TicTacToe(this), this);
	}
	
	@Override
	public void onDisable() {
		System.out.println("Plugin shutting down!");
	}
	
	

}
