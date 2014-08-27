package team019;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class ScoutBot {
	
	static Direction dir;
	static Team mine;
	static int numbOfMoves = 0;
	static boolean ranIntoMine = false;
	static boolean foundDir;
	static MapLocation[] lastSpots = new MapLocation[5];
	static boolean initializedLastSpots = false;
	
	public static void run(RobotController rc, boolean left) 
	{
		while (true)
		{
			try 
			{
				if (rc.getType() == RobotType.SOLDIER)
				{
					if (rc.isActive())
					{
						if (!initializedLastSpots)
						{
							lastSpots[0] = new MapLocation(rc.getLocation().x, rc.getLocation().y);
							lastSpots[1] = new MapLocation(rc.getLocation().x, rc.getLocation().y);
							lastSpots[2] = new MapLocation(rc.getLocation().x, rc.getLocation().y);
							lastSpots[3] = new MapLocation(rc.getLocation().x, rc.getLocation().y);
							lastSpots[4] = new MapLocation(rc.getLocation().x, rc.getLocation().y);
							initializedLastSpots = true;
						}
						else
						{
							/*
							lastSpots[4] = lastSpots[3];
							lastSpots[3] = lastSpots[2];
							lastSpots[2] = lastSpots[1];
							lastSpots[1] = lastSpots[0];
							lastSpots[0] = rc.getLocation();
							*/
						}
						numbOfMoves++;
						if (dir == null)
						{
							rc.wearHat();
							dir = rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation());
						}
						else if (!ranIntoMine)
						{
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						}
						mine = rc.senseMine(rc.getLocation().add(dir));
						
						if (rc.getLocation().isAdjacentTo(rc.senseEnemyHQLocation()))
						{
							
						}
						else if (mine != null && mine != rc.getTeam())
						{
							foundDir = false;
							
							int k = 0;
							while (k < 7 && !foundDir)
							{
								//k++;
								dir = rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation());
								for (int n = 0; n <= k && !foundDir; n++)
								{
									
										if (k % 2 == 0)
										{
											if (left)
											{
												dir = dir.rotateLeft();
											}
											else
											{
												dir = dir.rotateRight();
											}
										}
										else
										{
											if (left)
											{
												dir = dir.rotateRight();
											}
											else
											{
												dir = dir.rotateLeft();
											}
										}
									
									mine = rc.senseMine(rc.getLocation().add(dir));
									if (mine != null && mine != rc.getTeam())
									{
										
									}
									else if (lastSpots[0].equals(rc.getLocation()) || lastSpots[1].equals(rc.getLocation()) || lastSpots[2].equals(rc.getLocation()) || lastSpots[3].equals(rc.getLocation()) || lastSpots[4].equals(rc.getLocation()))
									{
										
									}
									else
									{
										if (rc.canMove(dir))
										{
											rc.move(dir);
											foundDir = true;
										}
									}
								}
								k++;
							}
							
							if (!foundDir)
							{
								dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
								rc.defuseMine(rc.getLocation().add(dir));
							}
						}
						else if (!rc.canMove(dir))
						{
							int i = 0;
							while (!rc.canMove(dir) && i < 8 || (mine != null && mine != rc.getTeam()))
							{
								i++;
								for (int j = 0; j < i; j++)
								{
									if (i % 2 == 0)
									{
										dir = dir.rotateLeft();
									}
									else
									{
										dir = dir.rotateRight();
									}
								}
								mine = rc.senseMine(rc.getLocation().add(dir));
							}
							if (i < 8)
							{
								rc.move(dir);
							}
						}
						else 
						{
							if (rc.canMove(dir))
							{
								rc.move(dir);
							}
						}
						
						
						lastSpots[4] = lastSpots[3];
						lastSpots[3] = lastSpots[2];
						lastSpots[2] = lastSpots[1];
						lastSpots[1] = lastSpots[0];
						lastSpots[0] = rc.getLocation();
						
					}
					else
					{
						rc.yield();
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
