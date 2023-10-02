package me.may.gem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.may.gem.command.GemCommand;
import me.may.gem.dao.GemDao;
import me.may.gem.dto.Gem;
import me.may.gem.dto.GemType;
import me.may.gem.dto.PunchWeaponType;
import me.may.gem.listener.ComposeListener;
import me.may.gem.listener.PunchListener;
import me.may.gem.utils.FileUtil;

public class Entry extends JavaPlugin {
	
	private static File gemFile;
	private static Entry instance;
	public static String invName;
	public String composeInvName;
	public String inlayTool;
	public String takeTool;
	public String inlayLore;
	public String punchLore;
	
	public void onEnable() {
		gemFile = new File(getDataFolder().getAbsolutePath() + "\\Gems");
		Bukkit.getConsoleSender().sendMessage("§a[宝石] §e>> §f已加载");
		Bukkit.getPluginCommand("gem").setExecutor(new GemCommand());
		Bukkit.getPluginManager().registerEvents(new PunchListener(), this);
		Bukkit.getPluginManager().registerEvents(new ComposeListener(), this);
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		if (!gemFile.exists()) {
			gemFile.mkdirs();
			File file = new File(gemFile, "ExampleGem.yml");
			FileConfiguration filec = FileUtil.loadYml(file);
			filec.createSection("Gem");
			filec.set("Gem.Name", "§8[§a一级演示宝石§8]");
			filec.set("Gem.Type", "演示宝石");
			filec.set("Gem.Material", 351);
			filec.set("Gem.Data", 4);
			filec.set("Gem.CanUpgrade", true);
			filec.set("Gem.NextLevelGem", "§8[§a二级演示宝石§8]");
			
			List<String> types = new ArrayList<String>();
			types.add(GemType.HEALTH.toString());
			types.add(GemType.STRENTH.toString());
			filec.set("Gem.GemType", types);
			
			List<String> gemTypeAmount = new ArrayList<String>();
			gemTypeAmount.add(GemType.HEALTH.toString() + " 2");
			gemTypeAmount.add(GemType.STRENTH.toString() + " 3");
			filec.set("Gem.GemTypeAmount", gemTypeAmount);
			
			List<String> punchWeaponTypes = new ArrayList<String>();
			punchWeaponTypes.add(PunchWeaponType.ALL.toString());
			filec.set("Gem.PunchType", punchWeaponTypes);
			List<String> description = new ArrayList<String>();
			description.add("§e作用：§7增加人物属性");
			description.add("§e说明: §7拥有血量加成,力量加成的宝石");
			description.add("");
			description.add("§4§l§m一一一一一一一一一一一");
			description.add("§7可以将其镶嵌在§a所有§7位置上");
			filec.set("Gem.Description", description);
			FileUtil.saveYml(filec, file);
			
			//宝石拥有的属性
			List<GemType> gemTypes = new ArrayList<GemType>();
			for (String string : filec.getStringList("Gem.GemType")) {
				gemTypes.add(GemType.valueOf(string));
			}
			
			//每个属性可以加多少值
			Map<GemType, Double> gemTypeAmounts = new HashMap<GemType, Double>();
			for (String string : filec.getStringList("Gem.GemTypeAmount")) {
				gemTypeAmounts.put(GemType.valueOf(string.split(" ")[0]), Double.valueOf(string.split(" ")[1]));
			}
			
			//可以镶嵌的位置
			List<PunchWeaponType> punchTypes = new ArrayList<PunchWeaponType>();
			for (String string : filec.getStringList("Gem.PunchType")) {
				punchTypes.add(PunchWeaponType.valueOf(string));
			}
			
			Gem gem = new Gem("§8[§a一级演示宝石§8]","演示宝石" , 351, (short)4, gemTypeAmounts, punchTypes, gemTypes, description, true, "§8[§a二级演示宝石§8]");
			GemDao.gems.add(gem);
			Bukkit.getConsoleSender().sendMessage("§a[宝石] §e>> §f发现无宝石文件夹,已自动创建!");
		}
		invName = getConfig().getString("Gui.Title").replaceAll("&", "§");
		instance = this;
		inlayTool = getConfig().getString("Tools.InlayLore").replaceAll("&", "§");
		takeTool = getConfig().getString("Tools.TakeLore").replaceAll("&", "§");
		inlayLore = getConfig().getString("CheckItemLore.Inlay").replaceAll("&", "§");
		punchLore = getConfig().getString("CheckItemLore.Punch").replaceAll("&", "§");
		composeInvName = getConfig().getString("Gui.Compose.Title").replaceAll("&", "§");
		loadGems();
	}

