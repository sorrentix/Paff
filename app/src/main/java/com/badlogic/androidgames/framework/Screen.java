package com.badlogic.androidgames.framework;

import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Vec2;
import com.paff.orlandale.paff.Assets;
import com.paff.orlandale.paff.EventManager;
import com.paff.orlandale.paff.GameObject;
import com.paff.orlandale.paff.Physic;
import com.paff.orlandale.paff.PhysicWorld;
import com.paff.orlandale.paff.Position;
import com.paff.orlandale.paff.Size;
import com.paff.orlandale.paff.Text;

public abstract class Screen {
    protected final Game game;

    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();


    public GameObject setButton(Position p, Pixmap img, Sound s, Input i){
        GameObject g;
        g = new GameObject();
        g.addComponent(img);
        g.addComponent(s);
        g.addComponent(p);
        g.addComponent(new Size(img.getWidth(), img.getHeight()));
        g.addComponent(new EventManager(g.position, g.size, i));
        return g;
    }

    public static GameObject setSimpleImage(Position p, Pixmap img){
        GameObject g;
        g = new GameObject();
        g.addComponent(img);
        g.addComponent(p);
        g.addComponent(new Size(img.getWidth(), img.getHeight()));
        return g;
    }

    public static GameObject setBubble(PhysicWorld p, float radius, Vec2 pos, BodyType b, Input i, float expirationTime){
        GameObject g;
        g = new GameObject();
        g.addComponent(Assets.bubblexplosion);
        g.addComponent(new Physic(p,expirationTime));
        g.physic.CircleShape(radius);
        g.physic.Body(pos, 1, b);
        g.addComponent(new EventManager(g.position, g.size, i));
        return g;
    }

    public static GameObject setText(Position p, Pixmap img, Sound s, Text t){
        GameObject g = new GameObject();
        g.addComponent(p);
        g.addComponent(img);
        g.addComponent(s);
        g.addComponent(t);
        return g;
    }
    public static GameObject setText(Position p, Text t){
        GameObject g = new GameObject();
        g.addComponent(p);
        g.addComponent(t);
        return g;
    }


}
