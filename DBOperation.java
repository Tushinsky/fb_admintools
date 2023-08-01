/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Sergii.Tushinskyi
 */
public class DBOperation implements MoveOnStepImpl {

    /**
     * @return the btnSendTo
     */
    public JButton getBtnSendTo() {
        return btnSendTo;
    }

    /**
     * @param btnSendTo the btnSendTo to set
     */
    public void setBtnSendTo(JButton btnSendTo) {
        this.btnSendTo = btnSendTo;
    }

    /**
     * @return the btnMoveNext
     */
    public JButton getBtnMoveNext() {
        return btnMoveNext;
    }

    /**
     * @param btnMoveNext the btnMoveNext to set
     */
    public void setBtnMoveNext(JButton btnMoveNext) {
        this.btnMoveNext = btnMoveNext;
    }

    /**
     * @return the btnMovePreviouse
     */
    public JButton getBtnMovePreviouse() {
        return btnMovePreviouse;
    }

    /**
     * @param btnMovePreviouse the btnMovePreviouse to set
     */
    public void setBtnMovePreviouse(JButton btnMovePreviouse) {
        this.btnMovePreviouse = btnMovePreviouse;
    }

    /**
     * @return the btnOkButton
     */
    public JButton getBtnOkButton() {
        return btnOkButton;
    }

    /**
     * @param btnOkButton the btnOkButton to set
     */
    public void setBtnOkButton(JButton btnOkButton) {
        this.btnOkButton = btnOkButton;
    }

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
    private JButton btnSendTo;// кнопка перемещения выбранных элементов списка в др список
    private JButton btnMoveNext;// кнопка выполнения следующего действия
    private JButton btnMovePreviouse;// кнопка выполнения предыдущего действия
    private JButton btnOkButton;// кнопка выполнения операций
    
    public DBOperation(JDBCConnection connection, JList lstTableName, 
            JList lstTargetList, JTextArea txtStep, JLabel lblTableName, 
            JLabel lblTargetLabel, JTable tableData, JButton btnSendTo, 
            JButton btnMoveNext, JButton btnMovePrevoiuse, 
            JButton btnOkButton) {
        this.connection = connection;
        this.lstTableName = lstTableName;
        this.lstTargetList = lstTargetList;
        this.txtStep = txtStep;
        this.lblTableName = lblTableName;
        this.lblTargetLabel = lblTargetLabel;
        this.tableData = tableData;
        this.btnSendTo = btnSendTo;
        this.btnMoveNext = btnMoveNext;
        this.btnMovePreviouse = btnMovePrevoiuse;
        this.btnOkButton = btnOkButton;
        // блокируем доступ к кнопкам
        this.btnOkButton.setEnabled(false);
        this.btnSendTo.setEnabled(false);
//        this.btnMoveNext.setEnabled(false);
        this.btnMovePreviouse.setEnabled(false);
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
     * @return the table_Name - наименование таблиц базы данных
     */
    public Object[] getTable_Name() {
        return getDBTableName();
    }

    /**
     * @return the column_Name - наименование полей выбранной таблицы базы данных
     */
    public Object[] getColumn_Name() {
        return getDBTableColumnName();
    }
    
    /**
     * 
     * @param nameArray - массив значений для заполнения модели списка
     * @return модель списка, заданную по умолчанию
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
        column_Name = getConnection().getListColumnTable(table);
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

    @Override
    public void Start() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Заносит имя выбранной таблицы в список назначения
     */
    public void sendSelectTableName() {
        model = new DefaultListModel();
        // добавляем в модель
        model.addElement(lstTableName.getSelectedValue());
        table = lstTableName.getSelectedValue().toString();

        // устанавливаем модель для списка
        lstTargetList.setModel(model);

    }
    
    /**
     * Заносит наименования выбранных полей в список назначения
     */
    public void sendSelectColumnName() {
        model = new DefaultListModel();
        int[] index = lstTableName.getSelectedIndices();
        column = new String[index.length];
        // массив выделенных элементов
        for(int i = 0; i < index.length; i++){
            lstTableName.setSelectedIndex(index[i]);
            model.addElement(lstTableName.getSelectedValue());
            column[i] = lstTableName.getSelectedValue().toString();
        }

        // устанавливаем модель для списка
        lstTargetList.setModel(model);

    }
    
    /**
     * Сопоставление выбранных полей выбранной таблицы со столбцами таблицы данных
     */
    public void compareSendSelectColumnName() {
        // индекс выделенного элемента
        int j = lstTargetList.getSelectedIndex();

        Object value = lstTargetList.getSelectedValue() +
                "-" + lstTableName.getSelectedValue();

        model = new DefaultListModel();// переопределяем модель
        for(int i = 0; i < lstTargetList.getModel().getSize(); i++){
            lstTargetList.setSelectedIndex(i);
            model.addElement(lstTargetList.getSelectedValue());
        }

        model.setElementAt(value, j);// новое значение

        // устанавливаем модель для списка
        lstTargetList.setModel(model);

    }
    
    /**
     * Заполняет список именами таблиц базы данных
     * @param txtStepText текст для отображения в поле ввода
     */
    public void fullTableNameList(String txtStepText) {
        // получаем список таблиц
        Object[] tableName = getDBTableName();
        // модель списка
        setListModel(lstTableName, tableName);// заполнение списка данными
        // извещение пользователя о дальнейших действиях
        txtStep.setText(txtStepText);
        lblTableName.setText("список таблиц");
        lblTargetLabel.setText("выбранная таблица");

    }
    
    /**
     * Заполняет список именами полей выбранной таблицы базы данных
     * @param txtStepText текст для отображения в поле ввода
     */
    public void fullColumnNameList(String txtStepText) {
        Object[] columnName = getDBTableColumnName();// получаем список полей
        setListModel(lstTableName, columnName);// заполнение списка данными
        txtStep.setText(txtStepText);
        lblTableName.setText("список полей");
        lblTargetLabel.setText("выбранные поля");
        setListModel(lstTargetList, new Object[]{});// очищаем список

    }
    
    /**
     * Заполняет списки именами выбранных полей таблицы базы данных и именами столбцов
     * таблицы, содержащей данные
     * @param txtStepText 
     */
    public void fullCompareColumnnameList(String txtStepText) {
        String[] nameItem;// имена выбранных полей
        // заполняем список полями данных
        nameItem = new String[tableData.getColumnCount()];
        for(int i = 0; i < nameItem.length; i++)
            nameItem[i] = tableData.getColumnName(i);
        setListModel(lstTableName, nameItem);
        setListModel(lstTargetList, column);
        // запрещаем множественное выделение
        lstTableName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        txtStep.setText(txtStepText);
        lblTableName.setText("список полей");
        lblTargetLabel.setText("выбранные поля");

    }
}
