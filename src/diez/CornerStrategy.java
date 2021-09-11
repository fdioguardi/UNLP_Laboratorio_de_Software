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
    public void onScannedRobot() {
        robot.turnGunTo(robot.scannedAngle);
        if (robot.scannedDistance < 100) {
            robot.fire(3);
        }
    }

    @Override
    public void onHitByBullet() {
        robot.turnGunTo(robot.hitByBulletAngle);
    }

    @Override
    public void onHitWall() {

    }

    @Override
    public void onHitRobot() {
        this.onScannedRobot();
    }
}
