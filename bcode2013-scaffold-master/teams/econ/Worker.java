package econ;

import battlecode.common.*;

public class Worker {
    int type = 0;
    final int channel = 567;
    final int attacker = 1;
    final int defender = 2;
    final int worker = 3;
    public void run(RobotController rc) {
        rc.setIndicatorString(1, "Worker");
        Direction dir;
        MapLocation enemyHQ = rc.senseEnemyHQLocation();
        Team mine;
        MapLocation encamps[] = rc.senseAllEncampmentSquares();
        int spot = -1;
        MapLocation encamp = enemyHQ;

        for (int i = 0; i < encamps.length; i++) {
            for (int j = i; j < encamps.length; j++) {
                if (rc.getLocation().distanceSquaredTo(encamps[i]) > rc.getLocation().distanceSquaredTo(encamps[j])) {
                    MapLocation temp = encamps[i];
                    encamps[i] = encamps[j];
                    encamps[j] = temp;
                }
            }
        }



        while (true) {
            try {
                if (spot == -1) {
                    spot = rc.readBroadcast(channel+1);
                    //rc.setIndicatorString(2, ""+spot);
                    encamp = encamps[spot];
                    rc.setIndicatorString(2, ""+encamp);
                }

                if (rc.isActive()) {
                    dir = rc.getLocation().directionTo(encamp);
                    mine = rc.senseMine(rc.getLocation().add(dir));

                    if (rc.getLocation().equals(encamp)) {
                        if (spot % 4 == 3) {
                            rc.captureEncampment(RobotType.GENERATOR);
                        } else {
                            rc.captureEncampment(RobotType.SUPPLIER);
                        }
                    } else if (mine != null && mine != rc.getTeam()) {
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