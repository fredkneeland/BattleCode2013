package econBot;

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

public class Miner {
	static Direction dir;
	static int distance = 3;
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.SOLDIER) 
				{
					if (rc.isActive()) 
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
						
						/*
						Team mine2 = rc.senseMine(rc.senseHQLocation().add(Direction.NORTH_EAST, 2));
						Team mine3 = rc.senseMine(rc.senseHQLocation().add(Direction.NORTH_WEST, 2));
						Team mine4 = rc.senseMine(rc.senseHQLocation().add(Direction.SOUTH_EAST, 2));
						Team mine5 = rc.senseMine(rc.senseHQLocation().add(Direction.SOUTH_WEST, 2));
						if (mine2 == null || mine2 != rc.getTeam())
						{
							dir = rc.getLocation().directionTo(rc.senseHQLocation().add(Direction.NORTH_EAST, 2));
						}
						else if (mine3 == null || mine3 != rc.getTeam())
						{
							dir = rc.getLocation().directionTo(rc.senseHQLocation().add(Direction.NORTH_WEST, 2));
						}
						else if (mine4 == null || mine4 != rc.getTeam())
						{
							dir = rc.getLocation().directionTo(rc.senseHQLocation().add(Direction.SOUTH_EAST, 2));
						}
						else if (mine5 == null || mine5 != rc.getTeam())
						{
							dir = rc.getLocation().directionTo(rc.senseHQLocation().add(Direction.SOUTH_WEST, 2));
						}
						*/
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
							//RobotPlayer.Move(rc, dir);
							mine = rc.senseMine(rc.getLocation().add(dir));
							if(rc.canMove(dir) && (dir != Direction.NONE || dir != Direction.OMNI)) 
							{
								if( mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir));
								}
								else
								{
									rc.move(dir);
								}
							}
							else
							{
								
								while (!rc.canMove(dir))
								{
									dir = Direction.values()[(int)(Math.random()*8)];
								}
								mine = rc.senseMine(rc.getLocation().add(dir));
								if( mine!= null && mine != rc.getTeam())
								{
									rc.defuseMine(rc.getLocation().add(dir));
								}
								else{
								rc.move(dir);
								}
							}
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
