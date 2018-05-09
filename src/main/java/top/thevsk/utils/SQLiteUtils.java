package top.thevsk.utils;

import com.alibaba.fastjson.JSON;
import top.thevsk.start.JettyStart;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteUtils {

    private static final String blank = " ";

    private Connection connection;

    private String filePath;

    public SQLiteUtils(String filePath) throws Exception {
        this.filePath = filePath;
        init();
    }

    private void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
    }

    public void createTable(String tableName, List<TableColumn> tableColumns) throws Exception {
        StringBuilder sbf = new StringBuilder();
        sbf.append("CREATE TABLE");
        sbf.append(blank);
        sbf.append(tableName);
        sbf.append(blank);
        sbf.append("(");
        sbf.append(blank);
        for (int i = 0; i < tableColumns.size(); i++) {
            if (i != 0) {
                sbf.append(blank);
                sbf.append(",");
                sbf.append(blank);
            }
            sbf.append(tableColumns.get(i).columnName);
            sbf.append(blank);
            sbf.append(tableColumns.get(i).columnType.desc);
            sbf.append(blank);
            if (tableColumns.get(i).notNull) {
                sbf.append("NOT NULL");
            }
        }
        sbf.append(blank);
        sbf.append(")");
        Statement statement = connection.createStatement();
        statement.execute(sbf.toString());
        statement.close();
    }

    private PreparedStatement executeHelp(String sql, Object... values) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
        }
        return preparedStatement;
    }

    public ResultSet executeQuery(String sql, Object... values) throws Exception {
        PreparedStatement preparedStatement = executeHelp(sql, values);
        return preparedStatement.executeQuery();
    }

    public int executeUpdate(String sql, Object... values) throws Exception {
        PreparedStatement preparedStatement = executeHelp(sql, values);
        int result = preparedStatement.executeUpdate();
        preparedStatement.close();
        return result;
    }

    public boolean execute(String sql, Object... values) throws Exception {
        PreparedStatement preparedStatement = executeHelp(sql, values);
        boolean result = preparedStatement.execute();
        preparedStatement.close();
        return result;
    }

    public List<Map<String, Object>> executeQueryList(String sql, Object... values) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSet resultSet = executeQuery(sql, values);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        while (resultSet.next()) {
            list.add(new HashMap<String, Object>() {
                {
                    for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                        put(resultSetMetaData.getColumnName(i + 1), resultSet.getObject(resultSetMetaData.getColumnName(i + 1)));
                    }
                }
            });
        }
        return list;
    }

    public Map<String, Object> executeQueryMap(String sql, Object... values) throws Exception {
        ResultSet resultSet = executeQuery(sql, values);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        if (resultSet.next())
            return new HashMap<String, Object>() {
                {
                    for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                        put(resultSetMetaData.getColumnName(i + 1), resultSet.getObject(resultSetMetaData.getColumnName(i + 1)));
                    }
                }
            };
        else return null;
    }

    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] args) {
        try {
            SQLiteUtils sqLite = new SQLiteUtils(JettyStart.getStartPath() + JettyStart.separator + "test.db");
            sqLite.createTable("test", new ArrayList<TableColumn>() {
                {
                    add(new TableColumn("id"));
                    add(new TableColumn("name"));
                }
            });
            System.out.println(JSON.toJSONString(sqLite.execute("insert into test values(?, ?)", 1, "asdb")));
            System.out.println(JSON.toJSONString(sqLite.execute("insert into test values(?, ?)", 2, "zxcb")));
            System.out.println(JSON.toJSONString(sqLite.executeQueryList("select * from test ")));
            System.out.println(JSON.toJSONString(sqLite.executeUpdate("delete from test where id = ?", 1)));
            System.out.println(JSON.toJSONString(sqLite.executeQueryMap("select * from test where id = ? ", 2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Type {
        TEXT("text"),
        NUMERIC("numeric"),
        INTEGER("integer"),
        REAL("real"),
        NONE("none");
        private String desc;

        Type(String desc) {
            this.desc = desc;
        }
    }

    public static class TableColumn {
        private String columnName;
        private boolean notNull = false;
        private Type columnType = Type.NONE;

        public TableColumn(String columnName) {
            this.columnName = columnName;
        }

        public TableColumn(String columnName, Type columnType) {
            this.columnName = columnName;
            this.columnType = columnType;
        }

        public TableColumn(String columnName, boolean notNull, Type columnType) {
            this.columnName = columnName;
            this.notNull = notNull;
            this.columnType = columnType;
        }
    }
}
