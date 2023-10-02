package me.may.gem.dao;

import java.util.ArrayList;
import java.util.List;

import me.may.gem.dto.Gem;

public class GemDao {
	
	public static List<Gem> gems = new ArrayList<Gem>();
	
	public static List<String> gemNames = new ArrayList<String>();
	
	/**
	 * 使用宝石名获取宝石对象[如果宝石不存在则返回Null]
	 * @param gemName 宝石名
	 * @return {@link Gem}
	 */
	public static Gem getGem(String gemName) {
		if (gemName.isEmpty()) {
			return null;
		}
		if(!gemNames.contains(gemName)) {
			return null;
		}
		for (Gem gem : gems) {
			if (gem.getName().equals(gemName)) {
				return gem;
			}
		}
		return null;
	}
}
