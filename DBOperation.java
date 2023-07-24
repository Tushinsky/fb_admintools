/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
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

    public DBOperation(JDBCConnection connection, JList lstTableName, 
            JList lstTargetList, JTextArea txtStep, JLabel lblTableName, 
            JLabel lblTargetLabel) {
        this.connection = connection;
        this.lstTableName = lstTableName;
        this.lstTargetList = lstTargetList;
        this.txtStep = txtStep;
        this.lblTableName = lblTableName;
        this.lblTargetLabel = lblTargetLabel;
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
    public DefaultListModel model (Object[] nameArray) {
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
     * @param model
    */
    public void setListModel(JList list, DefaultListModel model){
            list.setModel(model);
    }

    
    private Object[] getDBTableName() {
        table_Name = connection.getListTable();
        return table_Name;
        
    }
    
    private Object[] getDBTableColumnName() {
        column_Name = connection.getListColumnTable(getTable());
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
}
