/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import admintools.CSVOperate;
import admintools.DBExportAction;
import admintools.DBImportAction;
import admintools.DBUpdateAction;
import admintools.JDBCConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lera
 */
public class OperateFrame extends javax.swing.JFrame {
    private JDBCConnection connection;
    private ConnectOptions connOptions;
    private final String defaultTitle = "Admin Tools for database operations: ";
    public enum Operation {Import, Export, Update};
    private Operation idOperation;// идентификатор операций (импорт, экспорт, обновление)
    /**
     * Creates new form OperateFrame
     */
    public OperateFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNameLabel = new javax.swing.JLabel();
        lblTargetLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstTableName = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstTargetList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnPreviouse = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        OKButton = new javax.swing.JButton();
        btnSendTo = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtStep = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        openButton = new javax.swing.JButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        lblNameLabel.setText("jLabel1");

        lblTargetLabel.setText("jLabel2");

        jScrollPane1.setViewportView(lstTableName);

        lstTargetList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(lstTargetList);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setCellSelectionEnabled(true);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane3.setViewportView(jTable1);

        btnPreviouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/NavBack.png"))); // NOI18N

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/NavForward.png"))); // NOI18N

        OKButton.setText("Отмена");

        btnSendTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/NavForward.png"))); // NOI18N

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtStep.setBackground(new java.awt.Color(212, 208, 200));
        txtStep.setColumns(20);
        txtStep.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        txtStep.setLineWrap(true);
        txtStep.setRows(5);
        txtStep.setWrapStyleWord(true);
        txtStep.setFocusable(false);
        jScrollPane4.setViewportView(txtStep);

        jToolBar1.setRollover(true);

        openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/OpenFile.png"))); // NOI18N
        openButton.setText("открыть");
        openButton.setFocusable(false);
        openButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        openButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(openButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSendTo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTargetLabel)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnPreviouse)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNext)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OKButton))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTargetLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnPreviouse)
                                    .addComponent(btnNext)
                                    .addComponent(OKButton)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSendTo)
                                .addGap(51, 51, 51))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
       
        URL url;
        url = OperateFrame.class.getClassLoader().getResource("image/base.png");
        setIconImage(new ImageIcon(url).getImage());
        setFrameTitle();
//        setIdOperation();// задаём тип выполняемой опереции
        this.setLocationRelativeTo(null);// располагаем форму по середине экрана
//        try {
//            btnPreviouse.doClick();
//            
//        } catch(java.lang.Exception ex){
//        }
    }//GEN-LAST:event_formComponentShown

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setMultiSelectionEnabled(false);// запрещаем множественный выбор
        // задаём фильтр файлов
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".csv") || 
                        f.getName().toLowerCase().endsWith(".txt") || 
                        f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Файлы с разделителями, текстовые файлы (*.csv,*.txt)";
            }
        };
        chooser.setFileFilter(filter);// устанавливаем фильтр
        int result = chooser.showOpenDialog(this);
        
        // проверяем сделал ли выбор пользователь
        if(result == JFileChooser.APPROVE_OPTION){
            // если выбор сделан
//            setFrameTitle();
            
            String name;
            try {
                name = chooser.getSelectedFile().getCanonicalPath();
                System.out.println("file - " + name);
                
//                setTitle(getTitle() + " : " + name);
                // отображаем окно выбора разделителя и заголовков
                OptionCSVDialog dialog = new OptionCSVDialog(this, true);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
                // проверяем сделал ли выбор пользователь
                if(dialog.isOk()) {
                    CSVOperate csvReader = new CSVOperate();
                    csvReader.setFileName(name);
                    csvReader.setHeader(dialog.isHeader());// заданы ли заголовки столбцов
                    csvReader.setSeparator(dialog.getSeparator());
                    csvReader.readData();

                    // РїРѕР»СѓС‡Р°РµРј РјРѕРґРµР»СЊ РґР°РЅРЅС‹С… РґР»СЏ С‚Р°Р±Р»РёС†С‹
                    DefaultTableModel model = new DefaultTableModel(csvReader.getData(), 
                            csvReader.getColumnName());
                    jTable1.setModel(model);
                }
            } catch (IOException ex) {
                Logger.getLogger(OperateFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_openButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OperateFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(() -> {
            new OperateFrame().setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKButton;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPreviouse;
    private javax.swing.JButton btnSendTo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblNameLabel;
    private javax.swing.JLabel lblTargetLabel;
    private javax.swing.JList lstTableName;
    private javax.swing.JList lstTargetList;
    private javax.swing.JButton openButton;
    private javax.swing.JTextArea txtStep;
    // End of variables declaration//GEN-END:variables

//    /**
//     * @param connection the connection to set
//     */
//    public void setConnection(JDBCConnection connection) {
//        this.connection = connection;
//    }
    
    /**
     * @param idOperation the idOperation to set
     */
    private void setOperation() {
        // название кнопки
        switch (idOperation) {
            case Import:
                OKButton.setText("Импорт");
                DBImportAction importAction = new DBImportAction(lstTableName, 
                        lstTargetList, txtStep, lblNameLabel, lblTargetLabel, 
                        jTable1, connection, btnSendTo, btnNext, btnPreviouse, OKButton);
                importAction.Start();
                break;
            case Export:
                OKButton.setText("Экспорт");
                DBExportAction exportAction = new DBExportAction(lstTableName, 
                        lstTargetList, txtStep, lblNameLabel, lblTargetLabel, 
                        jTable1, connection, btnSendTo, btnNext, btnPreviouse, OKButton);
                exportAction.Start();
                break;
            default:
                OKButton.setText("Обновить данные");
                DBUpdateAction updateAction = new DBUpdateAction(lstTableName, 
                        lstTargetList, txtStep, lblNameLabel, lblTargetLabel, 
                        jTable1, connection, btnSendTo, btnNext, btnPreviouse, OKButton);
                updateAction.Start();
                break;
        }
        OKButton.setEnabled(false);// делаем её недоступной
    }
    
    /**
     * устанавливает заголовок окна
     */
    private void setFrameTitle(){
        String title;
        switch (idOperation) {
            case Import:
                title = defaultTitle + "Импорт данных";
                break;
            case Export:
                title = defaultTitle + "Экспорт данных";
                break;
            default:
                title = defaultTitle + "Обновление данных";
                break;
        }
        setTitle(title);
        setOperation();
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
            logframe.showDialog(OperateFrame.this);// показываем его
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
                setConnection(new JDBCConnection(driver, url, connOptions.getUsername(),
                        connOptions.getPassword()));
                if (getConnection().isClosedConn() != true){
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
            if (getConnection() != null && !connection.isClosedConn()) {
            JDBCConnection.getConn().close();
            if( getConnection().isClosedConn()){
                    setConnection(null);
                
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

    /**
     * @return the idOperation
     */
    public Operation getIdOperation() {
        return idOperation;
    }

    /**
     * @param idOperation the idOperation to set
     */
    public void setIdOperation(Operation idOperation) {
        this.idOperation = idOperation;
    }

    /**
     * @return the connection
     */
    public JDBCConnection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(JDBCConnection connection) {
        this.connection = connection;
    }
}
