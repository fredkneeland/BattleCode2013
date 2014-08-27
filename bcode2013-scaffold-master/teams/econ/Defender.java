package econ;

import battlecode.common.*;

public class Defender {
    int type = 0;
    final int channel = 567;
    final int attacker = 1;
    final int defender = 2;
    final int worker = 3;
    public void run(RobotController rc) {
        rc.setIndicatorString(1, "Defender");
        Direction dir;
        MapLocation enemyHQ = rc.senseEnemyHQLocation();
        Team mine;
        while (true) {
            try {
                if (rc.isActive()) {

                    Robot units[] = rc.senseNearbyGameObjects(Robot.class, 1000);
                    MapLocation target = null;
                    int numb = 0;
                    for (int i = 0; i < units.length; i++) {
                        if ( rc.getTeam() != units[i].getTeam() ) {
                            numb++;
                            target = rc.senseLocationOf(units[i]);
                        }
                    }

                    dir = Direction.values()[(int)(Math.random()*8)];
                    mine = rc.senseMine(rc.getLocation().add(dir));
                    if (rc.senseEncampmentSquare(rc.getLocation())) {
                        rc.captureEncampment(RobotType.ARTILLERY);
                    } else if (numb > 0) {
                        dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
                        if (target != null) {
                            dir = rc.getLocation().directionTo(target);
                        }
                        mine = rc.senseMine(rc.getLocation().add(dir));
                        if (mine != null && mine != rc.getTeam()) {
                            rc.defuseMine(rc.getLocation().add(dir));
                        }
                        while (!rc.canMove(dir)) {
                            dir = Direction.values()[(int)(Math.random()*8)];
                            mine = rc.senseMine(rc.getLocation().add(dir));
                            if (mine != null && mine != rc.getTeam()) {
                                rc.defuseMine(rc.getLocation().add(dir));
                            }
                        }
                        if (rc.canMove(dir)) {
                            rc.move(dir);
                        }
                    } else if (rc.senseMine(rc.getLocation()) == null) {
                        rc.layMine();
                    } else if (mine != null && mine != rc.getTeam()) {
                        rc.defuseMine(rc.getLocation().add(dir));
                    } else {
                        while (!rc.canMove(dir)) {
                            dir = Direction.values()[(int)(Math.random()*8)];
                            mine = rc.senseMine(rc.getLocation().add(dir));
                            if (mine != null && mine != rc.getTeam()) {
                                rc.defuseMine(rc.getLocation().add(dir));
                                break;
                            }
                        }

                        if (rc.canMove(dir) && rc.isActive() && (mine == null || mine == rc.getTeam())) {
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