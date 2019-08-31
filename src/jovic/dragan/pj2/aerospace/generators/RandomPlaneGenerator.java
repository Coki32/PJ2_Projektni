package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomPlaneGenerator {
    private List<Constructor<?>> constructorList;

    public RandomPlaneGenerator(Class<? extends AerospaceObject>... classes){
        constructorList = new ArrayList<>();
        for(var clazz : classes)
            registerNewConstructor(clazz);
    }

    public void registerNewConstructor(Class<? extends AerospaceObject> clazz){
        constructorList.addAll(Arrays.stream(clazz.getConstructors())
                .filter(ctor -> {
                    Class<?>[] parameters = ctor.getParameterTypes();
                    return ctor.getParameterCount() == 5 &&
                            parameters[0].equals(int.class) &&
                            parameters[1].equals(int.class) &&
                            parameters[2].equals(int.class) &&
                            parameters[3].equals(int.class) &&
                            parameters[4].equals(Direction.class);
                })
                .collect(Collectors.toList()));
    }

    public AerospaceObject getRandom(int x, int y, int altitude, int speed, Direction dir){
        AerospaceObject object = null;
        try {
            object = (AerospaceObject) constructorList.get(
                    Util.randomBetween(0, constructorList.size() - 1)).newInstance(new Object[]{x, y, altitude, speed, dir});
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException ex){
            GenericLogger.log(this.getClass(),ex);
        }
        return object;
    }
}
