package com.paff.orlandale.paff;

import android.util.Log;

import com.google.fpl.liquidfun.Body;
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
        Body ba = fa.getBody(), bb = fb.getBody();
        Log.e("CONTATTO", "beginContact: CONTATTO AVVENUTO");
        physicWorld.collisionDetected(ba,bb);



    }
}
