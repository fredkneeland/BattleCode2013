package econ;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class DefensiveBot
{
	static Direction dir;
	static boolean encamper = false;
	//static int x_1 = 0;
	//static int y_1 = 0;
	static MapLocation rallyPoint;
	static int Supplier = 1;
	static boolean distress = false;
	static boolean Attack = false;
	static boolean enemySeen = false;
	static boolean shield = false;
	static final int baseBroadcast = 233;
	static Direction dir22;
	public static void run(RobotController rc, int MyType2, int x_1, int y_1) 
	{
		while (true)
		{
			try 
			{
				if (rc.getType() == RobotType.SOLDIER)
				{
					if (rc.isActive())
					{
					rallyPoint = new MapLocation(x_1, y_1);
					
					if (rallyPoint.distanceSquaredTo(rc.senseHQLocation()) > 122)
					{
						dir22 = rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation());
						rallyPoint = rc.senseHQLocation();
						for (int z = 0; z < 6; z++)
						{
							rallyPoint = rallyPoint.add(dir);
						}
					}
					
					Direction dir2 = null;
					dir2 = rc.getLocation().directionTo(rallyPoint);
					MapLocation enemyLoc;
					Robot[] enemy;
					enemy = rc.senseNearbyGameObjects(Robot.class);
					enemySeen = false;
					for (int i =0; i < enemy.length; i++)
					{
						if (rc.getTeam().equals(enemy[i].getTeam()))
						{
							
						}
						else if (rc.getLocation().distanceSquaredTo(rc.senseLocationOf(enemy[i])) < 10)
						{
							enemyLoc = rc.senseLocationOf(enemy[i]);
							dir2 = rc.getLocation().directionTo(enemyLoc);
							enemySeen = true;
						}
					}
					if (rc.readBroadcast(baseBroadcast+4) == 3)
					{
						distress = true;
					}
					else
					{
						distress = false;
					}
					if (Clock.getRoundNum() % 200 == 0 && !enemySeen)
					{
						Attack = true;
					}
					
					if (distress)
					{
						dir2 = rc.getLocation().directionTo(rc.senseHQLocation().add(rc.getLocation().directionTo(rc.senseEnemyHQLocation())));;
					}
					
					if (rc.readBroadcast(232) == 1)
					{
						Attack = true;
						dir2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						RobotPlayer.Move(rc, dir2);
					}
					else if (distress)
					{
						if ((dir2 == Direction.OMNI || dir2 == Direction.NONE))
						{
							
						}
						else
						{
							RobotPlayer.Move(rc, dir2);
						}
					}
					
					else if (rc.getLocation().equals(rallyPoint) && !distress && !Attack && !enemySeen)
					{
						if (rc.senseEncampmentSquare(rc.getLocation()))
						{	
							rc.captureEncampment(RobotType.MEDBAY);
						}
						if (rc.senseMine(rc.getLocation()) == null)
						{
							rc.layMine();
						}
						
					}
					else if (rc.senseEncampmentSquare(rc.getLocation()) && !Attack && !enemySeen && rc.getLocation().distanceSquaredTo(rc.senseHQLocation()) > 4)
					{
						if (Clock.getRoundNum() > 0)
						{
							rc.captureEncampment(RobotType.ARTILLERY);
						}
						else
						{
							rc.captureEncampment(RobotType.SHIELDS);
							shield = true;
						}
					}
					else if (rc.getEnergon() < 13)
					{
						Direction dir3;
						while (rc.getEnergon() < 40)
						{
							dir3 = rc.getLocation().directionTo(rallyPoint);
								RobotPlayer.Move(rc, dir3);
							rc.yield();
						}
					}
					else if (!distress && Attack && !enemySeen)
					{
						dir2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						RobotPlayer.Move(rc, dir2);
					}
					else if (Attack && enemySeen && !distress)
					{
						RobotPlayer.fightingMove(rc, dir2);
					} 
					else if (enemySeen && enemy.length > 20)
					{
						dir2 = rc.getLocation().directionTo(rallyPoint);
						RobotPlayer.Move(rc, dir2);
					}
					else if (enemySeen  && !distress)
					{
						RobotPlayer.fightingMove(rc, dir2);
					}
					
					else
					{
						RobotPlayer.Move(rc, dir2);
					}
					}
				}
				rc.yield();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}


}
