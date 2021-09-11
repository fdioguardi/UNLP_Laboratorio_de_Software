package diez;

import robocode.JuniorRobot;

public abstract class Strategy implements IStrategy {
    protected JuniorRobot robot;

    public Strategy(JuniorRobot robot) {
        this.robot = robot;
    }
}
