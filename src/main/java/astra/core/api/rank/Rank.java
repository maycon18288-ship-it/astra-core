package astra.core.api.rank;

import astra.core.Manager;
import astra.core.mysql.Database;
import astra.core.mysql.cache.TagCache;
import astra.core.utils.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Rank {

    private final int id;
    private final String name;
    private final String prefix;
    private final String permission;

    private final boolean alwaysVisible;

    public Rank(String name, String prefix, String permission, boolean alwaysVisible) {
        this.id = ROLES.size();
        this.name = StringUtils.formatColors(name);
        this.prefix = StringUtils.formatColors(prefix);
        this.permission = permission;
        this.alwaysVisible = alwaysVisible;

        ROLES.add(this);
    }

    public static void loadRanks() {
        new Rank("§4Admin", "§4§lADMIN §4", "role.admin", false);
        new Rank("§3Coord", "§3§lCOORD§3 ", "role.coord", false);
        new Rank("§5Mod+", "§5§lMOD+§5 ", "role.mod+", false);
        new Rank("§5Mod", "§5§lMOD§5 ", "role.mod", false);
        new Rank("§9Helper", "§9§lHELPER§9 ", "role.helper", false);
        new Rank("§2Builder", "§2§lBUILDER§2 ", "role.builder", false);
        new Rank("§1Beta", "§1§lBETA§1 ", "role.beta", false);
        new Rank("§3Partner+", "§3§lPARTNER+§3 ", "role.partner+", false);
        new Rank("§bPartner", "§b§lPARTNER§b ", "role.partner", false);
        new Rank("§cNatal", "§c§lNATAL§c ", "tag.natal", false);
        new Rank("§6Halloween", "§6§lHALLOWEEN§6 ", "tag.halloween", false);
        new Rank("§aFérias", "§a§lFÉRIAS§a ", "tag.ferias", false);
        new Rank("§6Carnaval", "§6§lCARNAVAL§6 ", "tag.carnaval", false);
        new Rank("§cQuerubim", "§c§lQUERUBIM §c", "role.querubim", false);
        new Rank("§5Serafim", "§5§lSERAFIM §5", "role.serafim", false);
        new Rank("§eArcanjo", "§e§lARCANJO §e", "role.arcanjo", false);
        new Rank("§9Apoiador", "§9§lAPOIADOR §9", "role.apoiador", false);
        new Rank("§7Membro", "§7", "", false);
    }

    public boolean isDefault() {
        return this.permission.isEmpty();
    }

    public boolean has(Object player) {
        return this.isDefault() || Manager.hasPermission(player, this.permission);
    }
    private static final List<Rank> ROLES = new ArrayList<>();
    public static String getPrefixed(String name) {
        return getPrefixed(name, false);
    }
    public static String getColored(String name) {
        return getColored(name, false);
    }
    public static String getPrefixed(String name, boolean removeFake) {
        return getTaggedName(name, false, removeFake);
    }
    public static String getColored(String name, boolean removeFake) {
        return getTaggedName(name, true, removeFake);
    }

    static String getTaggedName(String name, boolean onlyColor, boolean removeFake) {
        String prefix = "§7";

        if (!removeFake && Manager.isFake(name)) {
            prefix = Manager.getFakeRole(name).getPrefix();
            if (onlyColor) {
                prefix = StringUtils.getLastColor(prefix);
            }

            return prefix + name;
        }

        Object target = Manager.getPlayer(name);
        if (target != null) {
            String currentTag = TagCache.isPresent(Manager.getName(target)) ? TagCache.get(Manager.getName(target)) : null;
            if (currentTag != null) {
                Rank role = getRoleByName(currentTag.split(" : ")[0]);
                prefix = role.getPrefix();
            } else {
                prefix = getRank(target, true).getPrefix();
            }

            if (onlyColor) {
                prefix = StringUtils.getLastColor(prefix);
            }
            return prefix + name;
        }

        String rs = TagCache.isPresent(name) ? TagCache.get(name) : Database.getInstance().getTagAndName(name);
        if (rs != null) {
            prefix = getRoleByName(rs.split(" : ")[0]).getPrefix();
            if (onlyColor) {
                prefix = StringUtils.getLastColor(prefix);
            }
            name = rs.split(" : ")[1];
            if (!removeFake && Manager.isFake(name)) {
                name = Manager.getFake(name);
            }
            return prefix + name;
        }

        return prefix + name;
    }

    public static Rank getRoleByName(String name) {
        for (Rank role : ROLES) {
            if (StringUtils.stripColors(role.getName()).equalsIgnoreCase(name)) {
                return role;
            }
        }

        return getLastRole();
    }

    public static Rank getRoleByPermission(String permission) {
        for (Rank role : ROLES) {
            if (role.getPermission().equals(permission)) {
                return role;
            }
        }

        return null;
    }

    public static Rank getRank(Object player) {
        return getRank(player, false);
    }
    public static Rank getRank(Object player, boolean removeFake) {
        if (!removeFake && Manager.isFake(Manager.getName(player))) {
            return Manager.getFakeRole(Manager.getName(player));
        }

        String currentTag = TagCache.isPresent(Manager.getName(player)) ? TagCache.get(Manager.getName(player)) : null;
        if (currentTag != null) {
            return getRoleByName(currentTag.split(" : ")[0]);
        }

        for (Rank role : ROLES) {
            if (role.has(player)) {
                return role;
            }
        }

        return getLastRole();
    }

    public static Rank getLastRole() {
        if (ROLES.isEmpty()) return null;
        return ROLES.get(ROLES.size() - 1);
    }

    public static List<Rank> listRoles() {
        return ROLES;
    }
}

