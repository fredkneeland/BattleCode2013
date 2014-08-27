package simpleSolder;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.Upgrade;

public class RobotPlayer {
	
	static final int channel1 = 549;
	static final int channel2 = 3;
	static final int channel3 = 57681;
	static final int frontEncode = 140000000;
	static final int backEncode = 34;
	static int message;
	static int myType = 0;
	static int input = 0;
	static int input2 = 0;
	static int input3 = 0;
	static boolean direction = false;
	static int type = 0;
	static int MineLayer = 1;
	static int Defender2 = 2;
	static int Killer = 3;
	static int Flanker = 4;
	static int SmartDefender = 5;
	static int Encamper = 6;
	static int Formation = 7;
	static int Scout = 8;
	static int Artilery = 9;
	static int Rusher = 10;
	static int MineEnemy = 11;
	static int x = -1;
	static int y = -1;
	static int x2;
	static int y2;
	static MapLocation dRallyPoint;
	static Direction enemyHQ;
	static int position;
	static boolean leader = false;
	static MapLocation[] encampSpots;
	static boolean scoutLeft = true;
	
	public static void run(RobotController rc) {
		while (true)
		{
			try
			{
				if (rc.getType() == RobotType.HQ) 
				{
					encampSpots = rc.senseEncampmentSquares(rc.getLocation(), 49, Team.NEUTRAL);
					
					if (rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()) < 600)
					{
						CloseBot.run(rc);
					}
					else if (encampSpots.length > 5)
					{
						NukeBot.run(rc);
					}
					
					else
					{
						HQBot.run(rc);
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
						myType = HQBot.readMessage(rc, 0);
						if (rc.getTeamPower() > 200)
						{
							//rc.wearHat();
						}
					}
					
					if (myType == Killer)
					{
						KillerBot.run(rc);
					}
					else if (myType == Defender2)
					{
						if (x < 0)
						{
							// read x values
							x = HQBot.readMessage(rc, 1);
							//read y values
							y = HQBot.readMessage(rc, 2);
						}
						Defender.run(rc, 0, x, y);
						
					}
					else if (myType == MineLayer)
					{
						DMineLayer.run(rc);
					}
					else if (myType == SmartDefender)
					{
						if (x < 0)
						{
							// read x values
							x = HQBot.readMessage(rc, 1);
							//read y values
							y = HQBot.readMessage(rc, 2);
						}
						SmartD.run(rc, 0, x, y);
					}
					else if (myType == Flanker)
					{
						int i = HQBot.readMessage(rc, 1);
						if (i % 2 == 0)
						{
							direction = false;
						}
						else
						{
							direction = true;
						}
						FlankerBot.run(rc, direction);
						direction = !direction;
					}
					else if (myType == Encamper)
					{
						if (x < 0)
						{
							// read x values
							x = HQBot.readMessage(rc, 1);
							//read y values
							y = HQBot.readMessage(rc, 2);
						}
						EncamperBot.run(rc, 0, x, y);
					}
					else if (myType == Formation)
					{
						if (x < 0)
						{
							// read x values
							x = HQBot.readMessage(rc, 1);
							//read y values
							y = HQBot.readMessage(rc, 2);
							x2 = HQBot.readMessage(rc, 3);
							y2 = HQBot.readMessage(rc, 4);
							position = HQBot.readMessage(rc, 5);
							if (HQBot.readMessage(rc, 6) == 1)
							{
								leader = true;
							}
							else
							{
								leader = false;
							}
						}
						ColumnBot.run(rc, position, leader, x, y, x2, y2);
					}
					else if (myType == Scout)
					{
						if (HQBot.readMessage(rc, 1) == 1)
						{
							scoutLeft = true;
						}
						else
						{
							scoutLeft = false;
						}
						ScoutBot.run(rc, scoutLeft);
					}
					else if (myType == Artilery)
					{
						if (x < 0)
						{
							// read x values
							x = HQBot.readMessage(rc, 1);
							//read y values
							y = HQBot.readMessage(rc, 2);
						}
						ArtileryBot.run(rc, x, y);
					}
					else if (myType == Rusher)
					{
						RusherBot.run(rc);
					}
					else if (myType == MineEnemy)
					{
						MineToEnemy.run(rc);
					}
					else
					{
						//DMineLayer.run(rc);
						RusherBot.run(rc);
					}
				}
				
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}
}
