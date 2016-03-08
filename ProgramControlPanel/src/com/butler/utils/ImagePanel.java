package com.butler.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5308455040320954294L;
	private BufferedImage image;

    public ImagePanel() {
       
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }
    
    public void setImage(BufferedImage image) {
    	this.image = image;
    	repaint();
    }

}