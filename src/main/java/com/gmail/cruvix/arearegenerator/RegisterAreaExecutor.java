package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterAreaExecutor implements CommandExecutor {
	private ToolManager toolManager = ToolManager.getInstance();

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0.isOp()) {
			if (arg3.length == 1) {
				String areaName = arg3[0];
				AreaRegister areaRegister = AreaRegister.getInstance();
				if (!areaRegister.containsAreaName(areaName)) {
					Coordinate point1 = new Coordinate(this.toolManager.getPoint1().getX(), this.toolManager.getPoint1().getY());
					Coordinate point2 = new Coordinate(this.toolManager.getPoint2().getX(), this.toolManager.getPoint2().getY());
					if (this.coordValid() && !areaRegister.isRiding(point1, point2)) {
						Player player = (Player)arg0;
						areaRegister.addAreaInformations(new AreaInformation(areaName, player.getWorld().getName(), point1, point2));
						areaRegister.saveAreaInformationJSON();
					} else {
						arg0.sendMessage(ChatColor.RED + "Coordinate not valid!");
					}
				} else {
					arg0.sendMessage(ChatColor.RED + "Name already given to an other area!");
				}
			} else if (arg3.length == 0) {
				arg0.sendMessage(ChatColor.RED + "No arguments given!");
			} else {
				arg0.sendMessage(ChatColor.RED + "Too many arguments!");
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "Your are not allowed to perform this command!");
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