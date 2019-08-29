package jovic.dragan.pj2.preferences;

import jovic.dragan.pj2.Interfaces.IPreferences;


//TODO: Mozda napravi generic preference, ako ostane vremena
public class GenericPreferences <T extends IPreferences> {
    private T preferences;
    private String filename;
    public GenericPreferences(String filename){
        this.filename = filename;
    }
}
