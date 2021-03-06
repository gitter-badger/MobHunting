package one.lindegaard.MobHunting.modifier;

import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import one.lindegaard.MobHunting.DamageInformation;
import one.lindegaard.MobHunting.HuntData;
import one.lindegaard.MobHunting.Messages;
import one.lindegaard.MobHunting.MobHunting;

public class RankBonus implements IModifier {

	@Override
	public String getName() {
		return ChatColor.GRAY + Messages.getString("bonus.rank.name");
	}

	@Override
	public double getMultiplier(LivingEntity deadEntity, Player killer, HuntData data, DamageInformation extraInfo,
			EntityDamageByEntityEvent lastDamageCause) {
		if (!killer.isOp()) {
			Iterator<Entry<String, String>> ranks = MobHunting.getConfigManager().rankMultiplier.entrySet().iterator();
			double mul = 0;
			while (ranks.hasNext()) {
				Entry<String, String> rank = ranks.next();
				if (!rank.getKey().equalsIgnoreCase("mobhunting")
						&& !rank.getKey().equalsIgnoreCase("mobhunting.multiplier")) {
					if (killer.hasPermission(rank.getKey())) {
						mul = (Double.valueOf(rank.getValue()) > mul) ? Double.valueOf(rank.getValue()) : mul;
					}
				}
			}
			mul = (mul == 0) ? 1 : mul;
			return mul;
		} else if (MobHunting.getConfigManager().rankMultiplier.containsKey("mobhunting.multiplier.op"))
			return Double.valueOf(MobHunting.getConfigManager().rankMultiplier.get("mobhunting.multiplier.op"));
		return 1;
	}

	@Override
	public boolean doesApply(LivingEntity deadEntity, Player killer, HuntData data, DamageInformation extraInfo,
			EntityDamageByEntityEvent lastDamageCause) {
		if (!killer.isOp()) {
			Iterator<Entry<String, String>> ranks = MobHunting.getConfigManager().rankMultiplier.entrySet().iterator();
			boolean hasRank = false;
			while (ranks.hasNext()) {
				Entry<String, String> rank = ranks.next();
				if (!rank.getKey().equalsIgnoreCase("mobhunting")
						&& !rank.getKey().equalsIgnoreCase("mobhunting.multiplier")) {
					if (killer.hasPermission(rank.getKey())) {
						//Messages.debug("RankMultiplier Key=%s Value=%s", rank.getKey(), rank.getValue());
						hasRank = true;
					}
				}
			}
			return hasRank;
		} else if (MobHunting.getConfigManager().rankMultiplier.containsKey("mobhunting.multiplier.op")) {
			Messages.debug("RankMultiplier Key=mobhunting.multiplier.op Value=%s Player is OP",
					MobHunting.getConfigManager().rankMultiplier.get("mobhunting.multiplier.op"));
			return true;
		}
		Messages.debug("%s has no Rank Multiplier", killer.getName());
		return false;
	}
}
