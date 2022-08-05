package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterAreaExecutor implements CommandExecutor {
	private ToolManager toolManager = ToolManager.getInstance();

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			if (strings.length == 1) {
				String areaName = strings[0];
				AreaRegister areaRegister = AreaRegister.getInstance();
				if (!areaRegister.containsAreaName(areaName)) {
					Coordinate point1 = new Coordinate(this.toolManager.getPoint1().getX(), this.toolManager.getPoint1().getY());
					Coordinate point2 = new Coordinate(this.toolManager.getPoint2().getX(), this.toolManager.getPoint2().getY());
					if (this.coordValid() && !areaRegister.isRiding(point1, point2)) {
						Player player = (Player)commandSender;
						areaRegister.addAreaInformations(new AreaInformation(areaName, player.getWorld().getName(), point1, point2));
						areaRegister.saveAreaInformationJSON();
					} else {
						commandSender.sendMessage(ChatColor.RED + "Coordinate not valid!");
					}
				} else {
					commandSender.sendMessage(ChatColor.RED + "Name already given to an other area!");
				}
			} else if (strings.length == 0) {
				commandSender.sendMessage(ChatColor.RED + "No arguments given!");
			} else {
				commandSender.sendMessage(ChatColor.RED + "Too many arguments!");
			}
		} else {
			commandSender.sendMessage(ChatColor.RED + "Your are not allowed to perform this command!");
		}

		return false;
	}

	private boolean coordValid() {
		boolean isValid = true;
		if (this.toolManager.getPoint1().equals(this.toolManager.getPoint2())) {
			isValid = false;
		}
		return isValid;
	}
}