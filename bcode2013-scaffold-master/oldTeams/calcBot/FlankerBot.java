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

public class FlankerBot {
	static Direction dir;
	static boolean goRight = false;
	static boolean goStraight;
	
	public static void run(RobotController rc) 
	{
		while (true)
		{
			try 
			{
				if (rc.getType() == RobotType.SOLDIER)
				{
					if (rc.isActive())
					{
					
						dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						dir = dir.rotateLeft();
						
						if (!rc.canMove(dir))
						{
							if (rc.senseObjectAtLocation(rc.getLocation().add(dir)) == null)
							{
								goStraight = true;
								/*
								if (rc.getLocation().distanceSquaredTo(rc.senseHQLocation()) > 101)
								{
									goRight = true;
								}
								else
								{
									goStraight = true;
								}
								*/
							}
							
						}
						if (goRight)
						{
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							dir = dir.rotateRight();
						}
						if (goStraight)
						{
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						}
						
						RobotPlayer.Move(rc, dir);
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
