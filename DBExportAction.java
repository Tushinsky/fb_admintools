/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import frame.DBTableModel;
import frame.OperateFrame;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sergey
 */
public class DBExportAction extends DBOperation {

    private final int exportStep = 3;// количество шагов для операции импорта
    private int step = -1;// номер шага для выбранной операции с базой данных

    public DBExportAction() {
    }

    @Override
    public void moveNext() {
        super.moveNext(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void movePreviouse() {
        super.movePreviouse(); //To change body of generated methods, choose Tools | Templates.
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
        String sqlQuery = "SELECT " + fields + " FROM " + super.getTable() + ";";
            try {
                DBTableModel tModel = 
                        new DBTableModel(super.getConnection().ExecuteQuery(sqlQuery));
                DefaultTableModel dtModel = 
                        new DefaultTableModel(tModel.getContent(), 
                        tModel.getColumnName());
                // модель данных для таблицы
                super.getTableData().setModel(dtModel);
            } catch (SQLException ex) {
                Logger.getLogger(OperateFrame.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
    }


}
