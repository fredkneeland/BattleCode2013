package team019;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class ArtileryBot {

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
	public static void run(RobotController rc, int x_1, int y_1) 
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
					
						
						if (!mine1)
						{
							if (rc.getLocation().equals(encamp))
							{
								mine1 = true;
							}
							else
							{
								HQBot.Move(rc, dir2);
							}
						}
						
						else if (rc.getLocation().equals(encamp))
						{
							rc.captureEncampment(RobotType.ARTILLERY);
						}
						else
						{
							dir3 = rc.getLocation().directionTo(encamp);
							HQBot.Move(rc, dir3);
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

