package me.may.gem.utils;

import java.lang.reflect.Method;

import org.bukkit.inventory.ItemStack;

public class NBTUtil {

	private static Method setTag;
	private static Method set;
	
	static {
		try {
			setTag = NMSUtil.getNMSClass("ItemStack").getMethod("setTag", NMSUtil.getNMSClass("NBTTagCompound"));
			set = NMSUtil.getNMSClass("NBTTagCompound").getMethod("set", new Class[] { String.class, NMSUtil.getNMSClass("NBTBase") } );
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static Object nbtTagByte(byte num) {
		try {
			return NMSUtil.getNMSClass("NBTTagByte").getConstructor(Byte.TYPE).newInstance(num);
		} catch (Exception e) {
			System.out.println("错误: " + e.getMessage());
		}
		return null;
	}

	public static Object getItemNBT(ItemStack is) {
		Object nmsItem = NMSUtil.getNMSItem(is);
		if (nmsItem == null) {
			return null;
		}
		try {
			return nmsItem.getClass().getMethod("getTag").invoke(nmsItem) != null
					? nmsItem.getClass().getMethod("getTag").invoke(nmsItem)
					: NMSUtil.getNMSClass("NBTTagCompound").newInstance();
		} catch (Exception e) {
			System.out.println("错误: " + e.getMessage());
		}
		return null;
	}

	public static ItemStack setUnbreakable(ItemStack is, boolean unbreak) {
		
		Object nmsItem = NMSUtil.getNMSItem(is);
		Object itemNbt = getItemNBT(is);
		ItemStack item = is;
		try {
			//NPE检查
			if (set == null) {
				set = NMSUtil.getNMSClass("NBTTagCompound").getMethod("set", new Class[] { String.class, NMSUtil.getNMSClass("NBTBase") } );
			}
			set.invoke(itemNbt, new Object[] { "Unbreakable", nbtTagByte((byte) (unbreak ? 1 : 0)) } );
			
			//NPE检查
			if (setTag == null) {
				setTag = NMSUtil.getNMSClass("ItemStack").getMethod("setTag", NMSUtil.getNMSClass("NBTTagCompound"));
			}
			
			setTag.invoke(nmsItem, itemNbt);
			item = (ItemStack) NMSUtil.getOBCClass("inventory.CraftItemStack").getMethod("asCraftMirror", NMSUtil.getNMSClass("ItemStack")).invoke(nmsItem, nmsItem);
			System.out.println("Yep");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
}
