package Rusher;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class RobotPlayer {
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.HQ) {
					if (rc.isActive()) {
						// Spawn a soldier
						Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						Team mine3;
						mine3 = rc.senseMine(rc.getLocation().add(dir));
						while (!rc.canMove(dir) || ((mine3 != null) && (mine3 != rc.getTeam())))
						{
							dir = Direction.values()[(int)(Math.random()*8)];
							mine3 = rc.senseMine(rc.getLocation().add(dir));
						}
						if (rc.canMove(dir))
							rc.spawn(dir);
					}
				} else if (rc.getType() == RobotType.SOLDIER) {
					if (rc.isActive()) {
						if (Math.random()<0.005) {
							// Lay a mine 
							//if(rc.senseMine(rc.getLocation())==null)
								//rc.layMine();
						} else { 
							// Choose a random direction, and move that way if possible
							Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
							Team mine = rc.senseMine(rc.getLocation().add(dir));
							if( mine!= null && mine != rc.getTeam()){
								rc.defuseMine(rc.getLocation().add(dir));
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
								else if(rc.canMove(dir)) {
									rc.move(dir);
									rc.setIndicatorString(0, "Last direction moved: "+dir.toString());
								}
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
