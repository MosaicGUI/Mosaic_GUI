//Author : Pierre-Aime IMBERT
//mosaicgui.imbert@sfr.fr
//GPLv3 licence

package mosaic_gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Normal 
    {
    static BufferedImage findNearestColor(BufferedImage imgg) 
        {   
        for (int i=0;i<imgg.getHeight();i++)
            {
            for (int j=0;j<imgg.getWidth();j++) 
                {
                int bestindex=0;
                int minDistanceSquared = 255*255 + 255*255 + 255*255 + 1;

                int Alpha=0;
                
                for (int k=0;k<Mosaic_GUI.nombrecouleurs;k++)
                    {  
                    Color mycolor = new Color(imgg.getRGB(j, i));
                    Alpha = imgg.getRGB(j,i)>>24&0xFF;
                    int Rdiff = mycolor.getRed();
                    int Gdiff = mycolor.getGreen();
                    int Bdiff = mycolor.getBlue();
                
                    if (Rdiff<Mosaic_GUI.palette_couleurs[k][0])
                        {
                        Rdiff=Mosaic_GUI.palette_couleurs[k][0]-Rdiff;
                        }
                    else
                        {
                        Rdiff=Rdiff-Mosaic_GUI.palette_couleurs[k][0];
                        }
                    
                    if (Gdiff<Mosaic_GUI.palette_couleurs[k][1])
                        {
                        Gdiff=Mosaic_GUI.palette_couleurs[k][1]-Gdiff;
                        }
                    else
                        {
                        Gdiff=Gdiff-Mosaic_GUI.palette_couleurs[k][1];
                        }
                    
                    if (Bdiff<Mosaic_GUI.palette_couleurs[k][2])
                        {
                        Bdiff=Mosaic_GUI.palette_couleurs[k][2]-Bdiff;
                        }
                    else
                        {
                        Bdiff=Bdiff-Mosaic_GUI.palette_couleurs[k][2];
                        }
                    
                    int distanceSquared = Rdiff*Rdiff + Gdiff*Gdiff + Bdiff*Bdiff;
        
                    if (distanceSquared < minDistanceSquared) 
                        {
                        minDistanceSquared = distanceSquared;
                        bestindex = k;
                        }
                    }
                    int col=0;
                    if (Alpha==255) col = (255 << 24 | Mosaic_GUI.palette_couleurs[bestindex][0] << 16) | (Mosaic_GUI.palette_couleurs[bestindex][1] << 8) | Mosaic_GUI.palette_couleurs[bestindex][2];
                    imgg.setRGB(j, i, col);
                }
            }
        return imgg;
        }
    }