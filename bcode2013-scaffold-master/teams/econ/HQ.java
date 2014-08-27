package econ;

import battlecode.common.*;
import java.util.*;

public class HQ {
    int numbOfSoldiers = 0;
    final int channel = 567;
    final int attacker = 1;
    final int defender = 2;
    final int worker = 3;
    final int scout = 4;
    int workers = 0;
    double energon = 500.0;
    public void run(RobotController rc) {
        Direction dir;
        MapLocation enemyHQ = rc.senseEnemyHQLocation();
        Team mine;
        int type = 0;
        final int channel = 567;
        int width = rc.getMapWidth();
        int height = rc.getMapHeight();
        int[][] mapValues = new int[width][height];
        Direction[] dirs = Direction.values();
        Queue<MapLocation> queue = new LinkedList<MapLocation>();
        Queue<MapLocation> visited = new LinkedList<MapLocation>();
        queue.add(rc.getLocation());
        mapValues[rc.getLocation().x][rc.getLocation().y] = 0;

        System.out.println("before while loop, " + dirs.length);
        System.out.println(queue.size());

        int counter = 0;

        while (queue.size() > 0) {
            MapLocation current = queue.remove();
            //System.out.println(current);
            visited.add(current);
            int value = mapValues[current.x][current.y];

            for (int i = 0; i < dirs.length; i++) {
                if (dirs[i] == Direction.OMNI || dirs[i] == Direction.NONE) {
                    continue;
                }
                MapLocation newSpot = current;//.add(dirs[i]);
                if (newSpot == null) {
                    continue;
                }
                int x = newSpot.x;
                int y = newSpot.y;

                /*if (x >= width || x < 0 || y >= height || y < 0 ) {
                    continue;
                } else {
                    if (mapValues[x][y] == 0) {
                        if (rc.senseMine(newSpot) != null) {
                            mapValues[x][y] = value + 12;
                        } else {
                            mapValues[x][y] = value + 1;
                        }
                    } else {
                        int additionalValue = 0;
                        if (rc.senseMine(newSpot) != null) {
                            additionalValue = 12;
                        } else {
                            additionalValue = 1;
                        }
                        int total = value + additionalValue;

                        if (mapValues[x][y] < total) {
                            mapValues[x][y] = total;
                        }
                    }*/

                    if (!visited.contains(newSpot) && !queue.contains(newSpot)) {
                        queue.add(newSpot);
                        //System.out.println("Not visited: " + newSpot);
                    } else {
                        //System.out.println("visited: " + newSpot);
                    }
                //}
            }
            //System.out.println(visited.size());
            System.out.println(counter++);
        }



        System.out.println("Finished calculating values");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(mapValues[i][j] + ", ");
            }
            System.out.println();
        }

        Stack<MapLocation> path = new Stack<MapLocation>();

        path.push(enemyHQ);

        MapLocation searching = enemyHQ;
        while (!rc.getLocation().isAdjacentTo(searching)) {
            int smallestValue = 9999999;
            MapLocation pathToTake = searching;
            for (int i = 0; i < dirs.length; i++) {
                MapLocation spotToCheck = searching.add(dirs[i]);
                int x = spotToCheck.x;
                int y = spotToCheck.y;

                if (x >= width || x < 0 || y >= height || y < 0 ) {
                    continue;
                }

                if (smallestValue > mapValues[x][y]) {
                    smallestValue = mapValues[x][y];
                    pathToTake = spotToCheck;
                }
            }

            path.push(pathToTake);
            searching = pathToTake;
        }

        System.out.println("After while loop");
        /*
        while (true) {
            try {
                if (rc.isActive()) {
                    Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
                    Team mine = rc.senseMine(rc.getLocation().add(dir));

                    int i = 0;
                    while (!rc.canMove(dir) || (mine != null && mine != rc.getTeam())) {
                        dir = Direction.values()[(int)(Math.random()*8)];
                        mine = rc.senseMine(rc.getLocation().add(dir));
                        i++;
                        if (i > 100) {
                        //    rc.researchUpgrade(Upgrade.NUKE);
                            break;
                        }
                    }

                    if (i > 100) {
                    } else if (rc.getTeamPower() < 100 && !rc.hasUpgrade(Upgrade.FUSION)) {
                        rc.researchUpgrade(Upgrade.FUSION);
                    } else if (rc.getTeamPower() < 100 && !rc.hasUpgrade(Upgrade.PICKAXE)) {
                        rc.researchUpgrade(Upgrade.PICKAXE);
                    } else if (rc.getTeamPower() < 100 && !rc.hasUpgrade(Upgrade.DEFUSION)) {
                        rc.researchUpgrade(Upgrade.DEFUSION);
                    } else if (rc.getTeamPower() < 100 ) {
                        rc.researchUpgrade(Upgrade.NUKE);
                    } else if (rc.canMove(dir)) {
                        rc.spawn(dir);
                        numbOfSoldiers++;
                        if (true) {
                            rc.broadcast(channel, scout);
                        } else if (rc.getEnergon() < energon) {
                            rc.broadcast(channel, defender);
                            energon -= 5;
                        } else if (numbOfSoldiers < 12) {
                            rc.broadcast(channel, defender);
                        } else if (numbOfSoldiers < 15) {
                            rc.broadcast(channel, worker);
                            rc.broadcast(channel+1, workers);
                            workers++;
                        } else if (numbOfSoldiers % 5 == 0 && workers < 12) {
                            rc.broadcast(channel, worker);
                            rc.broadcast(channel+1, workers);
                            workers++;
                        } else if (numbOfSoldiers % 4 == 3) {
                            rc.broadcast(channel, attacker);
                        } else {
                            rc.broadcast(channel, defender);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}