package jovic.dragan.pj2.Interfaces;

import jovic.dragan.pj2.aerospace.equipment.Cargo;

import java.util.*;

/**
 * 
 */
public interface Transporter {

    void addCargo(Cargo c);

    void getCurrentCargoWeight();

    void getMaximumCargoWeight();

}