package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class UnregisterAreaExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			if (strings.length == 0) {
				AreaInformation areaInformation = AreaVerificator.verifyAreas(commandSender);
				if (areaInformation != null) {
					areaInformation.getEventHandler().clearPlacedBlocks();
					HandlerList.unregisterAll(areaInformation.getEventHandler());
					AreaRegister.getInstance().removeAreaInformations(areaInformation);
					DatabaseManager.dropArea(areaInformation);
					commandSender.sendMessage(ChatColor.GREEN + "The area " + areaInformation.getAreaName() + " was successfully unregistered.");
					areaInformation = null;
				} else {
					commandSender.sendMessage(ChatColor.RED + "You are not in a registered area!");
				}
			} else {
				commandSender.sendMessage(ChatColor.RED + "No arguments are expected!");
			}
		} else {
			commandSender.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
		}

		return false;
	}
}