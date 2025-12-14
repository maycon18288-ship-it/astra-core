package astra.core.mysql.tables;

import astra.core.mysql.Database;
import astra.core.mysql.data.DataContainer;
import astra.core.mysql.data.DataTable;
import astra.core.mysql.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
        name = "Skins",
        create = "CREATE TABLE IF NOT EXISTS `Skins` (`name` VARCHAR(32), `info` TEXT, `skins` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `Skins` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `Skins` VALUES (?, ?, ?)",
        update = "UPDATE `Skins` SET `info` = ?, `skins` = ? WHERE LOWER(`name`) = ?"
)
public class SkinsTable extends DataTable {
    public void init(Database database) {
    }

    public Map<String, DataContainer> getDefaultValues() {
        Map<String, DataContainer> defaultValues = new LinkedHashMap();
        defaultValues.put("info", new DataContainer("{\"name\": \"default\", \"value\": \"ewogICJ0aW1lc3RhbXAiIDogMTYxMjU1OTc5ODMwOCwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZiOTAzMjBjN2FlOGFmNjE0ZTFmNmRiM2Y5MDdmNDBmNjdlNjY4MGUxZWI4N2U5MDNlYmI5YjI2Zjk0YzljMjQiCiAgICB9CiAgfQp9\", \"signature\": \"LjxNQagEnWdrNSVk9bM0iR/Sa/3xNOZwTxH61Ky5DSa+fRHmXvA8bYRu5usLIRWr9O89ObC3kiOLy8MbQFikERcXqgpZMgHh0GGQW+SEo4JyjI3iE3fT5v2YV1JFeSmGBYRy0v38osV+JfLapps39PKddwkY++19IlDWUQqskyDVyin2JdcNK7naeFxubxEX6R4QLqa5NHOdLU6Qdmw+i8kwJXw7ah/s8RhvBQlP/vm3URrunG9SvcYNfzw+c/7mHmKECe89xNjGpM9PvCFP8iZJd5XJIwLqBltuZaVHDyL7sMMBuZ853qjGt7cvGqqyDBgrdvkbKFCdV2GHIUE4pj6lbDmli/BBQtOP4GCkg/XSZB/J9gu00VgmVFJYSntHqRoHhh1rFM01hm91UpNzHOG13rqUXGdTl8QxxMaj1205h6JKRjNFJqDjJFJ/VmQ9zBMMDRZhRPFMwCnXYriJJbsaWVpKxs26Krz5oRIufqNdbhDH0BCwsA2b1gjPNBPl3hPcH5oVb8hYy9G9dbpnSIP5dbGCHHFX65idP2Dq3HvKA9gO5rWhqY+UVdlduzaOJwFLUPiVL4nTpeLmP+ya+LYYnkhY4FrjFAEzaVBpnWil+gCyz0radMpon1wrV7ytBFQR9fj1piYCMvvhgVTGCPAMPgXf1hME7LgX2G1ofyQ=\"}"));
        defaultValues.put("skins", new DataContainer("{}"));
        return defaultValues;
    }
}