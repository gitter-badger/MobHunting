package au.com.mineauz.MobHunting.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import au.com.mineauz.MobHunting.ExtendedMobType;
import au.com.mineauz.MobHunting.Messages;
import au.com.mineauz.MobHunting.MobHunting;
import au.com.mineauz.MobHunting.storage.DataCallback;
import au.com.mineauz.MobHunting.storage.StatStore;
import au.com.mineauz.MobHunting.storage.TimePeriod;

public class TopCommand implements ICommand
{
	public TopCommand()
	{
	}
	
	@Override
	public String getName()
	{
		return "top";
	}

	@Override
	public String[] getAliases()
	{
		return null;
	}

	@Override
	public String getPermission()
	{
		return "mobhunting.top";
	}

	@Override
	public String[] getUsageString( String label, CommandSender sender )
	{
		return new String[] { label + ChatColor.GOLD + "<type> (kill|assist|both) (day|week|month|year|alltime)" };
	}

	@Override
	public String getDescription()
	{
		return Messages.getString("mobhunting.commands.top.description");
	}

	@Override
	public boolean canBeConsole()
	{
		return true;
	}

	@Override
	public boolean canBeCommandBlock()
	{
		return true;
	}
	
	private String[] generateTypes()
	{
		String[] types = new String[ExtendedMobType.values().length * 2 + 1];
		for(int i = 0; i < ExtendedMobType.values().length; ++i)
			types[i] = Messages.getString("mob." + ExtendedMobType.values()[i].name() + ".name").replaceAll(" ", "_");
		
		types[types.length -1] = Messages.getString("mobhunting.commands.top.total");
		
		return types;
	}
	
	private String[] generateExtTypes()
	{
		String[] types = new String[3];
		types[0] = Messages.getString("stats.assist");
		types[1] = Messages.getString("stats.kill");
		types[2] = Messages.getString("stats.both");
		
		return types;
	}
	
	private String[] generatePeriods()
	{
		String[] periods = new String[5];
		periods[0] = Messages.getString("stats.day");
		periods[1] = Messages.getString("stats.week");
		periods[2] = Messages.getString("stats.month");
		periods[3] = Messages.getString("stats.year");
		periods[4] = Messages.getString("stats.alltime");
		
		return periods;
	}

	@Override
	public boolean onCommand( CommandSender sender, String label, String[] args )
	{
		if(args.length != 3)
			return false;
		
		String[] types = generateTypes();
		ExtendedMobType type = null;
		// Verify it
		boolean ok = false;
		for(int i = 0; i < types.length; ++i)
		{
			if(types[i].equalsIgnoreCase(args[0]))
			{
				if(i != types.length)
					type = ExtendedMobType.values()[i];
				
				ok = true;
				break;
			}
		}
		
		if(!ok)
		{
			sender.sendMessage(ChatColor.RED + Messages.getString("mobhunting.commands.top.unknown-stat", "stat", ChatColor.YELLOW + args[0] + ChatColor.RED));
			return false;
		}
		
		// Check the time period
		String[] periods = generatePeriods();
		TimePeriod period = null;
		
		ok = false;
		for(int i = 0; i < periods.length; ++i)
		{
			if(periods[i].equalsIgnoreCase(args[2]))
			{
				period = TimePeriod.values()[i];
				
				ok = true;
				break;
			}
		}
		
		if(!ok)
		{
			sender.sendMessage(ChatColor.RED + Messages.getString("mobhunting.commands.top.unknown-period", "period", ChatColor.YELLOW + args[2] + ChatColor.RED));
			return false;
		}
		
		String[] extendedTypes = generateExtTypes();
		
		
		LeaderboardDisplay callback = new LeaderboardDisplay(sender);
		
		
		if(args[1].equalsIgnoreCase(extendedTypes[0]))
			MobHunting.instance.getDataStore().requestStats(type, true, false, period, callback);
		else if(args[1].equalsIgnoreCase(extendedTypes[1]))
			MobHunting.instance.getDataStore().requestStats(type, false, true, period, callback);
		else if(args[1].equalsIgnoreCase(extendedTypes[2]))
			MobHunting.instance.getDataStore().requestStats(type, true, true, period, callback);
		else
			return false;
		
		return true;
	}

	@Override
	public List<String> onTabComplete( CommandSender sender, String label, String[] args )
	{
		ArrayList<String> items;
		
		if(args.length == 1)
			items = new ArrayList<String>(Arrays.asList(generateTypes()));
		else if(args.length == 2)
			items = new ArrayList<String>(Arrays.asList(generateExtTypes()));
		else if(args.length == 3)
			items = new ArrayList<String>(Arrays.asList(generatePeriods()));
		else
			return null;

		if(!args[args.length-1].trim().isEmpty())
		{
			String match = args[0].trim().toLowerCase();
			
			Iterator<String> it = items.iterator();
			while(it.hasNext())
			{
				String name = it.next();
				if(!name.toLowerCase().startsWith(match))
					it.remove();
			}
		}
		return items;
	}

	private static class LeaderboardDisplay implements DataCallback<List<StatStore>>
	{
		private CommandSender mSender;
		
		public LeaderboardDisplay(CommandSender sender)
		{
			mSender = sender;
		}
		
		@Override
		public void onCompleted( List<StatStore> data )
		{
			
		}

		@Override
		public void onError( Throwable error )
		{
			mSender.sendMessage(ChatColor.RED + Messages.getString("mobhunting.commands.base.error"));
			error.printStackTrace();
		}
		
	}
}