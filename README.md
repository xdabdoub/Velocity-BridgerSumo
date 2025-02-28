# Bridger Sumo (v3.0.1) - Velocity Server

Bridger Sumo is a custom-developed Minecraft minigame commissioned for a Minecraft network. This version introduces two exciting game modes: **Normal Sumo** and **Block Sumo**.

🔗 Learn more: [Bridger Sumo V3 Repository](https://github.com/xdabdoub/BridgerSumo_V3)

---

## Velocity Proxy

[Velocity](https://github.com/PaperMC/Velocity) is a high-performance Minecraft server proxy known for its excellent server support, scalability, and flexibility.

---

## Project Overview

This plugin is designed to function as a remote matchmaking server within a **Velocity** proxy, facilitating game requests from Spigot sub-servers. To achieve seamless communication between these servers, we utilize **Redis Pub/Sub** messaging.

### Why was this needed?

In the traditional setup, players would have to join a **Sumo sub-server** and manually wait for matchmaking to begin. If no available game was found, they would be sent back to their original lobby. This was inefficient and created a frustrating player experience.

To improve this, we implemented a system where:
- Players can **select their preferred game mode before joining** a sub-server.
- Matchmaking is handled remotely, ensuring a **game is available before the player joins**.

---

## How It Works

This plugin operates as a **matchmaking server**, listening for requests from sub-servers. It uses **callback technology** to request game availability and process player matchmaking efficiently.

### Step-by-step process:

1. **Game Mode Selection**
   - Players choose their preferred game mode (**BLOCK_SUMO** or **NORMAL_SUMO**) via an interactive graphical interface.

2. **Sending a Matchmaking Request**
   - The request is forwarded to the **Velocity proxy server** using **Redis Pub/Sub** messaging.

3. **Game Availability Check**
   - The Velocity server initiates a **Future callback** to check for available games that match the player's selected mode.
   - The request is structured as follows:

   ```json
   {
     "uuid": "player-uuid",
     "subchannel": "gameType",
     "response": "BLOCK_SUMO" // Either BLOCK_SUMO or NORMAL_SUMO
   }
   ```

4. **Receiving the Game Status Response**
   - The sub-server responds with the game status:

   ```json
   {
     "uuid": "player-uuid",
     "subchannel": "gameFound",
     "response": true // Either true or false
   }
   ```
   - If `true`, a game has been found for the player.
   - The sub-server also saves the **game information** along with the **player's UUID** in a `HashMap`, ensuring the player is assigned to their specific game upon joining.

5. **Handling the Player’s Connection**
   - If a game is available, the **Velocity server redirects the player** to the sub-server hosting that match.

6. **Game Management**
   - Once connected, the **sub-server** handles all **player interactions, match management, and game mechanics**.

---

## Technologies Used

- **Velocity Proxy** – Efficient proxy for managing player connections.
- **Spigot Sub-Servers** – Hosts the actual game instances.
- **Redis Pub/Sub** – Enables real-time communication between servers.
- **Future Callbacks** – Ensures smooth asynchronous matchmaking.
- **Java** – Primary programming language for the plugin.

---

## Conclusion

This system streamlines matchmaking in **Bridger Sumo**, ensuring a **faster and smoother player experience**. By implementing **remote matchmaking and Redis-based communication**, players no longer need to wait inside sub-servers, and games are guaranteed to be available before they even join.

---

<details>
  <summary><h2>Media</h2></summary>

![No Game Found](https://media.discordapp.net/attachments/831831006351982592/1199249990162321438/image.png?ex=67c2b316&is=67c16196&hm=6d942e55709c19ab8a53b8b6d1b9f6812fe2cd784fd242dfdcebd7c0daaa69f6&=&format=webp&quality=lossless&width=616&height=52)

![Game Found](https://media.discordapp.net/attachments/831831006351982592/1199249891604574249/image.png?ex=67c2b2ff&is=67c1617f&hm=265a64f8c82cbba24a3b584c7ec99633393eac09d22213ff656c2f159dd5c640&=&format=webp&quality=lossless&width=669&height=44)

![Usage](https://media.discordapp.net/attachments/831831006351982592/1199249836390756412/image.png?ex=67c2b2f1&is=67c16171&hm=c934b08892620b5970fab8ae51f7e8eedd7e6980863e429d85d92f7ee04fa96b&=&format=webp&quality=lossless&width=435&height=32)

![Loaded Successfully](https://media.discordapp.net/attachments/831831006351982592/1198956444918239322/image.png?ex=67c2f334&is=67c1a1b4&hm=6b15a1ba0acfe97e61104a77152cb3da513fcbf01f1d89405af202cce42a5533&=&format=webp&quality=lossless&width=1184&height=30)
