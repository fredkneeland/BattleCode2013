package simpleSolder;

import calcBot.RobotPlayer;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class SmartD {
	
	static MapLocation mineSpot;
	static MapLocation mineSpotAhead;
	static MapLocation waitSpot;
	static GameObject enemys;
	static Direction dir;
	static boolean enemyOnMine = false;
	static boolean secondMineLayed = false;
	static boolean firstMineLayed = false;
	
	public static void run(RobotController rc, int MyType2, int x_1, int y_1) 
	{
		mineSpot = new MapLocation(x_1, y_1);
		waitSpot = mineSpot.subtract(rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
		while (true)
		{
			try 
			{
				if (rc.getType() == RobotType.SOLDIER)
				{
					if (rc.isActive())
					{
						Team mine = rc.senseMine(mineSpot);
						Team mine2 = rc.senseMine(mineSpot.add(rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation())));
						mineSpotAhead = mineSpot.add(rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation()));
						
						if ((mine2 == null || mine2 != rc.getTeam()) && (!rc.getLocation().equals(mineSpotAhead)) && !secondMineLayed)
						{
							dir = rc.getLocation().directionTo(mineSpotAhead);
							HQBot.Move(rc, dir);
						}
						else if ((rc.getLocation().equals(mineSpotAhead) && mine2 == null) && !secondMineLayed)
						{
							rc.layMine();
						}
						
						else if ((mine == null || mine != rc.getTeam()) && (!rc.getLocation().equals(mineSpot)) && !firstMineLayed)
						{
							dir = rc.getLocation().directionTo(mineSpot);
							HQBot.Move(rc, dir);
						}
						else if ((rc.getLocation().equals(mineSpot) && mine == null) && !firstMineLayed)
						{
							rc.layMine();
						}
						else
						{
							secondMineLayed = true;
							firstMineLayed = true;
							if (!enemyOnMine)
							{
								GameObject enemy = rc.senseObjectAtLocation(mineSpot);
								if (enemy != null)
								{
									enemyOnMine = true;
									/*
									if (enemy.getTeam() != rc.getTeam())
									{
										enemyOnMine = true;
									}
									else if (!rc.getLocation().equals(waitSpot))
									{
										dir = rc.getLocation().directionTo(waitSpot);
										HQBot.Move(rc, dir);
									}
									*/
								}
								if (!rc.getLocation().equals(waitSpot))
								{
									dir = rc.getLocation().directionTo(waitSpot);
									HQBot.Move(rc, dir);
								}
								else
								{
									
								}
							}
							else
							{
								Class<Robot> Robot = Robot.class;
								Team enemyTeam = Team.A;
								if (rc.getTeam() == Team.A)
								{
									enemyTeam = Team.B;
								}
								else
								{
									enemyTeam = Team.A;
								}
								//enemys = rc.senseNearbyGameObjects(Robot, 4, enemyTeam);
								enemys = rc.senseObjectAtLocation(mineSpotAhead);
								if (enemys == null)
								{
									if (!rc.getLocation().equals(waitSpot))
									{
										dir = rc.getLocation().directionTo(waitSpot);
										HQBot.Move(rc, dir);
									}
								}
								else
								{
									if (!rc.getLocation().equals(mineSpot))
									{
										dir = rc.getLocation().directionTo(mineSpot);
										HQBot.Move(rc, dir);
									}
								}
							}
						}
					}
				}
				rc.yield();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
}
