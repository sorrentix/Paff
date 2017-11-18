package com.badlogic.androidgames.framework;

import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.paff.orlandale.paff.Component;

public interface Pixmap extends Component {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();

}
