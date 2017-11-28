package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;

import java.lang.reflect.Field;

/**
 * Created by Yoshi on 18/11/2017.
 */


/**
 * Classe principale per l'implementazione delle entità di gioco (pattern entity - component)
 */
public class GameObject {

    public Physic physic           = null;
    public Text text               = null;
    public Position position       = null;
    public Size size               = null;
    public Pixmap image            = null;
    public Sound sound             = null;
    public EventManager evtManager = null;


    /**
     * Aggiunge un istanza di una classe che implementa la tag interface Component.
     * Per la creazione di un Component non appartenente già ai campi di GameObject,
     * seguire i seguenti step:
     * <ol>
     *     <li>Creare la nuova classe componenete</li>
     *     <li>Fare implementare alla nuova classe la tag interface Component</li>
     *     <li>Aggiungere un campo di classe a GameObject del tipo della nuova classe</li>
     * </ol>
     * @param  c  Component da aggiungere al GameObject
     */
    public void addComponent(Component c){
        Field[] fields = this.getClass().getFields();
        for(Field field : fields){

            if (field.getType().isAssignableFrom(c.getClass())){
                field.setAccessible(true);
                try {
                    field.set(this,c);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * Consente di rimuovere un component assegnato ad un GameObject
     * @param  c classe del componente da rimuovare
     */
    public Component removeComponent(Class c){
        Component com = null;
        Field[] fields = this.getClass().getFields();
        for(Field field : fields){
            if (c == field.getType()){
                field.setAccessible(true);
                try {
                    com = (Component) field.get(this);
                    field.set (this,null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return com;
            }
        }
        return com;
    }


}
