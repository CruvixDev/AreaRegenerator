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
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0.isOp()) {
			if (arg3.length == 3) {
				BlockSetEvent blockSetEvent = new BlockSetEvent();
				AreaInformation areaInformation = AreaRegister.getInstance().isInArea(new Coordinate(Integer.parseInt(arg3[0]), Integer.parseInt(arg3[2])));
				Location location = new Location(this.areaRegenerator.getServer().getWorld(areaInformation.getWorldName()), Double.parseDouble(arg3[0]), Double.parseDouble(arg3[1]), Double.parseDouble(arg3[2]));
				blockSetEvent.setBlockSetLocation(location);
				PluginManager pm = this.areaRegenerator.getServer().getPluginManager();
				pm.callEvent(blockSetEvent);
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
		}

		return false;
	}
}