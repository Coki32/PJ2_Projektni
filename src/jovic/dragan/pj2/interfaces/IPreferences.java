package jovic.dragan.pj2.interfaces;

//marker interface
public abstract class IPreferences {

    abstract IPreferences initHardcoded();
    abstract IPreferences load();
}
