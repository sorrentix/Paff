package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;

import java.lang.reflect.Field;

/**
 * Created by Yoshi on 18/11/2017.
 */

public class GameObject {

    public Physic physic = null;
    public Text text = null;
    public Position position = null;
    public Size size = null;
    public Pixmap image = null;
    public Sound sound = null;
    public EventManager evtManager = null;


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
