package theSwarm;

import simpleSolder.HQBot;
import simpleSolder.RusherBot;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class Creeper {
	static Direction dir;
	static int distance = 3;
	public static void run(RobotController rc) {
		
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.SOLDIER) 
				{
					dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
					MapLocation spot = rc.senseHQLocation().add(dir);
					Team mined = rc.senseMine(spot);
					if (mined == null)
					{
						dir = rc.getLocation().directionTo(spot);
					}
					else
					{
						MapLocation[] mineSpots = rc.senseMineLocations(rc.senseHQLocation(), (distance*distance), rc.getTeam());
						if ( mineSpots.length > (distance * distance * 3))
						{
							distance++;
						}
						while (mined != null || spot.equals(rc.senseHQLocation()))
						{
							spot = rc.senseHQLocation().add(Direction.values()[(int)(Math.random()*8)]);
						
							for (int i = 0; i < distance; i++)
							{
								spot = spot.add(Direction.values()[(int)(Math.random()*8)]);
							}
							
							mined = rc.senseMine(spot);
						}
						dir = rc.getLocation().directionTo(spot);
						
					}
					
					Team mine = rc.senseMine(rc.getLocation().add(dir));
					boolean encampment = rc.senseEncampmentSquare(rc.getLocation());
					
					if (encampment && rc.senseMine(rc.getLocation()) == null)
					{
						//rc.captureEncampment(RobotType.ARTILLERY);
						if (rc.isActive())
						{
							rc.layMine();
						}
					}
					else if (rc.senseMine(rc.getLocation()) == null)
					{
						if (rc.isActive())
						{
							rc.layMine();
						}
					}
					else
					{
						Hatchery.Move(rc, rc.getLocation().add(dir));
					}
				}
				
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}
}
