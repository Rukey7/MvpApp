package com.dl7.helperlibrary.indicator;


import com.dl7.helperlibrary.indicator.sprite.Sprite;
import com.dl7.helperlibrary.indicator.style.ChasingDots;
import com.dl7.helperlibrary.indicator.style.Circle;
import com.dl7.helperlibrary.indicator.style.CubeGrid;
import com.dl7.helperlibrary.indicator.style.DoubleBounce;
import com.dl7.helperlibrary.indicator.style.FadingCircle;
import com.dl7.helperlibrary.indicator.style.FoldingCube;
import com.dl7.helperlibrary.indicator.style.MultiplePulse;
import com.dl7.helperlibrary.indicator.style.MultiplePulseRing;
import com.dl7.helperlibrary.indicator.style.Pulse;
import com.dl7.helperlibrary.indicator.style.PulseRing;
import com.dl7.helperlibrary.indicator.style.RotatingCircle;
import com.dl7.helperlibrary.indicator.style.RotatingPlane;
import com.dl7.helperlibrary.indicator.style.ThreeBounce;
import com.dl7.helperlibrary.indicator.style.WanderingCubes;
import com.dl7.helperlibrary.indicator.style.Wave;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
