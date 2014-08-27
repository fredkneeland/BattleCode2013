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

public class EncamperBot 
{
	static Direction dir;
	static boolean encamper = false;
	//static int x_1 = 0;
	//static int y_1 = 0;
	static MapLocation encamp;
	static int Supplier = 1;
	static boolean mine1 = false;
	static boolean mine2 = false;
	static boolean mine3 = false;
	static boolean beenToEncamp = false;
	static MapLocation encampAdjacent = null;
	static MapLocation newSpot = null;
	static Direction dir3 = null;
	public static void run(RobotController rc, int MyType2, int x_1, int y_1) 
	{
		encamp = new MapLocation(x_1, y_1);
		while (true)
		{
			try 
			{
				if (rc.getType() == RobotType.SOLDIER)
				{
					if (rc.isActive())
					{
						dir3 = rc.getLocation().directionTo(encamp);
						Direction dir2 = null;
						dir2 = rc.getLocation().directionTo(encamp);
						// if we havent put mines around our encampments
					
						
						if (!mine1)
						{
							if (rc.getLocation().equals(encamp))
							{
								dir2 = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
								//Robot object = (Robot) rc.senseObjectAtLocation(rc.getLocation().add(dir2));
								Team mine = rc.senseMine(rc.getLocation().add(dir2));
										//rc.senseObjectAtLocation(rc.getLocation().directionTo(rc.senseEnemyHQLocation()))
								if (mine != null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir2));
								}
								else if (mine != null && mine == rc.getTeam())
								{
									mine1 = true;
								}
								else if (!rc.canMove(dir2))
								{
									
								}
								else
								{
									RobotPlayer.Move(rc, dir2);
								}
							}
							else if (rc.getLocation().equals(encamp.add(encamp.directionTo(rc.senseEnemyHQLocation()))))
							{
								
								if (rc.senseMine(rc.getLocation()) != null)
								{
									mine1 = true;
								}
								else
								{
									rc.layMine();
								}
							}
							else
							{
								RobotPlayer.Move(rc, dir2);
							}
						}
						else if (!mine2)
						{
							dir2 = encamp.directionTo(rc.senseEnemyHQLocation());
							dir2 = dir2.rotateLeft();
							newSpot = encamp.add(dir2);
							Team mine = rc.senseMine(newSpot);
							if (mine != null && mine == rc.getTeam())
							{
								mine2 = true;
							}
							else if (mine != null && mine != rc.getTeam())
							{
								rc.defuseMine(newSpot);
							}
							/*
							else if (rc.senseObjectAtLocation(newSpot).getTeam() == rc.getTeam())
							{
								mine2 = true;
							}
							*/
							else if (!rc.getLocation().equals(newSpot))
							{
								dir2 = rc.getLocation().directionTo(newSpot);
								if (mine != null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir2));
								}
								else if (mine != null && mine == rc.getTeam())
								{
									mine2 = true;
								}
								else if (!rc.canMove(dir2))
								{
								
								}
								else
								{
									RobotPlayer.Move(rc, dir2);
								}
							}
							else
							{
								
								if (mine != null)
								{
									mine2 = true;
								}
								else
								{
									rc.layMine();
								}
							}
						}
						else if (!mine3)
						{
							dir2 = encamp.directionTo(rc.senseEnemyHQLocation());
							dir2 = dir2.rotateRight();
							newSpot = encamp.add(dir2);
							Team mine = rc.senseMine(newSpot);
							if (mine != null && mine == rc.getTeam())
							{
								mine3 = true;
							}
							/*
							else if (rc.senseObjectAtLocation(newSpot).getTeam() == rc.getTeam())
							{
								mine3 = true;
							}
							*/
							else if (!rc.getLocation().equals(newSpot))
							{
								dir2 = rc.getLocation().directionTo(newSpot);
								if (mine != null && mine != rc.getTeam())
								{
									rc.defuseMine(newSpot);
								}
								else if (mine != null && mine == rc.getTeam())
								{
									mine3 = true;
								}
								else if (!rc.canMove(dir2))
								{
									
								}
								else
								{
									RobotPlayer.Move(rc, dir2);
								}
							}
							else
							{
								
								if (mine != null)
								{
									mine3 = true;
								}
								else
								{
									rc.layMine();
								}
							}
						}
						else if (rc.getLocation().equals(encamp) && mine1 && mine2 && mine3)
						{
							if (rc.getTeamPower() < 250)
							{
								rc.captureEncampment(RobotType.GENERATOR);
							}
							else 
							{
								rc.captureEncampment(RobotType.SUPPLIER);
							}
						}
						/*
						else if (rc.senseMine(encamp) != null)
						{
							//rc.defuseMine(encamp);
							//rc.captureEncampment(RobotType.SUPPLIER);
						}
						*/
						else
						{
							dir3 = rc.getLocation().directionTo(encamp);
							RobotPlayer.Move(rc, dir3);
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