	/**
	 * 读取所有的宝石
	 */
	public static void loadGems() {
		GemDao.gems.clear();
		GemDao.gemNames.clear();
		FileConfiguration filec = null;
		for (File file : gemFile.listFiles()) {
			filec = FileUtil.loadYml(file);
			List<GemType> gemTypes = new ArrayList<GemType>();
			for (String string : filec.getStringList("Gem.GemType")) {
				gemTypes.add(GemType.valueOf(string));
			}
			
			//每个属性可以加多少值
			Map<GemType, Double> gemTypeAmount = new HashMap<GemType, Double>();
			for (String string : filec.getStringList("Gem.GemTypeAmount")) {
				gemTypeAmount.put(GemType.valueOf(string.split(" ")[0]), Double.valueOf(string.split(" ")[1]));
			}
			
			List<PunchWeaponType> punchTypes = new ArrayList<PunchWeaponType>();
			for (String string : filec.getStringList("Gem.PunchType")) {
				punchTypes.add(PunchWeaponType.valueOf(string));
			}
			
			List<String> description = filec.getStringList("Gem.Description");
			for (int i = 0; i < description.size(); i++) {
				description.set(i, description.get(i).replaceAll("&", "§"));
			}
			
			if (filec.getBoolean("Gem.CanUpgrade")) {
				GemDao.gems.add(new Gem(filec.getString("Gem.Name").replaceAll("&", "§"), filec.getString("Gem.Type").replaceAll("&", "§"), filec.getInt("Gem.Material"), (short)filec.getInt("Gem.Data"), gemTypeAmount, punchTypes, gemTypes, description, true, filec.getString("Gem.NextLevelGem").replaceAll("&", "§")));
			}else {
				GemDao.gems.add(new Gem(filec.getString("Gem.Name").replaceAll("&", "§"), filec.getString("Gem.Type").replaceAll("&", "§"), filec.getInt("Gem.Material"), (short)filec.getInt("Gem.Data"), gemTypeAmount, punchTypes, gemTypes, description));
			}
		}
		for (Gem gem : GemDao.gems) {
			GemDao.gemNames.add(gem.getName());
		}
		Bukkit.getConsoleSender().sendMessage("§a[宝石] §e>> §f已读取所有的宝石至缓存中!");
	}
	
	/**
	 * 取物品上已打孔但未镶嵌的数量
	 * @param item 物品
	 * @return 打孔但未镶嵌的数量
	 */
	public int getItemOpenHoleAmount(ItemStack item) {
		int openHole = 0;
		if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i = 0; i < lore.size(); i++) {
				if (lore.get(i).indexOf(punchLore) != -1) {
					openHole++;
				}
			}
		}
		return openHole;
	}
	
	public boolean canPunchItem(ItemStack item) {
		if (item == null || item.getType().equals(Material.AIR)) {
			return false;
		}
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return false;
		}
		int closeHole = getItemCloseHoleAmount(item);
		int openHole = getItemOpenHoleAmount(item);
		int gems = getItemGems(item).size();
		if (closeHole + openHole + gems != 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 取物品上未打孔的数量
	 * @param item 物品
	 * @return 未打孔的数量
	 */
	public int getItemCloseHoleAmount(ItemStack item) {
		int closeHole = 0; //未打孔的洞
		if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i = 0; i < lore.size(); i++) {
				//如果这行Lore是没有未打孔字样的那就是未打孔 则不作任何操作
				//如果存在了那未打孔的洞就+1
				if (lore.get(i).indexOf(inlayLore) != -1) {
					closeHole++;
				}
			}
		}
		return closeHole;
	}
	
	/**
	 * 判断物品是否为打孔器
	 * 
	 * @param item 物品
	 * @return true[是]/false[不是]
	 */
	public boolean isInlayTool(ItemStack item) {
		if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i = 0; i < lore.size(); i++) {
				if (lore.get(i).indexOf(inlayTool) != -1) { //判断是为打孔器
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断一个物品是否是宝石
	 * 
	 * @param item 宝石
	 * @return true[是]/false[不是]
	 */
	public boolean isGem(ItemStack item) {
		if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			for (String name : GemDao.gemNames) {
				if (name.indexOf(item.getItemMeta().getDisplayName()) != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断一个物品是否为宝石摘除符
	 * @param item 物品
	 * @return true[是]/false[不是]
	 */
	public boolean isTakeTool(ItemStack item) {
		if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i = 0; i < lore.size(); i++) {
				if (lore.get(i).indexOf(takeTool) != -1) { //判断是为打孔器
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 取物品上的所有宝石
	 * @param item 物品
	 * @return 一个宝石的List
	 */
	public List<Gem> getItemGems(ItemStack item) {
		List<Gem> gems = new ArrayList<Gem>();
		if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			Gem gem = null;
			for (int i = 0; i < lore.size(); i++) {
				gem = GemDao.getGem(lore.get(i));
				if (gem == null) {
					continue;
				}else {
					gems.add(gem);
				}
			}
		}
		return gems;
	}
	
	/**
	 * 判断一个物品是否已绑定
	 * 
	 * @param item
	 *            物品
	 * @return true[已绑定]/false[未绑定]
	 */
	public boolean isBind(ItemStack item) {
		if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return false;
		}
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		for (int i = 0; i < lore.size(); i++) {
			String string = lore.get(i);
			if (string.indexOf("已绑定") != -1) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isTool(ItemStack item) {
		if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return false;
		}
		if (isInlayTool(item) || isTakeTool(item)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 取主类实例
	 * @return
	 */
	public static Entry getInstance() {
		return instance;
	}
}
