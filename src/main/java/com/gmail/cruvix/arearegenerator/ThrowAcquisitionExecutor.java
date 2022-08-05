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
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0.isOp()) {
			AreaInformation areaInformation = AreaVerificator.verifyAreas(arg0);
			if (areaInformation != null) {
				PluginManager pm = this.areaRegenerator.getServer().getPluginManager();
				pm.registerEvents(areaInformation.getEventHandler(), this.areaRegenerator);
				arg0.sendMessage(ChatColor.GREEN + "Acquisition thrown.");
			} else {
				arg0.sendMessage(ChatColor.RED + "You are not in a registered Area!");
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
		}

		return false;
	}
}