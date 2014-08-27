package econ;

import battlecode.common.*;

public class Attacker {
    int type = 0;
    final int channel = 567;
    final int attacker = 1;
    final int defender = 2;
    final int worker = 3;
    public void run(RobotController rc) {
        rc.setIndicatorString(1, "Attacker");
        Direction dir;
        MapLocation enemyHQ = rc.senseEnemyHQLocation();
        Team mine;
        while (true) {
            try {
                if (rc.isActive()) {
                    if (rc.getRobot().getID() % 2 == 0) {
                        dir = rc.getLocation().directionTo(enemyHQ);
                        dir = dir.rotateLeft();
                    } else {
                        dir = rc.getLocation().directionTo(enemyHQ);
                        dir = dir.rotateRight();
                    }

                    mine = rc.senseMine(rc.getLocation().add(dir));

                    if (mine != null && mine != rc.getTeam()) {
                        rc.defuseMine(rc.getLocation().add(dir));
                    } else {
                        while (!rc.canMove(dir) || (mine != null && mine != rc.getTeam())) {
                            dir = Direction.values()[(int)(Math.random()*8)];
                            mine = rc.senseMine(rc.getLocation().add(dir));
                            if (mine != null && mine != rc.getTeam()) {
                                rc.defuseMine(rc.getLocation().add(dir));
                            }
                        }

                        if (rc.canMove(dir)) {
                            rc.move(dir);
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}