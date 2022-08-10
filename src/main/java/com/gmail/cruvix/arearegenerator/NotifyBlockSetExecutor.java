package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

public class NotifyBlockSetExecutor implements CommandExecutor {
	private AreaRegenerator areaRegenerator;

	public NotifyBlockSetExecutor(AreaRegenerator areaRegenerator) {
		this.areaRegenerator = areaRegenerator;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			if (strings.length == 3) {
				BlockSetEvent blockSetEvent = new BlockSetEvent();
				AreaInformation areaInformation = AreaRegister.getInstance().isInArea(new Coordinate(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),Integer.parseInt(strings[2])));
				Location location = new Location(this.areaRegenerator.getServer().getWorld(areaInformation.getWorldName()), Double.parseDouble(strings[0]), Double.parseDouble(strings[1]), Double.parseDouble(strings[2]));
				blockSetEvent.setBlockSetLocation(location);
				PluginManager pm = this.areaRegenerator.getServer().getPluginManager();
				pm.callEvent(blockSetEvent);
			}
		} else {
			commandSender.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
		}

		return false;
	}
}