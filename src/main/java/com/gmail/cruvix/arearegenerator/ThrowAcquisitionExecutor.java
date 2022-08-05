package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

public class ThrowAcquisitionExecutor implements CommandExecutor {
	private AreaRegenerator areaRegenerator;

	public ThrowAcquisitionExecutor(AreaRegenerator areaRegenerator) {
		this.areaRegenerator = areaRegenerator;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender.isOp()) {
			AreaInformation areaInformation = AreaVerificator.verifyAreas(commandSender);
			if (areaInformation != null) {
				PluginManager pm = this.areaRegenerator.getServer().getPluginManager();
				pm.registerEvents(areaInformation.getEventHandler(), this.areaRegenerator);
				commandSender.sendMessage(ChatColor.GREEN + "Acquisition thrown.");
			} else {
				commandSender.sendMessage(ChatColor.RED + "You are not in a registered Area!");
			}
		} else {
			commandSender.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
		}

		return false;
	}
}