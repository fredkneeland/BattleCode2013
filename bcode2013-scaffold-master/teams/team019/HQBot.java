package team019;

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

public class HQBot {
	static Direction dir;
	static int counter = 0;
	static final int channel1 = 549;
	static final int channel2 = 3;
	static final int channel3 = 57681;
	static final int frontEncode = 140000000;
	static final int backEncode = 34;
	static boolean alternate = false;
	static int message = 0;
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
	static int MineEnemy = 11;
	static MapLocation dRallyPoint = null;
	static Direction enemyHQ;
	static MapLocation[] rallyEncamps;
	static boolean medBay = false;
	static MapLocation best;
	static int input = 0;
	static int input2 = 0;
	static int input3 = 0;
	static int energonLevel = 500;
	static MapLocation[] smartDSpots = new MapLocation[9];
	static MapLocation smartDSpot;
	static int numbOfSmartD = 0;
	static MapLocation[] encampSpots;
	static int dist1;
	static int dist2;
	static MapLocation temp;
	static int numbOfEncampers;
	static boolean orderedEncamps = false;
	static int numbOfFormation = 0;
	static int numbOfFormation2 = 0;
	static MapLocation[] formationStart = new MapLocation[4];
	static MapLocation[] formationTarget = new MapLocation[4];
	static int x;
	static int y;
	static int x2;
	static int y2;
	static final int advance = 34;
	static Direction enemyHQ1;
	static int numbOfSoldiers = 0;
	static int distToEnemy = 0;
	static MapLocation[] origionalMineSpots;
	static MapLocation[] upDatedMineSpots;
	static MapLocation center;
	static boolean enemyRushing = false;
	static int centerX;
	static int centerY;
	static int distanceToCenter;
	static boolean enemyNuke = false;
	static int numbOfFlankers = 0;
	static int numbOfScouts = 0;
	
	public static void run(RobotController rc) {
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
							centerX = (rc.getLocation().x + rc.senseEnemyHQLocation().x) /4;
							centerY = (rc.getLocation().y + rc.senseEnemyHQLocation().y) /4;
							center = new MapLocation(centerX, centerY);
							distanceToCenter = (((centerX) - rc.senseEnemyHQLocation().x)*((centerX) - rc.senseEnemyHQLocation().x)) + (((centerY) - rc.senseEnemyHQLocation().y)*((centerY) - rc.senseEnemyHQLocation().y));
							origionalMineSpots = rc.senseMineLocations(center, (distanceToCenter), Team.NEUTRAL);
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
						if (!orderedEncamps)
						{
							enemyHQ1 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
                            Team mine = rc.senseMine(rc.getLocation().add(enemyHQ1));
							encampSpots = rc.senseAllEncampmentSquares();
							for (int i = 0; i < encampSpots.length; i++)
							{
								for (int j = 1; j < encampSpots.length-i; j++)
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
                                        Direction orgDir = enemyHQ1;
                                        while (!rc.canMove(enemyHQ1) || (mine != null && mine != rc.getTeam())) {
                                            enemyHQ1 = enemyHQ1.rotateLeft();
                                            mine = rc.senseMine(rc.getLocation().add(enemyHQ1));
                                            if (enemyHQ1 == orgDir) {
                                                break;
                                            }
                                        }
										//broadcast(rc, MineLayer, 0);
										if (rc.canMove(enemyHQ1) && (mine == null || mine == rc.getTeam()))
										{
											rc.spawn(enemyHQ1);
											if (numbOfSoldiers < 6)
											{
												broadcast(rc, Scout, 0);
												broadcast(rc, ((numbOfScouts % 2)+1), 1);
												numbOfScouts++;
											}
                                            else if (numbOfSoldiers < 10) {
                                                x = dRallyPoint.x;
                                                y = dRallyPoint.y;
                                                broadcast(rc, x, 1);
                                                broadcast(rc, y, 2);
                                                broadcast(rc, Defender2, 0);
                                            }
											else if (numbOfSoldiers < 14)
											{
												broadcast(rc, MineLayer, 0);
											}
											else
											{
												x = dRallyPoint.x;
												y = dRallyPoint.y;
												broadcast(rc, x, 1);
												broadcast(rc, y, 2);
												broadcast(rc, Defender2, 0);
											}
											numbOfSoldiers++;
										}
									}
								}
							}
							
