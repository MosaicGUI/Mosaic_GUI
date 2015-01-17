//Author : Pierre-Aime IMBERT
//mosaicgui.imbert@sfr.fr
//GPLv3 licence

package mosaic_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class Cropping extends JPanel
    {
    BufferedImage thumbnail=null;
    Dimension size;
    Rectangle clip;
 
    public Cropping(BufferedImage image)
        {
        this.thumbnail = image;
        size = new Dimension(thumbnail.getWidth(), thumbnail.getHeight());
        }
 
    protected void paintComponent(Graphics g)
        {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int x = (getWidth() - size.width)/2;
        int y = (getHeight() - size.height)/2;
        g2.drawImage(thumbnail, x, y, this);
        if(clip == null)
            {
            createClip();
            }
        g2.setPaint(Color.red);
        g2.draw(clip);
        }
 
    public void setClip(int x, int y)
        {
        // keep clip within raster
        int x0 = (getWidth() - size.width)/2;
        int y0 = (getHeight() - size.height)/2;
        if(x < x0 || x + clip.width  > x0 + size.width || y < y0 || y + clip.height > y0 + size.height) return;
        clip.setLocation(x, y);
        repaint();
        }
 
    public Dimension getPreferredSize()
        {
        return size;
        }
 
    private void createClip()
        {
        clip = new Rectangle(thumbnail.getWidth()/4, thumbnail.getHeight()/4);
        clip.x = getWidth()/6;
        clip.y = getWidth()/6;
        }
 
    private void clipImage()
        {
        BufferedImage clipped = null;
        try
            {
            int w = clip.width;
            int h = clip.height;
            int x0 = (getWidth()  - size.width)/2;
            int y0 = (getHeight() - size.height)/2;
            int x = clip.x - x0;
            int y = clip.y - y0;
            clipped = thumbnail.getSubimage(x, y, w, h);
            }
        catch(RasterFormatException rfe)
            {
            System.out.println("raster format error: " + rfe.getMessage());
            return;
            }
        
        //Destroy the complete Cropping Frame by destroy all his components
        Mosaic_GUI.f.removeAll();
        Mosaic_GUI.f.revalidate();
        Mosaic_GUI.f.repaint();
        Mosaic_GUI.f.dispose();

        File outputfile = new File(System.getProperty("user.home")+"\\thumbnail.png");
        try {
            ImageIO.write(clipped, "png", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(Mosaic_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
 
    JPanel getUIPanel()
        {
        JButton validclip = new JButton();
        if (Mosaic_GUI.lang==1) validclip.setText("Rogner");
        else if (Mosaic_GUI.lang==2) validclip.setText("Crop");
        validclip.addActionListener(new ActionListener()
            {
            public void actionPerformed(ActionEvent e)
                {
                clipImage();
                }
            });
        
        JPanel panel = new JPanel();
        panel.addKeyListener(new KeyListener() 
            {
            @Override
            public void keyTyped(KeyEvent e) 
                {
                }
            @Override
            public void keyPressed(KeyEvent e) 
                {
                int keyCode = e.getKeyCode();
                switch( keyCode ) 
                    { 
                    case KeyEvent.VK_UP:
                    if(clip.y>0)
                            {
                            clip.height=clip.height-2;
                            if(clip.height<1)
                                {
                                clip.height=1;
                                }
                            repaint();
                            }
                    break;
                    case KeyEvent.VK_DOWN:
                        if(clip.y+clip.height<thumbnail.getHeight())
                            {
                            clip.height=clip.height+2;
                            if(clip.y+clip.height>thumbnail.getHeight())
                                {
                                clip.height=clip.height-2;
                                }
                            repaint();
                            }
                    break;
                    case KeyEvent.VK_LEFT:
                        if(clip.x>0)
                            {
                            clip.width=clip.width-2;
                            if(clip.width<1)
                                {
                                clip.width=1;
                                }
                            repaint();
                            }
                    break;
                    case KeyEvent.VK_RIGHT :
                        if(clip.x+clip.width<thumbnail.getWidth())
                            {
                            clip.width=clip.width+2;
                            if(clip.x+clip.width>thumbnail.getWidth())
                                {
                                clip.width=clip.width-2;
                                }
                            repaint();
                            }
                    break;
                    }
                }
            @Override
            public void keyReleased(KeyEvent e) 
                {
                }
            });
        
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        
        panel.add(validclip);
        return panel;
        }
    }

class ClipMover extends MouseInputAdapter
    {
    Cropping cropping;
    Point offset;
    boolean dragging;
 
    public ClipMover(Cropping c)
        {
        cropping = c;
        offset = new Point();
        dragging = false;
        }
 
    public void mousePressed(MouseEvent e)
        {
        Point p = e.getPoint();
        if(cropping.clip.contains(p))
            {
            offset.x = p.x - cropping.clip.x;
            offset.y = p.y - cropping.clip.y;
            dragging = true;
            }
        }
 
    public void mouseReleased(MouseEvent e)
        {
        dragging = false;
        }
 
    public void mouseDragged(MouseEvent e)
        {
        if(dragging)
            {
            int x = e.getX() - offset.x;
            int y = e.getY() - offset.y;
            cropping.setClip(x, y);
            }
        }
    }
