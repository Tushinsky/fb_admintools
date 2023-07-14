/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 *
 * @author Sergii.Tushinskyi
 */
public class InformDialog extends JDialog {

    public enum InformType{CONNECT, LOADING, SAVING};
    private JLabel messageLabel;
    private Timer t;
    
    public InformDialog() {
        super();
        setParameters();
    }
    
    
    public InformDialog(Frame owner) {
        super(owner);
        setParameters();
    }
    
    /**
     * устанавливает параметры окна
     */
    private void setParameters(){
        setUndecorated(true);// отображаем без рамки
        setAlwaysOnTop(true);// поверх всех окон
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

//        Toolkit kit = getToolkit();
//        Dimension screenSize = kit.getScreenSize();
//        int screenHeight = screenSize.height;
//        int screenWidth = screenSize.width;
        // размеры окна сообщения
        setSize(200, 50);
        
         // отображаем форму посередине экрана
        setLocationRelativeTo(this.getParent());
//        setLocation((screenWidth - 200)/2, (screenHeight - 50)/2);
        //добавляем панель для вывода сообщения
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
//            // цвет фона панели
//            messagePanel.setBackground(dtPane.getBackground());
        messageLabel = new JLabel();

        // размер метки по размеру панели
        messageLabel.setSize(messagePanel.getWidth(), messagePanel.getHeight());

        // выравниваание текста по центру метки
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 14));// шрифт

        // цвет обрамления
        messageLabel.setBorder(BorderFactory.createLineBorder(Color.magenta));

        messagePanel.add(messageLabel, BorderLayout.CENTER);// добавляем метку
        getContentPane().add(messagePanel, BorderLayout.CENTER);// и панель
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                // останавливаем таймер
                t.stop();
                t = null;
            }
        });
    }
    
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
    
    /**
     * @param type the type to set
     */
    public void setType(InformType type) {
        switch (type) {
            case CONNECT:
                // операция соединения с базой данных
                setModal(true);// модальная форма
                // добавляем таймер
                t = new Timer(750, (ActionEvent e) -> {
                    dispose();
                });
                t.start();// запускаем таймер
                break;
            case LOADING:
                // операция загрузки содержимого
                setModal(false);// немодальное окно
                // добавляем таймер
                t = new Timer(1500, (ActionEvent e) -> {
                    dispose();
                });
                t.start();// запускаем таймер
                break;
            default:
                // операция сохранения результатов
                // операция соединения с базой данных
                setModal(true);// модальная форма
                // добавляем таймер
                t = new Timer(500, (ActionEvent e) -> {
                    dispose();
                });
                t.start();// запускаем таймер
                break;
        }
    }

    
}
