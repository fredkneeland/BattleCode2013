package theSwarm;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class Zerglings {
	static boolean attack = false;
	static MapLocation rallyPoint;
	static Direction dir;
	public static void run(RobotController rc) {
		dir = rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation());
		rallyPoint = rc.getLocation().add(dir, 5);
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.SOLDIER) 
				{
					
					if (Clock.getRoundNum() % 200 < 5)
					{
						attack = true;
					}
					
					
					if (attack)
					{
						Hatchery.Move(rc, rc.senseEnemyHQLocation());
					}
					else
					{
						Hatchery.Move(rc, rallyPoint);
					}
					
				}
				
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}

}
