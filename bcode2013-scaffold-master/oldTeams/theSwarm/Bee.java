package theSwarm;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class Bee {
	static MapLocation encampSpot;
	static MapLocation[] encampSpots;
	static int i = 1;
	public static void run(RobotController rc) {
		
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.SOLDIER) 
				{
					if (encampSpot == null)
					{
						encampSpots = rc.senseEncampmentSquares(rc.getLocation(), 33, Team.NEUTRAL);
						if (encampSpots.length > 0)
						{
							encampSpot = encampSpots[0];
						}
					}
					
					// if we didn't find anything
					if (encampSpot == null)
					{
						encampSpots = rc.senseAllEncampmentSquares();
						if (rc.isActive())
						{
							Hatchery.Move(rc, encampSpots[0]);
						}
					}
					else
					{
						if (rc.getLocation().equals(encampSpot))//(rc.senseEncampmentSquare(rc.getLocation()))//(rc.getLocation().equals(encampSpot))
						{
							if (rc.isActive())
							{
								if (rc.senseCaptureCost() < rc.getTeamPower())
								{
									encampSpots = rc.senseAlliedEncampmentSquares();
									if (encampSpots.length < 1)
									{
										rc.captureEncampment(RobotType.SUPPLIER);
									}
									else if (rc.getTeamPower() > 3000)
									{
										rc.captureEncampment(RobotType.SUPPLIER);
									}
									else if (rc.getTeamPower() < 250)
									{
										rc.captureEncampment(RobotType.GENERATOR);
									}
									else if (encampSpots.length < 5)
									{
										rc.captureEncampment(RobotType.GENERATOR);
									}
									else
									{
										rc.captureEncampment(RobotType.SUPPLIER);
									}
								}
							}
						}
						else if (rc.canSenseSquare(encampSpot))
						{
							if (rc.senseObjectAtLocation(encampSpot) != null)
							{
								if (rc.senseObjectAtLocation(encampSpot).getTeam() != rc.getTeam())
								{
									Hatchery.Move(rc, encampSpot);
								}
								else
								{
									if (encampSpots.length > 1)
									{
										Hatchery.Move(rc, encampSpots[i]);
										encampSpot = encampSpots[i];
										i++;
									}
									else
									{
										Hatchery.Move(rc, rc.getLocation().add(Direction.values()[(int)(Math.random()*8)]));
										encampSpot = null;
									}
									
								}
							}
							else
							{
								if (rc.canMove(rc.getLocation().directionTo(encampSpot)))
								{
									Hatchery.Move(rc, encampSpot);
								}
							}
						}
						else
						{
							if (rc.canMove(rc.getLocation().directionTo(encampSpot)))
							{
								Hatchery.Move(rc, encampSpot);
							}
						}
					}
				}
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}

}
