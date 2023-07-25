/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Sergii.Tushinskyi
 */
public class DBImportAction extends DBOperation {

    private final int importStep = 4;// количество шагов для операции импорта
    private int step = 0;// номер шага для выбранной операции с базой данных
    private DefaultListModel model;// модель для заполнения списка
    
    public DBImportAction(JList lstTableName, JList lstTargetList, 
            JTextArea txtStep, JLabel lblTableName, JLabel lblTargetLabel, JTable tableData,
            JDBCConnection connection) {
        super(connection, lstTableName, lstTargetList, txtStep, lblTableName, 
                lblTargetLabel, tableData);
    }

    @Override
    public void moveNext() {
        //To change body of generated methods, choose Tools | Templates.
        // увеличиваем шаг
        step++;
        if(step <= importStep){
            importData();
        } else{
            step--;
            }
    }

    @Override
    public void movePreviouse() {
        //To change body of generated methods, choose Tools | Templates.
        // уменьшаем шаг
        step--;
        if(step >= 0){

            importData();
        } else{
            step = 0;
        }
    }

    @Override
    public void setTable(String table) {
        super.setTable(table); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addListItem() {
        super.addListItem(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void importData() {
        switch(step) {
            case 0:
                // получение списка таблиц базы данных, выбор таблицы для импорта
                // получаем список таблиц
                Object[] tableName = super.getTable_Name();
                // модель списка
                super.setListModel(super.getLstTableName(), tableName);// заполнение списка данными
                // извещение пользователя о дальнейших действиях
                super.getTxtStep().setText("Шаг 1: выберите таблицу для импорта данных");
                super.getLblTableName().setText("список таблиц");
                super.getLblTargetLabel().setText("выбранная таблица");
                break;
            case 1:
                // получение перечня полей выбранной таблицы, выбор полей для импорта
                Object[] columnName = super.getColumn_Name();// получаем список полей
                super.setListModel(super.getLstTableName(), columnName);// заполнение списка данными
                super.getTxtStep().setText("Шаг 2: выберите поля, в которые будут импортироваться" +
                        " данные");
                super.getLblTableName().setText("список полей");
                super.getLblTargetLabel().setText("выбранные поля");

                break;
            case 2:
                // сопоставление полей для импорта
                String[] nameItem;// имена выбранных полей
                // заполняем список полями данных
                nameItem = new String[super.getTableData().getColumnCount()];
                for(int i = 0; i < nameItem.length; i++)
                    nameItem[i] = super.getTableData().getColumnName(i);
                super.setListModel(super.getLstTableName(), nameItem);
                super.setListModel(super.getLstTargetList(), super.getColumn());
                // запрещаем множественное выделение
                super.getLstTableName().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                super.getTxtStep().setText("Шаг 3: выберите поля, из которых будут импортироваться" +
                        " данные, и соответствующие им поля из правого списка");
                super.getLblTableName().setText("список полей");
                super.getLblTargetLabel().setText("выбранные поля");

                break;
            case 3:
                // извещение о том, что можно делать импорт
                super.getTxtStep().setText("Шаг 4: нажмите Импорт для импорта данных");
                // доступ к кнопке для выполнения импорта
                
                break;
            case 4:
                /**
                 * выполнение импорта данных, подсчёт количества импортируемых строк,
                 * подсчёт количества ошибок импорта, извещение о количестве импорта и ошибок
                **/
                super.getTxtStep().setText("Выполняется импорт данных. Ожидайте...");
                int counter = importDataToDB();
                String text = super.getTxtStep().getText() + "\n\r";
                super.getTxtStep().setText(text + "Выполнен импорт данных в количестве " + counter);
                break;
        }
    }
    
    private int importDataToDB(){
        int rowCount = super.getTableData().getRowCount();

        // перебираем строки в правом списке полей,
        // используя символ-разделитель "-", и заполняем ими соответствующие массивы
        int row = super.getLstTargetList().getModel().getSize();// количество элементов
        String[] leftCols = new String[row];// список выбранных полей таблицы БД
        String[] rightCols = new String[row];// список импортируемых полей
        int[] colIndex = new int[row];// индекы столбцов таблицы
        ArrayList error = new ArrayList();// массив ошибочных записей
        // заполняем массивы полей
        for(int i = 0; i < row; i++){
            super.getLstTargetList().setSelectedIndex(i);// выделяем элемент списка
            int pos = super.getLstTargetList().getSelectedValue().toString().indexOf("-");
            leftCols[i] = super.getLstTargetList().getSelectedValue().toString().substring(0, pos);
            rightCols[i] = super.getLstTargetList().getSelectedValue().toString().substring(pos + 1);
            // получаем индекс столбца по имени
            for(int j = 0; j < super.getTableData().getColumnCount(); j++){
                if(rightCols[i].equals(super.getTableData().getColumnName(j))){
                    colIndex[i] = j;
//                    System.out.println(colIndex[i]);
                    break;// прерываем цикл
                }
            }
        }
        String sqlQuery;// строка-запрос на добавление записи
        String listfield = "";
        int retval = 0;
        for (String leftCol : leftCols) {
            listfield = listfield + leftCol + ",";
        }
        String fields = listfield.substring(0, listfield.length()-1);

        for(int i = 0; i < rowCount; i++){
            // получаем список значений для вставки
            String listvalues = "";
            for(int j = 0; j < colIndex.length; j++){
//                System.out.println(colIndex[j]);
                String value = getConnection().getCellValue(leftCols[j],
                        super.getTableData().getValueAt(i, colIndex[j]).toString());
                listvalues = listvalues + value + ",";
            }
            String values = listvalues.substring(0, listvalues.length() - 1);
            sqlQuery = "INSERT INTO " + super.getTable() + "(" + fields + ")" +
                    " VALUES(" + values + ");";
            int count = 0;
                try {
//                    System.out.println(sqlQuery);
                    count = getConnection().ExecuteUpdate(sqlQuery);
                } catch (SQLException ex) {
                    error.add("Ошибка в строке " + (i + 1) +
                            " : код-" + ex.getErrorCode() + "\n\r" +
                            ex.getMessage() + "\n\r");
                }
            retval = retval + count;
            // определяем процент выполнения
            int percent = (int)(retval * 100) / rowCount;
        }
        // проверяем наличие сообщений в массиве ошибок
        if(error.isEmpty()){
            // если массив ошибок пуст
            super.getTxtStep().setText("Ошибки не обнаружены");
        }else{
            String text = "";
            for(int i = 0; i < error.size(); i++)
                text = text + error.get(i).toString();
            super.getTxtStep().setText(text);
        }
//        setCursor(Cursor.getDefaultCursor());// возвращаем исходный вид курсора
        return retval;
    }
    
    private void addListItemImport(){
        model = new DefaultListModel();
        switch(importStep){
            case 0:// выбор таблицы

                // добавляем в модель
                model.addElement(super.getLstTableName().getSelectedValue());
                super.setTable(super.getLstTableName().getSelectedValue().toString());

                // устанавливаем модель для списка
                super.getLstTargetList().setModel(model);
                break;
            case 1:// выбор полей
                model = new DefaultListModel();
                int[] index = super.getLstTableName().getSelectedIndices();
                super.setColumn(new String[index.length]);
                // массив выделенных элементов
                for(int i = 0; i < index.length; i++){
                    super.getLstTableName().setSelectedIndex(index[i]);
                    model.addElement(super.getLstTableName().getSelectedValue());
                    super.getColumn()[i] = super.getLstTableName().getSelectedValue().toString();
                }

                // устанавливаем модель для списка
                super.getLstTargetList().setModel(model);

                break;
            case 2:// сопоставление полей
                // индекс выделенного элемента
                int j = super.getLstTargetList().getSelectedIndex();

                Object value = super.getLstTargetList().getSelectedValue() +
                        "-" +super.getLstTableName().getSelectedValue();

                model = new DefaultListModel();// переопределяем модель
                for(int i = 0; 
                        i < super.getLstTargetList().getModel().getSize(); i++){
                    super.getLstTargetList().setSelectedIndex(i);
                    model.addElement(super.getLstTargetList().getSelectedValue());
                }

                model.setElementAt(value, j);// новое значение

                // устанавливаем модель для списка
                super.getLstTargetList().setModel(model);
                break;
        }

    }
}
