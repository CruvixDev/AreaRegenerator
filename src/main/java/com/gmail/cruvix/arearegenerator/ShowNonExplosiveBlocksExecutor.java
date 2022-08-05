package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShowNonExplosiveBlocksExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            AreaInformation areaInformation = AreaVerificator.verifyAreas(commandSender);
            if (areaInformation != null) {
                commandSender.sendMessage(ChatColor.GREEN + areaInformation.showNonExplosiveBlocks());
            } else {
                commandSender.sendMessage(ChatColor.RED + "You are not in a registered Area!");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
        }

        return false;
    }
}