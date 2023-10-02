package me.may.gem.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.DumperOptions;

public class FileUtil {
	/**
	 * 用路径读取Yml
	 * @param path
	 * @return
	 */
	public static FileConfiguration loadYml(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			}catch (IOException e) {
				System.out.println("错误:" + e.toString());
			}
	    }
		FileConfiguration YML = YamlConfiguration.loadConfiguration(file);
	    DumperOptions yamlOptions = null;
	    try {
	    	Field f = YamlConfiguration.class.getDeclaredField("yamlOptions");
	    	f.setAccessible(true);
	    	yamlOptions = new DumperOptions() {
	    		public void setAllowUnicode(boolean allowUnicode) {
	    			super.setAllowUnicode(false);
	    		}
	    		public void setLineBreak(DumperOptions.LineBreak lineBreak) {
	    			super.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
	    		}
	    	};
	    	yamlOptions.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
	    	f.set(YML, yamlOptions);
	    }catch (ReflectiveOperationException ex) {
	      System.out.println("错误:" + ex.toString());
	    }
		return YamlConfiguration.loadConfiguration(new File(path));
	}
	
	/**
	 * 保存Yml
	 * @param Filec
	 * @param file
	 */
	public static void saveYml(FileConfiguration Filec, File file) {
		try {
			Filec.save(file);
	    }catch (IOException e) {
	      System.out.println("错误:" + e.toString());
	    }
	}
	
	/**
	 * 读取Yml
	 * @param file
	 * @return YML
	 */
	public static FileConfiguration loadYml(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			}catch (IOException e) {
				System.out.println("错误:" + e.toString());
			}
	    }
	    FileConfiguration YML = YamlConfiguration.loadConfiguration(file);
	    DumperOptions yamlOptions = null;
	    try {
	    	Field f = YamlConfiguration.class.getDeclaredField("yamlOptions");
	    	f.setAccessible(true);
	    	yamlOptions = new DumperOptions() {
	    		public void setAllowUnicode(boolean allowUnicode) {
	    			super.setAllowUnicode(false);
	    		}
	    		public void setLineBreak(DumperOptions.LineBreak lineBreak) {
	    			super.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
	    		}
	    	};
	    	yamlOptions.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
	    	f.set(YML, yamlOptions);
	    }catch (ReflectiveOperationException ex) {
	      System.out.println("错误:" + ex.toString());
	    }
	    return YML;
	}
}
