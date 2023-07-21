/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.*;

/**
 * 
 * @author Sergii.Tushinskyi
 */
public class LoginFrame extends JPanel{

    /**
     * @param hostIP the hostIP to set
     */
    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    /**
     * @param serverPort the serverPort to set
     */
    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param databaseName the databaseName to set
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    // элементы управления, располагающиеся на форме
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JLabel lblDatabase;
    private JButton okButton;// кнопка подтверждения ввода
    private JButton cancelButton;// кнопка отмены ввода
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtDatabase;
    private JTextField txtServerPort;
    private JTextField txtHostIP;
    private JPanel panel;
    private JDialog dialog;// диалоговое окно для отображения элементов управления
    private boolean ok;// флаг подтверждения или отмены выбора
    private String hostIP;// адрес хоста сервера
    private String serverPort;// номер порта сервера
    private String userName;// имя подключающегося пользователя
    private String password;// пароль пользователя
    private String databaseName;// имя базы данных, к которой подключается пользователь
    private String aliasName;// псевдоним базы данных, который будет отображаться в строке состояния
    
    public LoginFrame(){
        super.setLayout(new BorderLayout());
        aliasName = null;
        // инициализация свойств
        userName = "sysdba";
        serverPort = "3050";
        databaseName = "";
        password = "masterke";
        hostIP = "";
        
        // читаем файл свойств с названиями баз данных
//        readAliasname();
    }
    
    /**
     * Выводит панель для выбора в диалоговом окне.
     * @param owner компонент в собственном фрейме или null 
     */
    public void showDialog(Component owner){
//        ok = false;
        initComponents();
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
            url = LoginFrame.class.getClassLoader().getResource("image/padlock.png");
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
    
    private void initComponents() {
        panel = new JPanel(new BorderLayout());// панель для размещения содержимого
        // создаём кнопки
        okButton = new JButton("OK");
        okButton.addActionListener((ActionEvent e) -> {
            ok = true;
            
            char[] passchar = txtPassword.getPassword();// получаем массив символов пароля
            // преобразовываем его в строку
            password = "";
            for(int i = 0; i < passchar.length; i++)
                password += passchar[i];
            
            databaseName = txtDatabase.getText();
            hostIP = txtHostIP.getText();
            serverPort = txtServerPort.getText();
            userName = txtUsername.getText();
            
            dialog.setVisible(false);
        });
        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener((ActionEvent e) -> {
            ok = false;
            dialog.setVisible(false);
        });
        
        // размещаем метки, поля ввода и списки выбора на панели, помещая их в боксы
        Box nameBox = Box.createHorizontalBox();
        nameBox.add(Box.createHorizontalStrut(10));
        nameBox.add(lblUsername = new JLabel("User name"));
        nameBox.add(Box.createHorizontalStrut(5));
        nameBox.add(txtUsername = new JTextField(userName,20));
        nameBox.add(Box.createHorizontalStrut(10));

        Box passwordBox = Box.createHorizontalBox();
        passwordBox.add(Box.createHorizontalStrut(10));
        passwordBox.add(lblPassword = new JLabel("Password"));
        passwordBox.add(Box.createHorizontalStrut(10));
        passwordBox.add(txtPassword = new JPasswordField(password,15));
        passwordBox.add(Box.createHorizontalStrut(10));

        Box databaseBox = Box.createHorizontalBox();
        databaseBox.add(Box.createHorizontalStrut(10));
        databaseBox.add(lblDatabase = new JLabel("Database name"));
        databaseBox.add(Box.createHorizontalStrut(10));
        databaseBox.add(txtDatabase = new JTextField(databaseName, 20));
        databaseBox.add(Box.createHorizontalStrut(10));
        
        Box hostBox = Box.createHorizontalBox();
        hostBox.add(Box.createHorizontalStrut(10));
        hostBox.add(new JLabel("IP addres"));
        hostBox.add(Box.createHorizontalStrut(10));
        hostBox.add(txtHostIP = new JTextField(hostIP, 20));
        hostBox.add(Box.createHorizontalStrut(10));
        
        Box serverBox = Box.createHorizontalBox();
        serverBox.add(Box.createHorizontalStrut(10));
        serverBox.add(new JLabel("Port    "));
        serverBox.add(Box.createHorizontalStrut(10));
        serverBox.add(txtServerPort = new JTextField(serverPort, 20));
        serverBox.add(Box.createHorizontalStrut(10));
        
        // размещаем кнопки на панели кнопок
        Box buttonBox = commandButtonBox();

        Box vertBox = Box.createVerticalBox();
        vertBox.add(nameBox);
        vertBox.add(Box.createVerticalStrut(10));
        vertBox.add(passwordBox);
        vertBox.add(Box.createVerticalStrut(10));
        vertBox.add(hostBox);
        vertBox.add(Box.createVerticalStrut(10));
        vertBox.add(serverBox);
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
        txtDatabase.addKeyListener(ka);
        txtHostIP.addKeyListener(ka);
        txtServerPort.addKeyListener(ka);
        
        // добавляем элементы на панель
        panel.add(vertBox, BorderLayout.CENTER);

        panel.add(buttonBox, BorderLayout.SOUTH);
        
        super.add(panel, BorderLayout.CENTER);
        
        
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

    /**
     * @return the ok
     */
    public boolean isOk() {
        return ok;
    }
}
