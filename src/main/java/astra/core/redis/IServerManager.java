package astra.core.redis;

public interface IServerManager {
    void connectToGameServer(String playerName, String gameType);
    java.util.Map<String, ServerStatus> getServers();
    
    class ServerStatus {
        private String name;
        private String type;
        private int maxPlayers;
        private int onlinePlayers;
        private String state;
        private String icon;
        private int slot;
        private boolean showInMenu;

        public ServerStatus(String name, String type, int maxPlayers, int onlinePlayers, String state, String icon, int slot, boolean showInMenu) {
            this.name = name;
            this.type = type;
            this.maxPlayers = maxPlayers;
            this.onlinePlayers = onlinePlayers;
            this.state = state;
            this.icon = icon;
            this.slot = slot;
            this.showInMenu = showInMenu;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public int getMaxPlayers() { return maxPlayers; }
        public int getOnlinePlayers() { return onlinePlayers; }
        public String getState() { return state; }
        public String getIcon() { return icon; }
        public int getSlot() { return slot; }
        public boolean isShowInMenu() { return showInMenu; }
    }
} 