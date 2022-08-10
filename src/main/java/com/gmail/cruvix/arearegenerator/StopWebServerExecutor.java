package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopWebServerExecutor implements CommandExecutor {
    private WebServer webServer;

    public StopWebServerExecutor(WebServer webServer) {
        this.webServer = webServer;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            boolean state = webServer.stopWebServer();
            if (state) {
                commandSender.sendMessage(ChatColor.YELLOW + "The web server is successfully stopped!");
            }
            else {
                commandSender.sendMessage(ChatColor.YELLOW + "The web server failed to stop or already stopped!");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
        }
        return false;
    }
}
