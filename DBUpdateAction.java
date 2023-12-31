/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Sergey
 */
public class DBUpdateAction extends DBOperation{
    
    private final int updateStep = 5;// количество шагов для операции импорта
    private int step = -1;// номер шага для выбранной операции с базой данных
//    private DefaultListModel model;// модель для заполнения списка
    private String keyColumn;// имя ключевого поля для обновления данных

    public DBUpdateAction(JList lstTableName, JList lstTargetList, 
            JTextArea txtStep, JLabel lblTableName, JLabel lblTargetLabel, JTable tableData,
            JDBCConnection connection, JButton btnSendTo, 
            JButton btnMoveNext, JButton btnMovePrevoiuse, 
            JButton btnOkButton) {
        super(connection, lstTableName, lstTargetList, txtStep, lblTableName, 
                lblTargetLabel, tableData, btnSendTo, btnMoveNext, 
                btnMovePrevoiuse, btnOkButton);
        // добавляем слушатели элементам интерфейса
        super.getBtnMoveNext().addActionListener(MoveNextListener());
        super.getBtnMovePreviouse().addActionListener(MovePreviouseListener());
        super.getBtnSendTo().addActionListener(ButtonSendToListener());
        super.getBtnOkButton().addActionListener(ButtonOkListener());
        super.getLstTableName().addListSelectionListener(ListNameListener());
        super.getLstTargetList().addListSelectionListener(ListTargetListener());
    }

    @Override
    public void moveNext() {
        //To change body of generated methods, choose Tools | Templates.
        // увеличиваем шаг
        step++;
        if(step <= updateStep){
            updateData();
            // блокируем кнопки перемещения элементов списка и следующего шага
            super.setButtonEnabled();
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
            // блокируем кнопки перемещения элементов списка и следующего шага
            super.setButtonEnabled();
            updateData();
        } else{
            step = 0;
        }
    }

