/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import frame.DBTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sergey
 */
public class DBExportAction extends DBOperation {

    private final int exportStep = 3;// количество шагов для операции импорта
    private int step = -1;// номер шага для выбранной операции с базой данных

    public DBExportAction(JList lstTableName, JList lstTargetList, 
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
        if(step <= exportStep){
            exportData();
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
            exportData();
        } else{
            step = 0;
        }
    }

    @Override
    public void Start() {
        moveNext(); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
    * операция экспорта данных
    */
    private void exportData(){
        switch(step){
            case 0:
                // получение перечня таблиц базы данных, заполнение списка, выбор таблицы для экспорта
                super.fullTableNameList("Шаг 1: выберите таблицу для экспорта данных");
                break;
            case 1:
                /*
                получение перечня полей выбранной таблицы, заполнение списка,
                выбор полей для экспорта
                */
                super.fullColumnNameList("Шаг 2: выберите поля, из которых будут " +
                        "экспортироваться данные");
                break;
            case 2:
                // получаем данные для экспорта
                getDataFromDBTable();
                super.getTxtStep().setText("Шаг 3: нажмите Экспорт для экспорта данных");
                super.getBtnOkButton().setEnabled(true);
                break;
            case 3:
                String filename = getFileName();
                if(filename != null) {
                    
                    System.out.println("filename" + filename);
                    String text = super.getTxtStep().getText() + "\n\r";
                    super.getTxtStep().setText( text + "Ожидайте, выполняется экспорт данных...");
                    System.out.println("start time = " + System.nanoTime());
                    int count = exportDataFromDB(filename);
                    System.out.println("finished time = " + System.nanoTime());
                    text = super.getTxtStep().getText() + "\n\r";
                    super.getTxtStep().setText(text + "Выполнен экспорт данных в количестве " + count);
                }
                break;
        }
    }
    
    /**
    * получение данных из выбранной таблицы базы данных
    */
    private void getDataFromDBTable(){
        // получаем перечень полей выбранной таблицы
        String fieldList = "";
        int row = super.getLstTargetList().getModel().getSize();// количество элементов
        for(int i = 0; i < row; i++)
            fieldList = fieldList + 
                    super.getLstTargetList().getModel().getElementAt(i).toString() + ",";
        // удаляем последний символ
        String fields = fieldList.substring(0, fieldList.length() - 1);
        // строка-запрос на выборку данных
        String sqlQuery = "SELECT " + fields + " FROM " + super.getTablename() + ";";
            try {
                DBTableModel tModel = 
                        new DBTableModel(super.getConnection().ExecuteQuery(sqlQuery));
                DefaultTableModel dtModel = 
                        new DefaultTableModel(tModel.getContent(), 
                        tModel.getColumnName());
                // модель данных для таблицы
                super.getTableData().setModel(dtModel);
            } catch (SQLException ex) {
                Logger.getLogger(DBExportAction.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
    }

    /**
     * Вполняет экспорт данных в указанный файл
     * @param filename имя файла для экспорта данных
     * @return количество записей, которые были экспортированы
     */
    private int exportDataFromDB(String filename) {
        int columnCount = super.getTableData().getColumnCount();// количество столбцов таблицы
        int rowCount = super.getTableData().getRowCount();// количество строк таблицы
        Object[][] data = new Object[rowCount][];// массив данных для экспорта
        // формируем данные
        for(int i = 0; i < rowCount; i++){
            Object[] row = new Object[columnCount];
            for(int j = 0; j < columnCount; j++) {
                row[j] = super.getTableData().getValueAt(i, j);
            }
            data[i] = row;// записываем сформированные данные в массив
        }
        // формируем заголовки столбцов
        String[] columnName = new String[columnCount];
        for(int i = 0; i < columnCount; i++) {
            columnName[i] = super.getTableData().getColumnName(i);
        }
        CSVOperate operate = new CSVOperate(filename, ";");// создаём класс для экспорта данных
        operate.setData(data);// задаём данные для экспорта
        operate.setColumnName(columnName);// задаём заголовки столбцов
        
        return operate.writeData();// пытаемся выполнить экспорт
    }
    
    /**
     * Возвращает имя файла CSV, в который будут экспортироваться данные
     * @return имя файла для экспорта данных
     */
    private String getFileName() {
        // отображаем окно выбора файла
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        // фильтр файлов по формату
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".csv") || 
                        f.getName().toLowerCase().endsWith(".txt") || 
                        f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Текстовые файлы с разделителями (*.csv,*.txt)";
            }
        };
        chooser.setFileFilter(filter);// устанавливаем фильтр для окна выбора файла
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) try {
            System.out.println(chooser.getSelectedFile().getCanonicalPath());
            return chooser.getSelectedFile().getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(DBExportAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
//            if(super.getLstTableName().getSelectedIndices().length > 0){
//            try{
                addListItemExport();
                // разрешаем доступ к выполнению следующего шага
                super.getBtnMoveNext().setEnabled(true);
//            } catch(Exception ex) {
//                // сообщение об ошибке
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "OperateFrame", JOptionPane.ERROR_MESSAGE);
//            }
            
//        }
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
                    // экспорт данных
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

    /**
     * 
     */
    private void addListItemExport() {
        //To change body of generated methods, choose Tools | Templates.
        switch(step){
            case 0:// выбор таблицы

                super.sendSelectTableName();
                break;
            case 1:// выбор полей
                super.sendSelectColumnName();

                break;
        }
    }
}
