/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import admintools.UserProperties;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.*;

/**
 * 
 * @author Sergii.Tushinskyi
 */
public class LoginFrame extends JPanel{

    // элементы управления, располагающиеся на форме
    private final JLabel lblUsername;
    private final JLabel lblPassword;
    private final JLabel lblDatabase;
    private final JButton okButton;// кнопка подтверждения ввода
    private final JButton cancelButton;// кнопка отмены ввода
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JComboBox cmbDatabase;
    private final JPanel panel;
    private JDialog dialog;// диалоговое окно для отображения элементов управления
//    private boolean ok;// флаг подтверждения или отмены выбора
    private String hostIP;// адрес хоста сервера
    private String serverPort;// номер порта сервера
    private String userName;// имя подключающегося пользователя
    private String password;// пароль пользователя
    private String databaseName;// имя базы данных, к которой подключается пользователь
    private String aliasName;// псевдоним базы данных, который будет отображаться в строке состояния
    
    public LoginFrame(){
        super.setLayout(new BorderLayout());
        panel = new JPanel(new BorderLayout());// панель для размещения содержимого
        aliasName = null;
        // создаём кнопки
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //                ok = true;
                userName = txtUsername.getText();
                
                char[] passchar = txtPassword.getPassword();// получаем массив символов пароля
                // преобразовываем его в строку
                password = "";
                for(int i = 0; i < passchar.length; i++)
                    password = password + passchar[i];
                
//                System.out.println("database - " + databaseName);
//                System.out.println("host - " + hostIP);
//                System.out.println("serverport - " + serverPort);
//                System.out.println("user - " + userName);
//                System.out.println("password - " + password);
                aliasName = cmbDatabase.getSelectedItem().toString();
                dialog.setVisible(false);
            }
        });
        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener((ActionEvent e) -> {
            dialog.setVisible(false);
        });
        
        
        // размещаем метки, поля ввода и списки выбора на панели, помещая их в боксы
        Box nameBox = Box.createHorizontalBox();
        nameBox.add(Box.createHorizontalStrut(10));
        nameBox.add(lblUsername = new JLabel("User name"));
        nameBox.add(Box.createHorizontalStrut(5));
        nameBox.add(txtUsername = new JTextField("sysdba",15));
        nameBox.add(Box.createHorizontalStrut(10));

        Box passwordBox = Box.createHorizontalBox();
        passwordBox.add(Box.createHorizontalStrut(10));
        passwordBox.add(lblPassword = new JLabel("Password"));
        passwordBox.add(Box.createHorizontalStrut(10));
        passwordBox.add(txtPassword = new JPasswordField("masterke",15));
        passwordBox.add(Box.createHorizontalStrut(10));

        Box databaseBox = Box.createHorizontalBox();
        databaseBox.add(Box.createHorizontalStrut(10));
        databaseBox.add(lblDatabase = new JLabel("Database name"));
        databaseBox.add(Box.createHorizontalStrut(10));
        databaseBox.add(cmbDatabase = new JComboBox());
        databaseBox.add(Box.createHorizontalStrut(10));
        // размещаем кнопки на панели кнопок
        Box buttonBox = commandButtonBox();

        Box vertBox = Box.createVerticalBox();
        vertBox.add(nameBox);
        vertBox.add(Box.createVerticalStrut(10));
        vertBox.add(passwordBox);
        vertBox.add(Box.createVerticalStrut(10));
        vertBox.add(databaseBox);
        vertBox.add(Box.createVerticalStrut(10));
        // добавим обработку нажатия кнопок в текстовых поля ввода имени пользователя
        // и ввода пароля
        KeyAdapter ka = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke); //To change body of generated methods, choose Tools | Templates.
                // если нажата клавиша ввода, то инициируем действие
                // для клавиши OK, если Отмена, то инициируем действие для клавиши Cancel
                if(ke.getKeyCode() == KeyEvent.VK_ENTER)
                    okButton.doClick();
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE)
                    cancelButton.doClick();
            }
            
        };
        txtPassword.addKeyListener(ka);
