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
public class DBImportAction extends DBOperation {

    private final int importStep = 4;// количество шагов для операции импорта
    private int step = 0;// номер шага для выбранной операции с базой данных
    
    public DBImportAction(JList lstTableName, JList lstTargetList, 
            JTextArea txtStep, JLabel lblTableName, JLabel lblTargetLabel, 
            JDBCConnection connection) {
        super(connection, lstTableName, lstTargetList, txtStep, lblTableName, lblTargetLabel);
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
    
    private void importData() {
        switch(step) {
            case 0:
                // получение списка таблиц базы данных, выбор таблицы для импорта
                // получаем список таблиц
                Object[] tableName = super.getTable_Name();
                // модель списка
                DefaultListModel model = super.model(tableName);
                super.setListModel(super.getLstTableName(), model);
                super.getTxtStep().setText("Шаг 1: выберите таблицу для импорта данных");
                super.getLblTableName().setText("список таблиц");
                super.getLblTargetLabel().setText("выбранная таблица");
                break;
            case 1:
                // получение перечня полей выбранной таблицы, выбор полей для импорта
                Object[] columnName = super.getColumn_Name();// получаем список полей
                break;
            case 2:
                // сопоставление полей для импорта
                break;
            case 3:
                // извещение о том, что можно делать импорт
                break;
            case 4:
                /**
                 * выполнение импорта данных, подсчёт количества импортируемых строк,
                 * подсчёт количества ошибок импорта, извещение о количестве импорта и ошибок
                **/
                break;
        }
    }
}
