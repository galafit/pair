package main.chart.axis;

import java.awt.*;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class AxisData {
    private Double min = null;
    private Double max = null;
    private final double DEFAULT_MIN = 0;
    private final double DEFAULT_MAX = 1;
    protected boolean isHorizontal;
    protected boolean isAutoScale = true;
    protected boolean isInverted = false;
    private double lowerPadding = 0.02;
    private double upperPadding = 0.02;

    public void resetRange() {
        min = null;
        max = null;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    abstract public double pointsPerUnit(Rectangle area);

    abstract public int valueToPoint(double value, Rectangle area);

    public Double getMin() {
        double resultantMin = (min == null) ? DEFAULT_MIN : min;
        double resultantMax = (max == null) ? DEFAULT_MAX : max;

        resultantMin = (!isAutoScale()) ? resultantMin : resultantMin - lowerPadding * (resultantMax - resultantMin);

        return resultantMin;

    }

    public Double getMax() {
        double resultantMin = (min == null) ? DEFAULT_MIN : min;
        double resultantMax = (max == null) ? DEFAULT_MAX : max;

        resultantMax = (!isAutoScale()) ? resultantMax : resultantMax + upperPadding * (resultantMax - resultantMin);
        return resultantMax;

    }

    /**
     * If isAutoScale = FALSE this method simply sets: min = newMin, max = newMax.
     * But if isAutoScale = TRUE then it only extends the range and sets:
     * min = Math.min(min, newMin), max = Math.max(max, newMax).
     *
     * @param newMin new min value
     * @param newMax new max value
     */
    public void setRange(double newMin, double newMax) {
        if (!isAutoScale) {
            min = newMin;
            max = newMax;
        } else {
            min = (min == null) ? newMin : Math.min(newMin, min);
            max = (max == null) ? newMax : Math.max(newMax, max);
        }
    }

    abstract public TickProvider getTicksProvider(Rectangle area);

    public boolean isAutoScale() {
        return isAutoScale;
    }

    public void setAutoScale(boolean isAutoScale) {
        this.isAutoScale = isAutoScale;
    }
}
