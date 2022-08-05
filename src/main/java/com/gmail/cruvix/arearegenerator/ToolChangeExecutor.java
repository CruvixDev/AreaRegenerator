package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToolChangeExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			if (strings.length == 1) {
				Material newTool = MaterialsTranslator.translateIntoMaterial(strings[0]);
				if (newTool != null) {
					ToolManager.getInstance().setTool(newTool);
					commandSender.sendMessage(ChatColor.GREEN + "Tool change successfully : " + newTool);
				} else {
					commandSender.sendMessage(ChatColor.RED + "Tool not valid!");
				}
			} else {
				commandSender.sendMessage(ChatColor.RED + "Too many arguments!");
			}
		} else {
			commandSender.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
		}

		return false;
	}
}