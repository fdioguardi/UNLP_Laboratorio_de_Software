package diez;

public class FoulStrategy extends Strategy {
    private final int gunTurnAmt = 10;
    private int count = 0;

    public FoulStrategy(Messi robot) {
        super(robot);
        robot.out.println("Foul Strategy");
    }

    @Override
    public void run() {
        robot.turnGunRight(this.gunTurnAmt);

    }

    @Override
    public void onScannedRobot() {
        this.count = 0;
        if (robot.scannedDistance > 150.0) {
            robot.turnGunRight(robot.scannedBearing);
            robot.turnRight(robot.scannedBearing);
            robot.ahead(robot.scannedDistance - 140);
        } else {
            robot.turnGunRight(robot.scannedBearing);
            robot.fire(3);
            if (robot.scannedDistance < 100.0D) {
                if (robot.scannedBearing > -90.0D && robot.scannedBearing <= 90.0D) {
                    robot.back(40);
                } else {
                    robot.ahead(40);
                }
            }
        }
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
