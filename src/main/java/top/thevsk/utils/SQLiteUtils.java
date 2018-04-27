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

    private Statement statement;

    private String filePath;

    public SQLiteUtils(String filePath) throws Exception {
        this.filePath = filePath;
        init();
    }

    private void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        statement = connection.createStatement();
    }

    public boolean createTable(String tableName, List<TableColumn> tableColumns) throws Exception {
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
        return execute(sbf.toString());
    }

    public boolean execute(String sql) throws Exception {
        return statement.execute(sql);
    }

    public ResultSet executeQuery(String sql) throws Exception {
        return statement.executeQuery(sql);
    }

    public List<Map<String, Object>> executeQueryList(String sql) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSet resultSet = executeQuery(sql);
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

    public Map<String, Object> executeQueryMap(String sql) throws Exception {
        ResultSet resultSet = executeQuery(sql);
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

    public int executeUpdate(String sql) throws Exception {
        return statement.executeUpdate(sql);
    }

    public void close() throws Exception {
        statement.close();
        connection.close();
    }

    public static void main(String[] args) {
        try {
            SQLiteUtils sqLite = new SQLiteUtils(JettyStart.getStartPath() + JettyStart.separator + "test.db");
            System.out.println(JSON.toJSONString(sqLite.execute("insert into test values(1, 'asd');")));
            System.out.println(JSON.toJSONString(sqLite.executeQueryList("select * from test;")));
            System.out.println(JSON.toJSONString(sqLite.executeUpdate("delete from test;")));
            System.out.println(JSON.toJSONString(sqLite.executeQueryMap("select * from test;")));
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
