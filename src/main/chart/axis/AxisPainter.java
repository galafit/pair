package main.chart.axis;

import main.chart.Tick;
import main.chart.TickProvider;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Created by hdablin on 06.04.17.
 */
public class AxisPainter {
    private AxisData axis;
    private AxisViewSettings axisViewSettings;
    private int tickLabelPadding = 2;


    public AxisPainter(AxisData axis, AxisViewSettings axisViewSettings) {
        this.axis = axis;
        this.axisViewSettings = axisViewSettings;
    }

    public void draw(Graphics g, Rectangle area, int anchorPoint) {

        Font font = g.getFont();
        g.setFont(new Font(font.getFontName(), Font.PLAIN, axisViewSettings.getLabelFontSize()));


        TickProvider tickProvider = getTickProvider(g, area);
        List<Tick> ticks = tickProvider.getTicks();

        if (axisViewSettings.isAxisLineVisible()) {
            drawAxisLine(g, area, anchorPoint);
        }

        if (axisViewSettings.isTicksVisible()) {
            drawTicks(g, area, anchorPoint, ticks);
        }

        if (axisViewSettings.isMinorGridVisible()) {
            drawMinorGrid(g, area, tickProvider.getMinorTicks(axisViewSettings.getMinorGridDivider()));
        }

        if (axisViewSettings.isGridVisible()) {
            drawGrid(g, area, ticks);
        }

    }

    private void drawAxisLine(Graphics g, Rectangle area, int anchorPoint) {
        g.setColor(axisViewSettings.getAxisColor());
        if (axis.getAxisPosition().isTopOrBottom()) {
            g.drawLine(area.x, anchorPoint, area.width + area.x, anchorPoint);
        } else {
            g.drawLine(anchorPoint, area.y, anchorPoint, area.y + area.height);
        }

    }

    private void drawGrid(Graphics g, Rectangle area, List<Tick> ticks) {
        g.setColor(axisViewSettings.getGridColor());
        if (axis.getAxisPosition().isTopOrBottom()) {
            for (Tick tick : ticks) {
                g.drawLine(axis.valueToPoint(tick.getValue(), area), area.y, axis.valueToPoint(tick.getValue(), area), area.y + area.height);
            }
        } else {

        }

    }

    private TickProvider getTickProvider(Graphics g, Rectangle area) {
        TickProvider tickProvider = axis.getTicksProvider(area);

        if (tickProvider.getTickInterval().isNaN()) {
            tickProvider.setTickPixelInterval(axisViewSettings.getTickPixelInterval());
        }

        List<Tick> ticks = tickProvider.getTicks();

        int maxTickSize = getMaxTickSize(g, ticks);

        if (ticks.size() > 1) {

            if ((maxTickSize + axisViewSettings.getMinLabelSpace()) > tickProvider.getTickPixelInterval()) {
                tickProvider.setMinTickPixelInterval(maxTickSize + axisViewSettings.getMinLabelSpace());
            }
        }

        return tickProvider;

    }

    private void drawTicks(Graphics g, Rectangle area, int anchorPoint, List<Tick> ticks) {
        g.setColor(axisViewSettings.getAxisColor());
        for (Tick tick : ticks) {
            int tickPoint = axis.valueToPoint(tick.getValue(), area);
            drawLabel(g, anchorPoint, tickPoint, tick.getLabel());
            drawTick(g, anchorPoint, tickPoint);
        }

    }

    private void drawMinorGrid(Graphics g, Rectangle area, List<Double> minorTicks) {
        g.setColor(axisViewSettings.getMinorGridColor());
        for (Double tick : minorTicks) {
            g.drawLine(axis.valueToPoint(tick, area), area.y, axis.valueToPoint(tick, area), area.y + area.height);
        }
    }

    private void drawTick(Graphics g, int anchorPoint, int tickPoint) {
        if (axis.getAxisPosition().isTopOrBottom()) {
            g.drawLine(tickPoint, anchorPoint + axisViewSettings.getTickSize() / 2, tickPoint, anchorPoint - axisViewSettings.getTickSize() / 2);
        } else {
            g.drawLine(anchorPoint, tickPoint, anchorPoint + axisViewSettings.getTickSize(), tickPoint);
        }
    }

    private void drawLabel(Graphics g, int anchorPoint, int tickPoint, String label) {
        if (axis.getAxisPosition().isTopOrBottom()) {
            g.drawString(label, tickPoint - getLabelWidth(g, label) / 2, anchorPoint - axisViewSettings.getTickSize() / 2 - tickLabelPadding);
        } else {
            //g.drawRect(anchorPoint - getLabelWidth(g,label) - tickLabelPadding,tickPoint-getLabelHeight(g,label),getLabelWidth(g,label),getLabelHeight(g,label));
            g.drawString(label, anchorPoint - getLabelWidth(g, label) - tickLabelPadding, tickPoint + getLabelHeight(g, label) / 2);
        }
    }

    private int getLabelWidth(Graphics g, String label) {
        return g.getFontMetrics().stringWidth(label);
    }

    private int getLabelHeight(Graphics g, String label) {
        // return (int)(g.getFontMetrics().getStringBounds(label,(Graphics2D)(g)).getHeight());
        TextLayout layout = new TextLayout(label, g.getFont(), ((Graphics2D) g).getFontRenderContext());
        Rectangle2D labelBounds = layout.getBounds();
        return (int) labelBounds.getHeight();
    }

    private int getMaxTickSize(Graphics g, List<Tick> ticks) {
        if (axis.getAxisPosition().isTopOrBottom()) {
            int maxSize = 0;
            for (Tick tick : ticks) {
                maxSize = Math.max(maxSize, getLabelWidth(g, tick.getLabel()));
            }
            return maxSize;
        }
        return g.getFontMetrics().getHeight();
    }
}
