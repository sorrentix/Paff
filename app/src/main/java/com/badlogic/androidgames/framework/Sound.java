package com.badlogic.androidgames.framework;

import com.paff.orlandale.paff.Component;

public interface Sound  extends Component {
    public void play();

    public void playLoop();

    public void dispose();

    public boolean isLoaded();

    public void setIsLoaded();

    public void stop();

}
