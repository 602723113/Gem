package me.may.gem.listener;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.may.gem.Entry;
import me.may.gem.dao.GemDao;
import me.may.gem.dto.Gem;
import me.may.gem.utils.WeaponTypeUtils;

public class PunchListener implements Listener {

	private Map<String, Boolean> isUse = new HashMap<String, Boolean>();
	private List<Integer> tables = Arrays.asList(29, 30, 31, 32, 33);

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getTitle().equals(Entry.invName)) {
			if (!(e.getPlayer() instanceof Player)) {
				return;
			}
			Player player = (Player) e.getPlayer();
			if (isUse.containsKey(player.getName()) && !isUse.get(player.getName())) {
				if (inv.getItem(40) != null) {
					e.getPlayer().getInventory().addItem(e.getInventory().getItem(40));
				}

				ItemStack weapon = player.getInventory().getItemInMainHand();
				for (int i = 29; i <= 33; i++) {
					ItemStack item = inv.getItem(i);
					if (item != null) {
						//判断宝石
						if (Entry.getInstance().isGem(item)) {
							List<Gem> gems = Entry.getInstance().getItemGems(weapon);
							Gem gem = GemDao.getGem(item.getItemMeta().getDisplayName());
							// 判断是否武器上有该宝石
							if (!gems.isEmpty()) {
								if (!gems.contains(gem)) {
									e.getPlayer().getInventory().addItem(item);
									continue;
								}
								if (item.getAmount() != 1) {
									item.setAmount(item.getAmount() - 1);
									e.getPlayer().getInventory().addItem(item);
									continue;
								}
								switch (gems.size()) {
                                    case 0:
                                        e.getPlayer().getInventory().addItem(item);
                                        continue;
                                    case 1:
                                        if (i != 33) {
                                            e.getPlayer().getInventory().addItem(item);
                                            continue;
                                        }
                                    case 2:
                                        if (i != 33 && i != 32) {
                                            e.getPlayer().getInventory().addItem(item);
                                            continue;
                                        }
                                    case 3:
                                        if (i != 33 && i != 32 && i != 31) {
                                            e.getPlayer().getInventory().addItem(item);
                                            continue;
                                        }
                                    case 4:
                                        if (i != 33 && i != 32 && i != 31 && i != 30) {
                                            e.getPlayer().getInventory().addItem(item);
                                            continue;
                                        }
                                    case 5:
                                        if (i != 33 && i != 32 && i != 31 && i != 30 && i != 29) {
                                            e.getPlayer().getInventory().addItem(item);
                                            continue;
                                        }
                                    default:
                                        continue;
                                }
							} else {
								e.getPlayer().getInventory().addItem(item);
								continue;
							}
						}
						// 判断是否为工具
						if (Entry.getInstance().isTool(item)) {
							e.getPlayer().getInventory().addItem(item);
						}
					}
				}
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

		// PLACE_ALL 放入物品
		// PICKUP_ALL 左键拿出物品
		// PICKUP_HALF 右键拿出一半物品
		// PLACE_ONE 右键放入一个物品

		if (e.getInventory().getTitle().equals(Entry.invName)) {

			// 以下是防止刷物品
			if (e.getRawSlot() == 13 || e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
				e.setCancelled(true);
				return;
			}

			Player player = (Player) e.getWhoClicked();
			ItemStack weapon = player.getInventory().getItemInMainHand();
			ItemStack current = e.getCurrentItem();
			ItemStack cur = e.getCursor();

			isUse.put(player.getName(), false);

			// 判断点击的是否是空气
			if (current == null || current.getType() == Material.AIR) {
				return;
			}

			// 检查点击物品为放置无NBT数据的物品
			if (!current.hasItemMeta() || !current.getItemMeta().hasDisplayName()) {
				e.setCancelled(true);
				return;
			}

			// 检查点击的是否是打孔器
			if (Entry.getInstance().isInlayTool(current)) {
				e.setCancelled(false);
				return;
			}

			if (Entry.getInstance().isInlayTool(cur)) {
				e.setCancelled(false);
				return;
			}

			// 检查点击的是否是摘除符
			if (Entry.getInstance().isTakeTool(current)) {
				e.setCancelled(false);
				return;
			}
			if (Entry.getInstance().isTakeTool(cur)) {
				e.setCancelled(false);
				return;
			}

			/* 宝石摘除 */
			if (e.getRawSlot() == 29 || e.getRawSlot() == 30 || e.getRawSlot() == 31 || e.getRawSlot() == 32
					|| e.getRawSlot() == 33) {
				e.setCancelled(true);
				ItemStack item = e.getInventory().getItem(e.getRawSlot()); // 物品引用
				if (item == null || !Entry.getInstance().isGem(item)) {
					e.setCancelled(true);
					return;
				}

				Gem gem = GemDao.getGem(item.getItemMeta().getDisplayName());
				// 判断是否武器上有该宝石
				List<Gem> gems = Entry.getInstance().getItemGems(weapon);
				if (!gems.isEmpty()) {
					if (!gems.contains(gem)) {
						e.setCancelled(false);
						return;
					}
				} else {
					e.setCancelled(false);
					return;
				}

				ItemStack takeTool = e.getInventory().getItem(40);

				if (tables.contains(e.getRawSlot()) && takeTool == null) {
					//29 30 31 32 33
					//5  4  3  2  1
					switch (gems.size()) {
						case 0:
							e.setCancelled(true);
							return;
						case 1:
							if (e.getRawSlot() != 33) {
								e.setCancelled(false);
								return;
							} else {
								e.setCancelled(true);
								return;
							}
						case 2:
							if (e.getRawSlot() != 33 && e.getRawSlot() != 32) {
								e.setCancelled(false);
								return;
							} else {
								e.setCancelled(true);
								return;
							}
						case 3:
							if (e.getRawSlot() != 33 && e.getRawSlot() != 32 && e.getRawSlot() != 31) {
								e.setCancelled(false);
								return;
							} else {
								e.setCancelled(true);
								return;
							}
						case 4:
							if (e.getRawSlot() != 33 && e.getRawSlot() != 32 && e.getRawSlot() != 31 && e.getRawSlot() != 30) {
								e.setCancelled(false);
								return;
							} else {
								e.setCancelled(true);
								return;
							}
						case 5:
							if (e.getRawSlot() != 33 && e.getRawSlot() != 32 && e.getRawSlot() != 31 && e.getRawSlot() != 30 && e.getRawSlot() != 29) {
								e.setCancelled(false);
								return;
							} else {
								e.setCancelled(true);
								return;
							}
						default:
							e.setCancelled(true);
							return;
					}
				}

				if (item == null || !Entry.getInstance().isTakeTool(takeTool)) {
					player.sendMessage("§8[§6宝石§8] §e>> §7你放入的物品不是宝石摘除符!");
					e.setCancelled(true);
					return;
				}

				if (takeTool.getAmount() != 1) {
					player.sendMessage("§8[§6宝石§8] §e>> §7请拆分为一个物品");
					e.setCancelled(true);
					return;
				}

				// 已经判断好item是宝石 takeTool是宝石摘除符
				ItemMeta im = weapon.getItemMeta();
				List<String> lore = im.getLore();
				for (int i = 0; i < lore.size(); i++) {
					if (lore.get(i).indexOf(item.getItemMeta().getDisplayName()) != -1) {
						lore.set(i, Entry.getInstance().punchLore);
						im.setLore(lore);
						break;
					}
				}
				if (Entry.getInstance().isBind(weapon)) {
					ItemMeta meta = item.getItemMeta();
					List<String> gemLore = meta.getLore();
					gemLore.add("§4已绑定");
					meta.setLore(gemLore);
					item.setItemMeta(meta);
				}
				weapon.setItemMeta(im);
				player.getInventory().addItem(item);
				player.sendMessage("§8[§6宝石§8] §e>> §a摘除成功!");
				isUse.put(player.getName(), true);
				player.closeInventory();
				return;
			}

			// 检查点击的是否是宝石
			if (Entry.getInstance().isGem(current)) {
				e.setCancelled(false);
				return;
			}
			if (Entry.getInstance().isGem(cur)) {
				e.setCancelled(false);
				return;
			}

			if (e.getRawSlot() == 43) { // 点击运行
				ItemStack item = e.getInventory().getItem(40); // 物品引用
				if (item == null) {
					e.setCancelled(true);
					return;
				}

				// 判断是为打孔器
				if (Entry.getInstance().isInlayTool(item)) {
					if (item.getAmount() != 1) {
						player.sendMessage("§8[§6宝石§8] §e>> §7请拆分为一个物品");
						e.setCancelled(true);
						return;
					}
					int closeHole = Entry.getInstance().getItemCloseHoleAmount(weapon);
					if (closeHole == 0) {
						player.sendMessage("§8[§6宝石§8] §e>> §7无法再打更多的孔!");
					} else {
						ItemMeta im = weapon.getItemMeta();
						List<String> lore = im.getLore();
						for (int i = 0; i < lore.size(); i++) {
							if (lore.get(i).indexOf(Entry.getInstance().inlayLore) != -1) {
								lore.set(i, Entry.getInstance().punchLore);
								e.getInventory().setItem(40, new ItemStack(Material.AIR));
								break;
							}
						}
						im.setLore(lore);
						weapon.setItemMeta(im);
						player.sendMessage("§8[§6宝石§8] §e>> §a打孔成功!");
						isUse.put(player.getName(), true);
						player.closeInventory();
					}
					e.setCancelled(true);
					return;
				}

				// 判断是为宝石
				if (Entry.getInstance().isGem(item)) {
					// 搜索宝石的对象
					Gem gem = GemDao.getGem(item.getItemMeta().getDisplayName());

					// NPE检查
					if (item == null || gem == null) {
						e.setCancelled(true);
						player.getInventory().addItem(item);
						player.sendMessage("§8[§6宝石§8] §e>> §c发生错误!请检查放入的物品是否正确!");
						isUse.put(player.getName(), true);
						player.closeInventory();
						return;
					}

					// 判断有无相同宝石
					List<Gem> gems = Entry.getInstance().getItemGems(weapon);
					if (!gems.isEmpty()) {
						for (int i = 0; i < gems.size(); i++) {
							if (gems.get(i).getName().equalsIgnoreCase(gem.getName())
									|| gems.get(i).getType().equalsIgnoreCase(gem.getType())) {
								e.setCancelled(true);
								player.getInventory().addItem(item);
								player.sendMessage("§8[§6宝石§8] §e>> §7存在相同或相同类型的宝石!");
								isUse.put(player.getName(), true);
								player.closeInventory();
								return;
							}
						}
					}

					// 检查宝石只能安装的位置是否是 特定的装备
					if (!WeaponTypeUtils.checkWeaponGemIsSameType(player.getInventory().getItemInMainHand(), gem)) {
						player.sendMessage("§8[§6宝石§8] §e>> §7该物品无法镶嵌该宝石!");
						player.getInventory().addItem(item);
						e.setCancelled(true);
						isUse.put(player.getName(), true);
						player.closeInventory();
						return;
					}

					/* 开始镶嵌 */
					// 判断有无空位
					// if (Entry.getInstance().getItemGems(item).size() == 4
					// && Entry.getInstance().getItemOpenHoleAmount(item) == 0
					// && Entry.getInstance().getItemCloseHoleAmount(item) == 4) {
					// e.setCancelled(true);
					// player.getInventory().addItem(item);
					// player.sendMessage("§8[§6宝石§8] §e>> §c已无法镶嵌再多的宝石");
					// player.closeInventory();
					// return;
					// }

					if (Entry.getInstance().getItemOpenHoleAmount(weapon) == 0
						|| Entry.getInstance().getItemGems(weapon).size() == 5
						|| Entry.getInstance().getItemCloseHoleAmount(weapon) == 5) {
						e.setCancelled(true);
						player.getInventory().addItem(item);
						player.sendMessage("§8[§6宝石§8] §e>> §c已无法镶嵌再多的宝石");
						isUse.put(player.getName(), true);
						player.closeInventory();
						return;
					}

					ItemMeta im = weapon.getItemMeta();
					List<String> lore = new ArrayList<String>();
					if (im.hasLore()) {
						lore.addAll(im.getLore()); // 存入原来的Lore
					}

					if (lore.isEmpty()) {
						e.setCancelled(true);
						player.getInventory().addItem(item);
						player.sendMessage("§8[§6宝石§8] §e>> §c该装备未打孔或不可镶嵌宝石!");
						isUse.put(player.getName(), true);
						player.closeInventory();
						return;
					}
					for (int i = 0; i < lore.size(); i++) {
						// 如果遍历到了空镶嵌的位置则进行镶嵌
						if (lore.get(i).indexOf(Entry.getInstance().punchLore) != -1) {
							lore.set(i, gem.getName());
							break;
						}
					}
					im.setLore(lore);
					weapon.setItemMeta(im);
					player.sendMessage("§8[§6宝石§8] §e>> §a镶嵌成功!");
					isUse.put(player.getName(), true);
					player.closeInventory();
				}
			}
			e.setCancelled(true);
		}
	}
}
