/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import admintools.JDBCConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author ������
 */
abstract class MyTableModel extends AbstractTableModel {
    /**
     * @return content
     */
    private Object[][] content;//данные набора
    private String[] columnName;//наименование столбцов
    private Class[] columnClass;//типы данных
    private ResultSet rs;
    private Statement stmt;
    private JDBCConnection connection;// экземпляр соединения с базой данных
    private JTable table;// элемент управления - таблица
    private String tablename;// имя таблицы базы данных для операций обновления
    private boolean change = false;// флаг изменения данных в таблице
    private int[] colNoEditableList;// массив нередактируемых столбцов
    private int[] rowNoEditableList;// массив нередактируемых строк
    
    /**
     * конструктор принимает объект соединения и имя выбранной таблицы базы данных
     * @param conn
     * @param tableName
     * @throws SQLException 
     */
    public MyTableModel (JDBCConnection conn,String tableName) throws SQLException{
        super();
        connection = conn;
        getData();
        super.addTableModelListener(new MyModelListener());
    }
    
    /**
     * конструктор принимает набор данных
     * @param resultset
     * @throws SQLException 
     */
    public MyTableModel (ResultSet resultset) throws SQLException{
        super();
        rs = resultset;
        getData();
        super.addTableModelListener(new MyModelListener());
    }
    
    public MyTableModel(Object[][] content, String[] columnName,
            Class[] columnClass){
        this.content = content;
        this.columnName = columnName;
        this.columnClass = columnClass;
//        addTableModelListener(new MyModelListener());
    }

    public MyTableModel(Object[][] content) {
        this.content = content;
    }
    
    
    
    public void CloseRS() throws SQLException{
        
//        закрываем набор данных
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
    }
    
    @Override
    public int getRowCount() {
        return content.length;
    }
    @Override
    public String getColumnName (int columnIndex) {
        return columnName[columnIndex];
                
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }
    
