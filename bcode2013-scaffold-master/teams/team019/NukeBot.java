package team019;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class NukeBot {
	static int numbOfSoldiers = 0;
	static Direction dir;
	static int x = 0;
	static int y = 0;
	static MapLocation[] encampSpots;
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
	static int counter = 0;
	
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
						if (dir == null)
						{
							encampSpots = rc.senseEncampmentSquares(rc.getLocation(), 9, Team.NEUTRAL);
							if (encampSpots.length > 5)
							{
								
							}
							else
							{
								encampSpots = rc.senseEncampmentSquares(rc.getLocation(), 16, Team.NEUTRAL);
							}
							if (encampSpots.length > 5)
							{
								
							}
							else
							{
								encampSpots = rc.senseEncampmentSquares(rc.getLocation(), 25, Team.NEUTRAL);
							}
							if (encampSpots.length > 5)
							{
								
							}
							else
							{
								encampSpots = rc.senseEncampmentSquares(rc.getLocation(), 36, Team.NEUTRAL);
							}
							if (encampSpots.length > 5)
							{
								
							}
							else
							{
								encampSpots = rc.senseEncampmentSquares(rc.getLocation(), 49, Team.NEUTRAL);
							}
							
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						}
						counter = 0;
						Team mine = rc.senseMine(rc.getLocation().add(dir));
						if (mine != null && mine != rc.getTeam() || !rc.canMove(dir))
						{
							while (mine != null && mine != rc.getTeam() || !rc.canMove(dir))
							{
								dir = Direction.values()[(int)(Math.random()*8)];
								if (counter > 50)
								{
										if (!rc.hasUpgrade(Upgrade.VISION))
										{
											rc.researchUpgrade(Upgrade.VISION);
										}
										else
										{
											rc.researchUpgrade(Upgrade.NUKE);
										}
								}
								counter++;
							}
						}
					}
					
					if (rc.senseEnemyNukeHalfDone())
					{
						if (!rc.hasUpgrade(Upgrade.DEFUSION))
						{
							rc.researchUpgrade(Upgrade.DEFUSION);
						}
						else if (numbOfSoldiers % 2 == 0)
						{
							rc.spawn(dir);
							type = Flanker;
							HQBot.broadcast(rc, type, 0);
						}
						else
						{
							rc.spawn(dir);
							type = Rusher;
							HQBot.broadcast(rc, type, 0);
						}
					}
					else if (numbOfSoldiers < 3)
					{
						rc.spawn(dir);
						type = MineLayer;
						HQBot.broadcast(rc, type, 0);
					}
					else if (numbOfSoldiers < 9)
					{
						rc.spawn(dir);
						type = Artilery;
						x = encampSpots[numbOfSoldiers-3].x;
						y = encampSpots[numbOfSoldiers-3].y;
						HQBot.broadcast(rc, type, 0);
						HQBot.broadcast(rc, x, 1);
						HQBot.broadcast(rc, y, 2);
					}
					else if (!rc.hasUpgrade(Upgrade.VISION))
					{
						rc.researchUpgrade(Upgrade.VISION);
					}
					else
					{
						rc.researchUpgrade(Upgrade.NUKE);
					}
					numbOfSoldiers++;
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
