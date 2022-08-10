package com.gmail.cruvix.arearegenerator;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Properties;

public final class AreaRegenerator extends JavaPlugin {
	private static Properties properties = new Properties();
	private WebServer webServer;

	@Override
	public void onEnable() {

		properties.setProperty("jarPath", this.getClass().getProtectionDomain().getCodeSource().getLocation().toString());
		properties.setProperty("resourcesPath", this.getDataFolder().getAbsolutePath());
		webServer = WebServer.getInstance();

		this.getCommand("throwAcquisition").setExecutor(new ThrowAcquisitionExecutor(this));
		this.getCommand("stopAcquisition").setExecutor(new StopAcquisitionExecutor());
		this.getCommand("registerArea").setExecutor(new RegisterAreaExecutor());
		this.getCommand("setPlaceableBlock").setExecutor(new SetPlaceableBlocksExecutor());
		this.getCommand("unregisterArea").setExecutor(new UnregisterAreaExecutor());
		this.getCommand("showPlaceableBlocks").setExecutor(new ShowPlaceableBlocksExecutor());
		this.getCommand("showAreaRegistered").setExecutor(new ShowAreaRegistered());
		this.getCommand("clearPlaceableBlocks").setExecutor(new ClearPlaceableBlocksExecutor());
		this.getCommand("changeTool").setExecutor(new ToolChangeExecutor());
		this.getCommand("notifyBlockSet").setExecutor(new NotifyBlockSetExecutor(this));
		this.getCommand("setNonExplosiveBlocks").setExecutor(new SetNonExplosiveBlocksExecutor());
		this.getCommand("clearNonExplosiveBlocks").setExecutor(new ClearNonExplosiveBlocksExecutor());
		this.getCommand("showNonExplosiveBlocks").setExecutor(new ShowNonExplosiveBlocksExecutor());
		this.getCommand("startWebServer").setExecutor(new StartWebServerExecutor(webServer));
		this.getCommand("stopWebServer").setExecutor(new StopWebServerExecutor(webServer));
		this.getServer().getPluginManager().registerEvents(ToolManager.getInstance(), this);

		DatabaseManager.createDatabase();
	}

	@Override
	public void onDisable() {
		AreaRegister.getInstance().saveAreaInformationJSON();
		webServer.stopWebServer();
	}

	public static Properties getProperties() {
		return properties;
	}
}
