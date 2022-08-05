package com.gmail.cruvix.arearegenerator;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetPlaceableBlocksExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			AreaInformation areaInformation = AreaVerificator.verifyAreas(commandSender);
			if (areaInformation != null) {
				new ArrayList();
				ArrayList<Material> materialsList = MaterialsTranslator.translateIntoMaterials(strings);
				if (materialsList != null) {
					areaInformation.addPlaceableMaterials(materialsList);
				} else {
					commandSender.sendMessage(ChatColor.RED + "Materials list not valid!");
				}

				AreaRegister.getInstance().saveAreaInformationJSON();
			} else {
				commandSender.sendMessage(ChatColor.RED + "You are not in a registered Area!");
			}
		} else {
			commandSender.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
		}

		return false;
	}
}