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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Sergii.Tushinskyi
 */
public class DBImportAction extends DBOperation {

    private final int importStep = 4;// количество шагов для операции импорта
    private int step = -1;// номер шага для выбранной операции с базой данных
    
    public DBImportAction(JList lstTableName, JList lstTargetList, 
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
        if(step <= importStep){
            importData();
            // блокируем кнопки перемещения элементов списка и следующего шага
            super.getBtnSendTo().setEnabled(false);
            super.getBtnMoveNext().setEnabled(false);
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
            super.getBtnSendTo().setEnabled(false);
            super.getBtnMoveNext().setEnabled(false);
            importData();
        } else{
            step = 0;
            super.getBtnMovePreviouse().setEnabled(false);
        }
    }

    @Override
    public void Start() {
        // начальный этап операции по импорту данных
        moveNext();
    }
    
    

    @Override
    public void addListItem() {
        addListItemImport(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void importData() {
        switch(step) {
            case 0:
                /*
                получение перечня таблиц базы данных, заполнение списка,
                выбор таблицы для импорта
                */
                super.fullTableNameList("Шаг 1: выберите таблицу для импорта данных");
                break;
            case 1:
                /*
                получение перечня полей выбранной таблицы, заполнение списка,
                выбор полей для импорта
                */
                super.fullColumnNameList("Шаг 2: выберите поля, в которые будут импортироваться" +
                        " данные");
                break;
            case 2:
                // сопоставление полей для импорта
                super.fullCompareColumnnameList("Шаг 3: выберите поля, из которых будут импортироваться" +
                        " данные, и соответствующие им поля из правого списка");
                break;
            case 3:
                // извещение о том, что можно делать импорт
                super.getTxtStep().setText("Шаг 4: нажмите Импорт для импорта данных");
                // доступ к кнопке для выполнения импорта
                super.getBtnOkButton().setEnabled(true);
                break;
            case 4:
                /**
                 * выполнение импорта данных, подсчёт количества импортируемых строк,
                 * подсчёт количества ошибок импорта, извещение о количестве импорта и ошибок
                **/
                super.getTxtStep().setText("Выполняется импорт данных. Ожидайте...");
                int counter = importDataToDB();// выполняем импорт данных в выбранную таблицу
                String text = super.getTxtStep().getText() + "\n\r";
                super.getTxtStep().setText(text + "Выполнен импорт данных в количестве " + counter);
                // доступ к кнопке для выполнения импорта
                super.getBtnOkButton().setEnabled(false);
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
        switch(step){
            case 0:// выбор таблицы

                super.sendSelectTableName();
                break;
            case 1:// выбор полей
                super.sendSelectColumnName();

                break;
            case 2:// сопоставление полей
                super.compareSendSelectColumnName();
                break;
        }

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
            // на начальном этапе кнопка Previouse блокируется, на всех остальных она доступна
            if(step > 0) super.getBtnMovePreviouse().setEnabled(true);
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
            if(super.getLstTableName().getSelectedIndices().length > 0){
            try{
                addListItemImport();
                // разрешаем доступ к выполнению следующего шага
                super.getBtnMoveNext().setEnabled(true);
            } catch(Exception ex) {
                // сообщение об ошибке
                JOptionPane.showMessageDialog(null, ex.getMessage(), "OperateFrame", JOptionPane.ERROR_MESSAGE);
            }
            
        }
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
                case 0:
                case 1:
                    // выбор таблицы
                    // выбор полей таблицы
                    super.getBtnSendTo().setEnabled(true);
                    break;
                case 2:
                    // сопоставление выбранных полей импортируемым полям
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
            на шаге сопоставления выбранных полей импортируемым полям
            */
            if(step == 2) super.getBtnSendTo().setEnabled(true);
        };
        return listener;
    }
}
