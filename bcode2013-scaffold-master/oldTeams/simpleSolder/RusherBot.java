package simpleSolder;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class RusherBot {
	static Direction dir;
	static Team mine;
	static int counter = 0;
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
						mine = rc.senseMine(rc.getLocation().add(dir));
						
						if (mine != null && mine != rc.getTeam())
						{
							rc.defuseMine(rc.getLocation().add(dir));
						}
						else if (!rc.canMove(dir))
						{
							while ((!rc.canMove(dir)) && counter < 8)
							{
								counter++;
								dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
								for (int i = 0; i < counter; i++)
								{
									if (counter % 2 == 0)
									{
										dir = dir.rotateLeft();
									}
									else
									{
										dir = dir.rotateRight();
									}
								}
							}
							mine = rc.senseMine(rc.getLocation().add(dir));
							if (mine != null && mine != rc.getTeam())
							{
								rc.defuseMine(rc.getLocation().add(dir));
							}
							else if (!rc.canMove(dir))
							{
								
							}
							else
							{
								rc.move(dir);
							}
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
