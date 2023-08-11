/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import admintools.JDBCConnection;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Стартовая форма приложения. Содержит меню открытия/закрытия соединения с
 * базой данных, выбора действий с открытой базой данных
 * @author Sergii.Tushinskyi
 */
public class StartFrame extends JFrame {

    private JDBCConnection connection;
    private ConnectOptions connOptions;
    private JMenu mnuData;// меню Данные, содержащее меню выбора операций с базой данных
    
    public StartFrame() throws HeadlessException {
        super("Admin tools");
//        setTitle("Admin tools");// заголовок формы
        // добавляем слушателей открытия и закрытия формы
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent we) {
                super.windowOpened(we); //To change body of generated methods, choose Tools | Templates.
                initComponents();// инициализация компонентов формы
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);
            }

            @Override
            public void windowClosing(WindowEvent we) {
                super.windowClosing(we); //To change body of generated methods, choose Tools | Templates.
                closeConnection();// закрываем соединение с базой
                System.exit(0);// завершение работы
            }
            
        });
    }
    
    /**
     * Создание и инициализация компонентов пользовательского интерфейса
     */
    private void initComponents() {
        JMenuBar bar = new JMenuBar();// создаём панель главного меню
        bar.add(fileMenu());
        createMenuData();
        bar.add(mnuData);
        setJMenuBar(bar);
        this.getContentPane().add(mainPanel());
//        setSize(300, 200);
        pack();
    }
    
    /**
     * Создаёт главное меню Файл
     * @return созданное меню Файл
     */
    private JMenu fileMenu() {
        JMenu menu = new JMenu("Файл");// меню Файл
        JMenu mnuConnection = new JMenu("Соединение");
        mnuConnection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/base.png")));
        JMenuItem mnuConnectProperties = new JMenuItem("Открыть файл свойств");
        mnuConnectProperties.addActionListener(openConnectPropertiesListener());
        mnuConnectProperties.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mnuConnectProperties.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/CommentHS.png"))); // NOI18N
        JMenuItem mnuConnectParameters = new JMenuItem("Открыть окно параметров");
        mnuConnectParameters.addActionListener(openConnectParametersListener());
        mnuConnectParameters.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mnuConnectParameters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/info.png"))); // NOI18N
        // заполняем меню соединения
        mnuConnection.add(mnuConnectParameters);
        mnuConnection.add(mnuConnectProperties);
        
        JMenuItem mnuFileExit = new JMenuItem("Выход");// меню Выход
        mnuFileExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/exit.png"))); // NOI18N
        mnuFileExit.addActionListener((ActionEvent ae) -> {
            //To change body of generated methods, choose Tools | Templates.
            closeConnection();// закрываем соединение с базой
            System.exit(0);// завершение работы
        });
        // заполняем меню Файл
        menu.add(mnuConnection);
        menu.add(mnuFileExit);
        return menu;
    }
    
    /**
     * Создаёт меню Данные, содержащее пункты меню для выбора действий с базой данных
     */
    private void createMenuData() {
        mnuData = new JMenu("Данные");
        
    }
    
    /**
     * Создаёт главную панель - контейнер, на которой располагаются все остальные элементы
     * пользовательского интерфейса
     * @return созданную панель - контейнер
     */
    private JPanel mainPanel() {
        // создаём панель, на которой будут располагаться остальные элементы
        JPanel mainpanel = new JPanel(new BorderLayout(5, 5));
        Box connectBox = Box.createHorizontalBox();// контейнер для кнопок открытия соединения
        Box actionBox = Box.createHorizontalBox();// контейнер для кнопок действий с безой данных
        JButton propertyButton = new JButton("Открыть файл свойств", 
                new javax.swing.ImageIcon(getClass().getResource("/image/CommentHS.png")));
        propertyButton.addActionListener(openConnectPropertiesListener());
        JButton paramButton = new JButton("Открыть окно параметров", 
                new javax.swing.ImageIcon(getClass().getResource("/image/info.png")));
        paramButton.addActionListener(openConnectParametersListener());
        connectBox.add(propertyButton);
        connectBox.add(Box.createHorizontalStrut(5));
        connectBox.add(paramButton);
        mainpanel.add(connectBox, BorderLayout.NORTH);
        return mainpanel;
    }
    
    private ActionListener openConnectPropertiesListener() {
        ActionListener listener = (ActionEvent ae) -> {
            //To change body of generated methods, choose Tools | Templates.
            // отображаем диалоговое окно выбора файла
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Файлы свойств, (.properties)", "properties"));
        chooser.showDialog(this, "Открыть файл свойств подключения");
        File f = chooser.getSelectedFile();// выбранный пользователем файл
        if(chooser.accept(f)){
            // если пользователь выбрал файл, то печатаем его имя
            System.out.println(chooser.getName(f));
            try {
                mnuData.setEnabled(openConnection(f));
            } catch (IOException | SQLException | ClassNotFoundException ex) {
                Logger.getLogger(OperateFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        };
        return listener;
    }
    
    private ActionListener openConnectParametersListener() {
        ActionListener listener = (ActionEvent ae) -> {
            //To change body of generated methods, choose Tools | Templates.
            // отображаем на экране окно ввода параметров подключения
        try {
            mnuData.setEnabled(openConnection(null));
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(OperateFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        };
        return listener;
    }
    
    private class ConnectOptions{

        private String databaseName;
        private String hostIP;
        private String serverPort;
        private String username;
        private String Password;
        private String aliasName;
        private boolean accessOpen;// флаг открытия доступа к базе данных
        private File fileName;
        
        public ConnectOptions() {
            accessOpen = false;// доступ пока закрыт
        }

        /**
         * @return the databaseName
         */
        public String getDatabaseName() {
            return databaseName;
        }

        /**
         * @param databaseName the databaseName to set
         */
        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        /**
         * @return the hostIP
         */
        public String getHostIP() {
            return hostIP;
        }

        /**
         * @param hostIP the hostIP to set
         */
        public void setHostIP(String hostIP) {
            this.hostIP = hostIP;
        }

        /**
         * @return the serverPort
         */
        public String getServerPort() {
            return serverPort;
        }

        /**
         * @param serverPort the serverPort to set
         */
        public void setServerPort(String serverPort) {
            this.serverPort = serverPort;
        }

        /**
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @param username the username to set
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * @return the Password
         */
        public String getPassword() {
            return Password;
        }

        /**
         * @param Password the Password to set
         */
        public void setPassword(String Password) {
            this.Password = Password;
        }

        /**
         * @return the accessOpen
         */
        public boolean isAccessOpen() {
            return accessOpen;
        }

        /**
         * @param accessOpen the accessOpen to set
         */
        public void setAccessOpen(boolean accessOpen) {
            this.accessOpen = accessOpen;
        }
        
        /**
         * отображает окно доступа для подключения к базе данных
         */
        public void showLoginframe(){
            LoginFrame logframe = new LoginFrame();// создаём экземпляр формы
            // если задан файл свойств, то считываем его и задаём параметры соединения для формы доступа
            if(fileName != null) {
                try {
                    readConnectProperties();
                } catch (IOException ex) {
                    Logger.getLogger(OperateFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                logframe.setDatabaseName(databaseName);
                logframe.setHostIP(hostIP);
                logframe.setServerPort(serverPort);
                logframe.setUserName(username);
            }
            logframe.showDialog(StartFrame.this);// показываем его
            if(logframe.isOk()) {
                hostIP = logframe.getHostIP();
                serverPort = logframe.getServerPort();
                databaseName = logframe.getDatabaseName();
                username = logframe.getUserName();
                Password = logframe.getPassword();
                aliasName = logframe.getAliasName();
            } else {
                databaseName = "";
            }
        }

        /**
         * @return the aliasName
         */
        public String getAliasName() {
            return aliasName;
        }

        /**
         * @param aliasName the aliasName to set
         */
        public void setAliasName(String aliasName) {
            this.aliasName = aliasName;
        }

        /**
         * @return the fileName
         */
        public File getFileName() {
            return fileName;
        }

        /**
         * @param fileName the fileName to set
         */
        public void setFileName(File fileName) {
            this.fileName = fileName;
        }
        
        private void readConnectProperties() throws IOException {
            Properties props = new Properties();// создаём класс для чтения из файла свойств
            try {
                // создаём поток чтения данных из файла
                FileInputStream fin = new FileInputStream(fileName);
                props.load(fin);// считываем свойства

                // получаем все перечисенные свойства
                Enumeration e = props.propertyNames();
                while(e.hasMoreElements()) {
                    String propName = e.nextElement().toString();// получаем имя свойства
                    System.out.println(propName.toLowerCase() + "=" + props.getProperty(propName));
                    // проверяем, что содержится в имени свойства
                    if(propName.toLowerCase().contains("database")) {
                        databaseName = props.getProperty(propName);
                    } else if(propName.toLowerCase().contains("hostip")) {
                        hostIP = props.getProperty(propName);
                    } else if(propName.toLowerCase().contains("serverport")) {
                        serverPort = props.getProperty(propName);
                    } else if(propName.toLowerCase().contains("user")) {
                        username = props.getProperty(propName);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OperateFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean openConnection(File file) throws FileNotFoundException, 
            IOException, SQLException, ClassNotFoundException {
        // открываем соединение с базой данных
        //читаем файл свойств для загрузки драйвера и других параметров
//        Properties props= new Properties();
        String message = "Connection is not opened!";
        boolean retval;
        // создаём класс для получения доступа к базе данных
        if(connOptions == null) connOptions = new ConnectOptions();
        connOptions.setFileName(file);
        connOptions.showLoginframe();
        if(!connOptions.getDatabaseName().equals("")) {
            try {
                // set drivername
                String driver = "org.firebirdsql.jdbc.FBDriver";
                String url = "jdbc:firebirdsql://" + connOptions.getHostIP() + ":" +
                    connOptions.getServerPort() + "/" + 
                    connOptions.getDatabaseName();

                // открываем первоначальное соединение с базой данных
                connection = new JDBCConnection(driver, url, connOptions.getUsername(),
                        connOptions.getPassword());
                if (connection.isClosedConn() != true){
                    message = "Connection is opening!";
                    retval = true;
                } else{
                    retval = false;
    //                System.exit(0);
                }
                System.out.println("retval=" + retval);
                // окно сообщения по результатам соединения
                getInformDialog(this, message, InformDialog.InformType.CONNECT);
                return retval;
            } catch (SQLException | ClassNotFoundException ex){
                // окно сообщения по результатам соединения
                getInformDialog(this, message, InformDialog.InformType.CONNECT);
                return false;
            }
        } else {return false;}
            
    }
    
    private void closeConnection(){
        try {
            if (connection != null && !connection.isClosedConn()) {
            JDBCConnection.getConn().close();
            if(connection.isClosedConn()){
                connection = null;
                
            }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OperateFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getInformDialog(JFrame owner, String message, InformDialog.InformType type){
        InformDialog idialog;
        if(owner != null){
            idialog = new InformDialog(owner);
        } else {
            idialog = new InformDialog();
        }
        idialog.setMessage(message);
        idialog.setType(type);
        idialog.setVisible(true);
    }
    
    
}
