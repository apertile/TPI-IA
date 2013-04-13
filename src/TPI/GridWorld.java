/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author fede
 */
public class GridWorld extends JPanel {
    int grilla [][];
    int tamañoCelda = 50;
    
    Color colorBueno = Color.YELLOW;
    Color colorExcelente = Color.GREEN;
    Color colorMalo = Color.RED;
    Color colorNormal = Color.LIGHT_GRAY;
    Color colorPozo = Color.BLACK;//El pozo es de color negro
    Color colorFinal = Color.BLUE;
    Color colorCamino = Color.CYAN;
    Color colorAgente = Color.WHITE;
    
    int cantCeldas;
    
    public GridWorld (int tmño){
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    for (int fila = 0; fila<tmño;fila++){
        for (int columna = 0;columna<tmño;columna++){
            gbc.gridx = fila; gbc.gridy =columna;
            
        }
    }
    }
    
}
