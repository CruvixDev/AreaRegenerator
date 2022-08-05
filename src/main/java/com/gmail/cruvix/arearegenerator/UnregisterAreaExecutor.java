package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class UnregisterAreaExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0.isOp()) {
			if (arg3.length == 0) {
				AreaInformation areaInformation = AreaVerificator.verifyAreas(arg0);
				if (areaInformation != null) {
					areaInformation.getEventHandler().clearPlacedBlocks();
					HandlerList.unregisterAll(areaInformation.getEventHandler());
					AreaRegister.getInstance().removeAreaInformations(areaInformation);
					arg0.sendMessage(ChatColor.GREEN + "The area " + areaInformation.getAreaName() + " was successfully unregistered.");
					areaInformation = null;
					AreaRegister.getInstance().saveAreaInformationJSON();
				} else {
					arg0.sendMessage(ChatColor.RED + "You are not in a registered area!");
				}
			} else {
				arg0.sendMessage(ChatColor.RED + "No arguments are expected!");
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
		}

		return false;
	}
}