package diez;

import robocode.JuniorRobot;

public class HeartStrategy extends Strategy {
    private int direction = 1;

    public HeartStrategy(JuniorRobot robot) {
        super(robot);
        robot.out.println("Heart Strategy");
    }

    public void run() {
        int eleccionDivina = (int) (Math.random() * 4 + 1);
        int movimientoDivino = (int) (Math.random() * 600 + 400);
        int anguloDivino = (int) (Math.random() * 359);
        switch (eleccionDivina) {
            case 1:
                robot.turnAheadRight(movimientoDivino, anguloDivino);
                break;
            case 2:
                robot.turnAheadLeft(movimientoDivino, anguloDivino);
                break;
            case 3:
                robot.turnBackRight(movimientoDivino, anguloDivino);
                break;
            case 4:
                robot.turnBackLeft(movimientoDivino, anguloDivino);
                break;
        }
        robot.turnGunTo(anguloDivino);
    }

    @Override
    public void onScannedRobot() {
        robot.turnGunTo(robot.scannedAngle);
        if (robot.scannedDistance < 100) {
            robot.fire(3);
        } else {
            robot.fire(1);
        }
    }

    @Override
    public void onHitByBullet() {

    }

    @Override
    public void onHitWall() {
        robot.ahead(150 * this.direction);
        direction *= -1;
    }

    @Override
    public void onHitRobot() {
        robot.fire(1);
    }

}