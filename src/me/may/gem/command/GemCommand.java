package me.may.gem.command;

import me.may.gem.Entry;
import me.may.gem.dao.GemDao;
import me.may.gem.dto.Gem;
import me.may.gem.gui.ComposeGui;
import me.may.gem.gui.GemGui;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gem")) {
            if (args.length == 0) {
                sender.sendMessage("§7==== §8[§6宝石§8] §7====");
                sender.sendMessage("§b/gem open §7打开宝石镶嵌面板");
                sender.sendMessage("§b/gem compose §7打开宝石合成面板");
                sender.sendMessage("§b/gem give [宝石名] §7获得该宝石名的宝石");
                sender.sendMessage("§b/gem reload §7重载插件");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("§f控制台禁止使用!");
                return true;
            }

            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("open")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item == null || item.getType().equals(Material.AIR)) {
                    sender.sendMessage("§8[§6宝石§8] §e>> §c手上的物品不能为空");
                    return true;
                }
                if (Entry.getInstance().isGem(item)) {
                    sender.sendMessage("§8[§6宝石§8] §e>> §c你不能拿着宝石进行操作");
                    return true;
                }
                if (!Entry.getInstance().canPunchItem(item)) {
                    sender.sendMessage("§8[§6宝石§8] §e>> §c该物品无法进行宝石打孔/镶嵌/摘除");
                    return true;
                }
                GemGui.openPunchGemGui(player);
            }

            if (args[0].equalsIgnoreCase("compose")) {
                ComposeGui.openComposeGui(player);
            }

            if (args[0].equalsIgnoreCase("give")) {
                if (args.length != 2) {
                    sender.sendMessage("§8[§6宝石§8] §e>> §c参数不正确");
                    return true;
                }
                if (!player.isOp()) {
                    sender.sendMessage("§8[§6宝石§8] §e>> §c权限不足!");
                    return true;
                }
                Gem gem = GemDao.getGem(args[1].replaceAll("&", "§"));
                if (gem == null) {
                    sender.sendMessage("§8[§6宝石§8] §e>> §c宝石不存在!");
                    return true;
                }
                player.getInventory().addItem(gem.getItem());
                sender.sendMessage("§8[§6宝石§8] §e>> §a获得成功");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!player.isOp()) {
                    sender.sendMessage("§8[§6宝石§8] §e>> §c权限不足!");
                    return true;
                }
                Entry.loadGems();
                Entry.getInstance().reloadConfig();
                Entry.invName = Entry.getInstance().getConfig().getString("Gui.Title").replaceAll("&", "§");
                Entry.getInstance().inlayTool = Entry.getInstance().getConfig().getString("Tools.InlayLore").replaceAll("&", "§");
                Entry.getInstance().takeTool = Entry.getInstance().getConfig().getString("Tools.TakeLore").replaceAll("&", "§");
                Entry.getInstance().inlayLore = Entry.getInstance().getConfig().getString("CheckItemLore.Inlay").replaceAll("&", "§");
                Entry.getInstance().punchLore = Entry.getInstance().getConfig().getString("CheckItemLore.Punch").replaceAll("&", "§");
                Entry.getInstance().composeInvName = Entry.getInstance().getConfig().getString("Gui.Compose.Title").replaceAll("&", "§");
                sender.sendMessage("§8[§6宝石§8] §e>> §a重载成功!");
            }
        }
        return false;
    }

}
