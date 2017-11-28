package com.paff.orlandale.paff;

import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

/**
 * Created by ALESSANDROSERRAPICA on 15/11/2017.
 */

/**
 * Listener per il rilevamento di contatti tra corpi all'interno di un mondo fisico
 */
public class PaffContactListener extends ContactListener {

    private PhysicWorld physicWorld;

    public PaffContactListener(PhysicWorld physicworld){
        this.physicWorld = physicworld;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Physic ba = (Physic) fa.getBody().getUserData(), bb = (Physic) fb.getBody().getUserData();
        physicWorld.collisionDetected(ba,bb);
    }
}
