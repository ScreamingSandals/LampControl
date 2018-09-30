package cz.ceph.lampcontrol.utils;

import cz.ceph.lampcontrol.LampControl;

/**
 * Created by iamceph on 01.10.2018.
 */
public class VersionChecker {

    public boolean checkVersion() {
        return !LampControl.simpleVersion.equals("newer");
    }
}
