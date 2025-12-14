package astra.core.mysql.data;

import astra.core.mysql.Database;
import astra.core.mysql.data.interfaces.DataTableInfo;
import astra.core.mysql.tables.*;
import astra.core.mysql.tables.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class DataTable {

  public abstract void init(Database database);

  public abstract Map<String, DataContainer> getDefaultValues();

  public DataTableInfo getInfo() {
    return this.getClass().getAnnotation(DataTableInfo.class);
  }

  private static final List<DataTable> TABLES = new ArrayList<>();

  static {
    TABLES.add(new AccountTable());
    TABLES.add(new SkyWarsTable());
    TABLES.add(new BedWarsTable());
    TABLES.add(new SkinsTable());
  }

  public static void registerTable(DataTable table) {
    TABLES.add(table);
  }

  public static Collection<DataTable> listTables() {
    return TABLES;
  }
}
