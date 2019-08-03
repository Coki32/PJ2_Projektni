package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.util.Direction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomPlaneGenerator {
    private List<Constructor<?>> constructorList;
    private Random random;

    public RandomPlaneGenerator(Class<? extends AerospaceObject>... classes){
        constructorList = new ArrayList<>();
        for(var clazz : classes)
            registerNewConstructor(clazz);
        random = new Random();
    }

    public void registerNewConstructor(Class<? extends AerospaceObject> clazz){
        constructorList.addAll(Arrays.stream(clazz.getConstructors())
                .filter(ctor -> ctor.getParameterCount()==5)
                .collect(Collectors.toList()));
    }

    public AerospaceObject getRandom(int x, int y, int altitude, int speed, Direction dir){
        AerospaceObject object = null;
        try {
            object = (AerospaceObject) constructorList.get(
                    random.nextInt(constructorList.size())).newInstance(new Object[]{x,y,altitude,speed, dir});
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException ex){
            GenericLogger.log(this.getClass(),ex);
        }
        return object;
    }
}
