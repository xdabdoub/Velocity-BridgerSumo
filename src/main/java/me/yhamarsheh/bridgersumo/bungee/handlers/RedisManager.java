package me.yhamarsheh.bridgersumo.bungee.handlers;

import me.yhamarsheh.bridgersumo.bungee.BridgerSumo;
import me.yhamarsheh.bridgersumo.bungee.enums.GameType;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.time.Duration;
import java.util.UUID;

public class RedisManager {

    private final BridgerSumo plugin;
    private final String CHANNEL;

    private JedisPool subscriber;

    public RedisManager(BridgerSumo plugin) {
        this.plugin = plugin;
        this.CHANNEL = "dabdoub:bridgersumo";

        subscribe();
    }
    public void subscribe(){
        subscriber = new JedisPool("168.119.212.45", 25569);
        subscriber.setMaxWait(Duration.ZERO);

        plugin.getProxyServer().getScheduler().buildTask(plugin, () -> {
            try (Jedis jedis = subscriber.getResource()) {
                jedis.auth("ai0E+EO0DOF03454T+%f:CCLCLCLLCD;DXzorOl104");
                jedis.subscribe(new ReceiveMessageHandler(), CHANNEL);
            }
        }).schedule();
    }

    public void sendGameType(UUID uuid, GameType gameType) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("uuid", uuid.toString());
        data.put("subchannel", "gameType");
        data.put("response", gameType.name());

        try (Jedis publisher = subscriber.getResource()) {
            publisher.auth("ai0E+EO0DOF03454T+%f:CCLCLCLLCD;DXzorOl104");
            publisher.publish(CHANNEL, data.toString());
        }
    }

    public void closeConnection() {
        subscriber.close();
    }

    class ReceiveMessageHandler extends JedisPubSub {

        @Override
        public void onMessage(String channel, String message) {
            if (!channel.equals(CHANNEL)) return;

            try {
                JSONObject jsonObject = new JSONObject(message);
                UUID uuid = UUID.fromString(jsonObject.getString("uuid"));

                String subChannel = jsonObject.getString("subchannel");
                if (!subChannel.equals("gameFound")) return;

                boolean f = jsonObject.getBoolean("response");
                plugin.getDataHandler().consume("bridger_sumo", uuid, f);
            } catch (JSONException e) {
                plugin.getLogger().severe("Couldn't respond to the message from the sumo server. More Info: " + e.getMessage()
                        + "\nResponse: " + message);
            }
        }
    }
}
