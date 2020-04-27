package io.github.rowak.nanoleafsimulator.panelcanvas;
import java.awt.Polygon;

public class InvertedTriangle extends Polygon
{
	public InvertedTriangle()
	{
		// top-left
		addPoint(-59, -23);
		addPoint(-51, -37);
		// top-right
		addPoint(51, -37);
		addPoint(59, -23);
		// bottom
		addPoint(7, 64);
		addPoint(-7, 64);
	}
}