							orderedEncamps = true;
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
						counter = 0;
						
						if (dir == null)
						{
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							
							smartDSpots[0] = rc.getLocation().add(dir, 5);
							smartDSpots[1] = rc.getLocation().add(dir, 5).add(dir.rotateLeft().rotateLeft());
							for (int i = 2; i < smartDSpots.length; i++)
							{
								
								if (i % 2 == 0)
								{
									smartDSpots[i] = smartDSpots[i-2].add(dir.rotateRight().rotateRight());
								}
								else
								{
									smartDSpots[i] = smartDSpots[i-2].add(dir.rotateLeft().rotateLeft());
								}
							}
							
							Direction target = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							Direction target2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							target = target.rotateLeft().rotateLeft();
							target2 = target2.rotateRight().rotateRight();
							MapLocation start = rc.getLocation().add(target);
							MapLocation start2 = rc.getLocation().add(target2);
							formationStart[0] = start;
							formationStart[1] = start2;
							distToEnemy = rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation());
							for (int i = 2; i < formationStart.length; i++)
							{
								if (i % 2 == 0)
								{
									formationStart[i] = formationStart[i-2].add(target.rotateRight());
								}
								else
								{
									formationStart[i] = formationStart[i-2].add(target2.rotateLeft());
								}
							}
							for (int i = 0; i < formationTarget.length; i++)
							{
								formationTarget[i] = formationStart[i].add(rc.getLocation().directionTo(rc.senseEnemyHQLocation()), (15));
							}
						}
						
						Team mine = rc.senseMine(rc.getLocation().add(dir));
						while (!rc.canMove(dir) || ((mine != null) && (mine != rc.getTeam())))
						{
							counter++;
							if (counter > 50)
							{
								rc.researchUpgrade(Upgrade.NUKE);
							}
							dir = Direction.values()[(int)(Math.random()*8)];
							mine = rc.senseMine(rc.getLocation().add(dir));
						}
						
						
						
