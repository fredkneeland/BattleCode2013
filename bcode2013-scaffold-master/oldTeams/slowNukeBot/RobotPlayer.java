package slowNukeBot;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class RobotPlayer {

	static int numbOfSoldiers = 0;
	static int myType = 0;
	static boolean enemySeen = false;
	
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.HQ) 
				{
					if (rc.isActive()) {
						// Spawn a soldier
						Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						Team mine = rc.senseMine(rc.getLocation().add(dir));
						while (!rc.canMove(dir) || ((mine != null) && (mine != rc.getTeam())))
						{
							dir = Direction.values()[(int)(Math.random()*8)];
							mine = rc.senseMine(rc.getLocation().add(dir));
							if (Clock.getRoundNum() > 200)
							{
								rc.researchUpgrade(Upgrade.NUKE);
							}
						}
						if (rc.canMove(dir))
						{
							numbOfSoldiers++;
							if (numbOfSoldiers < 20 && Clock.getRoundNum() < 200)
							{
								rc.spawn(dir);
								rc.broadcast(123, (numbOfSoldiers % 2));
							}
							else
							{
								rc.researchUpgrade(Upgrade.NUKE);
							}
						}
					}
				} 
				else if (rc.getType() == RobotType.ARTILLERY)
				{
					if (rc.isActive())
					{
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
							enemyLoc = rc.senseLocationOf(enemy[i]);
							if (rc.canAttackSquare(enemyLoc))
							{
								rc.attackSquare(enemyLoc);
							}
						}
					}
					}
				} else if (rc.getType() == RobotType.SOLDIER) {
					if (rc.isActive()) {
						Direction dir;
						if (myType == 0)
						{
							myType = rc.readBroadcast(123);
						}
						if (myType == 0)
						{
							 dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							 
						}
						else
						{
							 dir = Direction.values()[(int)(Math.random()*8)];
						}
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
								enemySeen = true;
							}
						}
						if (enemySeen)
						{
							dir = rc.getLocation().directionTo(rc.senseHQLocation());
						}
						
							Team mine = rc.senseMine(rc.getLocation().add(dir));
							if( mine!= null && mine != rc.getTeam()){
								rc.defuseMine(rc.getLocation().add(dir));
							}
							else if (rc.senseEncampmentSquare(rc.getLocation()))
							{
								rc.captureEncampment(RobotType.ARTILLERY);
							}
							else if (rc.senseMine(rc.getLocation())==null)
							{
								rc.layMine();
							}
							else if(rc.canMove(dir)) {
								rc.move(dir);
								rc.setIndicatorString(0, "Last direction moved: "+dir.toString());
							}
							else
							{
								while (!rc.canMove(dir))
								{
									dir = Direction.values()[(int)(Math.random()*8)];
								}
							    mine = rc.senseMine(rc.getLocation().add(dir));
								if( mine!= null && mine != rc.getTeam()){
									rc.defuseMine(rc.getLocation().add(dir));
								}
								else 
								{
									rc.move(dir);
								}
							}
						
					}
				}
				// End turn
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}

}
