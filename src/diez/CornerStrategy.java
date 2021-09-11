package diez;

import robocode.JuniorRobot;

import java.awt.geom.Point2D;

public class CornerStrategy extends Strategy {
    public CornerStrategy(JuniorRobot robot) {
        super(robot);
        robot.out.println("Corner Strategy");
    }

    @Override
    public void run() {
        robot.ahead(10);
    }

    @Override
    public void onHitRobot() {
        robot.turnGunTo(robot.scannedAngle);
        if (robot.energy > 1 && robot.gunReady)
            robot.fire(2);
    }

    @Override
    public void onHitByBullet() {

    }

    @Override
    public void onHitWall() {

    }
}
