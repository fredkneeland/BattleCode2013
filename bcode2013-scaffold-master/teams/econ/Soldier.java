package econ;

import battlecode.common.*;

public class Soldier {
    int type = 0;
    final int channel = 567;
    final int attackerVal = 1;
    final int defenderVal = 2;
    final int workerVal = 3;
    final int scoutVal = 4;
    public void run(RobotController rc) {
        while (true) {
            try {
                if (rc.isActive()) {
                    if (type == 0) {
                        type = rc.readBroadcast(channel);
                    }

                    switch (type) {
                        case attackerVal:
                            Attacker attacker = new Attacker();
                            attacker.run(rc);
                            break;
                        case defenderVal:
                            Defender defender = new Defender();
                            defender.run(rc);
                            break;
                        case workerVal:
                            Worker worker = new Worker();
                            worker.run(rc);
                            break;
                        case scoutVal:
                            Scout scout = new Scout();
                            scout.run(rc);
                        default:
                            attacker = new Attacker();
                            attacker.run(rc);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}