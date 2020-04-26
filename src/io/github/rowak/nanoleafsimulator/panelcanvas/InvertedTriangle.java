package io.github.rowak.nanoleafsimulator.panelcanvas;
import java.awt.Polygon;

public class InvertedTriangle extends Polygon
{
	public InvertedTriangle()
	{
//		// top-left
//		addPoint(-55, -15);
//		addPoint(-50, -25);
//		// top-right
//		addPoint(50, -25);
//		addPoint(55, -15);
//		// bottom
//		addPoint(5, 80);
//		addPoint(-5, 80);
		
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
