/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lera
 */
public class CSVOperate {
    private String fileName;// имя файла csv
    private String separator;// символ - разделитель полей
    private Object[][] data;// массив данных
    private String[] columnName;// наименования столбцов
    private boolean header;// флаг наличия в первой строке наименований заголовков столбцов
    private BufferedReader reader;// класс для чтения файла
    
    public CSVOperate(){
        
    }
    
    /**
     * Конструктор класса
     * @param filename имя файла, содержащего данные
     * @param separator символ - разделитель полей данных
     */
    public CSVOperate(String filename, String separator){
        this.fileName = filename;
        this.separator = separator;
    }

    /**
     * Задаёт имя файла .CSV
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Задаёт символ - разделитель данных
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Возвращает массив, содержащий данные
     * @return the data
     */
    public Object[][] getData() {
        return data;
    }

    /**
     * Задаёт массив, содержащий данные
     * @param data the data to set
     */
    public void setData(Object[][] data) {
        this.data = data;
    }
    
    /**
     * Считывает данные из файла
     */
    public void readData(){
        try {
            readFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVOperate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Записывает данные в файл
     */
    public void writeData(){
        writeFile();
    }

    /**
     * Возвращает флаг наличия в первой строке данных заголовков столбцов
     * @return the header
     */
    public boolean isHeader() {
        return header;
    }

    /**
     * Задаёт флаг наличия в первой строке данных заголовков столбцов
     * @param header the header to set
     */
    public void setHeader(boolean header) {
        this.header = header;
    }
    
    /**
     * Чтение данных из указанного файла
     * @throws FileNotFoundException если указанный файл не найден
     */
    private void readFile() throws FileNotFoundException{
        try {
            reader = new BufferedReader(new FileReader(fileName));
//            System.out.println("header=" + header);
            // проверяем флаг наличия заголовков столбцов
            if(header == false){
                getCellData();// чтение файла данных
                // получаем наименования столбцов
                columnName = new String[data[1].length];
                for(int i = 0; i <columnName.length; i++){
                    int j = i + 1;
                    columnName[i] = ("A" + j);
                }
            } else{
                // если в первой строке заголовков нет
                String line;
                line = reader.readLine();// читаем первую строку
                columnName = (String[]) line.split(separator);// преобразуем в массив наименований столбцов
                // получаем данные
                getCellData();
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVOperate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeFile(){
        
    }

    /**
     * @return the columnName
     */
    public String[] getColumnName() {
        return columnName;
    }

    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(String[] columnName) {
        this.columnName = columnName;
    }
    
    private void getCellData() throws IOException{
        ArrayList rowList = new ArrayList();// массив, в котором будут храниться преобразованные данные
        String line;// переменная, в которую будут считываться данные
        while((line = reader.readLine()) != null){
            // читаем файл, пока данные есть
            
            // преобразуем считываемую строку в массив, используя заданный разделитель
            String[] values = line.split(separator);
            
            ArrayList colList = new ArrayList();// промежуточный список для хранения данных
            for (String value : values) {
                Object cellValue = (Object) value;// приведение типа данных
                colList.add(cellValue);// добавление в список
            }
            Object[] cells = null;
            if(!colList.isEmpty()) cells = colList.toArray();// преобразуем список в массив
            rowList.add(cells);// добавляем его в начальный список
        }
        data = new Object[rowList.size()][];
        for (int i = 0; i < data.length; i++) {
            // вносим данные в массив
            data[i] = (Object[]) (Object) rowList.get(i);
        }
    }
}
