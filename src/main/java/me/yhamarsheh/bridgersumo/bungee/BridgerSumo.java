package me.yhamarsheh.bridgersumo.bungee;


import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.yhamarsheh.bridgersumo.bungee.commands.JoinCMD;
import me.yhamarsheh.bridgersumo.bungee.handlers.DataHandler;
import me.yhamarsheh.bridgersumo.bungee.handlers.RedisManager;
import me.yhamarsheh.bridgersumo.bungee.listeners.QuitListener;

import javax.inject.Inject;
import java.util.logging.Logger;

@Plugin(
        id = "bridgersumovelocity",
        name = "BridgerSumo-VELOCITY",
        version = "1.0",
        authors = "xDabDoub"
)
public class BridgerSumo {


    private final Logger logger;
    private final ProxyServer proxyServer;

    private final DataHandler dataHandler;
    private RedisManager redisManager;

    @Inject
    public BridgerSumo(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;

        this.dataHandler = new DataHandler(this);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        CommandManager commandManager = proxyServer.getCommandManager();

        JoinCMD joinCMD = new JoinCMD(this);
        commandManager.register(commandManager.metaBuilder("join").plugin(this).build(), joinCMD);

        redisManager = new RedisManager(this);

        new QuitListener(this);
    }

    @Subscribe
    public void onProxyDisable(ProxyShutdownEvent e) {
        redisManager.closeConnection();
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public Logger getLogger() {
        return logger;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }
}
