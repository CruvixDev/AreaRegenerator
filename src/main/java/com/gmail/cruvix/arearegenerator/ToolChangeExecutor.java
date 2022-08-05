package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToolChangeExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0.isOp()) {
			if (arg3.length == 1) {
				Material newTool = MaterialsTranslator.translateIntoMaterial(arg3[0]);
				if (newTool != null) {
					ToolManager.getInstance().setTool(newTool);
					arg0.sendMessage(ChatColor.GREEN + "Tool change successfully : " + newTool);
				} else {
					arg0.sendMessage(ChatColor.RED + "Tool not valid!");
				}
			} else {
				arg0.sendMessage(ChatColor.RED + "Too many arguments!");
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
		}

		return false;
	}
}