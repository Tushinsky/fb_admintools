/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author Sergii.Tushinskyi
 */
public class DBOperation implements MoveOnStepImpl {

    /**
     * @return the lstTableName
     */
    public JList getLstTableName() {
        return lstTableName;
    }

    /**
     * @param lstTableName the lstTableName to set
     */
    public void setLstTableName(JList lstTableName) {
        this.lstTableName = lstTableName;
    }

    /**
     * @return the lstTargetList
     */
    public JList getLstTargetList() {
        return lstTargetList;
    }

    /**
     * @param lstTargetList the lstTargetList to set
     */
    public void setLstTargetList(JList lstTargetList) {
        this.lstTargetList = lstTargetList;
    }

    /**
     * @return the txtStep
     */
    public JTextArea getTxtStep() {
        return txtStep;
    }

    /**
     * @param txtStep the txtStep to set
     */
    public void setTxtStep(JTextArea txtStep) {
        this.txtStep = txtStep;
    }

    /**
     * @return the lblTableName
     */
    public JLabel getLblTableName() {
        return lblTableName;
    }

    /**
     * @param lblTableName the lblTableName to set
     */
    public void setLblTableName(JLabel lblTableName) {
        this.lblTableName = lblTableName;
    }

    /**
     * @return the lblTargetLabel
     */
    public JLabel getLblTargetLabel() {
        return lblTargetLabel;
    }

    /**
     * @param lblTargetLabel the lblTargetLabel to set
     */
    public void setLblTargetLabel(JLabel lblTargetLabel) {
        this.lblTargetLabel = lblTargetLabel;
    }

    private JDBCConnection connection;
    private Object[] table_Name;// массив с именами таблиц базы данных
    private Object[] column_Name;// массив с именами полей выбранной таблицы
    private String table;// имя выбранной таблицы
    private JList lstTableName;// список с именами таблиц базы данных
    private JList lstTargetList;// целевой список
    private JTextArea txtStep;// поле ввода с описание шагов операции
    private JLabel lblTableName;// метка для списка с именами таблиц
    private JLabel lblTargetLabel;// метка для целевого списка
    private JTable tableData;// таблица с данными
    private DefaultListModel model;// модель для заполнения списка
    private String[] column;// массив полей выбранной таблицы
    
    public DBOperation(JDBCConnection connection, JList lstTableName, 
            JList lstTargetList, JTextArea txtStep, JLabel lblTableName, 
            JLabel lblTargetLabel, JTable tableData) {
        this.connection = connection;
        this.lstTableName = lstTableName;
        this.lstTargetList = lstTargetList;
        this.txtStep = txtStep;
        this.lblTableName = lblTableName;
        this.lblTargetLabel = lblTargetLabel;
        this.tableData = tableData;
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
    
    /**
     * 
     * @param nameArray
     * @return 
     */
    private DefaultListModel model (Object[] nameArray) {
        DefaultListModel retModel = new DefaultListModel();
        retModel.clear();
        // если передан массив и он содержит данные
        if(nameArray != null && nameArray.length > 0){
            for (Object nameArray1 : nameArray) {
                retModel.addElement(nameArray1.toString());
            }
        }
        return retModel;
    }
    
    /**
    * устанавливает модель для списка
     * @param list
     * @param nameArray
    */
    public void setListModel(JList list, Object[] nameArray){
            list.setModel(model(nameArray));
    }

    
    private Object[] getDBTableName() {
        table_Name = getConnection().getListTable();
        return table_Name;
        
    }
    
    private Object[] getDBTableColumnName() {
        column_Name = getConnection().getListColumnTable(getTable());
        return column_Name;
    }

    /**
     * @return the table
     */
    public String getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * @return the tableData
     */
    public JTable getTableData() {
        return tableData;
    }

    /**
     * @param tableData the tableData to set
     */
    public void setTableData(JTable tableData) {
        this.tableData = tableData;
    }

    /**
     * @return the connection
     */
    public JDBCConnection getConnection() {
        return connection;
    }
    
    /**
     * Добавление элемента в список
     */
    public void addListItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the column
     */
    public String[] getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(String[] column) {
        this.column = column;
    }
}
