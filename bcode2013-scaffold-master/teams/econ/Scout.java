package econ;

import battlecode.common.*;

import java.lang.System;
import java.util.*;
import java.util.LinkedList;

public class Scout {
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

        while (queue.size() > 0) {
            MapLocation current = queue.remove();
            System.out.println(current);
            visited.add(current);
            int value = mapValues[current.x][current.y];

            for (int i = 0; i < dirs.length; i++) {
                if (dirs[i] == Direction.OMNI || dirs[i] == Direction.NONE) {
                    continue;
                }
                MapLocation newSpot = current.add(dirs[i]);
                if (newSpot == null) {
                    continue;
                }
                int x = newSpot.x;
                int y = newSpot.y;

                if (x >= width || x < 0 || y >= height || y < 0 ) {
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
                    }

                    if (!visited.contains(newSpot) && !queue.contains(newSpot)) {
                        queue.add(newSpot);
                        //System.out.println("Not visited: " + newSpot);
                    } else {
                        //System.out.println("visited: " + newSpot);
                    }
                }
            }
            //System.out.println(visited.size());
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

        try {
            rc.wearHat();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MapLocation target = path.pop();

        while (true) {
            try {
                if (rc.isActive()) {
                    if (rc.getLocation().equals(target)) {
                        target = path.pop();
                    }
                    dir = rc.getLocation().directionTo(target);

                    mine = rc.senseMine(rc.getLocation().add(dir));

                    if (mine != null && mine != rc.getTeam()) {
                        rc.defuseMine(rc.getLocation().add(dir));
                    }

                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}