package diez;

import java.awt.geom.Point2D;

public class ManagerHub {
    private IRobotManager single_instance;

    public IRobotManager manager(Messi robot) {
        if (this.single_instance == null) this.single_instance = new ScaloniManager(robot);
        return this.single_instance;
    }

    private abstract class Manager implements IRobotManager {
        protected Messi robot;

        public Manager(Messi robot) {
            this.robot = robot;
        }
    }

    // Manager 1
    private class ScaloniManager extends Manager {
        private boolean strafing = false;
        private boolean dueling = false;
        private Point2D selectedCorner;
        private Strategy actualStrategy;

        public ScaloniManager(Messi robot) {
            super(robot);

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

        private boolean inPosition() {
            Point2D position = new Point2D.Double(robot.robotX, robot.robotY);
            Point2D pointWallY = new Point2D.Double(selectedCorner.getX(), position.getY());
            Point2D pointWallX = new Point2D.Double(position.getX(), selectedCorner.getY());
            double distanceWallY = pointWallY.distance(position);
            double distanceWallX = pointWallX.distance(position);

            return ((distanceWallY < 100) || (distanceWallX < 100));
        }

        @Override
        public IStrategy strategy() {
            if (this.robot.others == 1 && !this.dueling) {
                this.dueling = true;
                this.actualStrategy = new FoulStrategy(this.robot);
            } else if (this.inPosition() && !this.strafing && !this.dueling) {
                this.strafing = true;
                this.actualStrategy = new StrafeStrategy(this.robot);
            }
            return actualStrategy;
        }
    }

    // Manager 2
    private class SabellaManager extends Manager {
        private final IStrategy foulStrategy = new FoulStrategy(robot);
        private final IStrategy heartStrategy = new HeartStrategy(robot);

        public SabellaManager(Messi robot) {
            super(robot);
        }

        @Override
        public IStrategy strategy() {
            if (this.robot.energy >= 70) {
                return this.foulStrategy;
            }
            return this.heartStrategy;
        }
    }
}
