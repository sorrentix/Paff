package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;

import java.lang.reflect.Field;

/**
 * Created by Yoshi on 18/11/2017.
 */
/* se exaple
        GameObject g = new GameObject();
        g.addComponent(new Text());
        System.out.println("dadasdssssdsd_____________"+g.text.c);
        Text a = (Text) g.removeComponent(Text.class);
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
        System.out.println("opoete ovo: "+ c.getClass());
        for(Field field : fields){

            if (field.getType().isAssignableFrom(c.getClass())){
                field.setAccessible(true);
                try {
                    field.set(this,c);
                    System.out.println("tipe e sto settado: "+ field.getType());
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
