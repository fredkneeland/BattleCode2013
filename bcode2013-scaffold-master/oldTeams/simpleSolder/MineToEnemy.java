package simpleSolder;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import calcBot.RobotPlayer;

public class MineToEnemy {
	static Direction dir;
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
					Team mine = rc.senseMine(rc.getLocation().add(dir));
					boolean encampment = rc.senseEncampmentSquare(rc.getLocation());
					if (encampment)
					{
						rc.captureEncampment(RobotType.ARTILLERY);
					}
					else if (rc.senseMine(rc.getLocation()) == null)
					{
						rc.layMine();
					}
					else
					{
						HQBot.Move(rc, dir);
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
