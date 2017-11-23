package com.paff.orlandale.paff;

import android.util.Log;

import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

/**
 * Created by ALESSANDROSERRAPICA on 15/11/2017.
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
        if (ba != null && bb != null) {
            Log.e("CONTATTO", "body beginContact: CONTATTO AVVENUTO tra" + ba + "  e tra " + bb);
            physicWorld.collisionDetected(ba, bb);
        }


    }
}
