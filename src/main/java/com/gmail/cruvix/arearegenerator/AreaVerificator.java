package com.gmail.cruvix.arearegenerator;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class allowing to verify if the sender of a command is really in a registered area.
 * @author Julien Cruvieux
 */
public class AreaVerificator {

	/**
	 * Verify that the sender is in a registered area.
	 * @param sender the command sender (a player or a command block).
	 * @return the AreaInformations concerned by the command sender.
	 */
	public static AreaInformation verifyAreas(CommandSender sender) {
		Coordinate point = new Coordinate();
		if (sender instanceof BlockCommandSender) {
			BlockCommandSender blockCommandSender = (BlockCommandSender)sender;
			point.setX(blockCommandSender.getBlock().getX());
			point.setY(blockCommandSender.getBlock().getZ());
		} else if (sender instanceof Player) {
			Player player = (Player)sender;
			point.setX(player.getLocation().getBlock().getX());
			point.setY(player.getLocation().getBlock().getZ());
		}

		return AreaRegister.getInstance().isInArea(point);
	}
}