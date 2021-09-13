package diez;

public class FoulStrategy extends Strategy {

    public FoulStrategy(Messi robot) {
        super(robot);
        robot.out.println("Foul Strategy");
    }

    @Override
    public void run() {
        robot.turnRight(10);
    }

    @Override
    public void onScannedRobot() {
        robot.turnRight(robot.scannedBearing);
        robot.ahead(robot.scannedDistance + 100);
        if (robot.scannedDistance < 10) robot.fire(3);
        else robot.fire(1);
    }

    @Override
    public void onHitByBullet() {

    }

    @Override
    public void onHitWall() {

    }

    @Override
    public void onHitRobot() {

    }
}
