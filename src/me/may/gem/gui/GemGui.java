package me.may.gem.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import me.may.gem.Entry;
import me.may.gem.dto.Gem;

@SuppressWarnings("deprecation")
public class GemGui {
	
	private static FileConfiguration config;
	private static ItemStack green;
	private static ItemStack lever;
	private static ItemStack red;
	
	static {
		config = Entry.getInstance().getConfig();
		
		green = new ItemStack(Material.getMaterial(config.getInt("Gui.CutLine.Material")) ,1 ,(short)config.getInt("Gui.CutLine.Data"));
		ItemMeta im = green.getItemMeta();
		im.setDisplayName(config.getString("Gui.CutLine.displayName").replaceAll("&", "§"));
		green.setItemMeta(im);

		red = new ItemStack(Material.getMaterial(config.getInt("Gui.GemSlot.Material")) ,1 ,(short)config.getInt("Gui.GemSlot.Data"));
		im = red.getItemMeta();
		im.setDisplayName(config.getString("Gui.GemSlot.displayName").replaceAll("&", "§"));
		red.setItemMeta(im);
		
		lever = new ItemStack(Material.getMaterial(config.getInt("Gui.Button.Material")) ,1 ,(short)config.getInt("Gui.Button.Data"));
		im = lever.getItemMeta();
		im.setDisplayName(config.getString("Gui.Button.displayName").replaceAll("&", "§"));
		lever.setItemMeta(im);
		
	}
	
	public static void openPunchGemGui(Player player) {
		if (!player.isOnline()) {
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 54, Entry.invName);
		ItemStack item = player.getInventory().getItemInMainHand();
		
		/*Gui制作*/
		int[] greens = {0,1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,34,35,36,37,38,39,41,42,43,44,45,46,47,48,49,50,51,52,53};
		for(int i : greens){ inv.setItem(i, green); }
		
		//下方是对于打孔与未镶嵌的判断
		List<Integer> reds = Lists.newArrayList();
		reds.add(29);
		reds.add(30);
		reds.add(31);
		reds.add(32);
		reds.add(33);
		
		int closeHole = Entry.getInstance().getItemCloseHoleAmount(item); //未打孔的洞
//		System.out.println("Close: " + closeHole);
		
		if (closeHole != 0) {
			for (int i = 4; i >= closeHole; i--) {
				reds.remove(i);
			}
		}else {
			reds.clear();
		}
		
		int openHole = Entry.getInstance().getItemOpenHoleAmount(item);
//		System.out.println("Open: " + openHole);
		if (openHole == 5) {
			reds.clear();
		}
		
		if (reds.size() != 0) {
			for (int i = 0; i < reds.size(); i++) { inv.setItem(reds.get(i), red); }
		}
		
		//关于宝石读取
		List<Gem> gems = Entry.getInstance().getItemGems(item);
//		System.out.println("Gem: " + gems.size());
		if (!gems.isEmpty() || gems != null) {
			int start = 33;
			reds.clear();
			for (int i = 0; i < gems.size(); i++) {
				reds.add(start);
				start--;
			}
		}
		if (reds.size() != 0) {
			for (int i = 0; i < reds.size(); i++) { inv.setItem(reds.get(i), gems.get(i).getItem()); }
		}
				
		inv.setItem(43, lever);
		inv.setItem(13, player.getInventory().getItemInMainHand());
		
		player.closeInventory();
		player.openInventory(inv);
	}
}
