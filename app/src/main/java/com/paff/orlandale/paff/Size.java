package com.paff.orlandale.paff;

/**
 * Componente dimensione per il GameObject che non ha la componente fisica
 */
public class Size implements Component{
    public float width, height;

    public Size(float width, float height){
        this.width = width;
        this.height = height;
    }
}
