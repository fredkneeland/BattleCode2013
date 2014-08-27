package simpleSolder;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class FlankerBot {
	
	static Direction dir;
	static int distance = 3;
	static boolean attack = false;
	static int numberOfMoves = 0;
	static boolean goStraight;
	static boolean goRight;
	static MapLocation RallyPoint;
	public static void run(RobotController rc, boolean left) {
		
		if (left)
		{
			dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
			dir = dir.rotateLeft();
			RallyPoint = rc.senseHQLocation().add(dir);
			for (int i = 0; i < 10; i++)
			{
				RallyPoint = RallyPoint.add(dir);
			}
		}
		else
		{
			dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
			dir = dir.rotateRight();
			RallyPoint = rc.senseHQLocation().add(dir);
			for (int i = 0; i < 10; i++)
			{
				RallyPoint = RallyPoint.add(dir);
			}
		}
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.SOLDIER) 
				{
					if (rc.isActive()) 
					{
						dir = rc.getLocation().directionTo(RallyPoint);
						
						
						if (Clock.getRoundNum() % 100 == 0)
						{
							attack = true;
						}
						
						if (attack)
						{
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							if (left)
							{
								dir = dir.rotateLeft();
							}
							else
							{
								dir = dir.rotateRight();
							}
						}
						else
						{
							
						}
						
						if (!rc.canMove(dir))
						{
							if (rc.senseObjectAtLocation(rc.getLocation().add(dir)) == null)
							{
								goStraight = true;
							}
							
						}
						if (goStraight)
						{
							dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						}
						
						if (!rc.getLocation().isAdjacentTo(rc.senseEnemyHQLocation()))
						{
							HQBot.Move(rc, dir);	
						}
					}		
				}
				rc.yield();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

}
