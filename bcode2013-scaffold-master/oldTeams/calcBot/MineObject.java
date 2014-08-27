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


public class MineObject {
	static Robot enemy;
	public static void run(RobotController rc) 
	{
		while (true)
		{
			try 
			{
				enemy = (Robot) rc.senseObjectAtLocation(rc.getLocation());
				if (enemy != null && (enemy.getTeam() != rc.getTeam()))
				{
					rc.broadcast(1234, rc.getLocation().x);
					rc.broadcast(1235, rc.getLocation().y);
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
