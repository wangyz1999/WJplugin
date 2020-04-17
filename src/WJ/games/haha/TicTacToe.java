package WJ.games.haha;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

class Move 
{ 
    int row, col; 
}; 

public class TicTacToe implements CommandExecutor, Listener, TabCompleter{
	
	private Main plugin;
	
	public TicTacToe(Main instance) {
		plugin = instance;
	}
	
	private int size = 25;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (sender instanceof Player) {
			if (args.length <= 1) {
				return false;
			}
			if (args.length >= 2) {
				String board_name = args[1];
				if (args[0].equals("create")) {
					List<String> allBoards = plugin.getConfig().getStringList("All");
					allBoards.add(board_name);
					plugin.getConfig().set("All", allBoards);
					if (args.length >= 3) {
						if (args[2].equals("ai")) {
							plugin.getConfig().set(board_name + ".isAI", true);
							plugin.getConfig().set(board_name + ".strongAI", true);
							plugin.getConfig().set(board_name + ".playerFirst", true);
						}
						else if (args[2].equals("pvp")) {
							plugin.getConfig().set(board_name + ".isAI", false);
						}
						else return false;
					}
					else {
						plugin.getConfig().set(board_name + ".isAI", true);
						plugin.getConfig().set(board_name + ".strongAI", false);
						plugin.getConfig().set(board_name + ".playerFirst", false);
					}
					plugin.getConfig().set(board_name + ".isActivated", false);
					plugin.getConfig().set(board_name + ".moveCount", 0);
					List<List<Integer>> emptyCfg = Arrays.asList(Arrays.asList(0,0,0),
																 Arrays.asList(0,0,0),
																 Arrays.asList(0,0,0));
					plugin.getConfig().set(board_name + ".cfg", emptyCfg);
				}
				else if (args[0].equals("activate")) {
					Location loc = player.getLocation();
					int x = loc.getBlockX() - 12;
					int y = loc.getBlockY() - 1;
					int z = loc.getBlockZ() - 12;
					plugin.getConfig().set(board_name + ".x", x);
					plugin.getConfig().set(board_name + ".y", y);
					plugin.getConfig().set(board_name + ".z", z);
					plugin.getConfig().set(board_name + ".isActivated", true);
					
//					plugin.saveConfig();
					renderBoard(board_name, player.getWorld());
				}
				else if (args[0].equals("deactivate")) {
//					int x = plugin.getConfig().s
//					int y = 
//					int z = 
					
				}
				else if (args[0].equals("delete")) {
					List<String> allBoards = plugin.getConfig().getStringList("All");
					allBoards.remove(board_name);
					plugin.getConfig().set("All", allBoards);
					plugin.getConfig().set(board_name, null);
				}
				else {
					return false;
				}
			}
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Generating Board!"));
			player.sendMessage(args);
		}
		// command not player would be terminal
		else {
			sender.sendMessage("Terminal not allowed");
		}
		plugin.saveConfig();
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			return Arrays.asList("create", "activate", "deactivate", "delete");
		}
		
		if (args.length == 2) {
			return plugin.getConfig().getStringList("All");
		}
		
		if (args.length == 3) {
//			sender.sendMessage(args);
			if (args[0].equals("create")) {
				return Arrays.asList("ai", "pvp");
			}
		}
		
		if (args.length == 4) {
			if (args[2].equals("ai")) {
				return Arrays.asList("strong", "weak");
			}
		}
		
		if (args.length == 5) {
			if (args[2].equals("ai")) {
				return Arrays.asList("first", "second");
			}
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public void renderBoard(String board, World world) {
		FileConfiguration fileCfg = plugin.getConfig();
		int x = fileCfg.getInt(board + ".x");
		int y = fileCfg.getInt(board + ".y");
		int z = fileCfg.getInt(board + ".z");
		List<List<Integer>> cfg = (List<List<Integer>>) fileCfg.getList(board + ".cfg");
			

		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (i == 0 || i == 8 || i == 16 || i == 24 || j == 0 || j == 8 || j == 16 || j == 24)
					world.getBlockAt(x+i, y, z+j).setType(Material.BLACK_WOOL);
			}
		}
		
		for (int i=0; i<cfg.size(); i++) {
			for (int j=0; j<cfg.get(0).size(); j++) {
				if (cfg.get(i).get(j) == 0) {
					// Draw Empty
					for (int ii=0; ii<7; ii++) {
						for (int jj =0; jj<7; jj++) {
							world.getBlockAt(x+1+i*8+ii, y, z+1+j*8+jj).setType(Material.WHITE_WOOL);
						}
					}
				}
				else if (cfg.get(i).get(j) == 1) {
					// Draw Cross
					for (int ii=0; ii<7; ii++) {
						for (int jj =0; jj<7; jj++) {
							world.getBlockAt(x+1+i*8+ii, y, z+1+j*8+jj).setType(Material.WHITE_WOOL);
						}
					}
					world.getBlockAt(x+1+i*8+1, y, z+1+j*8+1).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+1, y, z+1+j*8+5).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+2, y, z+1+j*8+2).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+2, y, z+1+j*8+4).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+3, y, z+1+j*8+3).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+4, y, z+1+j*8+2).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+4, y, z+1+j*8+4).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+5, y, z+1+j*8+1).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+5, y, z+1+j*8+5).setType(Material.BLACK_WOOL);
				}
				else {
					for (int ii=0; ii<7; ii++) {
						for (int jj =0; jj<7; jj++) {
							world.getBlockAt(x+1+i*8+ii, y, z+1+j*8+jj).setType(Material.WHITE_WOOL);
						}
					}
					world.getBlockAt(x+1+i*8+1, y, z+1+j*8+2).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+1, y, z+1+j*8+3).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+1, y, z+1+j*8+4).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+2, y, z+1+j*8+1).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+3, y, z+1+j*8+1).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+4, y, z+1+j*8+1).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+2, y, z+1+j*8+5).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+3, y, z+1+j*8+5).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+4, y, z+1+j*8+5).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+5, y, z+1+j*8+2).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+5, y, z+1+j*8+3).setType(Material.BLACK_WOOL);
					world.getBlockAt(x+1+i*8+5, y, z+1+j*8+4).setType(Material.BLACK_WOOL);
				}
			}
		}
	}
	
	public void removeBoard(int x, int y, int z, World world) {
		int size = 25;
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				world.getBlockAt(x, y, z).setType(Material.AIR);
				x += 1;
			}
			x -= size;
			z += 1;
		}
	}
	
	public int evaluate(List<List<Integer>> b) 
	{ 
	    // Checking for Rows for X or O victory. 
		int player = 1;
		int opponent = 2;
	    for (int row = 0; row < 3; row++) 
	    { 
	        if (b.get(row).get(0) == b.get(row).get(1) && b.get(row).get(1) == b.get(row).get(2)) 
	        { 
	            if (b.get(row).get(0) == player) return 10; 
	            else if (b.get(row).get(0) == opponent) return -10; 
	        } 
	    } 
	  
	    // Checking for Columns for X or O victory. 
	    for (int col = 0; col < 3; col++) 
	    { 
	        if (b.get(0).get(col) == b.get(1).get(col) && b.get(1).get(col) == b.get(2).get(col)) 
	        { 
	            if (b.get(0).get(col) == player) return 10;  
	            else if (b.get(0).get(col) == opponent) return -10; 
	        } 
	    } 
	  
	    // Checking for Diagonals for X or O victory. 
	    if (b.get(0).get(0) == b.get(1).get(1) && b.get(1).get(1) == b.get(2).get(2)) 
	    { 
	        if (b.get(0).get(0) == player) return 10; 
	        else if (b.get(0).get(0) == opponent) return -10; 
	    } 
	  
	    if (b.get(0).get(2) == b.get(1).get(1) && b.get(1).get(1) == b.get(2).get(0)) 
	    { 
	        if (b.get(0).get(2) == player) return 10; 
	        else if (b.get(0).get(2) == opponent) return -10; 
	    } 
	  
	    // Else if none of them have won then return 0 
	    return 0; 
	} 
	
	public Boolean isMovesLeft(List<List<Integer>> board) 
	{ 
	    for (int i = 0; i < 3; i++) 
	        for (int j = 0; j < 3; j++) 
	            if (board.get(i).get(j) == 0) 
	                return true; 
	    return false; 
	} 
	
	public int minimax(List<List<Integer>> board,  int depth, Boolean isMax) 
	{
		int player = 1;
		int opponent = 2;
		int score = evaluate(board);
		
		
		// If Maximizer has won the game  
		// return his/her evaluated score 
		if (score == 10) return score; 
		
		// If Minimizer has won the game  
		// return his/her evaluated score 
		if (score == -10) return score; 
		
		// If there are no more moves and  
		// no winner then it is a tie 
		if (isMovesLeft(board) == false) return 0; 
		
		// If this maximizer's move 
		if (isMax) { 
			int best = -1000; 
		
			// Traverse all cells 
			for (int i = 0; i < 3; i++) { 
			    for (int j = 0; j < 3; j++) { 
			        // Check if cell is empty 
			        if (board.get(i).get(j) == 0) { 
			            // Make the move 
			            board.get(i).set(j, player);
			
			            // Call minimax recursively and choose 
			            // the maximum value 
			            best = Math.max(best, minimax(board, depth + 1, !isMax)); 
			            // Undo the move 
			            board.get(i).set(j, 0); 
			        } 
			    } 
			} 
			return best; 
		} 
		
		// If this minimizer's move 
		else { 
			int best = 1000; 
		
			// Traverse all cells 
			for (int i = 0; i < 3; i++) 
			{ 
			    for (int j = 0; j < 3; j++) 
			    { 
			        // Check if cell is empty 
			        if (board.get(i).get(j) == 0) 
			        { 
			            // Make the move 
			            board.get(1).set(j, opponent); 
			
			            // Call minimax recursively and choose 
			            // the minimum value 
			            best = Math.min(best, minimax(board, depth + 1, !isMax)); 
			
			            // Undo the move 
			            board.get(1).set(j, 0); 
			        } 
			    } 
			} 
			return best; 
		} 
	}
	
	public Move findBestMove(List<List<Integer>> board) 
	{ 
	    int bestVal = -1000; 
	    Move bestMove = new Move(); 
	    bestMove.row = -1; 
	    bestMove.col = -1; 
	    int player = 1;
	    // Traverse all cells, evaluate minimax function  
	    // for all empty cells. And return the cell  
	    // with optimal value. 
	    for (int i = 0; i < 3; i++) 
	    { 
	        for (int j = 0; j < 3; j++) 
	        { 
	            // Check if cell is empty 
	            if (board.get(i).get(j) == 0) 
	            { 
	                // Make the move 
	                board.get(i).set(j, player); 
	  
	                // compute evaluation function for this 
	                // move. 
	                int moveVal = minimax(board, 0, true); 
	                System.out.println(moveVal);
	  
	                // Undo the move 
	                board.get(i).set(j, 0); 
	  
	                // If the value of the current move is 
	                // more than the best value, then update 
	                // best/ 
	                if (moveVal > bestVal) 
	                { 
	                    bestMove.row = i; 
	                    bestMove.col = j; 
	                    bestVal = moveVal; 
	                } 
	            } 
	        } 
	    }  
	    return bestMove; 
	} 
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void placeMove(ProjectileHitEvent event) throws InterruptedException {
		if (event.getEntityType() == EntityType.ARROW) {
			Block block = event.getHitBlock();
			Projectile entity = event.getEntity();
			entity.remove();
			Location loc = block.getLocation();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			
			// Locate the target board
			List<String> all = plugin.getConfig().getStringList("All");
			String target = "";
			for (String b : all) {
//				List<List<Integer>> cfg = (List<List<Integer>>) plugin.getConfig().getList(b + ".cfg");
				int check_x = plugin.getConfig().getInt(b + ".x");
				int check_y = plugin.getConfig().getInt(b + ".y");
				int check_z = plugin.getConfig().getInt(b + ".z");
				if (y == check_y && x > check_x && x < check_x + size - 1 && z > check_z && z < check_z + size - 1) {
//					ProjectileSource pjs = event.getEntity().getShooter();
//					Player p = (Player) pjs;
//					p.sendMessage("hit On board");
//					p.sendMessage(x + " " + y + " " + z );
					List<List<Integer>> cfg = (List<List<Integer>>) plugin.getConfig().getList(b + ".cfg");
					if (isMovesLeft(cfg) == false) {
						List<List<Integer>> emptyCfg = Arrays.asList(Arrays.asList(0,0,0),
																	 Arrays.asList(0,0,0),
																	 Arrays.asList(0,0,0));
						plugin.getConfig().set(b + ".cfg", emptyCfg);
						plugin.getConfig().set(b + ".moveCount", 0);
						plugin.saveConfig();
						renderBoard(b, block.getWorld());
						return;
					}
					
					
					
					target = b;
					break;
				}
				 
			}
			
			// Place tile
			int x_idx = -1;
			int z_idx = -1;
			int check_x = plugin.getConfig().getInt(target + ".x");
			int check_z = plugin.getConfig().getInt(target + ".z");
			if (x > check_x && x < check_x+8) x_idx = 0;
			if (x > check_x+8 && x < check_x+16) x_idx = 1;
			if (x > check_x+16 && x < check_x+24) x_idx = 2;
			if (z > check_z && z < check_z+8) z_idx = 0;
			if (z > check_z+8 && z < check_z+16) z_idx = 1;
			if (z > check_z+16 && z < check_z+24) z_idx = 2;
			
//			ProjectileSource pjs = event.getEntity().getShooter();
//			Player p = (Player) pjs;
//			p.sendMessage("hit On board");
//			p.sendMessage(x_idx + " " + z_idx);
			if (x_idx != -1 && z_idx != -1) {
				List<List<Integer>> cfg = (List<List<Integer>>) plugin.getConfig().getList(target + ".cfg");
				if (cfg.get(x_idx).get(z_idx) == 0) {
					int m_count = plugin.getConfig().getInt(target + ".moveCount");
					if (m_count % 2 == 0) cfg.get(x_idx).set(z_idx, 1);
					else cfg.get(x_idx).set(z_idx, 2);
					plugin.getConfig().set(target + ".moveCount", m_count + 1);
					plugin.getConfig().set(target + ".cfg", cfg);
					plugin.saveConfig();
					renderBoard(target, block.getWorld());
					
					if (isMovesLeft(cfg) == false) {
						// To do: change tiles to red
						return;
					}
					
					if (plugin.getConfig().getBoolean(target + ".isAI") == true) {
						if (plugin.getConfig().getBoolean(target + ".strongAI") == true) {
							m_count = plugin.getConfig().getInt(target + ".moveCount");
							cfg = (List<List<Integer>>) plugin.getConfig().getList(target + ".cfg");
							Move bestMove = findBestMove(cfg);
							if (m_count % 2 == 0) cfg.get(bestMove.row).set(bestMove.col, 1);
							else cfg.get(bestMove.row).set(bestMove.col, 2);
							plugin.getConfig().set(target + ".moveCount", m_count + 1);
						}
						else {
							m_count = plugin.getConfig().getInt(target + ".moveCount");
							Random r = new Random();
							int rand_pos = r.nextInt(9 - m_count);
							int idx = 0;
							cfg = (List<List<Integer>>) plugin.getConfig().getList(target + ".cfg");
							for (int i=0; i<3; i++) {
								for (int j=0; j<3; j++) {
									if (cfg.get(i).get(j) == 0) {
										if (idx == rand_pos) {
											if (m_count % 2 == 0) cfg.get(i).set(j, 1);
											else cfg.get(i).set(j, 2);
											plugin.getConfig().set(target + ".moveCount", m_count + 1);
										}
										idx++;
									}
								}
							}
							plugin.saveConfig();
							renderBoard(target, block.getWorld());
							if (isMovesLeft(cfg) == false) {
								for (int i=0; i<3; i++) {
									for (int j=0; j<3; j++) {
										cfg.get(i).set(j, 0);
									}
								}
								plugin.getConfig().set(target + ".moveCount", 0);
							}
						}
					}
					
					renderBoard(target, block.getWorld());
					plugin.saveConfig();
				}
			}
			
//			Location loc = block.getLocation();
//			entity.getWorld().getBlockAt(loc).setType(Material.DIAMOND_BLOCK);
		}
	}
}
