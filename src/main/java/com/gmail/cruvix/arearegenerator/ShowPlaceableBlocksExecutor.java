package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShowPlaceableBlocksExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0.isOp()) {
			AreaInformation areaInformation = AreaVerificator.verifyAreas(arg0);
			if (areaInformation != null) {
				arg0.sendMessage(ChatColor.GREEN + areaInformation.showMaterials());
			} else {
				arg0.sendMessage(ChatColor.RED + "You are not in a registered Area!");
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
		}

		return false;
	}
}
