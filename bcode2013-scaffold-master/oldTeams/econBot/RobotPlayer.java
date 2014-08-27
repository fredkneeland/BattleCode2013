package econBot;

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

public class RobotPlayer {

	static int numbOfSoldiers = 0;
	static int myType = 0;
	static boolean enemySeen = false;
	static Direction dir;
	static MapLocation[] encampSpots;
	static int dist1;
	static int dist2;
	static MapLocation temp;
	static MapLocation sender;
	static int x = -1;
	static int y = -1;
	static boolean attack = false;
	static MapLocation rallyPoint = null;
	static int numbOfEncampers = 0;
	
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.HQ) 
				{
					if (rc.isActive()) {
						// Spawn a soldier
						if (dir == null)
						{
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							encampSpots = rc.senseAllEncampmentSquares();
							for (int i = 0; i < encampSpots.length; i++)
							{
								for (int j = 1; j < encampSpots.length - i; j++)
								{
									dist1 = rc.getLocation().distanceSquaredTo(encampSpots[j-1]);
									dist2 = rc.getLocation().distanceSquaredTo(encampSpots[j]);
									if (dist2 < dist1)
									{
										temp = encampSpots[j-1];
										encampSpots[j-1] = encampSpots[j];
										encampSpots[j] = temp;
									}
									if (rc.isActive())
									{
										if (!rc.hasUpgrade(Upgrade.FUSION))
										{
											rc.researchUpgrade(Upgrade.FUSION);
										}
										else if (!rc.hasUpgrade(Upgrade.DEFUSION))
										{
											rc.researchUpgrade(Upgrade.DEFUSION);
										}
										else if (!rc.hasUpgrade(Upgrade.PICKAXE))
										{
											rc.researchUpgrade(Upgrade.PICKAXE);
										}
									}
								}
							}
						}
						dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						Team mine = rc.senseMine(rc.getLocation().add(dir));
						while (!rc.canMove(dir) || ((mine != null) && (mine != rc.getTeam())))
						{
							dir = Direction.values()[(int)(Math.random()*8)];
							mine = rc.senseMine(rc.getLocation().add(dir));
							if (Clock.getRoundNum() > 200)
							{
								rc.researchUpgrade(Upgrade.NUKE);
							}
						}
						if (rc.canMove(dir))
						{
							if (!rc.hasUpgrade(Upgrade.FUSION))
							{
								rc.researchUpgrade(Upgrade.FUSION);
							}
							else if (!rc.hasUpgrade(Upgrade.DEFUSION))
							{
								rc.researchUpgrade(Upgrade.DEFUSION);
							}
							else if (!rc.hasUpgrade(Upgrade.PICKAXE))
							{
								rc.researchUpgrade(Upgrade.PICKAXE);
							}
							else if (Clock.getRoundNum() < 105)
							{
								rc.spawn(dir);
								rc.broadcast(123, 65);
							}
							rc.spawn(dir);
							rc.broadcast(123, (1));
							if (Clock.getRoundNum() < 400 || numbOfSoldiers % 5 == 0)
							{
								sender = encampSpots[numbOfEncampers];
								rc.broadcast(124, sender.x);
								rc.broadcast(125, sender.y);
								numbOfEncampers++;
								numbOfSoldiers++;
							}
							else
							{
								rc.broadcast(124, 0);
								rc.broadcast(125, 0);
								numbOfSoldiers++;
							}
							
						}
					}
				} 
				else if (rc.getType() == RobotType.ARTILLERY)
				{
					if (rc.isActive())
					{
					MapLocation enemyLoc;
					Robot[] enemy;
					enemy = rc.senseNearbyGameObjects(Robot.class);
					for (int i =0; i < enemy.length; i++)
					{
						if (rc.getTeam().equals(enemy[i].getTeam()))
						{
							
						}
						else
						{
							enemyLoc = rc.senseLocationOf(enemy[i]);
							if (rc.canAttackSquare(enemyLoc))
							{
								rc.attackSquare(enemyLoc);
								break;
							}
						}
					}
					}
				} else if (rc.getType() == RobotType.SOLDIER) {
					if (rc.isActive()) 
					{
						Direction dir;
						MapLocation encamp = null;
						if (rallyPoint == null)
						{
							rallyPoint = rc.senseHQLocation();
							dir = rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation());
							for (int i = 0; i < 10; i++)
							{
								rallyPoint = rallyPoint.add(dir);
							}
						}
						if (myType == 0)
						{
							myType = rc.readBroadcast(123);
						}
						if (myType == 65)
						{
							Miner.run(rc);
						}
						else
						{
						// go for resources
						if (Clock.getRoundNum() < 275)
						{
							if (x == -1)
							{
								x = rc.readBroadcast(124);
								y = rc.readBroadcast(125);
							}
							encamp = new MapLocation(x,y);
							dir = rc.getLocation().directionTo(encamp);
						}
						// pound enemy
						else
						{
							if (Clock.getRoundNum() % 200 == 0)
							{
								attack = true;
							}
							if (attack)
							{
								dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							}
							else
							{
								dir = rc.getLocation().directionTo(rallyPoint);
							}
						}
						
						if (Clock.getRoundNum() < 400 && rc.senseEncampmentSquare(rc.getLocation()))
						{
							if ((rc.getTeamPower() < 450 && rc.getTeamPower() > 75) || Clock.getRoundNum() < 175)
							{
								rc.captureEncampment(RobotType.GENERATOR);
							}
							else if (rc.getTeamPower() > 449)
							{
								rc.captureEncampment(RobotType.SUPPLIER);
							}
						}
						else
						{
							Team mine = rc.senseMine(rc.getLocation().add(dir));
							if(rc.canMove(dir) && (dir != Direction.NONE || dir != Direction.OMNI)) 
							{
								if( mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir));
								}
								else
								{
									rc.move(dir);
								}
							}
							else
							{
								
								while (!rc.canMove(dir))
								{
									dir = Direction.values()[(int)(Math.random()*8)];
								}
								mine = rc.senseMine(rc.getLocation().add(dir));
								if( mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir));
								}
								else{
								rc.move(dir);
								}
							}
						}
						}
					}
				}
				// End turn
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}
	
	public static void Move(RobotController rc, Direction dir) throws GameActionException
	{
		Team mine = rc.senseMine(rc.getLocation().add(dir));
		if(rc.canMove(dir) && (dir != Direction.NONE || dir != Direction.OMNI)) 
		{
			if( mine!= null && mine != rc.getTeam())
			{
				rc.defuseMine(rc.getLocation().add(dir));
			}
			else
			{
				rc.move(dir);
			}
		}
		else
		{
			
			while (!rc.canMove(dir))
			{
				dir = Direction.values()[(int)(Math.random()*8)];
			}
			mine = rc.senseMine(rc.getLocation().add(dir));
			if( mine!= null && mine != rc.getTeam())
			{
				rc.defuseMine(rc.getLocation().add(dir));
			}
			else{
			rc.move(dir);
			}
		}
	}

}
