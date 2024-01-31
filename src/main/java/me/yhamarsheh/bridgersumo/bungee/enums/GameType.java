package me.yhamarsheh.bridgersumo.bungee.enums;

public enum GameType {

    NORMAL("Normal Sumo"),
    BLOCK_SUMO("Block Sumo");

    String displayName;
    GameType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
