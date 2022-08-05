package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShowAreaRegistered implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			commandSender.sendMessage(ChatColor.GREEN + AreaRegister.getInstance().toString());
		} else {
			commandSender.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
		}
		return false;
	}
}