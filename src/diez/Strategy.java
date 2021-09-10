package diez;

public abstract class Strategy implements IStrategy {
    protected Messi robot;

    public Strategy(Messi robot) {
        this.robot = robot;
    }
}
