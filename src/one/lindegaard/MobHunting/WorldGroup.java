package one.lindegaard.MobHunting;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @author christian
 *
 */
public class WorldGroup {

	private static File file = new File(MobHunting.getInstance().getDataFolder(), "worldGroups.yml");
	private static YamlConfiguration config = new YamlConfiguration();
	private HashMap<String, List<String>> worldGroups = new HashMap<String, List<String>>();

	public WorldGroup() {
		if (worldGroups.isEmpty()) {
			worldGroups.put("DefaultGroup", Arrays.asList("world", "world_nether", "world_the_end"));
			worldGroups.put("CreativeGroup", Arrays.asList("creative"));
			worldGroups.put("SurvivalGroup", Arrays.asList("survival"));
		}
	}

	public void add(String world) {
		List<String> list = worldGroups.get("Default");
		if (!list.contains(world))
			list.add(world);
		worldGroups.put("Default", list);
	}

	public void add(String world, String worldGroup) {
		List<String> list = worldGroups.get(worldGroup);
		if (!list.contains(world))
			list.add(world);
		worldGroups.put(worldGroup, list);
	}

	public List<String> getWorlds() {
		return worldGroups.get("Default");
	}

	public List<String> getWorlds(String worldGroup) {
		return worldGroups.get(worldGroup);
	}

	public String getWorldGroup(String world) {
		for (String worldGroup : worldGroups.keySet()) {
			if (worldGroup.contains(world))
				return worldGroup;
		}
		return "Default";
	}

	public String getCurrentWorldGroup(Player player) {
		return getWorldGroup(player.getWorld().getName());
	}
	
	// ***************************************************************
	// write & read
	// ***************************************************************
	public void save() {
		try {
			config.options()
					.header("----------------------------------------------------------"
							+ "\nWorldGroups. New world are added in the Default Group"
							+ "\n----------------------------------------------------------");
			Messages.debug("Saving worldGroups");
			ConfigurationSection section = config.createSection("WorldGroups");
			section.set("WorldGroups", worldGroups);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		if (!file.exists())
			return;
		Messages.debug("Loading WorldGroups");
		try {
			config.load(file);
			ConfigurationSection section = config.getConfigurationSection("WorldGroups");
			if (section != null)
				section.get("WorldGroups", worldGroups);
		} catch (IllegalStateException | InvalidConfigurationException | IOException e) {
			e.printStackTrace();
		}

	}

}
