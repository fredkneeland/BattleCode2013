package simpleSolder;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class CloseBot {
	static int numbOfSoldiers = 0;
	static Direction dir;
	static int type = 0;
	static int MineLayer = 1;
	static int Defender2 = 2;
	static int Killer = 3;
	static int Flanker = 4;
	static int SmartDefender = 5;
	static int Encamper = 6;
	static int Formation = 7;
	static int Scout = 8;
	static int Artilery = 9;
	static int Rusher = 10;
	static MapLocation dRallyPoint;
	static Direction enemyHQ;
	static MapLocation[] rallyEncamps;
	static boolean medBay = false;
	static MapLocation best;
	static int x;
	static int y;
	static int energonLevel = 500;
	static int numbOfScouts = 0;
	static boolean enemyNuke = false;
	static int numbOfFlankers = 0;
	
	public static void run(RobotController rc) 
	{
		while (true)
		{
			try 
			{
				if (rc.getType() == RobotType.HQ)
				{
					if (rc.isActive())
					{
						if (dRallyPoint == null)
						{
							dRallyPoint = rc.senseHQLocation();
							enemyHQ = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							for (int i = 0; i < 6; i++)
							{
								dRallyPoint = dRallyPoint.add(enemyHQ);
							}
							
							rallyEncamps = rc.senseEncampmentSquares(dRallyPoint, 37, Team.NEUTRAL);
							if (rallyEncamps.length == 0)
							{
								medBay = false;
							}
							else
							{
								medBay = true;
								best = rallyEncamps[0];
								// loop through to find best encamp Spot
								for (int i = 1; i < rallyEncamps.length; i++)
								{
									if (dRallyPoint.distanceSquaredTo(best) > dRallyPoint.distanceSquaredTo(rallyEncamps[i]))
									{
										best = rallyEncamps[i];
									}
								}
								dRallyPoint = best;
							}
							
							
						}
						if (dir == null)
						{
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							Team mine = rc.senseMine(rc.getLocation().add(dir));
							while (mine != null && mine != rc.getTeam() || !rc.canMove(dir))
							{
								dir = Direction.values()[(int)(Math.random()*8)];
							}
						}
						
						if (energonLevel > rc.getEnergon())
						{
							HQBot.broadcast(rc, 37, 43);
							energonLevel -= 3;
						}
						else
						{
							HQBot.broadcast(rc, 0, 43);
						}
						
						if (rc.senseEnemyNukeHalfDone())
						{
							enemyNuke = true;
						}
						
						if (enemyNuke)
						{
							HQBot.broadcast(rc, 3, 22);
							rc.spawn(dir);
							if (numbOfSoldiers % 2 == 0)
							{
								type = Rusher;
							}
							else
							{
								type = Flanker;
								numbOfFlankers++;
								HQBot.broadcast(rc, numbOfFlankers, 1);
							}
							numbOfSoldiers++;
							HQBot.broadcast(rc, type, 0);
						}
						else if (rc.getTeamPower() < 150)
						{
							if (!rc.hasUpgrade(Upgrade.FUSION))
							{
								rc.researchUpgrade(Upgrade.FUSION);
							}
							else
							{
								rc.researchUpgrade(Upgrade.NUKE);
							}
						}
						else
						{
							rc.spawn(dir);
							
							if (numbOfSoldiers < 4)
							{
								type = Scout;
								HQBot.broadcast(rc, 1, ((numbOfScouts % 2)+1));
								numbOfScouts++;
							}
							else if (numbOfSoldiers < 7)
							{
								type = MineLayer;
							}
							else if (numbOfSoldiers < 19)
							{
								// defenders
								type = Defender2;
								x = dRallyPoint.x;
								y = dRallyPoint.y;
								HQBot.broadcast(rc, x, 1);
								HQBot.broadcast(rc, y, 2);
							}
							else if (numbOfSoldiers % 20 == 0)
							{
								type = MineLayer;
							}
							else if (numbOfSoldiers % 3 == 0)
							{
								type = Flanker;
								numbOfFlankers++;
								HQBot.broadcast(rc, numbOfFlankers, 1);
							}
							
							else
							{
								type = Defender2;
								x = dRallyPoint.x;
								y = dRallyPoint.y;
								HQBot.broadcast(rc, x, 1);
								HQBot.broadcast(rc, y, 2);
							}
							numbOfSoldiers++;
							HQBot.broadcast(rc, type, 0);
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
