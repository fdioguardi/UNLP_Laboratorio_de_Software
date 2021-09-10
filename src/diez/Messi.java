package diez;

import robocode.JuniorRobot;

public class Messi extends JuniorRobot {
    private Strategy strategy;
    private boolean init = true;
    private boolean strafing = false;
    private boolean inPosition = false;

    @Override
    public void run() {
        setColors(blue, white, white, yellow, black);
        // no podemos setearla en el constuctor ni en la declaracion
        // porque el robot explota (literalmente)
        // El programa no puede ejecutar cosas del robot antes de que inicie la batalla
        // como crea a los robots antes, boom.
        if (init) {
            init = false;
            strategy = new CornerStrategy(this);
        } else if (inPosition && !strafing) {
            strafing = true;
            strategy = new StrafeStrategy(this);
        }
        strategy.run();
    }

    public void declareInPosition() {
        this.inPosition = true;
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    @Override
    public void onScannedRobot() {
        strategy.onHitRobot();
    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
    @Override
    public void onHitByBullet() {
        strategy.onHitByBullet();
    }

    /**
     * onHitWall: What to do when you hit a wall
     */
    @Override
    public void onHitWall() {
        strategy.onHitWall();
    }

    public void onHitRobot() {
        strategy.onHitRobot();
    }
}
