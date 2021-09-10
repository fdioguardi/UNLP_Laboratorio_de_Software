package diez;

import java.awt.geom.Point2D;

public class CornerStrategy extends Strategy {
    private Point2D selectedCorner;

    public CornerStrategy(Messi robot) {
        super(robot);
        robot.out.println("Corner Strategy");

        Point2D.Double[] corners = new Point2D.Double[]{
                new Point2D.Double(0, 0),
                new Point2D.Double(0, robot.fieldHeight),
                new Point2D.Double(robot.fieldWidth, 0),
                new Point2D.Double(robot.fieldWidth, robot.fieldHeight)
        };

        //calcula esquina más cercana
        double min = Double.POSITIVE_INFINITY;
        selectedCorner = corners[0];
        for (Point2D.Double corner : corners) {
            double distance = corner.distance(robot.robotX, robot.robotY);
            if (distance < min) {
                selectedCorner = corner;
                min = distance;
            }
        }

        //rota el robot a la esquina más cercana
        if ((selectedCorner.getY() != 0) && (selectedCorner.getX() != 0)) {
            robot.turnTo(45);
        } else if ((selectedCorner.getY() == 0) && (selectedCorner.getX() != 0)) {
            robot.turnTo(135);
        } else if ((selectedCorner.getY() == 0) && (selectedCorner.getX() == 0)) {
            robot.turnTo(225);
        } else if ((selectedCorner.getY() != 0) && (selectedCorner.getX() == 0)) {
            robot.turnTo(315);
        }
    }

    @Override
    public void run() {
        Point2D position = new Point2D.Double(robot.robotX, robot.robotY);
        Point2D pointWallY = new Point2D.Double(selectedCorner.getX(), position.getY());
        Point2D pointWallX = new Point2D.Double(position.getX(), selectedCorner.getY());
        double distanceWallY = pointWallY.distance(position);
        double distanceWallX = pointWallX.distance(position);

        if ((distanceWallY < 100) || (distanceWallX < 100)) {
            robot.declareInPosition();
        } else {
            robot.ahead(10);
        }
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