    @Override
    public int getColumnCount() {
        if (content.length == 0) {
            return 0;
        } else {
            return content[0].length;
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return content[rowIndex][columnIndex];
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean result = true;// первоначально устанавливаем редактируемость ячейки
        if(colNoEditableList != null){
            // массив с нередактируемыми столбцами задан
            // сравниваем номер колонки с номером нередактируемых колонок
            for (int i = 0; i < colNoEditableList.length; i++) {
                if (columnIndex == colNoEditableList[i]){
                    result = false;
                    break;
                }
            }

        } else {
            result = false;
        }
        
        return result;
    }
    
    
    
    /**
     * обновление данных
     * @param sqlQuery - строка-запрос на обновление данных модели
     */
    public void RefreshData(String sqlQuery){
        
        try {
            rs = connection.ExecuteQuery(sqlQuery);
            getData();
        } catch (SQLException ex) {
            Logger.getLogger(MyTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param aValue
     * @param rowIndex
     * @param columnIndex 
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // устанавливаем значение в ячейке таблицы
        content[rowIndex][columnIndex]=aValue;
        
        // сообщаем таблице, что значение изменено
        fireTableCellUpdated(rowIndex, columnIndex);
        
    }
    
    public Object upgradeCellValue(Object aValue, int i) {
        Object retValue = null;
        if (columnClass[i] == String.class){
            retValue = "'" + aValue + "'";
        } else if (columnClass[i] == Integer.class || 
                columnClass[i] == Double.class || 
                columnClass[i] == Float.class || 
                columnClass[i] == Long.class || 
                columnClass[i] == Short.class || 
                columnClass[i] == BigDecimal.class){
            retValue = aValue;
        } else if (columnClass[i] == Boolean.class) {
            if (aValue.equals(true)) {
                retValue = 1;
            } else {retValue = 0;}
        }
        else if(columnClass[i] == Date.class || 
                columnClass[i] == Timestamp.class || 
                columnClass[i] == Time.class){
            retValue = "'" + aValue + "'";
        }
        else System.out.println("Тип данных не определён " + 
                columnName[i]);
        return retValue;
    }

    public void setTableColumnIdentifiers(TableColumnModel tableColumnModel) {
        //задаём идентификаторы для столбцов таблицы
        for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
            TableColumn tc = tableColumnModel.getColumn(i);
            tc.setIdentifier((Object) i);
        }
    }

    /**
     * @param colNoEditableList the colNoEditableList to set
     */
    public void setCellNoEditableList(int[] colNoEditableList) {
        this.colNoEditableList = colNoEditableList;
    }

    /**
     * @return the change
     */
    public boolean isChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(boolean change) {
        this.change = change;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(JDBCConnection connection) {
        this.connection = connection;
    }

    /**
     * @param table the table to set
     */
    public void setTable(JTable table) {
        this.table = table;
    }

    /**
     * @param tablename the tablename to set
     */
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
    
    /**
     * Добавляет новую строку с данными в конец модели данных
     * @param values массив значений для вставки
     */
    public void addRow(Object[] values){
        // создаём временный массив для хранения данных, больший исходного на 1
        Object[][] tmp = new Object[content.length + 1][];
        System.arraycopy(content, 0, tmp, 0, content.length);// копируем данные в новый массив
        tmp[tmp.length - 1] = values;// добавляем данные в конец массива
        content = tmp;
        fireTableRowsInserted(tmp.length - 1, tmp.length - 1);// извещаем об изменениях в модели
    }
    
    /**
     * Вставляет новую строку данных в модель после выбранной строки.
     * Если индекс строки, после которой выполняется вставка данных, равен -1,
     * значит вставка строки осуществляется в начало диапазона данных.
     * @param Index индекс строки, после которой осуществляется вставка (начинается с нуля)
     * @param values массив значений для вставки
     */
    public void insertRow(int Index, Object[] values){
        // создаём временный массив для хранения данных, больший исходного на 1
        Object[][] tmp = new Object[content.length + 1][];
        int newPos = Index +1;// позиция вставки новых данных
        
        // проверяем индекс, после которого осуществляется вставка данных
        if(Index == -1){
            // происходит вставка данных в начало массива
            // вставляем новые данные
            tmp[newPos] = values;
            // записываем оставшуюся часть массива
            System.arraycopy(content, newPos, tmp, newPos + 1, content.length);
        } else {
            // записываем первую часть массива данных
            System.arraycopy(content, 0, tmp, 0, Index + 1);
    
            // вставляем новые данные
            tmp[newPos] = values;

            // записываем оставшуюся часть массива
            System.arraycopy(content, newPos, tmp, newPos + 1, content.length - newPos);
        }
        content = tmp;// перезаписываем массив
        fireTableRowsInserted(newPos, newPos);// извещаем об изменениях в модели
    }
    
    /**
     * Удаляет строку с выбранным индексом из модели данных
     * @param Index индекс строки, которая удаляется (начинается с нуля)
     */
    public void removeRow(int Index){
        // создаём временный массив для хранения данных, меньший исходного на 1
        Object[][] tmp = new Object[content.length - 1][];
        int i = 0;// начальное значение счётчика цикла
        int j = 0;
        while(j < tmp.length){
            if(i != Index){
                tmp[j] = content[i];
                
                j++;// увеличиваем счетчик
            }
            i++;// увеличиваем счётчик цикла
        }
        content = tmp;// перезаписываем массив данных модели
        fireTableRowsDeleted(Index, Index);// извещаем таблицу об удалении строки
    }

    
    /**
     * получение данных для модели
     */
    private void getData() throws SQLException{
        DBTableModel dbModel = new DBTableModel(rs);
        content = dbModel.getContent();// передаём содержимое запроса
        columnClass = dbModel.getColumnClass();// типы данных полей запроса
        columnName = dbModel.getColumnName();
    }

    /**
     * внутренний класс, реализующий слушатель изменений модели,
     * и отвечающий за внесение изменений в избранную таблицу базы данных
     */
    private class MyModelListener implements TableModelListener{
        
        public MyModelListener(){
            
        }

        @Override
        public void tableChanged(TableModelEvent e) {
        // получаем значение для обновления
            Object retValue = 
                    upgradeCellValue
                    (getValueAt(e.getFirstRow(), 
                    e.getColumn()), e.getColumn());
            // строка-запрос на обновление данных
            String sqlString = "Update " + tablename + " Set " +
                    table.getColumnName(e.getColumn()) + "=" +
                    retValue + " Where " + table.getColumnName(0) +
                    "=" + table.getValueAt(table.getEditingRow(), 0);
            try {
                // выполняем запрос на обновление данных
                System.out.println(sqlString);
                int i = connection.ExecuteUpdate(sqlString);
            } catch (SQLException ex) {
                Logger.getLogger(MyModelListener.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }

    /**
     * @return the rowNoEditableList
     */
    public int[] getRowNoEditableList() {
        return rowNoEditableList;
    }

    /**
     * @param rowNoEditableList the rowNoEditableList to set
     */
    public void setRowNoEditableList(int[] rowNoEditableList) {
        this.rowNoEditableList = rowNoEditableList;
    }
}
