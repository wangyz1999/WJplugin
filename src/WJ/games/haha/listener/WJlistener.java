package WJ.games.haha.listener;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WJlistener implements Listener{
	
//	@EventHandler
//	public void LightBow(EntityShootBowEvent event) {
//		event.setProjectile();
//	}
//	
	
	
	@EventHandler
	public void CreatureSpawn(CreatureSpawnEvent event) {
		if (event.getEntityType() == EntityType.CREEPER) {
			Creeper creeper = (Creeper) event.getEntity();
			creeper.setPowered(true);
		}
		if (event.getEntityType() == EntityType.ZOMBIE) {
			Zombie zombie = (Zombie) event.getEntity();
			zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
			zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
			zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		}
	}
	
	
	@EventHandler
	public void Arrow(ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.ARROW) {
			Projectile entity = event.getEntity();
			Location loc = entity.getLocation();
			entity.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), (float) 5.0, false, true);
			entity.remove();
			Creeper creeper = (Creeper) entity.getWorld().spawnEntity(loc, EntityType.CREEPER);
			creeper.setPowered(true);
			creeper.ignite();
		}
	}
	
	
	@EventHandler
	public void onPlayerInteractBlock(PlayerInteractEvent event) throws InterruptedException {
	    Player player = event.getPlayer();
	    if (player.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD) {
	        player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
	    }
	    
	    if (player.getInventory().getItemInMainHand().getType() == Material.IRON_PICKAXE) {
	    	Location loc = player.getTargetBlock((Set<Material>) null, 200).getLocation();
	    	double y = loc.getY();
	    	Location p_loc = player.getLocation();
	    	double start_x = p_loc.getX();
	    	double start_z = p_loc.getZ();
	    	double distance = Math.sqrt(Math.pow(loc.getX()-p_loc.getX(), 2) + Math.pow(loc.getZ()-p_loc.getZ(), 2));
	    	
	    	for (int i=0; i<(int)distance; i+=2) {
	    		Thread.sleep(20);
	    		double ratio = i / distance;
	    		double x = start_x + (loc.getX() - start_x) * ratio;
	    		double z = start_z + (loc.getZ() - start_z) * ratio;
	    		player.getWorld().createExplosion(x, y, z, (float) 3.0, false, true);
	    	}
	    }
	    
	    if (player.getInventory().getItemInMainHand().getType() == Material.IRON_HOE) {
	    	Location loc = player.getTargetBlock((Set<Material>) null, 200).getLocation();
	    	player.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), (float) 5.0, false, true);
	    }
	    
	    if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE) {
	    	Location loc = player.getTargetBlock((Set<Material>) null, 200).getLocation();
	    	int[][] mario = {
	    			{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1},
	    			{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1},
	    			{1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,0,1,1,1,1,1,1},
	    			{1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1},
	    			{1,1,1,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1},
	    			{1,1,1,1,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1},
	    			{1,1,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
	    			{1,1,0,0,1,1,1,0,0,0,1,1,1,1,1,1,0,0,0,1,1,1},
	    			{1,0,0,0,0,0,0,0,0,1,1,1,0,1,1,0,0,0,0,1,1,1},
	    			{1,0,0,0,0,0,0,0,1,1,1,1,0,0,1,0,0,0,1,1,1,1},
	    			{1,0,0,1,1,0,0,0,1,1,1,1,0,0,1,0,0,0,0,1,1,1},
	    			{1,0,0,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,1,1},
	    			{1,0,0,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,1,1},
	    			{1,0,0,1,1,1,1,0,1,1,1,0,1,1,1,1,1,1,1,0,1,1},
	    			{1,1,0,0,1,1,1,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0},
	    			{1,1,1,1,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1},
	    			{1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1},
	    			{1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1},
	    			{1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1},
	    			{1,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,0,1,1,1},
	    			{1,0,1,1,1,1,0,0,0,0,0,1,1,0,0,0,1,1,0,1,1,1},
	    			{0,1,1,1,1,0,0,1,1,1,1,0,1,1,1,0,0,0,1,0,1,1},
	    			{0,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,1,1},
	    			{0,1,1,1,0,1,1,1,1,1,1,1,0,0,1,1,0,0,1,0,1,1},
	    			{0,1,1,1,0,1,1,1,1,1,1,1,0,0,1,1,0,0,0,1,1,1},
	    			{0,0,0,1,1,0,0,1,1,1,1,0,1,0,0,0,1,1,0,1,1,1},
	    			{1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,1,0,1,1,1},
	    			{1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1},
	    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1},
	    			{1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
	    			{1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
	    			{1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0},
	    			{1,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,1,1,1,1,0,0},
	    			{1,0,0,1,1,0,0,0,0,0,1,1,1,0,0,1,1,1,1,0,0,1},
	    			{1,0,0,1,1,0,0,0,0,0,1,1,1,0,0,1,1,1,1,0,1,1},
	    			{1,1,0,0,1,1,1,1,1,0,0,1,1,0,0,1,1,1,0,1,1,1},
	    			{1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1,1,1,1},
	    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
	    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
	    	};
	    	double x = loc.getX();
	    	double y = loc.getY();
	    	double z = loc.getZ();
	    	double size = 1;
	    	
	    	for (int i=0; i<mario.length; i++) {
	    		for (int j=0; j<mario[0].length; j++) {
	    			if (mario[i][j] == 0) {
	    				player.getWorld().createExplosion(x+j*size, y, z+i*size, (float) 1.0, false, true);
	    				Thread.sleep(80);
	    			}
	    		}
	    	}
	    }
	    
	}
}
