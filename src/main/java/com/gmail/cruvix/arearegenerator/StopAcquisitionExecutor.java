package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class StopAcquisitionExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			AreaInformation areaInformation = AreaVerificator.verifyAreas(commandSender);
			if (areaInformation != null) {
				areaInformation.getEventHandler().clearPlacedBlocks();
				HandlerList.unregisterAll(areaInformation.getEventHandler());
				commandSender.sendMessage(ChatColor.GREEN + "Acquisition stopped.");
			} else {
				commandSender.sendMessage(ChatColor.RED + "You are not in a registered Area!");
			}
		} else {
			commandSender.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
		}

		return false;
	}
}