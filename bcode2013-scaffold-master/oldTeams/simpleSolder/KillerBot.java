package simpleSolder;

import calcBot.RobotPlayer;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class KillerBot {
	static Direction dir3;
	static int distance = 3;
	static boolean attack = false;
	static int numberOfMoves = 0;
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.SOLDIER) 
				{
					if (rc.isActive()) 
					{
						Team mine2 = null;
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
				}
				rc.yield();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
}
