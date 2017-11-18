package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Input;

public class EventManager implements Component {
    private Position p;
    private Size s;
    private Input i;

    public EventManager(Position p, Size s, Input i){
        this.p = p;
        this.s = s;
    }

    public boolean inBounds(Input.TouchEvent event) {
        if (event.x > p.x && event.x < (p.x + (s.width - 1)) &&
                event.y > p.y && event.y < (p.y + (s.height - 1)))
            return true;
        else
            return false;
    }

    public boolean isAccelXOpposite(float accel) {
        return (accel>0 && i.getAccelX() <=0) || (accel<0 && i.getAccelX() >=0);
    }

}
