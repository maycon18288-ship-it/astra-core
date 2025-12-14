package astra.core.player;

import lombok.Getter;

public class PlayerProfileEnums {

    @Getter
    public enum ClanRequest {
        ATIVADO("§aAtivado"),
        DESATIVADO("§cDesativado");

        private final String name;

        ClanRequest(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public ClanRequest next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final ClanRequest[] VALUES = values();

        public static ClanRequest getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum Scoreboard {
        ATIVADO("§aAtivada"),
        DESATIVADO("§cDesativada");

        private final String name;

        Scoreboard(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public Scoreboard next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final Scoreboard[] VALUES = values();

        public static Scoreboard getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum Apelido {
        ATIVADO("§aAtivado"),
        DESATIVADO("§cDesativado");

        private final String name;

        Apelido(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public Apelido next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final Apelido[] VALUES = values();

        public static Apelido getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum PartidaAutomatica {
        ATIVADO("§aAtivada"),
        DESATIVADO("§cDesativada");

        private final String name;

        PartidaAutomatica(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public PartidaAutomatica next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final PartidaAutomatica[] VALUES = values();

        public static PartidaAutomatica getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum FriendRequest {
        ATIVADO("§aAtivado"),
        DESATIVADO("§cDesativado");

        private final String name;

        FriendRequest(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public FriendRequest next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final FriendRequest[] VALUES = values();

        public static FriendRequest getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum AutoGG {
        ATIVADO("§aAtivado"),
        DESATIVADO("§cDesativado");

        private final String name;

        AutoGG(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public AutoGG next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final AutoGG[] VALUES = values();

        public static AutoGG getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum Chat {
        ATIVADO("§aAtivado"),
        DESATIVADO("§cDesativado");

        private final String name;

        Chat(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public Chat next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final Chat[] VALUES = values();

        public static Chat getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum Notificacao {
        ATIVADO("§aAtivadas"),
        DESATIVADO("§cDesativadas");

        private final String name;

        Notificacao(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public Notificacao next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final Notificacao[] VALUES = values();

        public static Notificacao getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum Mencao {
        ATIVADO("§aAtivado"),
        DESATIVADO("§cDesativado");

        private final String name;

        Mencao(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public Mencao next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final Mencao[] VALUES = values();

        public static Mencao getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum PartyRequest {
        ATIVADO("§aAtivado"),
        DESATIVADO("§cDesativado");

        private final String name;

        PartyRequest(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public PartyRequest next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final PartyRequest[] VALUES = values();

        public static PartyRequest getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum PlayerVisibility {
        TODOS("§aTodos"),
        NENHUM("§cNenhum");

        private final String name;

        PlayerVisibility(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == TODOS ? "10" : "8";
        }

        public PlayerVisibility next() {
            return this == NENHUM ? TODOS : NENHUM;
        }

        private static final PlayerVisibility[] VALUES = values();

        public static PlayerVisibility getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum PrivateMessages {
        TODOS("§aAtivado"),
        NENHUM("§cDesativado");

        private final String name;

        PrivateMessages(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == TODOS ? "10" : "8";
        }

        public PrivateMessages next() {
            return this == NENHUM ? TODOS : NENHUM;
        }

        private static final PrivateMessages[] VALUES = values();

        public static PrivateMessages getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }

    @Getter
    public enum ProtectionLobby {
        ATIVADO("§aAtivado"),
        DESATIVADO("§cDesativado");

        private final String name;

        ProtectionLobby(String name) {
            this.name = name;
        }

        public String getItem() {
            return this == ATIVADO ? "10" : "8";
        }

        public ProtectionLobby next() {
            return this == DESATIVADO ? ATIVADO : DESATIVADO;
        }

        private static final ProtectionLobby[] VALUES = values();

        public static ProtectionLobby getOriginal(long ordinal) {
            return (ordinal >= 0 && ordinal < VALUES.length) ? VALUES[(int) ordinal] : null;
        }
    }
}
