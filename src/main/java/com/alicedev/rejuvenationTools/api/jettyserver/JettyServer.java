package com.alicedev.rejuvenationTools.api.jettyserver;

import org.bukkit.Bukkit;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JettyServer {
    private static Server server;
    private static final Logger logger = LoggerFactory.getLogger("RejuvenationTools");
    public static void start(int port) throws Exception {
        server = new Server(port);
        
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResourceAsString("plugins/RejuvenationTools/web");

        ContextHandlerCollection handlers = new ContextHandlerCollection();
        handlers.setHandlers(new org.eclipse.jetty.server.Handler[] { resourceHandler });

        server.setHandler(handlers);

        Thread jettyThread = new Thread(() -> {
            try {
                server.start();
                server.join();
            } catch (Exception e) {
                logger.error("Failed to start Jetty Server!");
            }
        });
        jettyThread.start();
    }
    public static void stop() throws Exception {
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }

}