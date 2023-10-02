package me.may.gem.utils;

import me.may.gem.Entry;
import me.may.gem.dto.Gem;
import me.may.gem.dto.PunchWeaponType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WeaponTypeUtils {


    @SuppressWarnings("deprecation")
    public static boolean checkWeaponGemIsSameType(ItemStack item, Gem gem) {
        if (gem.getCanPunchType().contains(PunchWeaponType.ALL)) {
            List<String> list = Entry.getInstance().getConfig().getStringList("WeaponType.ALL");
            for (int i = 0; i < list.size(); i++) {
                String temp = list.get(i);
                String[] idAndData = temp.split(":");
                int id = Integer.parseInt(idAndData[0]);
                int data = Integer.parseInt(idAndData[1]);
                if (item.getTypeId() == id && item.getDurability() == data) {
                    return true;
                }
                continue;
            }
            return false;
        }
        for (int i = 0; i < gem.getCanPunchType().size(); i++) {
            List<String> list = Entry.getInstance().getConfig().getStringList("WeaponType." + gem.getCanPunchType().get(i));
            for (int j = 0; j < list.size(); j++) {
                String temp = list.get(j);
                String[] idAndData = temp.split(":");
                int id = Integer.parseInt(idAndData[0]);
                int data = Integer.parseInt(idAndData[1]);
                if (item.getTypeId() == id && item.getDurability() == data) {
                    return true;
                }
            }
            continue;
        }
        return false;
    }

}
