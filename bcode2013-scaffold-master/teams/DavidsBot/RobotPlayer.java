package DavidsBot;

import battlecode.common.*;

public class RobotPlayer {
    public static void run(RobotController rc) {
        while (true) {
            try {
                if (rc.getType() == RobotType.HQ) {
                    if (rc.isActive()) {
                        if (Clock.getRoundNum() < 100 && !rc.hasUpgrade(Upgrade.FUSION)) {
                            rc.researchUpgrade(Upgrade.FUSION);
                        } else {
                            Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
                            Team mine = rc.senseMine(rc.getLocation().add(dir));

                            while (!rc.canMove(dir) || (mine != null && mine != rc.getTeam())) {
                                // code
                                dir = dir.rotateLeft();
                                mine = rc.senseMine(rc.getLocation().add(dir));
                            }
                            if (rc.canMove(dir)) {
                                rc.spawn(dir);
                            }
                        }

                    }
                } else if (rc.getType() == RobotType.SOLDIER) {

                }

                // End turn
                rc.yield();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}