/////////////////////////////// Research and Unit Spawning ////////////////////////////////////////////////////
						
						if (Clock.getRoundNum() < 400)
						{
							upDatedMineSpots = rc.senseMineLocations(center, (distanceToCenter), Team.NEUTRAL);
							if (upDatedMineSpots.length < (origionalMineSpots.length - (Clock.getRoundNum() / 15)))
							{
								enemyRushing = true;
							}
						}
						else
						{
							enemyRushing = false;
						}
						
						if (rc.senseEnemyNukeHalfDone())
						{
							enemyNuke = true;
							broadcast(rc, 3, 22);
						}
						
						
						if (rc.getTeamPower() < 150 && Clock.getRoundNum() > 200  && !enemyNuke)
						{
							if (!rc.hasUpgrade(Upgrade.FUSION))
							{
								rc.researchUpgrade(Upgrade.FUSION);
							}
                            else if (!rc.hasUpgrade(Upgrade.PICKAXE)) {
                                rc.researchUpgrade(Upgrade.PICKAXE);
                            }
                            else if (!rc.hasUpgrade(Upgrade.DEFUSION)) {
                                rc.researchUpgrade(Upgrade.DEFUSION);
                            }
							else
							{
								rc.researchUpgrade(Upgrade.NUKE);
							}
						}
						else
						{
							if (enemyNuke)
							{
								broadcast(rc, 3, 22);
								if (!rc.hasUpgrade(Upgrade.DEFUSION))
								{
									rc.researchUpgrade(Upgrade.DEFUSION);
								}
								else if (numbOfSoldiers % 2 == 0)
								{
									rc.spawn(dir);
									type = Flanker;
									numbOfFlankers++;
									HQBot.broadcast(rc, numbOfFlankers, 1);
								}
								else
								{
									rc.spawn(dir);
									type = Rusher;
								}
								numbOfSoldiers++;
							}
							else if (enemyRushing)
							{
								rc.spawn(dir);
								//System.out.print("enemyRushing ");
								if (numbOfSoldiers < 6)
								{
									broadcast(rc, Scout, 0);
									broadcast(rc, ((numbOfScouts % 2)+1), 1);
									numbOfScouts++;
								}
								else if (numbOfSoldiers < 12)
								{
                                    x = dRallyPoint.x;
                                    y = dRallyPoint.y;
                                    broadcast(rc, x, 1);
                                    broadcast(rc, y, 2);
                                    type = Defender2;
								}
								else if (numbOfSoldiers < 16)
								{
									broadcast(rc, MineLayer, 0);
								}
								else if (numbOfSoldiers == 16)
								{
									type = Encamper;
									message = encampSpots[numbOfEncampers].x;
									broadcast(rc, message, 1);
									message = encampSpots[numbOfEncampers].y;
									broadcast(rc, message, 2);
									numbOfEncampers++;
								}
                                else if (numbOfSoldiers < 20 && numbOfSoldiers % 6 == 2) {
                                    type = Encamper;
                                    message = encampSpots[numbOfEncampers].x;
                                    broadcast(rc, message, 1);
                                    message = encampSpots[numbOfEncampers].y;
                                    broadcast(rc, message, 2);
                                    numbOfEncampers++;
                                }
								else
								{
									x = dRallyPoint.x;
									y = dRallyPoint.y;
									broadcast(rc, x, 1);
									broadcast(rc, y, 2);
									type = Defender2;
								}
								numbOfSoldiers++;
							}
							else
							{
								if (Clock.getRoundNum() > 550 && !rc.hasUpgrade(Upgrade.DEFUSION))
								{
									rc.researchUpgrade(Upgrade.DEFUSION);
								}
								else if (Clock.getRoundNum() > 700 && !rc.hasUpgrade(Upgrade.PICKAXE))
								{
									rc.researchUpgrade(Upgrade.PICKAXE);
								}
								else
								{
									rc.spawn(dir);
									System.out.print("enemyNotRushing: "+Clock.getRoundNum());
									
									if (numbOfSoldiers < 6)
									{
										type = Scout;
										broadcast(rc, ((numbOfScouts % 2)+1), 1);
										numbOfScouts++;
									}
									else if (numbOfSoldiers == 4)
									{
                                        x = dRallyPoint.x;
                                        y = dRallyPoint.y;
                                        broadcast(rc, x, 1);
                                        broadcast(rc, y, 2);
                                        type = Defender2;
									}
									else if (numbOfSoldiers < 8)
									{
										type = MineLayer;
									}
									else if (numbOfSoldiers < 13)
									{
										x = dRallyPoint.x;
										y = dRallyPoint.y;
										broadcast(rc, x, 1);
										broadcast(rc, y, 2);
										type = Defender2;
									}
									else if (numbOfSoldiers < 28)
									{
										type = Encamper;
										message = encampSpots[numbOfEncampers].x;
										broadcast(rc, message, 1);
										message = encampSpots[numbOfEncampers].y;
										broadcast(rc, message, 2);
										numbOfEncampers++;
									}
									else if (numbOfSoldiers % 15 == 3)
									{
										type = MineLayer;
									}
									else if (numbOfSoldiers % 5 == 0)
									{
										x = dRallyPoint.x;
										y = dRallyPoint.y;
										broadcast(rc, x, 1);
										broadcast(rc, y, 2);
										type = Defender2;
									}
									else if (numbOfSoldiers % 5 == 1)
									{
										type = Flanker;
										numbOfFlankers++;
										HQBot.broadcast(rc, numbOfFlankers, 1);
									}
									else if (numbOfSoldiers % 5 == 2)
									{
										if (numbOfEncampers < encampSpots.length)
										{
											type = Encamper;
											message = encampSpots[numbOfEncampers].x;
											broadcast(rc, message, 1);
											message = encampSpots[numbOfEncampers].y;
											broadcast(rc, message, 2);
											numbOfEncampers++;
										}
										else
										{
											type = Flanker;
											numbOfFlankers++;
											HQBot.broadcast(rc, numbOfFlankers, 1);
										}
									}
									else if (numbOfSoldiers % 5 == 3)
									{
										if (numbOfSoldiers % 2 == 0)
										{
											type = MineEnemy;
										}
										else
										{
											type = Killer;
										}
									}
									else
									{
										type = Flanker;
										numbOfFlankers++;
									}
									
									numbOfSoldiers++;
								}
							}
							
							broadcast(rc, type, 0);
						}
						
						
						/*
						if (rc.getTeamPower() < 200)
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
							if ((rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()) < 600))
							{
								if (numbOfSoldiers < 3)
								{
									type = MineLayer;
								}
								else if (numbOfSoldiers % 5 == 0)
								{
									type = Flanker;
								}
								else
								{
									type = Defender2;
									x = dRallyPoint.x;
									y = dRallyPoint.y;
									broadcast(rc, x, 1);
									broadcast(rc, y, 2);
								}
								numbOfSoldiers++;
								broadcast(rc, type, 0);
							}
							else
							{
								if (numbOfSoldiers < 3)
								{
									type = MineLayer;
								}
								else if (numbOfSoldiers < 15)
								{
									type = Defender2;
									x = dRallyPoint.x;
									y = dRallyPoint.y;
									broadcast(rc, x, 1);
									broadcast(rc, y, 2);
								}
								else if (numbOfSoldiers % 5 == 0)
								{
									if (numbOfEncampers < encampSpots.length)
									{
										type = Encamper;
										message = encampSpots[numbOfEncampers].x;
										broadcast(rc, message, 1);
										message = encampSpots[numbOfEncampers].y;
										broadcast(rc, message, 2);
										numbOfEncampers++;
									}
									else
									{
										type = Flanker;
									}
								}
								else if (numbOfSoldiers % 5 == 1)
								{
									type = Flanker;
								}
								else if (numbOfSoldiers % 15 == 2)
								{
									type = MineLayer;
								}
								else
								{
									numbOfFormation2 = numbOfFormation % 4;
									type = Formation;
									x = formationStart[numbOfFormation2].x;
									y = formationStart[numbOfFormation2].y;
									x2 = formationTarget[numbOfFormation2].x;
									y2 = formationTarget[numbOfFormation2].y;
									
									broadcast(rc, x, 1);
									broadcast(rc, y, 2);
									broadcast(rc, x2, 3);
									broadcast(rc, y2, 4);
									broadcast(rc, numbOfFormation2, 5);
									if ((numbOfFormation % 20) < 4)
									{
										broadcast(rc, 1, 6);
									}
									
									else
									{
										broadcast(rc, 0, 6);
									}
									if (numbOfFormation % 20 == 0)
									{
										broadcast(rc, advance, 7);
									}
									else
									{
										broadcast(rc, 0, 7);
									}
									numbOfFormation++;
								}
								
								numbOfSoldiers++;
								broadcast(rc, type, 0);
							}
						}
						*/
						
						/*
						if (numbOfEncampers < encampSpots.length)
						{
							type = Encamper;
							message = encampSpots[numbOfEncampers].x;
							broadcast(rc, message, 1);
							message = encampSpots[numbOfEncampers].y;
							broadcast(rc, message, 2);
							numbOfEncampers++;
						}
						else
						{
							type = Flanker;
						}
						*/
						
						/*
						if (numbOfSmartD < 9)
						{
							type = SmartDefender;
							message = smartDSpots[numbOfSmartD].x;
							broadcast(rc, message, 1);
							message = smartDSpots[numbOfSmartD].y;
							broadcast(rc, message, 2);
							numbOfSmartD++;
							rc.spawn(dir);
							broadcast(rc, type, 0);
						}
						*/
						
						/*
						type = Flanker;
						rc.spawn(dir);
						broadcast(rc, type, 0);
						*/
						
						/*
						rc.spawn(dir);
						type = Defender2;
						x = dRallyPoint.x;
						y = dRallyPoint.y;
						broadcast(rc, type, 0);
						broadcast(rc, x, 1);
						broadcast(rc, y, 2);
						*/
						
						
						/*
						else
						{
							type = Flanker;
						}
						*/
						
						/*
						if (!rc.hasUpgrade(Upgrade.FUSION))
						{
							rc.researchUpgrade(Upgrade.FUSION);
						}
						else if (numbOfFormation < 500)
						{
						
							rc.spawn(dir);
							numbOfFormation2 = numbOfFormation % 4;
							type = Formation;
							broadcast(rc, type, 0);
							x = formationStart[numbOfFormation2].x;
							y = formationStart[numbOfFormation2].y;
							x2 = formationTarget[numbOfFormation2].x;
							y2 = formationTarget[numbOfFormation2].y;
							
							broadcast(rc, x, 1);
							broadcast(rc, y, 2);
							broadcast(rc, x2, 3);
							broadcast(rc, y2, 4);
							broadcast(rc, numbOfFormation2, 5);
							if ((numbOfFormation % 20) < 4)
							{
								broadcast(rc, 1, 6);
							}
							
							else
							{
								broadcast(rc, 0, 6);
							}
							if (numbOfFormation % 20 == 0)
							{
								broadcast(rc, advance, 7);
							}
							else
							{
								broadcast(rc, 0, 7);
							}
							numbOfFormation++;
						}
						*/
						
					}
					
				}
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}
	
	protected static void Move(RobotController rc, Direction dir) throws GameActionException
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
	
	protected static int readMessage(RobotController rc, int channelDif) throws GameActionException
	{
		int reader = 0;
		
		input = rc.readBroadcast(channel1 + channelDif);
		input2 = rc.readBroadcast(channel2 + channelDif);
		input3 = rc.readBroadcast(channel3 + channelDif);
		if (input > (frontEncode + backEncode - 1) && (input < (frontEncode + 10000000)))
		{
			reader = (((input - frontEncode) - backEncode) / 100);
		}
		else if (input2 > (frontEncode + backEncode - 1) && (input2 < (frontEncode + 10000000)))
		{
			reader = (((input2 - frontEncode) - backEncode) / 100);
		}
		else if (input3 > (frontEncode + backEncode - 1) && (input3 < (frontEncode + 10000000)))
		{
			reader = (((input3 - frontEncode) - backEncode) / 100);
		}
		
		return reader;
	}
	
	protected static void broadcast(RobotController rc, int message, int channelDif) throws GameActionException
	{
		message = message * 100;
		message = frontEncode + message + backEncode;
		rc.broadcast(channel1+channelDif, message);
		rc.broadcast(channel2+channelDif, message);
		rc.broadcast(channel3+channelDif, message);
	}
	// for moving in formation
	protected static void moveInFormation(RobotController rc, MapLocation target) throws GameActionException
	{
		Team mine = rc.senseMine(target);
		if (mine != null && mine != rc.getTeam())
		{
			rc.defuseMine(target);
		}
		else if (rc.canMove(rc.getLocation().directionTo(target)))
		{
			rc.move(rc.getLocation().directionTo(target));
		}
	}
}
