package astra.core.mysql.tables;

import astra.core.mysql.Database;
import astra.core.mysql.MySQLDatabase;
import astra.core.mysql.data.DataContainer;
import astra.core.mysql.data.DataTable;
import astra.core.mysql.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(name = "BedWars",
        create = "CREATE TABLE IF NOT EXISTS `BedWars` (`name` VARCHAR(32), `" +
                "1v1kills` LONG, `level` LONG, `geralkills` LONG, `geralwins` LONG, `geralbedsdestroyeds` LONG, `geralfinalkills` LONG, `geralwinstreak` LONG, `experience` LONG, `1v1deaths` LONG, `1v1games` LONG, `1v1bedsdestroyeds` LONG, `1v1bedslosteds` LONG, `1v1finalkills` LONG, `1v1finaldeaths` LONG, `1v1wins` LONG, `1v1winstreak` LONG, `2v2kills` LONG, `2v2deaths` LONG, `2v2games` LONG, `2v2bedsdestroyeds` LONG, `2v2bedslosteds` LONG, `2v2finalkills` LONG, `2v2finaldeaths` LONG, `2v2wins` LONG, `2v2winstreak` LONG, `4v4kills` LONG," +
                " `4v4deaths` LONG, `4v4games` LONG, `4v4bedsdestroyeds` LONG, `4v4bedslosteds` LONG, `4v4finalkills` LONG, `4v4finaldeaths` LONG, `4v4wins` LONG, `4v4winstreak` LONG, `3v3kills` LONG, `3v3deaths` LONG, `3v3games` LONG, `3v3bedsdestroyeds` LONG, `3v3bedslosteds` LONG, `3v3finalkills` LONG, `3v3finaldeaths` LONG, `3v3wins` LONG, `3v3winstreak` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlyassists` LONG, `monthlybeds` LONG, `monthlywins` LONG, `monthlywinstreak` LONG, `monthlygames` LONG, `month` TEXT, `Coins` DOUBLE," +
                " `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, `favorites` TEXT, `rejoin` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `BedWars` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `BedWars` VALUES (?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `BedWars` SET `1v1kills` = ?, `level` = ?, `geralkills` = ?, `geralwins` = ?, `geralbedsdestroyeds` = ?, `geralfinalkills` = ?, `geralwinstreak` = ?, `experience` = ?, `1v1deaths` = ?, `1v1games` = ?, `1v1bedsdestroyeds` = ?, `1v1bedslosteds` = ?, `1v1finalkills` = ?, `1v1finaldeaths` = ?, `1v1wins` = ? `1v1winstreak` = ?`, `2v2kills` = ?, `2v2deaths` = ?, `2v2games` = ?, `2v2bedsdestroyeds` = ?, `2v2bedslosteds` = ?, `2v2finalkills` = ?, `2v2finaldeaths` = ?, `2v2wins` = ?, `2v2winstreak` = ?`, `4v4kills` = ?, `4v4deaths` = ?, `4v4games` = ?, `4v4bedsdestroyeds` = ?, `4v4bedslosteds` = ?, `4v4finalkills` = ?, `4v4finaldeaths` = ?, `4v4winstreak` = ?, `3v3kills` = ?, `3v3deaths` = ?, `3v3bedsdestroyeds` = ?, `3v3bedslosteds` = ?, `3v3finalkills` = ?, `3v3finaldeaths` = ?, `3v3wins` = ?, `3v3winstreak` = ?, `monthlykills` = ?, `montlhydeaths` = ?, `monthlyassists` = ?, `monthlybeds` = ?, `monthlywins` = ?, `monthlywinstreak` = ?, `monthlygames` = ?, `month` = ?, `4v4wins` = ?, `Coins` = ?, `lastmap` = ?, `cosmetics` = ?, `selected` = ?, `rejoin` = ?, `kitconfig` = ? WHERE LOWER(`name`) = ?")
public class BedWarsTable extends DataTable {

    @Override
    public void init(Database database) {
        if (database instanceof MySQLDatabase) {
            if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `BedWars` LIKE 'lastmap'") == null) {
                ((MySQLDatabase) database).execute(
                        "ALTER TABLE `BedWars` ADD `lastmap` LONG DEFAULT 0 AFTER `Coins`, ADD `favorites` TEXT AFTER `selected`");
            }
        }
    }

    public Map<String, DataContainer> getDefaultValues() {
        Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
        for (String key : new String[]{"1v1", "2v2", "4v4", "3v3"}) {
            defaultValues.put("level", new DataContainer(0L));
            defaultValues.put("experience", new DataContainer(0L));
            defaultValues.put("geralkills", new DataContainer(0L));
            defaultValues.put("geralwins", new DataContainer(0L));
            defaultValues.put("geralbedsdestroyeds", new DataContainer(0L));
            defaultValues.put("geralfinalkills", new DataContainer(0L));
            defaultValues.put("geralwinstreak", new DataContainer(0L));
            defaultValues.put(key + "kills", new DataContainer(0L));
            defaultValues.put(key + "deaths", new DataContainer(0L));
            defaultValues.put(key + "games", new DataContainer(0L));
            defaultValues.put(key + "bedsdestroyeds", new DataContainer(0L));
            defaultValues.put(key + "bedslosteds", new DataContainer(0L));
            defaultValues.put(key + "finalkills", new DataContainer(0L));
            defaultValues.put(key + "finaldeaths", new DataContainer(0L));
            defaultValues.put(key + "wins", new DataContainer(0L));
            defaultValues.put(key + "winstreak", new DataContainer(0L));
        }
        for (String key : new String[]{"kills", "deaths",
                "assists", "beds", "wins", "winstreak", "games"}) {
            defaultValues.put("monthly" + key, new DataContainer(0L));
        }
        defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR)));
        defaultValues.put("Coins", new DataContainer(0.0D));
        defaultValues.put("lastmap", new DataContainer(0L));
        defaultValues.put("cosmetics", new DataContainer("{}"));
        defaultValues.put("selected", new DataContainer("{}"));
        defaultValues.put("favorites", new DataContainer("{}"));
        defaultValues.put("rejoin", new DataContainer("{}"));
        return defaultValues;
    }
}