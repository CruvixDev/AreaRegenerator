package com.gmail.cruvix.arearegenerator;

import org.bukkit.plugin.java.JavaPlugin;
import java.net.URL;

public final class AreaRegenerator extends JavaPlugin {
	private final URL jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation();
	private final String resourcesPath = this.getDataFolder().getAbsolutePath() + "/web";
	private final WebServer webServer = WebServer.getInstance(jarPath,resourcesPath);

	@Override
	public void onEnable() {
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
		this.getCommand("setNonExplosiveBlocks").setExecutor(new SetExploseableBlocksExecutor());
		this.getCommand("clearNonExplosiveBlocks").setExecutor(new ClearExploseableBlocksExecutor());
		this.getCommand("showNonExplosiveBlocks").setExecutor(new ShowNonExplosiveBlocksExecutor());
		this.getCommand("startWebServer").setExecutor(new StartWebServerExecutor(webServer));
		this.getCommand("stopWebServer").setExecutor(new StopWebServerExecutor(webServer));
		this.getServer().getPluginManager().registerEvents(ToolManager.getInstance(), this);
		AreaRegister.getInstance().readAreaInformationJSON();
	}

	@Override
	public void onDisable() {
		AreaRegister.getInstance().saveAreaInformationJSON();
		webServer.stopWebServer();
	}
}
