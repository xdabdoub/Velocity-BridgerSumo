# Bridger Sumo (v3.0.1)

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

## Media
Soon
