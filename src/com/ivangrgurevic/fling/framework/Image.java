package com.ivangrgurevic.fling.framework;

import com.ivangrgurevic.fling.framework.Graphics.ImageFormat;

public interface Image {
    public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();
}
