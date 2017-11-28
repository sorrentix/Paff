package com.paff.orlandale.paff;

/**
 * Componente posizione del GameObject che non ha componente fisica
 */
public class Position implements Component{
    public int x,y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
}
