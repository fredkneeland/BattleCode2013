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

public class Hatchery {
	static int Zergling = 1;
	static int Bees = 2;
	static int Creepers = 3;
	static int numbOfSoldiers = 0;
	static int type;
	static boolean research = true;
	static Direction dir;
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.HQ) 
				{
					if (dir == null)
					{
						dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
					}
					
					while (!rc.canMove(dir))
					{
						dir = Direction.values()[(int)(Math.random()*8)];
					}
					
					research = false;
					if (!rc.hasUpgrade(Upgrade.VISION))
					{
						if (rc.isActive())
						{
							rc.researchUpgrade(Upgrade.VISION);
						}
						research = true;
					}
					else if (numbOfSoldiers == 0)
					{
						type = Bees;
					}
					else if (numbOfSoldiers < 3)
					{
						type = Creepers;
					}
					else if (!rc.hasUpgrade(Upgrade.FUSION))
					{
						if (rc.isActive())
						{
							rc.researchUpgrade(Upgrade.FUSION);
						}
						research = true;
					}
					else if (numbOfSoldiers < 15)
					{
						type = Bees;
					}
					else if (Clock.getRoundNum() > 250 && !rc.hasUpgrade(Upgrade.DEFUSION))
					{
						if (rc.isActive())
						{
							rc.researchUpgrade(Upgrade.DEFUSION);
						}
						research = true;
					}
					else if ((numbOfSoldiers % 5) < 2)
					{
						type = Bees;
					}
					else
					{
						type = Zergling;
					}
					
					if (!research)
					{
						if (rc.isActive())
						{
							rc.spawn(dir);
							rc.broadcast(345, type);
							numbOfSoldiers++;
						}
					}
				}
				
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}
	
	protected static void Move(RobotController rc, MapLocation loc) throws GameActionException
	{
		if (rc.isActive())
		{
			Direction dir = rc.getLocation().directionTo(loc);
			Team mine = rc.senseMine(rc.getLocation().add(dir));
			if (dir != Direction.NONE && dir != Direction.OMNI)
			{
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
					else
					{
						rc.move(dir);
					}
				}
			}
		}
	}
}
