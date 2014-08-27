package team019;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class ColumnBot {
	static MapLocation target;
	static MapLocation start;
	static boolean gotToStart = false;
	static boolean gotToTarget = false;
	static boolean attack = false;
	static boolean readyToAdvance = false;
	static final int advance = 34;
	static int x;
	static int y;
	static int x2;
	static int y2;
	static MapLocation target1;
	static MapLocation target2;
	static MapLocation target3;
	static MapLocation target4;
	static GameObject slot1;
	static GameObject slot2;
	static GameObject slot3;
	static GameObject slot4;
	static Team mine1;
	static Team mine2;
	static Team mine3;
	static Team mine4;
	static Team mine;
	static boolean spot1 = false;
	static boolean spot2 = false;
	static boolean spot3 = false;
	static boolean spot4 = false;
	static Direction dirToEHQ;
	static boolean wearHat = false;
	static boolean broadcast = false;
	
	public static void run(RobotController rc, int Position, boolean leader, int x_1, int y_1, int x_2, int y_2) 
	{
		start = new MapLocation(x_1, y_1);
		target = new MapLocation(x_2, y_2);
		dirToEHQ = rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation());
		while (true)
		{
			try 
			{
				if (rc.getType() == RobotType.SOLDIER)
				{
					if (rc.isActive())
					{
						// if we are in the front
						if (leader)
						{
							if (!broadcast)
							{
								HQBot.broadcast(rc, target.x, 8+Position);
								HQBot.broadcast(rc, target.y, 12+Position);
								broadcast = true;
							}
							if (!wearHat)
							{
								rc.wearHat();
								wearHat = true;
							}
							// go to start Location
							if (!gotToStart)
							{
								// if we are at start stop going toward start
								if (rc.getLocation().equals(start))
								{
									gotToStart = true;
								}
								else
								{
									HQBot.Move(rc, rc.getLocation().directionTo(start));
								}
							}
							// go to front position
							else if (!gotToTarget)
							{
								// if we are at target don't need to go towards it
								if (rc.getLocation().equals(target))
								{
									gotToTarget = true;
								}
								else
								{
									HQBot.Move(rc, rc.getLocation().directionTo(target));
								}
								
							}
							else if (rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()) < 101)
							{
								HQBot.Move(rc, rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
								HQBot.broadcast(rc, 1, 16+Position);
							}
							else if (rc.senseMine(rc.getLocation()) != null && rc.senseMine(rc.getLocation()) != rc.getTeam())
							{
								HQBot.Move(rc, rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
							}
							// wait for signal
							else if (!attack)
							{
								// read broadcast as we wait to attack
								if (HQBot.readMessage(rc, 7) == advance)
								{
									attack = true;
								}
							}
							// attack
							else
							{
								//HQBot.Move(rc, rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
								
								if (rc.getLocation().equals(target))
								{
									readyToAdvance = false;
									//HQBot.broadcast(rc, 0, 16+Position);
								}
								
								// time to Set target
								if (!readyToAdvance)
								{
									target = target.add(dirToEHQ);
									HQBot.broadcast(rc, rc.getLocation().x, 8+Position);
									HQBot.broadcast(rc, rc.getLocation().y, 12+Position);
									readyToAdvance = true;
								}
								if (readyToAdvance)
								{
								
									// check to see if entire front line can advance
									mine = rc.senseMine(target);
									if (mine != null && mine != rc.getTeam())
									{
										HQBot.broadcast(rc, 0, 16+Position);
										rc.defuseMine(target);
									}
									else
									{
										/*
										if (rc.getLocation().equals(target))
										{
											readyToAdvance = false;
											//HQBot.broadcast(rc, 0, 16+Position);
										}
										else*/ if (rc.canMove(rc.getLocation().directionTo(target)))
										{
											HQBot.broadcast(rc, 1, 16+Position);
											x = HQBot.readMessage(rc, 16);
											y = HQBot.readMessage(rc, 17);
											x2 = HQBot.readMessage(rc, 18);
											y2 = HQBot.readMessage(rc, 19);
											
											if (x == 1 && y == 1 && x2 == 1 && y2 == 1)
											{
												rc.move(rc.getLocation().directionTo(target));
												
												if (rc.getLocation().equals(target))
												{
													readyToAdvance = false;
													//HQBot.broadcast(rc, 0, 16+Position);
												}
												
											}
										}
										else
										{
											//HQBot.broadcast(rc, 0, 16+Position);
										}
									}
								}
					
								
								
							}
						}
						
						// if we are a follower
						else
						{
							if (!gotToStart)
							{
								if (rc.getLocation().equals(start))
								{
									gotToStart = true;
								}
								else
								{
									HQBot.Move(rc, rc.getLocation().directionTo(start));
								}
							}
							
							else if (!gotToTarget)
							{
								if (rc.getLocation().equals(target))
								{
									gotToTarget = true;
									x = HQBot.readMessage(rc, Position + 8);
									y = HQBot.readMessage(rc, Position + 12);
									if (x == 0 || y == 0)
									{
										
									}
									else
									{
										target = new MapLocation(x,y);
									}
								}
								else
								{
									
									MapLocation next;
									next = rc.getLocation().add(rc.getLocation().directionTo(target));
									HQBot.moveInFormation(rc, next);
								}
							}
							
							else
							{
								//rc.wearHat();
								//target = target.add(target.directionTo(rc.senseEnemyHQLocation()));
								if (rc.getLocation().equals(target))
								{
									x = HQBot.readMessage(rc, Position + 8);
									y = HQBot.readMessage(rc, Position + 12);
									//rc.addMatchObservation("X: "+x+", Position:" + Position + "Y:, "+y);
									System.out.println("X: " + x + " Y: " + y + " Position: " + Position);
									
									if (x == 0 && y == 0)
									{
										leader = true;
										HQBot.broadcast(rc, 0, 16+Position);
										attack = true;
									}
									else
									{
										target = new MapLocation(x, y);
									}
									
								}
								
								
								if (rc.getLocation().equals(target))
								{
									leader = true;
									HQBot.broadcast(rc, 0, 16+Position);
									attack = true;
								}
								else
								{
									MapLocation next;
									next = rc.getLocation().add(rc.getLocation().directionTo(target));
									HQBot.moveInFormation(rc, next);
								}
							}
						}
					}
					rc.yield();
				} 
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
}
