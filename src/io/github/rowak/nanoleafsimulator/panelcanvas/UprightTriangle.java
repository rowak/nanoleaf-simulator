package io.github.rowak.nanoleafsimulator.panelcanvas;
import java.awt.Polygon;

public class UprightTriangle extends Polygon
{
	public UprightTriangle()
	{
		// top
		addPoint(7, -64);
		addPoint(-7, -64);
		// bottom-left
		addPoint(-59, 23);
		addPoint(-51, 37);
		// bottom-right
		addPoint(51, 37);
		addPoint(59, 23);
	}
}
