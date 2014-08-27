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

public class MineLayer2 {
	static Direction dir;
	static Direction dir2;
	static MapLocation HQ;
	static MapLocation target;
	static int numbOfLays = 0;
	public static void run(RobotController rc) 
	{
		HQ = rc.senseHQLocation();
		while (true)
		{
			try 
			{
				if (rc.getType() == RobotType.SOLDIER)
				{
					if (numbOfLays == 0)
					{
						target = HQ.add(Direction.NORTH);
						dir2 = Direction.NORTH;
					}
					else
					{
						dir2 = dir2.rotateLeft();
					}
					boolean encampment = rc.senseEncampmentSquare(target);
					Team mine = rc.senseMine(target);
					if (rc.isActive())
					{
						
						if (encampment)
						{
							numbOfLays++;
						}
						else if (mine != null && mine != rc.getTeam())
						{
							dir = rc.getLocation().directionTo(target);
							if (rc.getLocation().add(dir).equals(target))
							{
								rc.defuseMine(target);
							}
							else
							{
								RobotPlayer.Move(rc, dir);
							}
						}
						else if (mine != null && mine == rc.getTeam())
						{
							numbOfLays++;
						}
						else
						{
							dir = rc.getLocation().directionTo(target);
							if (rc.getLocation().equals(target))
							{
								rc.layMine();
							}
							else
							{
								RobotPlayer.Move(rc, dir);
							}
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
