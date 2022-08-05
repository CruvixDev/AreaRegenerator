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
            webServer.stopWebServer();
            commandSender.sendMessage(ChatColor.YELLOW + "Web server state : " + webServer.getState());
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
        }
        return false;
    }
}
