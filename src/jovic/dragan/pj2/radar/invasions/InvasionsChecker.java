package jovic.dragan.pj2.radar.invasions;

import jovic.dragan.pj2.radar.ObjectInfo;

import java.util.Queue;
import java.util.stream.Collectors;

public class InvasionsChecker implements Runnable{

    private Queue<ObjectInfo> infoList;

    public InvasionsChecker(Queue<ObjectInfo> infoList){
        this.infoList = infoList;
    }

    @Override
    public void run() {
        if(infoList!=null)
        for (ObjectInfo info: infoList.stream().filter(obj-> obj!=null && obj.isMilitary() && obj.isForeign()).collect(Collectors.toList())) {
            InvasionsLogger.logInvasion(info);
        }
    }
}
