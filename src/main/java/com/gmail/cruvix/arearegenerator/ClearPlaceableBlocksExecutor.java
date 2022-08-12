package com.gmail.cruvix.arearegenerator;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearPlaceableBlocksExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			AreaInformation areaInformation = AreaVerificator.verifyAreas(commandSender);
			if (areaInformation != null) {
				if (strings.length == 0) {
					DatabaseManager.dropBlocks(areaInformation,areaInformation.getPlaceableBlocks(),BlockMode.PLACEABLE);
					areaInformation.clearPlaceableMaterials();
				} else {
					ArrayList<Material> materialsList = MaterialsTranslator.translateIntoMaterials(strings);
					if (materialsList != null) {
						materialsList = areaInformation.clearPlaceableMaterials(materialsList);
						DatabaseManager.dropBlocks(areaInformation,materialsList,BlockMode.PLACEABLE);
					} else {
						commandSender.sendMessage(ChatColor.RED + "Materials list is not valid!");
					}
				}
			} else {
				commandSender.sendMessage(ChatColor.RED + "You are not in a registered Area!");
			}
		}

		return false;
	}
}