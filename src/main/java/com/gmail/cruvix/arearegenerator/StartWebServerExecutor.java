package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartWebServerExecutor implements CommandExecutor {
    private WebServer webServer;

    public StartWebServerExecutor(WebServer webServer) {
        this.webServer = webServer;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            boolean state = webServer.startWebServer();
            if (state) {
                commandSender.sendMessage(ChatColor.YELLOW + "The web server successfully start!");
            }
            else {
                commandSender.sendMessage(ChatColor.YELLOW + "The web server failed to start or already running!");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You are not allow to perform this command!");
        }
        return false;
    }
}
