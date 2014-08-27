package calcBot;

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
	static int[] bestEncampLocal;
	static int[] stackHelp;
	static MapLocation[] encampSpots;
	static MapLocation[] encampSpots2;
	static MapLocation[] encampSpots3;
	static MapLocation sender;
	static MapLocation encamp;
	static int x_1;
	static int y_1;
	static int x_2;
	static int y_2;
	static Direction dir = null;
	static int helper = 0;
	static int dist1;
	static int dist2;
	static MapLocation temp;
	static int myLocal = -1;
	static boolean encamper = false;
	static int bestDist = 23455300;
	static int bestIndex = 0;
	static int MineLayerType = 1;
	static int Encamper = 2;
	static int Rusher = 3;
	static int Defense = 4;
	static int MineAvoider = 5;
	static int Flanker = 6;
	static int MineLayerType2 = 7;
	static boolean encamp1 = false;
	static boolean encamp2 = false;
	static int Supplier = 1;
	static int Generator = 2;
	static int myType2 = 0;
	static int numbOfEncampers = 0;
	static double HQpower = 0;
	static MapLocation dRallyPoint;
	static int d_x = 0;
	static int d_y = 0;
	static int initialCount = 0;
	static int distanceToEnemy = 0;
	static int distanceToEHQ = 0;
	static int distanceToHQ = 0;
	static int nukeCount = 0;
	static int fusionCount = 0;
	static final int baseBroadcast = 233;
	static boolean hasBroadcast = false;
	static int nukeCounter = 0;
	static int startEnergy = 400;
	static int endEnergy = 0;
	static MapLocation origionalRally = null;
	static MapLocation bestRally2 = null;
	
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.HQ) 
				{
					if (dRallyPoint == null)
					{
						dRallyPoint = rc.getLocation().add(rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
						for (int z = 0; z < 5; z++)
						{
							//origionalRally = dRallyPoint.add(dir);
							dRallyPoint = dRallyPoint.add(rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
						}
					}
					if (rc.readBroadcast(baseBroadcast+100) == 2)
					{
						//numbOfEncampers++;
					}
					if (rc.isActive()) 
					{
						if (dir == null)
						{
							HQpower = rc.getEnergon();
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							Team mine = rc.senseMine(rc.getLocation().add(dir));
							while (!rc.canMove(dir) || ((mine != null) && (mine != rc.getTeam())))
							{
								dir = Direction.values()[(int)(Math.random()*8)];
								mine = rc.senseMine(rc.getLocation().add(dir));
							}
							distanceToEnemy = rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation());
							/*
							dRallyPoint = rc.getLocation().add(dir);
							for (int z = 0; z < 5; z++)
							{
								//origionalRally = dRallyPoint.add(dir);
								dRallyPoint = dRallyPoint.add(dir);
							}
							*/
							encampSpots = rc.senseAllEncampmentSquares();
							bestEncampLocal = new int[encampSpots.length];
							bestRally2 = encampSpots[0];
							dRallyPoint = origionalRally;
							for (int j = 0; j < encampSpots.length; j++)
							{
								Direction dir11 = null;
								Direction dir22 = null;
								
								MapLocation encamp;
								encamp = encampSpots[j];
								dir11 = rc.getLocation().directionTo(encamp);
								dir22 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
								distanceToEHQ = encamp.distanceSquaredTo(rc.senseEnemyHQLocation());
								distanceToHQ = encamp.distanceSquaredTo(rc.getLocation());
								
								/*
								if (encamp.distanceSquaredTo(origionalRally) < bestRally2.distanceSquaredTo(origionalRally))
								{
									if (encamp.distanceSquaredTo(origionalRally) < 26)
									{
										bestRally2 = encamp;
										dRallyPoint = bestRally2;
									}
								}
								*/
								
								
								if ((distanceToEHQ + distanceToHQ) == distanceToEnemy)
								{
									if (distanceToEHQ < distanceToEnemy)
									{
										dRallyPoint = encamp;
										break;
									}
								}
								if (distanceToEHQ < (distanceToEnemy - 20))
								{
									if (rc.getLocation().distanceSquaredTo(encamp) < 101)
									{
										if (dir11 == dir22 || dir11 == dir22.rotateLeft() || dir11 == dir22.rotateRight())
										{
											dRallyPoint = encamp;
										}
									}
								}
								
								if (rc.isActive())
								{
									rc.spawn(dir);
									rc.broadcast(baseBroadcast, MineLayerType);
									initialCount++;
									
								}
							}
							
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
										if (rc.canMove(dir))
										{
										
										}
										else
										{
											mine = rc.senseMine(rc.getLocation().add(dir));
											while (!rc.canMove(dir) || ((mine != null) && (mine != rc.getTeam())))
											{
												dir = Direction.values()[(int)(Math.random()*8)];
												mine = rc.senseMine(rc.getLocation().add(dir));
											}
										}
										rc.spawn(dir);
										if (initialCount < 2)
										{
											rc.broadcast(baseBroadcast, MineLayerType);
										}
										else
										{
											rc.broadcast(baseBroadcast, Defense);
											rc.broadcast((baseBroadcast - 99), dRallyPoint.x);
											rc.broadcast((baseBroadcast - 98), dRallyPoint.y);
										}
										initialCount++;
									}
								}
							}
						}
						Team mine = rc.senseMine(rc.getLocation().add(dir));
						while (!rc.canMove(dir) || ((mine != null) && (mine != rc.getTeam())))
						{
							dir = Direction.values()[(int)(Math.random()*8)];
							mine = rc.senseMine(rc.getLocation().add(dir));
						}
						
						if (HQpower > rc.getEnergon())
						{
							rc.broadcast((baseBroadcast+4), 3);
							HQpower--;
						}
						else
						{
							rc.broadcast((baseBroadcast+4), 0);
						}
						
						// Spawn a soldier
						if (rc.canMove(dir))
						{
							if (rc.getTeamPower() > 100.0)
							{
								if ((rc.senseEnemyNukeHalfDone() && !rc.hasUpgrade(Upgrade.DEFUSION)) || (Clock.getRoundNum() > 550 && !rc.hasUpgrade(Upgrade.DEFUSION)))
								{
									rc.researchUpgrade(Upgrade.DEFUSION);
								}
								/*
								else if (startEnergy < rc.getEnergon())
								{
									startEnergy--;
									rc.spawn(dir);
								}
								*/
								else if (Clock.getRoundNum() > 400 && !rc.hasUpgrade(Upgrade.PICKAXE))
								{
									rc.researchUpgrade(Upgrade.PICKAXE);
								}
								else if (fusionCount > 0 && !rc.hasUpgrade(Upgrade.FUSION))
								{
									rc.researchUpgrade(Upgrade.FUSION);
								}
								else if (nukeCounter > 199)
								{
									rc.researchUpgrade(Upgrade.NUKE);
								}
								else
								{
									rc.spawn(dir);
								
								
								if (HQpower > rc.getEnergon())
								{
									rc.broadcast(baseBroadcast, Defense);
									rc.broadcast((baseBroadcast - 99), dRallyPoint.x);
									rc.broadcast((baseBroadcast - 98), dRallyPoint.y);
									HQpower--;
								}
								else if ((rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()) < 600))
								{
									if (numbOfSoldiers % 5 == 4)
									{
										rc.broadcast(baseBroadcast, Flanker);
									}
									else
									{
										rc.broadcast(baseBroadcast, Defense);
										rc.broadcast((baseBroadcast - 99), dRallyPoint.x);
										rc.broadcast((baseBroadcast - 98), dRallyPoint.y);
									}
									numbOfSoldiers++;
								}
								else if (rc.senseEnemyNukeHalfDone())
								{
									rc.broadcast(baseBroadcast, Rusher);
									rc.broadcast((baseBroadcast-1), 1);
								}
								else if (numbOfSoldiers == 1 || numbOfSoldiers == 15)
								{
									rc.broadcast(baseBroadcast, Rusher);
								}
								else if (numbOfSoldiers < 22 && numbOfSoldiers != 0)
								{
									rc.broadcast(baseBroadcast, Defense);
									rc.broadcast((baseBroadcast - 99), dRallyPoint.x);
									rc.broadcast((baseBroadcast - 98), dRallyPoint.y);
								}
								else if (numbOfSoldiers < 27)
								{
									sender = encampSpots[numbOfEncampers];
									if (sender == dRallyPoint)
									{
										//numbOfEncampers++;
										sender = encampSpots[numbOfEncampers];
									}
									rc.broadcast(baseBroadcast, Encamper);
									rc.broadcast((baseBroadcast+1), sender.x);
									rc.broadcast((baseBroadcast+2), sender.y);
									numbOfEncampers++;
								}
								else if (((numbOfSoldiers % 5) == 0) || (numbOfSoldiers == 0))
								{	
									sender = encampSpots[numbOfEncampers];
									if (sender == dRallyPoint)
									{
										numbOfEncampers++;
										sender = encampSpots[numbOfEncampers];
									}
									rc.broadcast(baseBroadcast, Encamper);
									rc.broadcast((baseBroadcast+1), sender.x);
									rc.broadcast((baseBroadcast+2), sender.y);
									if (encamp1)
									{
										rc.broadcast(236, Supplier);
										encamp1 = !encamp1;
									}
									else
									{
										if (encamp2)
										{
											rc.broadcast(236, Supplier);
											encamp2 = !encamp2;
										}	
										else
										{	
											rc.broadcast(236, Generator);
											encamp2 = !encamp2;
										}
										encamp1 = !encamp1;
									}
									numbOfEncampers++;
								}
								else if (numbOfSoldiers % 5 == 1)
								{
									rc.broadcast(baseBroadcast, MineLayerType);
								}
								else if (numbOfSoldiers % 5 == 2)
								{
									rc.broadcast(baseBroadcast, Defense);
									rc.broadcast((baseBroadcast - 99), dRallyPoint.x);
									rc.broadcast((baseBroadcast - 98), dRallyPoint.y);
								}
								else if (numbOfSoldiers % 5 == 3)
								{
									rc.broadcast(baseBroadcast, Flanker);
								}
								else
								{
									rc.broadcast(baseBroadcast, Rusher);
								}
								numbOfSoldiers++;
								}
							}
							else
							{
								if (!rc.hasUpgrade(Upgrade.FUSION))
								{
									rc.researchUpgrade(Upgrade.FUSION);
									fusionCount++;
								}
								else
								{
									nukeCounter++;
									rc.researchUpgrade(Upgrade.NUKE);
								}
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
								}
							}
						}
					}
				} else if (rc.getType() == RobotType.SOLDIER) {
					if (rc.isActive()) {
						
						if (myType == 0)
						{
							myType = rc.readBroadcast(baseBroadcast);
							if (myType == Encamper)
							{
								myType2 = rc.readBroadcast(baseBroadcast+2);
								x_1 = rc.readBroadcast(baseBroadcast+1);
								y_1 = rc.readBroadcast(baseBroadcast+2);
								rc.broadcast((baseBroadcast+100), 2);
							}
							else if (myType == Defense)
							{
								x_2 = rc.readBroadcast((baseBroadcast - 99));
								y_2 = rc.readBroadcast((baseBroadcast - 98));
							}
							
						}
						else if (!hasBroadcast)
						{
							hasBroadcast = true;
							rc.broadcast((baseBroadcast+100), 1);
						}
						
						if (myType == Encamper)
						{
							EncamperBot.run(rc, myType2, x_1, y_1);
						}
						else if (myType == MineLayerType)
						{
							MineLayer.run(rc);
						}
						else  if (myType == Rusher)
						{
							Direction dir2 = null;
							dir2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							Move(rc, dir2);
						}
						else if (myType == MineAvoider)
						{
								//mineAvoider.run(rc);
						}
						else if (myType == Flanker)
						{
							FlankerBot.run(rc);
						}
						else if (myType == MineLayerType2)
						{
							MineLayer2.run(rc);
						}
						else
						{
							DefensiveBot.run(rc, myType, x_2, y_2);
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
	
	public static void fightingMove(RobotController rc, Direction dir) throws GameActionException
	{
		Team mine;
		if(rc.canMove(dir)) 
		{
			mine = rc.senseMine(rc.getLocation().add(dir));
			if( mine!= null && mine != rc.getTeam())
			{
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
			}
			else{
			rc.move(dir);
			}
		}
	}
	
	public static Direction EncampMineProtect(RobotController rc, MapLocation encampSpot, int side)
	{
		Direction dir = null;
		
		dir = encampSpot.directionTo(rc.senseEnemyHQLocation());
		
		return dir;
	}

}
