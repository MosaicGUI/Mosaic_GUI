//Author : Pierre-Aime IMBERT
//mosaicgui.imbert@gmail.com
//GPLv3 licence

//Floyd Steinberg filter with color blinking reduced by 25%

package mosaic_gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Floyd
{
static BufferedImage floydSteinbergDithering(BufferedImage img) 
    {
    int w=img.getWidth();
    int h=img.getHeight();
    int r[][]=new int[w][h];
    int g[][]=new int[w][h];
    int b[][]=new int[w][h];
    
    for(int j=0;j<h;j++)
        {
        for(int i=0;i<w;i++)
            {
            Color pixel=new Color(img.getRGB(i, j));
            r[i][j]=pixel.getRed();
            g[i][j]=pixel.getGreen();
            b[i][j]=pixel.getBlue();
            }
        }
    
    for(int j=0;j<h;j++)
        {
        for(int i=0;i<w;i++)
            {
            int bestindex=0;
            int minDistanceSquared = 255*255 + 255*255 + 255*255 + 1;
            
            for (int k=0;k<Mosaic_GUI.nombrecouleurs;k++)
                {  
                int Rdiff = r[i][j];
                int Gdiff = g[i][j];
                int Bdiff = b[i][j];
                    
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
                 
            int erreurr=r[i][j]-Mosaic_GUI.palette_couleurs[bestindex][0];
            int erreurg=g[i][j]-Mosaic_GUI.palette_couleurs[bestindex][1];
            int erreurb=b[i][j]-Mosaic_GUI.palette_couleurs[bestindex][2];
            
            r[i][j]=Mosaic_GUI.palette_couleurs[bestindex][0];
            g[i][j]=Mosaic_GUI.palette_couleurs[bestindex][1];
            b[i][j]=Mosaic_GUI.palette_couleurs[bestindex][2];
            
            if(i+1<w && j<h)
                {
                r[i+1][j]=(int)(r[i+1][j]+7.0/16.0*0.75*erreurr);
                if(r[i+1][j]<0) r[i+1][j]=0;
                else if(r[i+1][j]>255) r[i+1][j]=255;
                
                g[i+1][j]=(int)(g[i+1][j]+7.0/16.0*0.75*erreurg);
                if(g[i+1][j]<0) g[i+1][j]=0;
                else if(g[i+1][j]>255) g[i+1][j]=255;
                
                b[i+1][j]=(int)(b[i+1][j]+7.0/16.0*0.75*erreurb);
                if(b[i+1][j]<0) b[i+1][j]=0;
                else if(b[i+1][j]>255) b[i+1][j]=255;
                }
            
            if(i!=0 && j+1<h)
                {
                r[i-1][j+1]=(int) (r[i-1][j+1]+3.0/16.0*0.75*erreurr);
                if(r[i-1][j+1]<0) r[i-1][j+1]=0;
                else if(r[i-1][j+1]>255) r[i-1][j+1]=255;
                
                g[i-1][j+1]=(int) (g[i-1][j+1]+3.0/16.0*0.75*erreurg);
                if(g[i-1][j+1]<0) g[i-1][j+1]=0;
                else if(g[i-1][j+1]>255) g[i-1][j+1]=255;
                
                b[i-1][j+1]=(int) (b[i-1][j+1]+3.0/16.0*0.75*erreurb);
                if(b[i-1][j+1]<0) b[i-1][j+1]=0;
                else if(b[i-1][j+1]>255) b[i-1][j+1]=255;
                }
            
            if(i<w && j+1<h)
                {
                r[i][j+1]=(int) (r[i][j+1]+5.0/16.0*0.75*erreurr);
                if(r[i][j+1]<0) r[i][j+1]=0;
                else if(r[i][j+1]>255) r[i][j+1]=255;
                
                g[i][j+1]=(int) (g[i][j+1]+5.0/16.0*0.75*erreurg);
                if(g[i][j+1]<0) g[i][j+1]=0;
                else if(g[i][j+1]>255) g[i][j+1]=255;
                
                b[i][j+1]=(int) (b[i][j+1]+5.0/16.0*0.75*erreurb);
                if(b[i][j+1]<0) b[i][j+1]=0;
                else if(b[i][j+1]>255) b[i][j+1]=255;
                }
            
            if(i+1<w && j+1<h)
                {
                r[i+1][j+1]=(int) (r[i+1][j+1]+1.0/16.0*0.75*erreurr);
                if(r[i+1][j+1]<0) r[i+1][j+1]=0;
                else if(r[i+1][j+1]>255) r[i+1][j+1]=255;
                
                g[i+1][j+1]=(int) (g[i+1][j+1]+1.0/16.0*0.75*erreurg);
                if(g[i+1][j+1]<0) g[i+1][j+1]=0;
                else if(g[i+1][j+1]>255) g[i+1][j+1]=255;
                
                b[i+1][j+1]=(int) (b[i+1][j+1]+1.0/16.0*0.75*erreurb);
                if(b[i+1][j+1]<0) b[i+1][j+1]=0;
                else if(b[i+1][j+1]>255) b[i+1][j+1]=255;
                }
            }
        }
    
    for(int j=0;j<h;j++)
        {
        for(int i=0;i<w;i++)
            {
            int pixels=new Color(r[i][j],g[i][j],b[i][j]).getRGB();
            img.setRGB(i, j, pixels);
            }
        }
    
    return img;
    }
}
                    