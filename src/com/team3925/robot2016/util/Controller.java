package com.team3925.robot2016.util;

public abstract class Controller {
    protected boolean m_enabled = false;

    public abstract void reset();

    public abstract boolean isOnTarget();
}
