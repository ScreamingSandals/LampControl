package cz.ceph.lampcontrol.utils;

public enum Permissions {
    COMMANDS_LAMP_ON("lampcontrol.commands.on");

    public String permission;

    Permissions(String permission) {
        this.permission = permission;
    }
}
