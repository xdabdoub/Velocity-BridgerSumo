package me.yhamarsheh.bridgersumo.bungee.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.yhamarsheh.bridgersumo.bungee.BridgerSumo;
import me.yhamarsheh.bridgersumo.bungee.enums.GameType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinCMD implements SimpleCommand {

    private final BridgerSumo plugin;
    private static List<UUID> cooldown;
    public JoinCMD(BridgerSumo plugin) {
        this.plugin = plugin;
        this.cooldown = new ArrayList<>();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player player)) {
            source.sendMessage(Component.text("This command is only accessable by players!", NamedTextColor.RED));
            return;
        }

        if (args.length == 0) {
            source.sendMessage(Component.text("Invalid Usage: /join <gameType>", NamedTextColor.RED));
            return;
        }

        GameType gameType;
        try {
             gameType = GameType.valueOf(args[0]);
        } catch (IllegalArgumentException ex) {
            source.sendMessage(Component.text("Invalid game type!", NamedTextColor.RED));
            return;
        }

        if (cooldown.contains(player.getUniqueId())) return;
        cooldown.add(player.getUniqueId());

        source.sendMessage(Component.text("Attempting to find you a game of " + gameType.toString() + "...", NamedTextColor.GRAY));
        plugin.getDataHandler().request(player, "bridger_sumo", (receiver, msg) -> {

            if (msg) {
                source.sendMessage(Component.text("Found a " + gameType.toString() + " game for you! Sending you now..",
                        NamedTextColor.GREEN));
                player.createConnectionRequest(plugin.getProxyServer().getServer("sumo").get()).connect();
            } else {
                source.sendMessage(Component.text("Couldn't find a game to send you to.", NamedTextColor.RED));
            }

            cooldown.remove(player.getUniqueId());
        }, gameType);
        plugin.getProxyServer().getScheduler().buildTask(plugin, () -> {
            if (cooldown.contains(player.getUniqueId())) {
                plugin.getDataHandler().consume("bridger_sumo", player.getUniqueId(), false);
            }
        }).delay(Duration.ofMinutes(3));
    }

    public static List<UUID> getCooldown() {
        return cooldown;
    }
}
