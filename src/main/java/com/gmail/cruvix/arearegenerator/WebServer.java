package com.gmail.cruvix.arearegenerator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class WebServer {
    private final int PORT = 80;
    private URL jarPath;
    private String resourcesPath;
    private Thread serverThread;
    private Server server;
    private static WebServer webServer = null;

    private WebServer(URL jarPath, String resourcePath) {
        this.jarPath = jarPath;
        this.resourcesPath = resourcePath;
        this.server = new Server(PORT);
        ResourceHandler resourceHandler = new ResourceHandler();
        createFiles();
        resourceHandler.setResourceBase(resourcesPath);
        resourceHandler.setDirectoriesListed(true);
        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setHandler(resourceHandler);
        this.server.setHandler(contextHandler);
    }

    public static WebServer getInstance(URL jarPath, String resourcesPath) {
        if (webServer == null) {
            webServer = new WebServer(jarPath, resourcesPath);
        }
        return webServer;
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
                    Files.copy(jarFile.getInputStream(entry), Paths.get(resourcesPath+ "/" + entry.getName().substring("web/".length())), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startWebServer() {
        if (server.getState().equals(Server.STOPPED) || server.getState().equals(Server.FAILED)) {
            //the server is stopped or has failed before.
            this.serverThread = new Thread() {
                public void run() {
                    try {
                        server.start();
                        server.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            serverThread.start();
        }
    }

    public void stopWebServer() {
        if (server.getState().equals(Server.STARTED) || server.getState().equals(Server.STARTING)) {
            try{
                this.server.stop();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getState() {
        return this.server.getState();
    }

}
