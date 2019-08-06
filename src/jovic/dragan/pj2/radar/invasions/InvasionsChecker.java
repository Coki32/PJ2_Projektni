package jovic.dragan.pj2.radar.invasions;

import jovic.dragan.pj2.radar.ObjectInfo;

import java.util.List;
import java.util.stream.Collectors;

public class InvasionsChecker implements Runnable{

    private List<ObjectInfo> infoList;

    public InvasionsChecker(List<ObjectInfo> infoList){
        this.infoList = infoList;
    }

    @Override
    public void run() {
        for (ObjectInfo info: infoList.stream().filter(obj-> obj.isMilitary() && obj.isForeign()).collect(Collectors.toList())) {
            InvasionsLogger.logInvasion(info);
        }
    }
}
