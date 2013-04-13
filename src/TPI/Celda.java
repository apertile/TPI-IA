/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author fede
 */
public class Celda extends JPanel{
    int posicion [] = new int[2];
    private Color colorFondo;
    
    public Celda(int i, int j){
        colorFondo = getBackground();
        posicion [0]=i;
        posicion [1]=j;
        addMouseListener (new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            
            }
        });
    }
    
}
