package dimi3sinculotes;

import android.graphics.Point;
import android.widget.ImageView;

/**
 * Created by Rafal on 10/11/2016.
 */

public class Marciano {
    private int x, y, tipo;
    private ImageView iv;
    public Marciano (int x, int y, int tipo, ImageView iv){
        this.x = x;
        this.y = y;
        this.iv = iv;
    }

    public int getX(){
        return this.x;
    }

    public int getY() {
        return y;
    }



    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public ImageView getIv() {
        return iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }

    public Point getMedio(){
        Point p = new Point(this.x + iv.getWidth()/2, this.y + iv.getHeight());
        return p;
    }
}
