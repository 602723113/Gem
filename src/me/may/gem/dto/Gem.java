package me.may.gem.dto;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.may.gem.utils.NBTUtil;

public class Gem {

	private String name;
	private String type;
	private int material;
	private short data;
	private boolean canUpgrade;
	private String nextLevelGem;
	private Map<GemType, Double> gemAddtionData;
	private List<PunchWeaponType> canPunchType;
	private List<GemType> types;
	private List<String> descripition;

	public Gem(String name, String type, int material, short data, Map<GemType, Double> gemAddtionData,
			List<PunchWeaponType> canPunchType, List<GemType> types, List<String> descripition, boolean canUpgrade,
			String nextLevelGem) {
		this.name = name;
		this.type = type;
		this.material = material;
		this.data = data;
		this.gemAddtionData = gemAddtionData;
		this.canPunchType = canPunchType;
		this.types = types;
		this.descripition = descripition;
		this.canUpgrade = canUpgrade;
		this.nextLevelGem = nextLevelGem;
	}

	public Gem(String name, String type, int material, short data, Map<GemType, Double> gemAddtionData,
			List<PunchWeaponType> canPunchType, List<GemType> types, List<String> descripition) {
		this.name = name;
		this.type = type;
		this.material = material;
		this.data = data;
		this.gemAddtionData = gemAddtionData;
		this.canPunchType = canPunchType;
		this.types = types;
		this.descripition = descripition;
		this.canUpgrade = false;
		this.nextLevelGem = name;
	}

	@Override
	public String toString() {
		return "Gem [name=" + name + ", type=" + type + ", material=" + material + ", data=" + data + ", canUpgrade="
				+ canUpgrade + ", nextLevelGem=" + nextLevelGem + ", gemAddtionData=" + gemAddtionData
				+ ", canPunchType=" + canPunchType + ", types=" + types + ", descripition=" + descripition + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gem other = (Gem) obj;

		if (material != other.material)
			return false;

		if (data != other.data)
			return false;

		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaterial() {
		return material;
	}

	public void setMaterial(int material) {
		this.material = material;
	}

	public short getData() {
		return data;
	}

	public void setData(short data) {
		this.data = data;
	}

	public boolean isCanUpgrade() {
		return canUpgrade;
	}

	public void setCanUpgrade(boolean canUpgrade) {
		this.canUpgrade = canUpgrade;
	}

	public String getNextLevelGem() {
		return nextLevelGem;
	}

	public void setNextLevelGem(String nextLevelGem) {
		this.nextLevelGem = nextLevelGem;
	}

	public Map<GemType, Double> getGemAddtionData() {
		return gemAddtionData;
	}

	public void setGemAddtionData(Map<GemType, Double> gemAddtionData) {
		this.gemAddtionData = gemAddtionData;
	}

	public List<PunchWeaponType> getCanPunchType() {
		return canPunchType;
	}

	public void setCanPunchType(List<PunchWeaponType> canPunchType) {
		this.canPunchType = canPunchType;
	}

	public List<GemType> getTypes() {
		return types;
	}

	public void setTypes(List<GemType> types) {
		this.types = types;
	}

	public List<String> getDescripition() {
		return descripition;
	}

	public void setDescripition(List<String> descripition) {
		this.descripition = descripition;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.getMaterial(material), 1, data);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name.replaceAll("&", "§"));
		for (int i = 0; i < descripition.size(); i++) {
			descripition.set(i, descripition.get(i).replaceAll("&", "§"));
		}
		im.setLore(descripition);
		is.setItemMeta(im);
		is = NBTUtil.setUnbreakable(is, true);
		return is;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
