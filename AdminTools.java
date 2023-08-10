/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admintools;

import frame.StartFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Сергей
 */
public class AdminTools {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(ClassNotFoundException | InstantiationException |
                    IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(AdminTools.class.getName()).log(Level.SEVERE, null, ex);
            }
            StartFrame frame = new StartFrame();
            StartFrame.setDefaultLookAndFeelDecorated(true);
//            frame.setTitle("AdminTools");// задаём заголовок
//            frame.setType(Window.Type.NORMAL);
//            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setVisible(true);// отображаем форму
        });
    }
    
}
