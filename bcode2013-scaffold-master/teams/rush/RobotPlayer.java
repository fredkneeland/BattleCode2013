package rush;

import battlecode.common.*;

public class RobotPlayer {
    public static int numbOfSoldiers = 0;
    public static int numbOfTries = 0;
    public static void run(RobotController rc) {
        while (true) {
            try {
                if (rc.getType() == RobotType.HQ) {
                    if (rc.isActive()) {
                        // Spawn a soldier
                        Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
                        Team mine = rc.senseMine(rc.getLocation().add(dir));
                        numbOfTries = 0;
                        while (!rc.canMove(dir) || (mine != null && mine != rc.getTeam())) {
                            dir = Direction.values()[(int) (Math.random() * 8)];
                            mine = rc.senseMine(rc.getLocation().add(dir));
                            numbOfTries++;
                            if (numbOfTries > 100) {
                                numbOfSoldiers++;
                                break;
                            }
                        }
                        if (rc.canMove(dir) && numbOfTries < 90) {
                            numbOfSoldiers++;
                            rc.spawn(dir);
                        }
                    }
                } else if (rc.getType() == RobotType.ARTILLERY) {
                    Robot enemies[] = rc.senseNearbyGameObjects(Robot.class, 63);
                    int numb = 0;
                    for (int i = 0; i < enemies.length; i++) {
                        if (rc.getTeam() != enemies[i].getTeam()) {
                            enemies[0] = enemies[i];
                            numb++;
                        }
                    }

                    if (numb > 0) {
                        MapLocation target = rc.senseLocationOf(enemies[0]);
                        rc.attackSquare(target);
                    }
                } else if (rc.getType() == RobotType.SOLDIER) {
                    if (rc.isActive()) {
                        Team mine = rc.senseMine(rc.getLocation());
                        Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
                        mine = rc.senseMine(rc.getLocation().add(dir));
                        if (mine != null && mine != rc.getTeam()) {
                            rc.defuseMine(rc.getLocation().add(dir));
                        }
                        while (!rc.canMove(dir)) {
                            dir = Direction.values()[(int) (Math.random() * 8)];
                            mine = rc.senseMine(rc.getLocation().add(dir));
                            if (mine != null && mine != rc.getTeam()) {
                                rc.defuseMine(rc.getLocation().add(dir));
                            }
                        }
                        if (rc.canMove(dir)) {
                            rc.move(dir);
                        }
                        System.out.println("moving");
                    }
                }

                // End turn
                rc.yield();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}