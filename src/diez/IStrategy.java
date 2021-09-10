package diez;

public interface IStrategy {

    void run();

    void onHitRobot();

    void onHitByBullet();

    void onHitWall();
}