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
    private String fileName;// ��� ����� csv
    private String separator;// ������ - ����������� �����
    private Object[][] data;// ������ ������
    private Object[] columnName;// ������������ ��������
    private boolean header;// ���� ������� � ������ ������ ���������� ��������
    private BufferedReader reader;
    
    // ����������� �� ���������
    public CSVOperate(){
        
    }
    
    // ����������� � �������� ������ ��������� ����� � �������� �������� ������������
    public CSVOperate(String filename, String separator){
        this.fileName = filename;
        this.separator = separator;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * @return the data
     */
    public Object[][] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object[][] data) {
        this.data = data;
    }
    
    /**
     * ������ ������ �� ���������� �����
     */
    public void readData(){
        try {
            readFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVOperate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * ���������� ������ � ��������� ����
     */
    public void writeData(){
        writeFile();
    }

    /**
     * @return the header
     */
    public boolean isHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(boolean header) {
        this.header = header;
    }
    
    private void readFile() throws FileNotFoundException{
        try {
            reader = new BufferedReader(new FileReader(fileName));
            
            // ������ ���� ������ �� �������
            if(!header){
                getCellData();// ������ ��� ������
                // ���� ��������� �� �������, ��������� �� �� ���������
                columnName = new Object[data[1].length];
                for(int i = 0; i <columnName.length; i++){
                    int j = i + 1;
                    columnName[i] = (Object) ("A" + j);
                }
            } else{
                // ���� ��������� �������
                String line;
                line = reader.readLine();// ������ ������ ������
                columnName = (String[]) line.split(separator);// ���������
                // ������ ��������� ������
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
    public Object[] getColumnName() {
        return columnName;
    }

    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(Object[] columnName) {
        this.columnName = columnName;
    }
    
    private void getCellData() throws IOException{
        ArrayList rowList = new ArrayList();
        String line;
        while((line = reader.readLine()) != null){
            String[] values = line.split(separator);
            ArrayList colList = new ArrayList();
            for (String value : values) {
                Object cellValue = (Object) value;
                colList.add(cellValue);
            }
            Object[] cells = null;
            if(!colList.isEmpty()) cells = colList.toArray();
            rowList.add(cells);
        }
        data = new Object[rowList.size()][];
        for (int i = 0; i < data.length; i++) {
            data[i] = (Object[]) (Object) rowList.get(i);
        }
    }
}
