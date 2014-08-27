package econ;

import battlecode.common.*;

/** The example funcs player is a player meant to demonstrate basic usage of the most common commands.
 * Robots will move around randomly, occasionally mining and writing useless messages.
 * The HQ will spawn soldiers continuously.
 */
public class RobotPlayer {
    public static void run(RobotController rc) {
        while (true) {
            try {
                if (rc.getType() == RobotType.HQ) {
                    HQ hq = new HQ();
                    hq.run(rc);
                } else if (rc.getType() == RobotType.SOLDIER) {
                    Soldier soldier = new Soldier();
                    soldier.run(rc);
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
                }

                // End turn
                rc.yield();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
