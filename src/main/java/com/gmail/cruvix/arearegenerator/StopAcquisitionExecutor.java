package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class StopAcquisitionExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0.isOp()) {
			AreaInformation areaInformation = AreaVerificator.verifyAreas(arg0);
			if (areaInformation != null) {
				areaInformation.getEventHandler().clearPlacedBlocks();
				HandlerList.unregisterAll(areaInformation.getEventHandler());
				arg0.sendMessage(ChatColor.GREEN + "Acquisition stopped.");
			} else {
				arg0.sendMessage(ChatColor.RED + "You are not in a registered Area!");
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
		}

		return false;
	}
}