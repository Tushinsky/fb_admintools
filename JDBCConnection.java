/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCConnection {
    /**
     * @return the database connection
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException 
     */
    private static Statement stat;
    private static Connection conn;
    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    private DatabaseMetaData metadata;
    private Object[] listTable;// ������ ������ ���� ������
    private String tableName;// ��� ��������� �������
    private Object[] listColumnTable;// ������ ����� ��������� �������
    private Class[] columnClass;//���� ������
    
    public JDBCConnection(String driver, String url, String user, String password) throws FileNotFoundException, 
            IOException, SQLException, ClassNotFoundException {
        JDBCConnection.driver=driver;
        JDBCConnection.url=url;
        JDBCConnection.user=user;
        JDBCConnection.password=password;
        if (driver != null)
            Class.forName(driver);
//        System.setProperty("jdbc.driver", driver);
        try {
        conn = DriverManager.getConnection(url, user, password);
        
        if (conn != null) stat= getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
                ResultSet.CONCUR_READ_ONLY);
        } finally {}
    }
    
    public JDBCConnection() throws FileNotFoundException, 
            IOException, SQLException, ClassNotFoundException {
        if (driver != null)
            Class.forName(driver);
//            System.setProperty("jdbc.driver", driver);
        conn = DriverManager.getConnection(url, user, password);
        
        if (conn != null) stat= getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
                ResultSet.CONCUR_READ_ONLY);

    }

    /**
     * @return the stat
     */
    public static Statement getStat() {
        return stat;
    }

    /**
     * @return the conn
     */
    public static Connection getConn() {
        return conn;
    }
    
    public static PreparedStatement getPrepstat(String sqlQuery){
        try {
            return getConn().prepareStatement(sqlQuery);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * 
     * @return true ���� ���������� �������, ����� false
     * @throws SQLException 
     */
    public boolean isClosedConn() throws SQLException{
        return conn.isClosed();
    }
    
    /**
     * @param sqlString ������-������ � �����������
     * @param param ������ ����������
     * @return ����� ������, ���������� � ���������� ���������� �������
     * @throws SQLException 
     */
    public ResultSet ExecuteQuery(String sqlString, int[] param) throws SQLException{
//        PreparedStatement prepStat= conn.prepareStatement(sqlString);
//        � ���������� ������-������� �������� ��� ������� ? �� ��������
//        System.out.println(sqlString);
//        System.out.println("�����= " + sqlString.length());
        int startPosition=0;//��������� ������� ������
        int endPosition;
        String sqlQuery = "";
        for (int i=0; i < param.length; i++){
            endPosition = sqlString.indexOf("?", startPosition);
//            System.out.println("endposition= " + endPosition);
            if (endPosition!=0){
                sqlQuery = sqlQuery + 
                        sqlString.substring(startPosition, 
                        endPosition) + param[i];
                startPosition = endPosition + 1;//����������� ������� ������ �� 1
//                System.out.println("startposition= " + startPosition);
            }
        }
        //��������� ���������� ����� ������
        sqlQuery = sqlQuery + 
                sqlString.substring(startPosition, sqlString.length());
//        System.out.println(sqlQuery);
//        return prepStat.executeQuery(sqlString);
        return getStat().executeQuery(sqlQuery);
    }
    
    public ResultSet ExecuteQuery(String sqlString) throws SQLException{
//        System.out.println(sqlString);
        return getStat().executeQuery(sqlString);
    }
    
    public int ExecuteUpdate(String sqlString, int[] param) throws SQLException{
        // ���������� �������, � ������� ��������� ���� �������
        int startPosition=0;// ��������� ������� ������
        int endPosition;
        String sqlQuery = "";
        for (int i=0; i < param.length; i++){
            endPosition = sqlString.indexOf("?", startPosition);
            if (endPosition!=0){
                sqlQuery = sqlQuery + 
                        sqlString.substring(startPosition, 
                        endPosition) + param[i];
                startPosition = endPosition + 1;// ����������� ������� ������
            }
        }
        sqlQuery = sqlQuery + 
                sqlString.substring(startPosition, sqlString.length());
        System.out.println(sqlQuery);
        return getStat().executeUpdate(sqlQuery);
    }
    
    public void CloseConnection() throws SQLException{
        
        if (conn != null) {
            stat = null;
            conn.close();
        }
        conn = null;
    }

    public int ExecuteUpdate(String sqlString, String[] param) throws SQLException {
        //� ���������� ������-������� �������� ��� ������� ? �� ��������
        int startPosition=0;//��������� ������� ������
        int endPosition;
        String sqlQuery = "";
        for (String param1 : param) {
            endPosition = sqlString.indexOf("?", startPosition);
            if (endPosition!=0) {
                sqlQuery = sqlQuery + 
                        sqlString.substring(startPosition,
                                endPosition) + param1;
                startPosition = endPosition + 1;//����������� ������� ������ �� 1
            }
        }
        sqlQuery = sqlQuery + sqlString.substring(startPosition, 
                sqlString.length());
//        System.out.println(sqlQuery);
        return getStat().executeUpdate(sqlQuery);
    }
     
    public boolean ExecuteQuery(String sqlString, String[] param) throws SQLException{
//        PreparedStatement prepStat= conn.prepareStatement(sqlString);
//        � ���������� ������-������� �������� ��� ������� ? �� ��������
        System.out.println(sqlString);
//        System.out.println("�����= " + sqlString.length());
        int startPosition=0;//��������� ������� ������
        int endPosition;
        String sqlQuery = "";
        for (String param1 : param) {
            endPosition = sqlString.indexOf("?", startPosition);
//            System.out.println("endposition= " + endPosition);
            if (endPosition!=0) {
                sqlQuery = sqlQuery + 
                        sqlString.substring(startPosition,
                                endPosition) + param1;
                startPosition = endPosition + 1;//����������� ������� ������ �� 1
//                System.out.println("startposition= " + startPosition);
            }
        }
        //��������� ���������� ����� ������
        sqlQuery = sqlQuery + 
                sqlString.substring(startPosition, sqlString.length());
//        System.out.println(sqlQuery);
        //        return prepStat.executeQuery(sqlString);
        return getStat().execute(sqlQuery);
    }
    
    public int ExecuteUpdate(String sqlString) throws SQLException {
        return getStat().executeUpdate(sqlString);
    }

    /**
     * @return the metadata
     */
    public DatabaseMetaData getMetadata() {
        try {
            metadata = conn.getMetaData();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return metadata;
    }

    /**
     * @return the listTable
     */
    public Object[] getListTable() {
        try {
            metadata = conn.getMetaData();
            listTable =scanRS(metadata.getTables("TABLE_CATALOG",
                "TABLE_SCHEM", "%", null), "TABLE_NAME");
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listTable;
    }

    /**
     * @param tableName
     * @return the listColumnTable
     */
    public Object[] getListColumnTable(String tableName) {
        try {
            this.tableName = tableName;
            ResultSet rs = ExecuteQuery("SELECT * FROM " +
                    tableName + ";");
            listColumnTable = scanRS(rs, null);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listColumnTable;
    }
    
    /**
     * @param colname ������������ ������� � �������
     * @param value ��������, ������� ���������� �������� � ��������� ���� ������
     * @return ��������, ���������� � ������� ���� ������
     */
    public String getCellValue(String colname, String value){
        String cellvalue;
        cellvalue = upgradeCellValue(colname, value);
        return cellvalue;
    }
    
    private Object[] scanRS(ResultSet rs, String columnName)throws SQLException{
        ArrayList list = new ArrayList();
        if(rs != null) {
            int i;
            // �������� ������ ������
            ResultSetMetaData rsmd = rs.getMetaData();
            // ��������� ��������
            // ���������� ������ ������
            if(columnName != null){
                // �������� ������
                while(rs.next())
                    list.add(rs.getString(columnName));
            } else{
                // �������� ����� ��������� �������
                int numCol = rsmd.getColumnCount();
                ArrayList colTypelist = new ArrayList();//������ ����� ������
        //             ��������� ��������
                for (int j = 1; j <= numCol; j++){
                    list.add(rsmd.getColumnName(j));
                    int dbType = rsmd.getColumnType(j);// �������� ��� ������ �������
                    getColumnType(colTypelist, dbType);// ������� ��� � ������
                }
                columnClass = new Class[colTypelist.size()];
                colTypelist.toArray(columnClass);
            }
        } else {
            System.out.println("No data returned ");
        }
        return list.toArray();
    }
    
    private void getColumnType(ArrayList list, int columnType) {
        switch (columnType){
            case Types.CHAR:
                list.add(String.class);
                break;
            case Types.INTEGER:
                list.add(Integer.class);
                break;
            case Types.DOUBLE:
                list.add(Double.class);
                break;
            case Types.FLOAT:
                list.add(Float.class);
                break;
            case Types.BIGINT:
                list.add(Long.class);
                break;
            case Types.SMALLINT:
                list.add(Short.class);
                break;
            case Types.BOOLEAN:
                list.add(Boolean.class);
                break;
            case Types.TIME:
                list.add(Time.class);
                break;
            default:
                list.add(String.class);
                break;
            }
    }

    /**
     * @return the columnClass
     */
    public Class[] getColumnClass() {
        return columnClass;
    }
    
    /**
     * @param colname ������������ ������� � �������
     * @param cellvalue ������������� ��������
     * @return ��������, ���������� � ������� ���� ������
     */
    private String upgradeCellValue(String colname, String cellvalue){
        String retValue;
        
        // ���������� ������ ������� � �������� ������ � �������
        int index = 0;
        for(int i = 0; i < listColumnTable.length; i++){
            if(listColumnTable[i].equals(colname)){
                // ���� ����� ���������
                index = i;// ���������� ������ �������
                break;// ��������� ����
            }
        }
        // ���������� ��� ������ �� ������� � �������� ��������������
        Class cls = columnClass[index];
        if(cls.equals(String.class) || cls.equals(Time.class)){
            retValue = "'" + cellvalue + "'";
        } else {
            retValue = cellvalue;
        }
        return retValue;
    }
}
