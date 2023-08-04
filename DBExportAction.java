/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import frame.DBTableModel;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
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
        super.Start(); //To change body of generated methods, choose Tools | Templates.
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
                int count = 0;
                String text = super.getTxtStep().getText() + "\n\r";
                super.getTxtStep().setText(text + "Выполнен экспорт данных в количестве " + count);
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


}
