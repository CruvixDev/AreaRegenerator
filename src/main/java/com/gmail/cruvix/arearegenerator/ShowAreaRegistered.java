package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShowAreaRegistered implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0.isOp()) {
			arg0.sendMessage(ChatColor.GREEN + AreaRegister.getInstance().toString());
		} else {
			arg0.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
		}
		return false;
	}
}