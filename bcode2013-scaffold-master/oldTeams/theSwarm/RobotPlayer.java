package theSwarm;

import simpleSolder.ArtileryBot;
import simpleSolder.CloseBot;
import simpleSolder.ColumnBot;
import simpleSolder.DMineLayer;
import simpleSolder.Defender;
import simpleSolder.EncamperBot;
import simpleSolder.FlankerBot;
import simpleSolder.HQBot;
import simpleSolder.KillerBot;
import simpleSolder.MineToEnemy;
import simpleSolder.NukeBot;
import simpleSolder.RusherBot;
import simpleSolder.ScoutBot;
import simpleSolder.SmartD;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class RobotPlayer {
	
	static int Zergling = 1;
	static int Bees = 2;
	static int Creepers = 3;
	static int myType = 0;
	
	
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.HQ) 
				{
					Hatchery.run(rc);
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
									break;
								}
							}
						}
					}
				}
				else if (rc.getType() == RobotType.SOLDIER)
				{
					if (myType == 0)
					{
						myType = rc.readBroadcast(345);
					}
					if (myType == Zergling)
					{
						Zerglings.run(rc);
					}
					else if (myType == Bees)
					{
						Bee.run(rc);
					}
					else if (myType == Creepers)
					{
						Creeper.run(rc);
					}
				}
				
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}
}

