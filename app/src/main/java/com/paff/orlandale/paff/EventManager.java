package com.paff.orlandale.paff;

import android.util.Log;

import com.badlogic.androidgames.framework.Input;

/**
 * Classe usata per la gestione degli eventi che avvengono su un GameObject
 */
public class EventManager implements Component {
    private Position p;
    private Size s;
    private Input i;

    /**
     * Costruttore della classe, inizializza i campi.
     * @param  p  posizione del GameObject nel mondo pixel
     * @param  s  dimensione del GameObject nel mondo pixel
     * @param  i  gestore degli eventi, vedi classe Input
     */
    public EventManager(Position p, Size s, Input i){
        this.p = p;
        this.s = s;
        this.i = i;
    }

    /**
     * Controlla che un evento touch sia avvenuto all'interno dell'MBR (Minumum Bounding Rectangle)
     * del relativo GameObject
     * @param  event l'evento touch di cui si vuole controllare la posizione
     * @return       true se l'evento avviene all'interno dell'MBR del GameObject, false altrimenti
     */
    public boolean inBounds(Input.TouchEvent event) {
        return event.x > p.x && event.x < (p.x + (s.width - 1)) && event.y > p.y && event.y < (p.y + (s.height - 1));
    }

    /**
     * Controlla che il valore sull'asse X dell'accelerometro sia opposto in segno rispetto a quello in input.
     * Usato per controllare che ci sia un grosso cambio di inclinazione del telefono
     * @param  accel valore con cui confrontare l'accelerazione corrente (di solito è l'accelerazione al passo precedente)
     * @return       true se l'accelerazione corrente è opposta in segno a quella in ingresso, false altrimenti
     */
    public boolean isAccelXOpposite(float accel) {
        return (accel>0 && i.getAccelX() <=0) || (accel<0 && i.getAccelX() >=0);
        //Log.e("ACCELL", "isAccelXOpposite: "+ (Math.abs(accel) - Math.abs(i.getAccelX())));
        //return (accel>0 && i.getAccelX() <=0) || (accel<0 && i.getAccelX() >=0) || (Math.abs(i.getAccelX()) < Math.abs(accel) && (Math.abs(accel) - Math.abs(i.getAccelX())) > 0.1f);
    }


    /**
     * Controlla che il valore sull'asse X dell'accelerometro sia minore rispetto a quello in input.
     * Usato per controllare che ci sia un grosso cambio di inclinazione del telefono
     * @param  accel valore con cui confrontare l'accelerazione corrente (di solito è l'accelerazione al passo precedente)
     * @return       true se l'accelerazione corrente è minore di quella in input, false altrimenti
     */
    public boolean isAccelXLess (float accel){
        return (Math.abs(i.getAccelX()) < Math.abs(accel) && (Math.abs(accel) - Math.abs(i.getAccelX())) > 0.2f);
    }

}
