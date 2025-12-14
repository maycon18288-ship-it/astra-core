package astra.core.mysql.tables;

import astra.core.mysql.Database;
import astra.core.mysql.MySQLDatabase;
import astra.core.mysql.data.DataContainer;
import astra.core.mysql.data.DataTable;
import astra.core.mysql.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "Account",
    create = "CREATE TABLE IF NOT EXISTS `Account` (`name` VARCHAR(32), " +

            "`cash` LONG, " +
            "`titulopersonalizado` TEXT, " +
            "`medalha` TEXT, " +
            "`role` TEXT, " +
            "`preferencias` TEXT, " +
            "`titulos` TEXT, " +
            "`joinmessage` TEXT, " +
            "`tag` LONG, " +
            "`selected` TEXT, " +
            "`youtube` TEXT, " +
            "`x` TEXT, " +
            "`twitch` TEXT, " +
            "`discord` TEXT, " +
            "`instagram` TEXT, " +
            "`tiktok` TEXT, " +
            "`apelido` TEXT, " +
            "`created` LONG, " +
            "`lastlogin` LONG, " +

            "PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `Account` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `Account` VALUES (?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?)",

    update = "UPDATE `Account` SET " +
            "`cash` = ?, " +
            "`titulopersonalizado` = ?, " +
            "`medalha` = ?, " +
            "`role` = ?, " +
            "`preferencias` = ?, " +
            "`titulos` = ?, " +
            "`joinmessage` = ?, " +
            "`tag` = ?, " +
            "`selected` = ?, " +
            "`youtube` = ?, " +
            "`x` = ?, " +
            "`twitch` = ?, " +
            "`discord` = ?, " +
            "`instagram` = ?, " +
            "`tiktok` = ?, " +
            "`apelido` = ?, " +
            "`created` = ?, " +
            "`lastlogin` = ? WHERE LOWER(`name`) = ?"
    )

public class AccountTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `Account` LIKE 'cash'") == null) {
        ((MySQLDatabase) database).execute("ALTER TABLE `Account` ADD `cash` LONG AFTER `name`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {

    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("cash", new DataContainer(0L));
    defaultValues.put("titulopersonalizado", new DataContainer(""));
    defaultValues.put("medalha", new DataContainer("default"));
    defaultValues.put("role", new DataContainer("Membro"));
    defaultValues.put("preferencias", new DataContainer("{\"pv\":  0, \"pm\": 0, \"pl\": 0, \"sc\": 0, \"party\" 0, \"mencao\": 0, \"scoreboard\": 0, \"partidaautomatica\": 0, \"apelido\": 0}"));
    defaultValues.put("titulos", new DataContainer(""));
    defaultValues.put("joinmessage", new DataContainer(""));
    defaultValues.put("tag", new DataContainer(""));
    defaultValues.put("selected", new DataContainer("{\"titulos\": \"0\", \"icon\": \"0\"}"));
    defaultValues.put("youtube", new DataContainer(""));
    defaultValues.put("x", new DataContainer(""));
    defaultValues.put("twitch", new DataContainer(""));
    defaultValues.put("discord", new DataContainer(""));
    defaultValues.put("instagram", new DataContainer(""));
    defaultValues.put("tiktok", new DataContainer(""));
    defaultValues.put("apelido", new DataContainer(""));
    defaultValues.put("created", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("lastlogin", new DataContainer(System.currentTimeMillis()));

    return defaultValues;
  }
}
