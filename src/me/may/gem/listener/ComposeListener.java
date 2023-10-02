package me.may.gem.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import me.may.gem.Entry;
import me.may.gem.dao.GemDao;
import me.may.gem.dto.Gem;

public class ComposeListener implements Listener {

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getTitle().equals(Entry.getInstance().composeInvName)) {
			for (int i = 11; i < 16; i++) {
				if (inv.getItem(i) != null) {
					e.getPlayer().getInventory().addItem(new ItemStack[] { e.getInventory().getItem(i) });
				}
			}
			if (inv.getItem(40) != null) {
				e.getPlayer().getInventory().addItem(new ItemStack[] { e.getInventory().getItem(40) });
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getRawSlot() < 0) {
			e.setCancelled(true);
			return;
		}
		if (!(e.getWhoClicked() instanceof Player)) {
			e.setCancelled(true);
			return;
		}

		if (e.getInventory().getTitle().equals(Entry.getInstance().composeInvName)) {
			
			Player player = (Player) e.getWhoClicked();
			Inventory inv = e.getInventory();
			ItemStack current = e.getCurrentItem();
			ItemStack cur = e.getCursor();

			// 以下是防止刷物品
			if (e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
				e.setCancelled(true);
				return;
			}

			//判断点击的是否是空气
			if(current == null || current.getType() == Material.AIR) {
				return;
			}
			
			//检查点击物品为放置无NBT数据的物品
			if (!current.hasItemMeta() || !current.getItemMeta().hasDisplayName()) {
				e.setCancelled(true);
				return;
			}
			
			//检查点击的是否是宝石
			if (Entry.getInstance().isGem(current)) {
				e.setCancelled(false);
				return;
			}
			if (Entry.getInstance().isGem(cur)) {
				e.setCancelled(false);
				return;
			}
			
			if (e.getRawSlot() == 31) {
				// 11 12 13 14 15
				// 以下为循环判断物品
				List<ItemStack> items = Lists.newArrayList();
				int i = 11;
				while (i <= 15) {
					ItemStack item = inv.getItem(i);
					if (item == null || item.getType().equals(Material.AIR)) {
						e.setCancelled(true);
						return;
					}
					items.add(item);
					i++;
				}

				// 判断是否为五个宝石
				if (items.size() != 5) {
					e.setCancelled(true);
					player.sendMessage("§8[§6宝石§8] §e>> §c请放入五个宝石!");
					return;
				}
				for (int j = 0; j < items.size(); j++) {
					ItemStack itemStack = items.get(j);
					if (!Entry.getInstance().isGem(itemStack)) {
						e.setCancelled(true);
						player.sendMessage("§8[§6宝石§8] §e>> §c请放入正确的物品!");
						return;
					}
				}
				
				ItemStack A = inv.getItem(11);
				ItemStack B = inv.getItem(12);
				ItemStack C = inv.getItem(13);
				ItemStack D = inv.getItem(14);
				ItemStack E = inv.getItem(15);
				Gem Agem = GemDao.getGem(A.getItemMeta().getDisplayName());
				Gem Bgem = GemDao.getGem(B.getItemMeta().getDisplayName());
				Gem Cgem = GemDao.getGem(C.getItemMeta().getDisplayName());
				Gem Dgem = GemDao.getGem(D.getItemMeta().getDisplayName());
				Gem Egem = GemDao.getGem(E.getItemMeta().getDisplayName());
				String nextLevelGem = Agem.getNextLevelGem();
				if (!Agem.isCanUpgrade() || !Bgem.isCanUpgrade() || !Cgem.isCanUpgrade() || !Dgem.isCanUpgrade() || !Egem.isCanUpgrade()) {
					player.closeInventory();
					player.sendMessage("§8[§6宝石§8] §e>> §c存在无法升级的宝石!");
					return;
				}
				if (!Bgem.equals(Agem) || !Cgem.equals(Agem) || !Dgem.equals(Agem) || !Egem.equals(Agem)) {
					player.closeInventory();
					player.sendMessage("§8[§6宝石§8] §e>> §c存在不相同的宝石!");
					return;
				}
				List<ItemStack> gems = Arrays.asList(A, B, C, D, E);
				boolean isBind = false;
				for (int j = 0; j < gems.size(); j++) {
					if (Entry.getInstance().isBind(gems.get(j))) {
						isBind = true;
						break;
					}
				}
				
				//判断是否已绑定
				if (isBind) {
					ItemStack next = GemDao.getGem(nextLevelGem).getItem();
					ItemMeta im = next.getItemMeta();
					List<String> lore = im.getLore();
					lore.add("§4已绑定");
					im.setLore(lore);
					next.setItemMeta(im);
					inv.setItem(11, null);
					inv.setItem(12, null);
					inv.setItem(13, null);
					inv.setItem(14, null);
					inv.setItem(15, null);
					inv.setItem(40, next);
					player.updateInventory();
					player.sendMessage("§8[§6宝石§8] §e>> §a合成成功!");
				}else {
					inv.setItem(11, null);
					inv.setItem(12, null);
					inv.setItem(13, null);
					inv.setItem(14, null);
					inv.setItem(15, null);
					inv.setItem(40, GemDao.getGem(nextLevelGem).getItem());
					player.sendMessage("§8[§6宝石§8] §e>> §a合成成功!");
				}
			}
			e.setCancelled(true);
		}
	}
}
