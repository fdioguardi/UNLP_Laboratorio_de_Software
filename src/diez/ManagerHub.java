package diez;

public class ManagerHub {
    private IRobotManager single_instance;

    public IRobotManager manager() {
        return this.single_instance == null ? new DefensiveManager() : this.single_instance;
    }

    // Manager 1
    private class DefensiveManager implements IRobotManager {
        @Override
        public Strategy strategy() {
            return new CornerStrategy(null);
        }
    }

    // Manager 2
    private class OffensiveManager implements IRobotManager {
        @Override
        public Strategy strategy() {
            return new StrafeStrategy(null);
        }
    }
}