    @Override
    public void addListItem() {
        addListItemUpdate(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Start() {
        //To change body of generated methods, choose Tools | Templates.
        moveNext();
    }
    
    
    
    private void addListItemUpdate() {
//        model = new DefaultListModel();
        switch(step){
            case 0:// выбор таблицы

                super.sendSelectTableName();
                break;
            case 1:// выбор полей
                super.sendSelectColumnName();

                break;
            case 2:// выбор ключевого поля
                // добавляем в модель
//                model.addElement(super.getLstTableName().getSelectedValue());

                // первая часть - ключевое поле для обновления в таблице
                // вторая часть - сопоставленное ему поле в массиве данных,
                // значение которого будет использоваться для идентификации ключевого поля
                keyColumn = super.getLstTableName().getSelectedValue().toString() +
                        "-" + super.getLstTargetList().getSelectedValue().toString();
                System.out.println("keyColumn -" + keyColumn);
                break;
            case 3:// сопоставление полей
                super.compareSendSelectColumnName();
                break;
        }
    }
    
    /**
    * операция обновления данных
    */
    private void updateData(){
        switch(step){
            case 0:
                /*
                получение перечня таблиц базы данных, заполнение списка,
                выбор таблицы для обновления
                */
                super.fullTableNameList("Шаг 1: выберите таблицу для обновления данных");
                break;
            case 1:
                /*
                получение перечня полей выбранной таблицы, заполнение списка,
                выбор полей для обновления
                */
                super.fullColumnNameList("Шаг 2: выберите поля для обновления данных");
                break;
            case 2:
                // выбор ключевого поля, по которому будут обновляться данные
                String[] nameItem = new String[super.getTableData().getColumnCount()];
                for(int i = 0; i < nameItem.length; i++)
                    nameItem[i] = super.getTableData().getColumnName(i);
                setListModel(super.getLstTargetList(), nameItem);
                super.getTxtStep().setText("Шаг 3: выберите ключевое поле для обновления" +
                        " данных из левого списка и соответствующее ему поле из" +
                        " правого списка");
                super.getLblTableName().setText("список полей");
                super.getLblTargetLabel().setText("ключевое поле");
                break;
            case 3:
                // сопоставление полей данных и полей для обновления
                super.fullCompareColumnnameList("Шаг 4: выберите поля, из которых будут " +
                        "обновляться данные, и соответствующие " +
                        "им поля для обновления");
                break;
            case 4:
                // извещаем пользователя о следующем шаге
                super.getTxtStep().setText("Шаг 5: нажмите <Обновить> для обновления данных");
                super.getBtnOkButton().setEnabled(true);
                break;
            case 5:
                // обновление данных, подсчёт количества ошибок, извещение пользователя
                String text = super.getTxtStep().getText() + "\n\r";
                super.getTxtStep().setText(text + "Ожидайте, выполняется обновление данных...");
                int update = updateDataDB();
                text = super.getTxtStep().getText() + "\n\r";
                super.getTxtStep().setText( text + "Обновлены данные в количестве " + update);
                break;

        }
    }
        
    /**
    * обновление данных в выбранной таблице базы данных
    */
    private int updateDataDB(){
        int retval = 0;
//        ProgressDialog.setSize(370, 146);
//        ProgressDialog.setLocation(getLocation().x, getLocation().y);
//        ProgressDialog.setVisible(true);
        int rowCount = super.getTableData().getRowCount();

        // позиция разделителя в наименовании ключевого поля
        int pos = keyColumn.indexOf("-");

        // получаем наименования ключевых полей
        String tbKey = keyColumn.substring(0, pos);
        String targetKey= keyColumn.substring(pos + 1, keyColumn.length());

        // получаем индекс ключевого поля в таблице-источнике данных
        int Index = 0;
        for(int j = 0; j < super.getTableData().getColumnCount(); j++){
            if(targetKey.equals(super.getTableData().getColumnName(j))){
                Index = j;
                break;
            }
        }
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
            pos = super.getLstTargetList().getSelectedValue().toString().indexOf("-");
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
        int count = 0;// счётчик обновлённых строк
        for(int i = 0; i < rowCount; i++){
            // получаем список значений для вставки
            String listvalues = "";
            for(int j = 0; j < colIndex.length; j++){
//                System.out.println(colIndex[j]);
                String value = super.getConnection().getCellValue(leftCols[j],
                        super.getTableData().getValueAt(i, colIndex[j]).toString());
                listvalues = listvalues + leftCols[j] + "=" + value + ",";
            }
            // удаляем последний символ ","
            String values = listvalues.substring(0, listvalues.length() - 1);
            sqlQuery = "UPDATE " + super.getTablename() + " SET " + values + " WHERE " +
                    tbKey + "=" + super.getConnection().getCellValue(tbKey,
                        super.getTableData().getValueAt(i, Index).toString()) + ";";
            
            try {
//                    System.out.println(sqlQuery);
                count = super.getConnection().ExecuteUpdate(sqlQuery);
            } catch (SQLException ex) {
                    error.add("Ошибка в строке " + (i + 1) +
                            " : код-" + ex.getErrorCode() + "\n\r" +
                            ex.getMessage() + "\n\r");
//                    setCursor(Cursor.getDefaultCursor());// возвращаем исходный вид курсора
            }
            retval = retval + count;
            // определяем процент выполнения
//            int percent = (int)(retval * 100) / rowCount;
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
        return retval;
    }


    
    /**
     * Создаёт слушатель для действия MoveNext
     * @return созданный слушатель
     */
    private ActionListener MoveNextListener() {
        ActionListener listener;
        listener = (ActionEvent e) -> {
            //To change body of generated methods, choose Tools | Templates.
            moveNext();
        };
        return listener;
    }
    
    /**
     * Создаёт слушатель для действия MovePreviouse
     * @return созданный слушатель
     */
    private ActionListener MovePreviouseListener() {
        ActionListener listener;
        listener = (ActionEvent e) -> {
            //To change body of generated methods, choose Tools | Templates.
            movePreviouse();
        };
        return listener;
    }
    
    /**
     * Создаёт слушатель для заполнения списков
     * @return созданный слушатель
     */
    private ActionListener ButtonSendToListener() {
        ActionListener listener;
        listener = (ActionEvent e) -> {
            //To change body of generated methods, choose Tools | Templates.
//            if(super.getLstTableName().getSelectedIndices().length > 0){
                addListItemUpdate();
                // разрешаем доступ к выполнению следующего шага
                super.getBtnMoveNext().setEnabled(true);
//            }
        };
        return listener;
    }
    
    /**
     * Создаёт слушатель для подтверждения действия импорта
     * @return созданный слушатель
     */
    private ActionListener ButtonOkListener() {
        ActionListener listener;
        listener = (ActionEvent e) -> {
            //To change body of generated methods, choose Tools | Templates.
            moveNext();
        };
        return listener;
    }
    
    /**
     * создаёт слушатель для списка наименований таблиц и полей выбранной таблицы
     * @return созданный слушатель
     */
    private ListSelectionListener ListNameListener() {
        ListSelectionListener listener = (ListSelectionEvent e) -> {
            
            /*
            после выбора элемента списка разблокируем кнопку перемещения 
            */
            switch(step) {
                case 0:// выбор таблицы
                case 1:// выбор полей таблицы
                case 2:// выбор ключевого поля обновления
                    super.getBtnSendTo().setEnabled(true);
                    break;
                case 3:
                    // сопоставление выбранных полей обновляемым полям
                    super.getBtnSendTo().setEnabled(false);
                    break;
            }
            
        };
        return listener;
    }
    
    /**
     * Создаёт слушатель для списка, в который переносятся выбранные таблицы и поля
     * @return созданный слушатель
     */
    private ListSelectionListener ListTargetListener() {
        ListSelectionListener listener = (ListSelectionEvent e) -> {
            //To change body of generated methods, choose Tools | Templates.
            /*
            доступ к кнопке перемещения элементов списка разблокируем только
            на шаге сопоставления выбранных полей обновляемым полям
            */
            if(step == 3) super.getBtnSendTo().setEnabled(true);
        };
        return listener;
    }
    
}
