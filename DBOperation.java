/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

/**
 *
 * @author Sergii.Tushinskyi
 */
public class DBOperation implements MoveOnStepImpl {

    private JDBCConnection connection;
    private Object[] table_Name;// массив с именами таблиц базы данных
    private Object[] column_Name;// массив с именами полей выбранной таблицы


    public DBOperation(JDBCConnection connection) {
        this.connection = connection;
    }

    public DBOperation() {
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(JDBCConnection connection) {
        this.connection = connection;
    }

    @Override
    public void moveNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void movePreviouse() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * @return the table_Name
     */
    public Object[] getTable_Name() {
        return getDBTableName();
    }

    /**
     * @return the column_Name
     */
    public Object[] getColumn_Name() {
        return getDBTableColumnName();
    }
    
    private Object[] getDBTableName() {
        return null;
        
    }
    
    private Object[] getDBTableColumnName() {
        return null;
    }
}