//        txtPassword.setFocusable(true);// устанавливаем фокус на поле ввода пароля
        txtUsername.addKeyListener(ka);
        // добавляем элементы на панель
        panel.add(vertBox, BorderLayout.CENTER);

        panel.add(buttonBox, BorderLayout.SOUTH);
        
        super.add(panel, BorderLayout.CENTER);
        
        // читаем файл свойств с названиями баз данных
        readAliasname();
    }
    
    /**
     * Выводит панель для выбора в диалоговом окне.
     * @param owner компонент в собственном фрейме или null 
     */
    public void showDialog(Component owner){
//        ok = false;

        // находим собственный фрейм
        Frame parent;
        if(owner instanceof Frame)
            parent = (Frame) owner;
        else
            parent = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, owner);

        // если фрейм только что создан или был изменён,
        // создаём новое диалоговое окно
        if(dialog == null || dialog.getOwner() != parent){
            dialog = new JDialog(parent, true);
            dialog.getContentPane().add(this);
            dialog.setType(Window.Type.POPUP);// тип окна - всплывающий
            dialog.setAlwaysOnTop(true);// отображение поверх всех окон
            dialog.pack();// упаковываем всё содержимое
            dialog.setResizable(false);// изменение размеров не допускается
            dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            
            //задаём иконку для фрейма
            URL url;
            url = LoginFrame.class.getClassLoader().getResource("Images/padlock.png");
            Image image;
            image = new ImageIcon(url).getImage();
            if (image != null) dialog.setIconImage(image);
            
        }

        // задаём заголовок и выводим диалог на экран
        dialog.setTitle("Подключение");

        // задаём расположение в центре вызывающей формы
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
//        return ok;// возвращаем результат выбора
    }
    
    /**
     * 
     * @return Box
     */
    private Box commandButtonBox(){
        // размещаем кнопки на панели кнопок
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(okButton);
        buttonBox.add(Box.createHorizontalStrut(40));// вставляем между ними распорку
        buttonBox.add(cancelButton);
        buttonBox.add(Box.createHorizontalGlue());
        Box vertBox = Box.createVerticalBox();
        vertBox.add(Box.createVerticalStrut(5));
        vertBox.add(buttonBox);
        vertBox.add(Box.createVerticalStrut(10));
        return vertBox;
    }
    
    /**
     * Читает наименование всех баз данных, к которым может подключаться пользователь
     */
    private void readAliasname(){
        // создаем класс для чтения из файла свойств
        UserProperties props = new UserProperties("aliases.properties");
        
        // пытаемся прочитать наименования баз данных, разделитель ";"
        String[] alias = props.getProperty("aliasname").split(";");
        
        // создаём модель для списка и заполняем её именами баз
        ComboBoxModel model = new DefaultComboBoxModel(alias);
        
        // устанавливаем её для списка выбора
        cmbDatabase.setModel(model);
        
        // определяем действие для списка привыборе значения
        cmbDatabase.addActionListener((ActionEvent ae) -> {
            readConnectProperties(cmbDatabase.getSelectedItem().toString());
        });
        
        // выделяем первый элемент в списке
        cmbDatabase.setSelectedIndex(0);
    }
    
    /**
     * Считывает параметры соединения из файла свойств
     * @param pathname наименование раздела для чтения
     */
    private void readConnectProperties(String pathname){
        // создаем класс для чтения из файла свойств
        UserProperties props = new UserProperties("connectproperties.properties");
        databaseName = props.getProperty(pathname + ".database");
        hostIP = props.getProperty(pathname + ".hostIP");
        serverPort = props.getProperty(pathname + ".serverPort");
        userName = props.getProperty(pathname + ".user");
        password = "";
        txtUsername.setText(userName);
        txtPassword.setText(password);
    }

    /**
     * @return the hostIP
     */
    public String getHostIP() {
        return hostIP;
    }

    /**
     * @return the serverPort
     */
    public String getServerPort() {
        return serverPort;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the databaseName
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @return the aliasName
     */
    public String getAliasName() {
        return aliasName;
    }
}
