package one.lindegaard.MobHunting.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import one.lindegaard.MobHunting.HuntData;
import one.lindegaard.MobHunting.Messages;
import one.lindegaard.MobHunting.MobHunting;

public class ClearGrindingCommand implements ICommand {

	@Override
	public String getName() {
		return "cleargrinding"; 
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getPermission() {
		return "mobhunting.cleargrinding"; 
	}

	@Override
	public String[] getUsageString(String label, CommandSender sender) {
		return new String[] { label };
	}

	@Override
	public String getDescription() {
		return Messages.getString("mobhunting.commands.cleargrinding.description"); 
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public boolean canBeCommandBlock() {
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		if (args.length != 0)
			return false;

		Location loc = ((Player) sender).getLocation();
		MobHunting.getAreaManager().clearGrindingArea(loc);

		for (Player player : Bukkit.getOnlinePlayers()) {
			HuntData data = MobHunting.getMobHuntingManager().getHuntData(player);
			data.clearGrindingArea(loc);
		}

		sender.sendMessage(ChatColor.GREEN + Messages.getString("mobhunting.commands.cleargrinding.done"));

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
		return null;
	}

}
