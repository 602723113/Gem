package me.may.gem.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.may.gem.Entry;

/**
 * 宝石合成面板Gui
 * 
 * @author GayGuoGuo
 */
public class ComposeGui {

	public static void openComposeGui(Player player) {
		Inventory inv = Bukkit.createInventory(null, 54, Entry.getInstance().composeInvName);
		inv = setItem(inv);
		player.openInventory(inv);
	}

	private static Inventory setItem(Inventory inv) {
		ItemStack black = create(160, 15, " ", new String[0]);
		ItemStack blue = create(160, 3, " ", new String[0]);
		ItemStack begin = create(154, 0, "§b§l开始合成", new String[0]);
		inv.setItem(0, black);
		inv.setItem(1, black);
		inv.setItem(2, black);
		inv.setItem(3, black);
		inv.setItem(4, black);
		inv.setItem(5, black);
		inv.setItem(6, black);
		inv.setItem(7, black);
		inv.setItem(8, black);
		inv.setItem(9, black);
		inv.setItem(10, black);

		inv.setItem(16, black);
		inv.setItem(17, black);
		inv.setItem(18, black);
		inv.setItem(19, black);
		inv.setItem(20, black);
		inv.setItem(21, black);
		inv.setItem(22, blue);
		inv.setItem(23, black);
		inv.setItem(24, black);
		inv.setItem(25, black);
		inv.setItem(26, black);
		inv.setItem(27, black);
		inv.setItem(28, black);
		inv.setItem(29, black);
		inv.setItem(30, black);

		inv.setItem(31, begin);
		inv.setItem(32, black);
		inv.setItem(33, black);
		inv.setItem(34, black);
		inv.setItem(35, black);
		inv.setItem(36, black);
		inv.setItem(37, black);
		inv.setItem(38, black);
		inv.setItem(39, black);

		inv.setItem(41, black);
		inv.setItem(42, black);
		inv.setItem(43, black);
		inv.setItem(44, black);
		inv.setItem(45, black);
		inv.setItem(46, black);
		inv.setItem(47, black);
		inv.setItem(48, black);
		inv.setItem(49, black);
		inv.setItem(50, black);
		inv.setItem(51, black);
		inv.setItem(52, black);
		inv.setItem(53, black);
		return inv;
	}

	@SuppressWarnings("deprecation")
	private static ItemStack create(int ID, int damage, String name, String[] lore) {
		ItemStack item = new ItemStack(Material.getMaterial(ID));
		if (damage > 0) {
			item.setDurability((short) damage);
		}
		ItemMeta meta = item.getItemMeta();
		if (name != null) {
			meta.setDisplayName(name.replaceAll("&", "§"));
		}
		List<String> loreList1;
		if (meta.hasLore()) {
			loreList1 = meta.getLore();
		} else {
			loreList1 = new ArrayList<String>();
		}
		for (int i = 0; i < lore.length; i++) {
			String a = lore[i].replaceAll("&", "§");
			loreList1.add(a);
		}
		meta.setLore(loreList1);
		item.setItemMeta(meta);
		return item;
	}
}
