package me.yhamarsheh.bridgersumo.bungee.handlers;

import com.velocitypowered.api.proxy.Player;
import me.yhamarsheh.bridgersumo.bungee.BridgerSumo;
import me.yhamarsheh.bridgersumo.bungee.enums.GameType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.json.JSONException;

import java.util.*;
import java.util.function.BiConsumer;

public class DataHandler {

    private final Map<String, Deque<BiConsumer<UUID, Boolean>>> callbacks = new HashMap<>();
    private final BridgerSumo plugin;

    public DataHandler(BridgerSumo plugin) {
        this.plugin = plugin;
    }

    public void request(Player player, String subChannel, BiConsumer<UUID, Boolean> callback, GameType gameType) {
        callbacks.computeIfAbsent(subChannel, key -> new ArrayDeque<>()).add(callback);

        plugin.getProxyServer().getScheduler().buildTask(plugin, () -> {
            try {
                plugin.getRedisManager().sendGameType(player.getUniqueId(), gameType);
            } catch (JSONException e) {
                consume("bridger_sumo", player.getUniqueId(), false);
                player.sendMessage(Component.text("An error has occurred and we weren't able to find you a game of " + gameType.name() +
                        ". Please contact an administrator immediately. [EC: 201]", NamedTextColor.RED));
                plugin.getLogger().severe("An error occurred, and I was not able to send the GameType of the [UUID@"
                        + player.getUniqueId().toString() + "] to the 'sumo' server. More Info: " + e.getMessage());
            }
        }).schedule();
    }

    public void consume(String subChannel, UUID receiver, Boolean message) {
        Deque<BiConsumer<UUID, Boolean>> callBackQueue = callbacks.get(subChannel);
        if (callBackQueue != null && !callBackQueue.isEmpty()) {
            BiConsumer<UUID, Boolean> callback = callBackQueue.poll();
            callback.accept(receiver, message);
        }
    }

}
