package diez;

import robocode.JuniorRobot;

public class Messi extends JuniorRobot {
    ManagerHub managerHub = new ManagerHub();

    @Override
    public void run() {
        setColors(blue, white, white, yellow, black);
        managerHub.manager(this).strategy().run();
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    @Override
    public void onScannedRobot() {
        managerHub.manager(this).strategy().onHitRobot();
    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
    @Override
    public void onHitByBullet() {
        managerHub.manager(this).strategy().onHitByBullet();
    }

    /**
     * onHitWall: What to do when you hit a wall
     */
    @Override
    public void onHitWall() {
        managerHub.manager(this).strategy().onHitWall();
    }

    public void onHitRobot() {
        managerHub.manager(this).strategy().onHitRobot();
    }
}
