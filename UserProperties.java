/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**
 *
 * @author Сергей
 */
public class UserProperties {
    
    private String property;
    private Properties props;
    private String fileNameProperties;
    private String symbol;
    
    public UserProperties(String filename) {
        // ���������� ��������� ���� �������
        fileNameProperties = filename;
        if (props == null) props = new Properties();
        try {
            readFileProperties();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserProperties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public UserProperties() {
        
    }

    /**
     * @return the property
     */
    public String getProperty(String keyProperty) {
        property = props.getProperty(keyProperty);
        return property;
    }
    
    /**
     * @return the property
     */
    public ArrayList getListProperty(String keyProperty) {
        property = props.getProperty(keyProperty);
        ArrayList propList = getStringArray(property);
        return propList;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(String keyProperty, String value) {
        this.property = keyProperty;
        props.setProperty(property, value);
    }
    
    /**
     * @return устанавливает размеры и положение формы на экране
     */
    public void setFrameBound (JFrame frame) throws FileNotFoundException, IOException {
        Toolkit kit=Toolkit.getDefaultToolkit();
        Dimension screenSize=kit.getScreenSize();
        int screenHeight=screenSize.height;
        int screenWidth=screenSize.width;
        
//        readFileProperties();//читаем указанный файл свойств
        
        //задаём размеры и местоположение формы
        String index = props.getProperty("height");
        int i = index == null ? 300 : Integer.parseInt(index);
        int frameHeight = i;
        index = props.getProperty("width");
        i = index == null ? 300 : Integer.parseInt(index);
        int frameWidth= i;
        index = props.getProperty("top");
        i = index == null ? 
                Integer.parseInt(String.valueOf((screenHeight-frameHeight)/2)) :
                Integer.parseInt(index);
        int top = i;
        index = props.getProperty("left");
        i = index == null ?
                Integer.parseInt(String.valueOf((screenWidth-frameWidth)/2)) :
                Integer.parseInt(index);
        int left = i;
        // ������������� ������� � �������������� �����
        frame.setBounds(left, top, frameWidth, frameHeight);
    }
    
    /**
     * @return 
     */
    public void getFrameBound (JFrame frame) throws FileNotFoundException {
        // ������������� �������� �������
            props.setProperty("height",String.valueOf(frame.getHeight()));
            props.setProperty("width",String.valueOf(frame.getWidth()));
            props.setProperty("top", 
                    String.valueOf(frame.getLocation().y));
            props.setProperty("left", 
                    String.valueOf(frame.getLocation().x));
            writeFileProperties();// ���������� �������� � ���� �������
    }

    /**
     * @return the fileNameProperties
     */
    public String getFileNameProperties() {
        return fileNameProperties;
    }
    
    private void readFileProperties() throws FileNotFoundException, IOException {
        try (FileInputStream in = new FileInputStream(fileNameProperties)) {
            props.load(in);
            in.close();// ��������� ������� �����
        }
    }
    
    private void writeFileProperties() throws FileNotFoundException {
        // ������ �������� ����� ��� ������ ���������
            FileOutputStream out=new FileOutputStream(fileNameProperties);
            try {
                props.store(out, fileNameProperties);
                out.close();//��������� �������� �����
            } catch (IOException ex) {
                Logger.getLogger(UserProperties.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void writeProperties() throws FileNotFoundException {
        writeFileProperties();
    }
    
    public void moveComponent(JFrame frame) throws FileNotFoundException {
        props.setProperty("left", Integer.toString((int) frame.getLocation().getX()));
        props.setProperty("top", Integer.toString((int) frame.getLocation().getY()));
        writeFileProperties();
    }
    
    public void resizeComponent(JFrame frame) throws FileNotFoundException {
        props.setProperty("height", Integer.toString(frame.getHeight()));
        props.setProperty("width", Integer.toString(frame.getWidth()));
        writeFileProperties();
    }
    
    /**
    * @param url ����� �������
    * @param menuItem ����� ����
    * @param text ������������ ������ ����
    * @param action ��������, ����������� ������� ����
    */

    public void setMenuIconimage(URL url, JMenuItem menuItem, 
            String text, ActionListener action) throws FileNotFoundException, 
            IOException{
        ImageIcon image;
        menuItem.setText(text);
        if (url != null) image = new ImageIcon(url);
        else image = null;
        if (image!=null)
            menuItem.setIcon(image);
        menuItem.addActionListener(action);
    }
    
        private ArrayList getStringArray(String stringArray) {
        ArrayList resultArray = new ArrayList();
        //��������� ������ ��� ��������� ������
        int startPosition = 0;//��������� ������� ������
        while (startPosition < stringArray.length()) {
            // ���������� ������� ������� ","
            int endPosition = stringArray.indexOf(symbol, startPosition);
//                    System.out.println("startPosition =" + startPosition);
//                    System.out.println("endPosition =" + endPosition);
            if (endPosition != -1){
                // ���� ������� �������, �������� ����� ������ �� ������
                // � ������� � � ������
                resultArray.add(stringArray.substring(startPosition, 
                endPosition));
                //����������� ������� ������ �� 1
                startPosition = endPosition + 1;
            } else {
                // ���� ������� �� ������� (=-1), ������ ���������
                // ����� ������, �� ������� ���������� ��������� � ������
                resultArray.add(stringArray.substring(startPosition, 
                stringArray.length()));
                break;//����� �� �����
            }
        }
        return resultArray;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}
