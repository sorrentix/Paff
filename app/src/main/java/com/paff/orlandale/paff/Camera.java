package com.paff.orlandale.paff;

import com.google.fpl.liquidfun.Vec2;

import java.util.List;

/**
 * Created by Yoshi on 21/11/2017.
 */

public class Camera {
    private static Vec2 movement = new Vec2(0,0);
    private static Vec2 effectiveMovement = new Vec2(0,0);

    public  static void computeVerticalMovement(GameObject g){
        float verticalSpaceToCover  = g.physic.getPosY() <= 0.0f ? g.physic.getPosY() : 0.0f;
        if ( verticalSpaceToCover < 0.0f ) {
            float ratio = verticalSpaceToCover / GlobalConstants.Physics.Y_MIN;
            verticalSpaceToCover = (verticalSpaceToCover * ratio)/-100.0f;
            movement.setY(verticalSpaceToCover);
        }
    }

    public static void moveCameraVertically(List<GameObject> g){
        if (movement.getY() > 0.0f ) {
            for (int i = 0; i < g.size(); i++) {
                effectiveMovement.setX(g.get(i).physic.getPosX());
                effectiveMovement.setY(g.get(i).physic.getPosY() + movement.getY());
                g.get(i).physic.body.setTransform(effectiveMovement, 0);
            }
        }
    }

    public static void moveCameraVertically(GameObject g){
        if (movement.getY() > 0.0f ) {
            effectiveMovement.setX(g.physic.getPosX());
            effectiveMovement.setY(g.physic.getPosY() + movement.getY());
            g.physic.body.setTransform(effectiveMovement, 0);
        }
    }

    public static void moveCameraVerticallyForEndlessBackground(GameObject []b){
        if (movement.getY() > 0.0f ) {
            for (int i = 0; i< b.length; i++) {
                b[i].position.y = b[i].position.y + PhysicToPixel.Length(movement.getY());
                if ( b[i].position.y > GlobalConstants.FRAME_BUFFER_HEIGHT ){
                    b[i].position.y = -(b.length-1)*GlobalConstants.FRAME_BUFFER_HEIGHT;
                }
            }
        }
    }

}
