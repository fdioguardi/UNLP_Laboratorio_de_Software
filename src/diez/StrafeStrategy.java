package diez;

import java.awt.geom.Point2D;

public class StrafeStrategy extends Strategy {
    private final int move = 0;
    private final Point2D startPos;
    private final Point2D endPos;

    public StrafeStrategy(Messi robot) {
        super(robot);
        robot.out.println("Strafe Strategy");
        startPos = new Point2D.Double(robot.robotX, robot.robotY);
        endPos = new Point2D.Double(robot.robotY, robot.robotX);

        if ((startPos.getY() - endPos.getY()) > 0) {
            robot.turnTo(robot.heading - 90);
        } else {
            robot.turnTo(robot.heading + 90);
        }
    }

    @Override
    public void run() {
        robot.turnGunLeft(180);
        robot.ahead(150);
        robot.turnGunLeft(180);
        robot.back(150);
    }

    @Override
    public void onHitRobot() {
        robot.turnGunTo(robot.scannedAngle);
        if (robot.energy > 1 && robot.gunReady)
            robot.fire(1);
    }

    @Override
    public void onHitByBullet() {

    }

    @Override
    public void onHitWall() {
    }
}
