package FirstTeam;

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

/** The example funcs player is a player meant to demonstrate basic usage of the most common commands.
 * Robots will move around randomly, occasionally mining and writing useless messages.
 * The HQ will spawn soldiers continuously. 
 */
public class RobotPlayer {
	static int numbOfSoldiers = 0;
	static int soldierAtEncampent = 0;
	static boolean encampmentType = false;
	static Direction dir = null;
	static int i = 0;
	static int nothing = 0;
	static int Encamper = 1;
	static int Solder = 2;
	static int MineLayer = 3;
	static int Defense = 4;
	static int Rusher = 5;
	static int Killer = 6;
	static int Flanker = 7;
	static int encampType = 0;
	static boolean attack = false;
	static boolean encamperBool = false;
	static boolean mineLayer = false;
	static int numberOfMoves = 0;
	static boolean movey = false;
	static int moveDirection;
	static double origE;
	static boolean otherNuke = false;
	static boolean center1 = false;
	static boolean center2 = false;
	static int numbOfEncamp = 0;
	static boolean ourNuke = false;
	static int nukeCount = 0;
	static boolean invasion = false;
	
	public static void run(RobotController rc) {
		MapLocation[] encampments = null;
		RobotController[] enemys = null;
		MapLocation[] mines = null;
		int myType = 0;
		int encampIndex = 0;
		while (true) {
			try {
				if (rc.getType() == RobotType.HQ) {
					if (rc.isActive()) {
						// Spawn a soldier
						//Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						if (dir == null)
						{
							origE = rc.getEnergon();
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							while ((rc.senseMine(rc.getLocation().add(dir)) != null))
							{
								
								dir = Direction.values()[(int)(Math.random()*8)];
							}
							mines = rc.senseMineLocations(rc.getLocation(), 300, Team.NEUTRAL);
		
							//rc.researchUpgrade(Upgrade.PICKAXE);
						}
						if (rc.senseEnemyNukeHalfDone())
						{
							otherNuke = true;
						}
						if (rc.readBroadcast(504) == 1)
						{
							invasion = true;
						}
						Team mine3;
						mine3 = rc.senseMine(rc.getLocation().add(dir));
						while (!rc.canMove(dir) || ((mine3 != null) && (mine3 != rc.getTeam())))
						{
							dir = Direction.values()[(int)(Math.random()*8)];
							mine3 = rc.senseMine(rc.getLocation().add(dir));
						}
								
						
						if (rc.canMove(dir)) 
						{
							if (otherNuke && !ourNuke)
							{
								numbOfSoldiers++;
								rc.spawn(dir);
							}
							
							else if ((rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()) > 600) && (mines.length > 500))
							{
							
								rc.researchUpgrade(Upgrade.NUKE);
							}
							
							else if (origE < rc.getEnergon())
							{
								numbOfSoldiers++;
								rc.spawn(dir);
							}
							else if (rc.getTeamPower() < 50)
							{
								
							}
							
							else if (Clock.getRoundNum() > 500 && Clock.getRoundNum() < 550 && origE == rc.getEnergon())
							{
								rc.researchUpgrade(Upgrade.PICKAXE);
							}
							
							
							else if (Clock.getRoundNum() > 1700)
							{
								rc.researchUpgrade(Upgrade.NUKE);
							}
							
							else
							{
								numbOfSoldiers++;
								rc.spawn(dir);
							}
							
							if (otherNuke)
							{
								rc.broadcast(500, Rusher);
							}
							else if ((rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()) < 600))
							{
								if (numbOfSoldiers < 2 || numbOfSoldiers == 5)
								{
									rc.broadcast(500, MineLayer);
									rc.broadcast(501, 4);
								}
								else
								{
									rc.broadcast(500, Defense);
								}
							}
							else if (rc.getEnergon() < origE && Clock.getRoundNum() < 600 || (invasion && Clock.getRoundNum() < 600))
							{
								rc.broadcast(500, Defense);
							}
							
							else if (numbOfSoldiers < 2)
							{
								rc.broadcast(500, Encamper);
								rc.broadcast(501, numbOfEncamp);
								rc.broadcast(502, 0);
								numbOfEncamp++;
							}
							else if (numbOfSoldiers < 3 || numbOfSoldiers == 5)
							{
								rc.broadcast(500, MineLayer);
								rc.broadcast(501, 4);
							}
							else if (numbOfSoldiers == 4 || numbOfSoldiers == 10)
							{
								rc.broadcast(500, Rusher);
							}
							else if (numbOfSoldiers < 5)
							{
								rc.broadcast(500, Killer);
							}
							else if (numbOfSoldiers < 10)
							{
								rc.broadcast(500, Defense);
							}
							else if (numbOfSoldiers < 15)
							{
								rc.broadcast(500, Killer);
							}
							else if (numbOfSoldiers < 20)
							{
								rc.broadcast(500, Defense);
							}
							else if (numbOfSoldiers < 23)
							{
								rc.broadcast(500, Encamper);
								rc.broadcast(501, numbOfEncamp);
								rc.broadcast(502, 1);
								numbOfEncamp++;
							}
							else if (numbOfSoldiers < 32)
							{
								rc.broadcast(500, MineLayer);
								if (numbOfSoldiers < 26)
								{
									rc.broadcast(501, 1);
								}
								else if (numbOfSoldiers < 28)
								{
									rc.broadcast(501, 2);					
								}
								else 
								{
									rc.broadcast(501, 3);
								}
							}
							else if (numbOfSoldiers < 26)
							{
								rc.broadcast(500, Encamper);
								rc.broadcast(501, (numbOfEncamp));
								rc.broadcast(502, 0);
								numbOfEncamp++;
							}
							else if (numbOfSoldiers % 5 == 0) {
								rc.broadcast(500, Encamper);
								rc.broadcast(501, (numbOfEncamp));
								numbOfEncamp++;
								if (numbOfSoldiers % 4 == 0)
								{
									rc.broadcast(502, 0);
								}
								else
								{
									rc.broadcast(502, 1);
								}
							}
							
							else if (numbOfSoldiers % 5 == 1)
							{
								rc.broadcast(500, MineLayer);
								if (numbOfSoldiers % 2 == 0)
								{
									if (center1)
									{
										rc.broadcast(501, 2);
									}
									else
									{
										rc.broadcast(501, 1);
									}
									center1 = !center1;
								}
								else
								{
									if (center2)
									{
										rc.broadcast(501, 3);
									}
									else
									{
										rc.broadcast(501, 1);
									}
									center2 = !center2;
								}
							}
							else if (numbOfSoldiers % 5 == 2)
							{
								rc.broadcast(500, Solder);
							}
							else if (numbOfSoldiers % 5 == 3)
							{
								rc.broadcast(500, Defense);
							}
							else
							{
								rc.broadcast(500, Killer);
							}
						}
					}
				}
				/// Artiller soldiers
				else if (rc.getType() == RobotType.ARTILLERY)
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
				// regular soldier
				else if (rc.getType() == RobotType.SOLDIER) {
					if (encampments == null) {
						encampments = rc.senseAllEncampmentSquares();
					}
					if (myType == nothing) {
						myType = rc.readBroadcast(500);
						if (myType == Encamper) {
							encamperBool = true;
							encampIndex = rc.readBroadcast(501);
							encampType = rc.readBroadcast(502);
						}
						else if (myType == MineLayer)
						{
							mineLayer = true;
						}
					}
					
					if (rc.isActive()) {
						
						Direction dir3 = null;
						Team mine2 = null;
						
						if (myType == Flanker)
						{
							
						}
						/// Find enemy and kill them
						else if (myType == Killer)
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
									if (Clock.getRoundNum() < 200)
									{
										rc.broadcast(504, 1);
									}
									enemyLoc = rc.senseLocationOf(enemy[i]);
									dir3 = rc.getLocation().directionTo(enemyLoc);
									
								}
							}
							
							if (Clock.getRoundNum() % 200 == 0)
							{
								attack = true;
							}
							if (numberOfMoves < 15)
							{
								dir3 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							}
							else if (dir3 == null && attack)
							{
								dir3 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							}
							mine2 = rc.senseMine(rc.getLocation().add(dir3));
							if (rc.canMove(dir3))
							{
								if (mine2!= null && mine2 != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir3));
								}
								else
								{
									rc.move(dir3);
									dir3 = null;
									numberOfMoves++;
								}
								dir3 = null;
							}
							else
							{
								while (!rc.canMove(dir3))
								{
									dir3 = Direction.values()[(int)(Math.random()*8)];
								}
								mine2 = rc.senseMine(rc.getLocation().add(dir3));
								if (mine2!= null && mine2 != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir3));
								}
								else
								{
									rc.move(dir3);
									numberOfMoves++;
								}
								dir3 = null;
								
							}
						}
						// run towards enemy HQ
						else if (myType == Rusher)
						{
							Direction dir4 = null;
							dir4 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							mine2 = rc.senseMine(rc.getLocation().add(dir4));
							if (rc.canMove(dir4))
							{
								if (mine2!= null && mine2 != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir4));
								}
								else
								{
									rc.move(dir4);
									numberOfMoves++;
								}
							}
							else
							{
								while (!rc.canMove(dir4))
								{
									dir4 = Direction.values()[(int)(Math.random()*8)];
								}
								mine2 = rc.senseMine(rc.getLocation().add(dir4));
								if (mine2!= null && mine2 != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir4));
								}
								else
								{
									rc.move(dir4);
									numberOfMoves++;
								}
							}
							
						}
						// wait around base and attack enemy soldiers
						else if (myType == Defense)
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
									dir3 = rc.getLocation().directionTo(enemyLoc);
									
								}
							}
							if (Clock.getRoundNum() % 200 == 0)
							{
								attack = true;
							}
							if (numberOfMoves < 4)
							{
								dir3 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							}
							else if (dir3 == null && attack)
							{
								dir3 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							}
							mine2 = rc.senseMine(rc.getLocation().add(dir3));
							if (rc.canMove(dir3))
							{
								if (mine2!= null && mine2 != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir3));
								}
								else
								{
									rc.move(dir3);
									
									numberOfMoves++;
								}
								dir3 = null;
							}
							else
							{
								while (!rc.canMove(dir3))
								{
									dir3 = Direction.values()[(int)(Math.random()*8)];
								}
								mine2 = rc.senseMine(rc.getLocation().add(dir3));
								if (mine2!= null && mine2 != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir3));
								}
								else
								{
									rc.move(dir3);
									numberOfMoves++;
								}
								dir3 = null;
								
							}
							
						}
						//// lays mines to protect our base
						else if (mineLayer)
						{
							Direction dir2 = null;
							dir2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							boolean encampment = rc.senseEncampmentSquare(rc.getLocation());
							Team mine = null; 
							int numbOfmoves = 0;
							if (moveDirection == 0)
							{
								moveDirection = rc.readBroadcast(501);
							}
							if (moveDirection == 1)
							{
								//dir2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							}
							else if (moveDirection == 2)
							{
								dir2 = dir2.rotateRight();
							}
							else if (moveDirection == 3)
							{
								dir2 = dir2.rotateLeft();
							}
							else
							{
								if (numbOfmoves % 2 == 0)
								{
									dir2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
								}
								else
								{
									while (dir2 == rc.getLocation().directionTo(rc.senseEnemyHQLocation()))
									{
										dir2 = Direction.values()[(int)(Math.random()*8)];
									}
								}
								numbOfmoves++;
							}
							
							mine = rc.senseMine(rc.getLocation().add(dir2));
							
							if (encampment)
							{
								rc.captureEncampment(RobotType.ARTILLERY);
							}
							else if (rc.senseMine(rc.getLocation()) == null)
							{
								rc.layMine();
							}
							else if (rc.canMove(dir2)) 
							{
								
								if (mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir2));
								}
								else
								{
									rc.move(dir2);
								}
							}
							else
							{
								while (!rc.canMove(dir2))
								{
									dir2 = Direction.values()[(int)(Math.random()*8)];
								}
							
									rc.move(dir2);
								
							}
						}
						else if (encamperBool)
						{
							Direction dir2 = null;
							dir2 = rc.getLocation().directionTo(encampments[encampIndex % encampments.length]);
							Team mine = rc.senseMine(rc.getLocation().add(dir2));
							boolean encampment = rc.senseEncampmentSquare(rc.getLocation());
							if (encampment)
							{
								if (encampType == 0)
								{
									rc.captureEncampment(RobotType.SUPPLIER);
								}
								else
								{
									rc.captureEncampment(RobotType.GENERATOR);
								}
							}
							else if(rc.canMove(dir2)) 
							{
								if( mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir2));
								}
								else if (encampment)
								{
									if (encampType == 0)
									{
										rc.captureEncampment(RobotType.SUPPLIER);
									}
									else if (encampType == 1)
									{
										rc.captureEncampment(RobotType.GENERATOR);
									}
								}
								else
								{
									if (encampment)
									{
										if (encampType == 0)
										{
											rc.captureEncampment(RobotType.SUPPLIER);
										}
										else if (encampType == 1)
										{
											rc.captureEncampment(RobotType.GENERATOR);
										}
									}
									numberOfMoves++;
									rc.move(dir2);
								}
							}
							else
							{
								
								while (!rc.canMove(dir2))
								{
									dir2 = Direction.values()[(int)(Math.random()*8)];
								}
								mine = rc.senseMine(rc.getLocation().add(dir2));
								if( mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir2));
								}
								else{
								rc.move(dir2);
								}
							}
						}
						// if we are a soldier going to attack the enemy
						else
						{
							Direction dir2 = null;
							if ( Clock.getRoundNum() % 200 == 0)
							{
								attack = true;
							}
							if (attack)
							{
								dir2 =  rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							}
							else
							{
								dir2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							}
							
							boolean encampment = rc.senseEncampmentSquare(rc.getLocation());
							Team mine = rc.senseMine(rc.getLocation().add(dir2));
							
							if (encampment)
							{
								rc.captureEncampment(RobotType.ARTILLERY);
							}
							else if (!attack && (rc.senseMine(rc.getLocation()) == null))
							{
								rc.layMine();
							}
							else if(rc.canMove(dir2)) 
							{
								if( mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir2));
								}
								
								else
								{
									numberOfMoves++;
									rc.move(dir2);
								}
							}
							else
							{
								while (!rc.canMove(dir2))
								{
									dir2 = Direction.values()[(int)(Math.random()*8)];
								}
								mine = rc.senseMine(rc.getLocation().add(dir2));
								if( mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir2));
								}
								else{
								rc.move(dir2);
								}
							}
						}
					}
					
					/*
					if (Math.random()<0.01 && rc.getTeamPower()>5) {
						// Write the number 5 to a position on the message board corresponding to the robot's ID
						rc.broadcast(rc.getRobot().getID()%GameConstants.BROADCAST_MAX_CHANNELS, 5);
					}
					*/
				}

				// End turn
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}