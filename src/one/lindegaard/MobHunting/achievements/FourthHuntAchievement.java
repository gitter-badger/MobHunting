package one.lindegaard.MobHunting.achievements;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import one.lindegaard.MobHunting.ExtendedMobType;
import one.lindegaard.MobHunting.Messages;
import one.lindegaard.MobHunting.MobHunting;

public class FourthHuntAchievement implements ProgressAchievement {
	private ExtendedMobType mType;

	public FourthHuntAchievement(ExtendedMobType entity) {
		mType = entity;
	}

	@Override
	public String getName() {
		return Messages.getString("achievements.hunter.4.name", "mob",
				mType.getName());
	}

	@Override
	public String getID() {
		return "hunting-level4-" + mType.name().toLowerCase();
	}

	@Override
	public String getDescription() {
		return Messages.getString("achievements.hunter.4.description", "count",
				getMaxProgress(), "mob", mType.getName());
	}

	@Override
	public double getPrize() {
		return MobHunting.getConfigManager().specialHunter4;
	}

	@Override
	public int getMaxProgress() {
		return mType.getMax() * 10;
	}

	@Override
	public String inheritFrom() {
		return "hunting-level3-" + mType.name().toLowerCase();
	}
	
	@Override
	public String nextLevelId() {
		return "hunting-level5-" + mType.name().toLowerCase();
	}

	@Override
	public String getPrizeCmd() {
		return MobHunting.getConfigManager().specialHunter4Cmd;
	}

	@Override
	public String getPrizeCmdDescription() {
		return MobHunting.getConfigManager().specialHunter4CmdDesc;
	}
	
	@Override
	public ItemStack getSymbol() {
		return new ItemStack(Material.EMERALD);
	}
	
	@Override
	public ExtendedMobType getExtendedMobType() {
		return mType;
	}
}
