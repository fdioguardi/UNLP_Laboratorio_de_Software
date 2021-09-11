package diez;

import robocode.JuniorRobot;

import java.awt.geom.Point2D;

public class ManagerHub {
    private IRobotManager single_instance;

    public IRobotManager manager(Messi robot) {
        return this.single_instance == null ? new ScaloniManager(robot) : this.single_instance;
    }

    // Manager 1
    private class ScaloniManager implements IRobotManager {
        private boolean strafing = false;
        private Strategy actualStrategy;
        private Point2D selectedCorner;
        private Messi robot;

        public ScaloniManager(Messi robot) {
            this.robot = robot;

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

            actualStrategy = new CornerStrategy(robot);
        }

        @Override
        public Strategy strategy() {
            Point2D position = new Point2D.Double(robot.robotX, robot.robotY);
            Point2D pointWallY = new Point2D.Double(selectedCorner.getX(), position.getY());
            Point2D pointWallX = new Point2D.Double(position.getX(), selectedCorner.getY());
            double distanceWallY = pointWallY.distance(position);
            double distanceWallX = pointWallX.distance(position);

            boolean inPosition = ((distanceWallY < 100) || (distanceWallX < 100));

            if (inPosition && !strafing) {
                strafing = true;
                actualStrategy = new StrafeStrategy(robot);
            }
            return actualStrategy;
        }
    }

    // Manager 2
    private class SabellaManager implements IRobotManager {
        @Override
        public Strategy strategy() {
            return new StrafeStrategy(null);
        }
    }
}
