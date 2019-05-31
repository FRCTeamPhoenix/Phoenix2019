/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.util;

import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2.LinkType;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;

/**
 * Add your docs here.
 */
public class PixyDriver {

    static Pixy2 pixy;

    public static double xCoord;
    public static double distance;

    public static void init() {
        pixy = Pixy2.createInstance(LinkType.SPI);
        pixy.init();
    }

    public static void get() {
        pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG1, 1);
        if(pixy.getCCC().getBlocks().size() == 0) {
            return;
        }
        Block ball = pixy.getCCC().getBlocks().get(0);
        
        xCoord = ball.getX() + ball.getWidth() / 2;
        distance = ball.getWidth();

        if(ball.getX() <= 0) {
            
        }
        System.out.println(xCoord);
    }
}
