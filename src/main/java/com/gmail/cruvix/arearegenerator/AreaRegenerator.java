package com.gmail.cruvix.arearegenerator;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class AreaRegenerator extends JavaPlugin {
	private final URL jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation();
	private final String resourcesPath = this.getDataFolder().getAbsolutePath() + "/web";

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
		this.getServer().getPluginManager().registerEvents(ToolManager.getInstance(), this);
		AreaRegister.getInstance().readAreaInformationJSON();

		/*Thread serverThread = new Thread() {
			public void run() {
				Server server = new Server(8080);
				ResourceHandler resourceHandler = new ResourceHandler();
				createFiles();
				resourceHandler.setResourceBase(resourcesPath);
				resourceHandler.setDirectoriesListed(true);
				ContextHandler contextHandler = new ContextHandler("/");
				contextHandler.setHandler(resourceHandler);
				server.setHandler(contextHandler);
				try {
					server.start();
					server.join();
				}
				catch (Exception e) {
					e.printStackTrace();
					try {
						server.stop();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};
		serverThread.start();*/
	}

	@Override
	public void onDisable() {
		AreaRegister.getInstance().saveAreaInformationJSON();
	}

	public void createFiles() {
		try {
			File web = new File(resourcesPath);
			web.mkdirs();
			ZipFile jarFile = new ZipFile(new File(jarPath.getPath()));
			Enumeration entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.getName().startsWith("web/") && !entry.isDirectory()) {
					Files.copy(jarFile.getInputStream(entry),Paths.get(resourcesPath+ "/" + entry.getName().substring("web/".length())),StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getResourcesPath() {
		return this.resourcesPath;
	}
}
