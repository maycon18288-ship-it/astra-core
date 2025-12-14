package astra.core.mysql.tables;

import astra.core.mysql.Database;
import astra.core.mysql.MySQLDatabase;
import astra.core.mysql.data.DataContainer;
import astra.core.mysql.data.DataTable;
import astra.core.mysql.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
        name = "SkyWars",
        create = "CREATE TABLE IF NOT EXISTS `SkyWars` (`name` VARCHAR(32), `level` LONG, `experience` LONG, `1v1kills` LONG, `1v1deaths` LONG, `1v1assists` LONG, `1v1games` LONG, `1v1wins` LONG, `2v2kills` LONG, `2v2deaths` LONG, `2v2assists` LONG, `2v2games` LONG, `2v2wins` LONG, `rankedkills` LONG, `rankeddeaths` LONG, `rankedassists` LONG, `rankedgames` LONG, `rankedwins` LONG, `rankedpoints` LONG, `monthlykills` LONG, `monthlywins` LONG, `weeklykills` LONG, `weeklywins` LONG, `daykills` LONG, `daywins` LONG, `day` TEXT, `week` TEXT, `month` TEXT, `Coins` DOUBLE, `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, `kitconfig` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `SkyWars` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `SkyWars` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `SkyWars` SET `level` = ?, `experience` = ?, `1v1kills` = ?, `1v1deaths` = ?, `1v1assists` = ?, `1v1games` = ?, `1v1wins` = ?, `2v2kills` = ?, `2v2deaths` = ?, `2v2assists` = ?, `2v2games` = ?, `2v2wins` = ?, `rankedkills` = ?, `rankeddeaths` = ?, `rankedassists` = ?, `rankedgames` = ?, `rankedwins` = ?, `rankedpoints` = ?, `monthlykills` = ?, `monthlywins` = ?, `weeklykills` = ?, `weeklywins` = ?, `daykills` = ?, `daywins` = ?, `day` = ?, `week` = ?, `month` = ?, `Coins` = ?, `lastmap` = ?, `cosmetics` = ?, `selected` = ?, `kitconfig` = ? WHERE LOWER(`name`) = ?"
)
public class SkyWarsTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `SkyWars` LIKE 'lastmap'") == null) {
        ((MySQLDatabase) database).execute(
                "ALTER TABLE `SkyWars` ADD `lastmap` LONG DEFAULT 0 AFTER `Coins`, ADD `kitconfig` TEXT AFTER `selected`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("level", new DataContainer(0L));
    defaultValues.put("experience", new DataContainer(0L));
    defaultValues.put("1v1kills", new DataContainer(0L));
    defaultValues.put("1v1deaths", new DataContainer(0L));
    defaultValues.put("1v1assists", new DataContainer(0L));
    defaultValues.put("1v1games", new DataContainer(0L));
    defaultValues.put("1v1wins", new DataContainer(0L));
    defaultValues.put("2v2kills", new DataContainer(0L));
    defaultValues.put("2v2deaths", new DataContainer(0L));
    defaultValues.put("2v2assists", new DataContainer(0L));
    defaultValues.put("2v2games", new DataContainer(0L));
    defaultValues.put("2v2wins", new DataContainer(0L));
    defaultValues.put("rankedkills", new DataContainer(0L));
    defaultValues.put("rankeddeaths", new DataContainer(0L));
    defaultValues.put("rankedassists", new DataContainer(0L));
    defaultValues.put("rankedgames", new DataContainer(0L));
    defaultValues.put("rankedwins", new DataContainer(0L));
    defaultValues.put("rankedpoints", new DataContainer(0L));
    for (String key : new String[]{"kills", "wins"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    for (String key : new String[]{"kills", "wins"}) {
      defaultValues.put("weekly" + key, new DataContainer(0L));
    }
    for (String key : new String[]{"kills", "wins"}) {
      defaultValues.put("day" + key, new DataContainer(0L));
    }
    defaultValues.put("day", new DataContainer((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1) + "/" + Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)));
    defaultValues.put("week", new DataContainer((Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)));
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("Coins", new DataContainer(0.0D));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("kitconfig", new DataContainer("{}"));
    return defaultValues;
  }
}