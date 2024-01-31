package me.yhamarsheh.bridgersumo.bungee.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import me.yhamarsheh.bridgersumo.bungee.BridgerSumo;
import me.yhamarsheh.bridgersumo.bungee.commands.JoinCMD;

import java.util.List;
import java.util.UUID;

public class QuitListener {

    private BridgerSumo plugin;
    public QuitListener(BridgerSumo plugin) {
        this.plugin = plugin;
        plugin.getProxyServer().getEventManager().register(plugin, this);
    }

    @Subscribe
    public void onQuit(DisconnectEvent e) {
        List<UUID> uuidList = JoinCMD.getCooldown();
        uuidList.remove(e.getPlayer().getUniqueId());
    }
}
