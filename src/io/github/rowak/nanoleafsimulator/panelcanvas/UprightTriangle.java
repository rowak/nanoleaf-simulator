package io.github.rowak.nanoleafsimulator.panelcanvas;
import java.awt.Polygon;

public class UprightTriangle extends Polygon
{
	public UprightTriangle()
	{
//		// top
//		addPoint(7, -60);
//		addPoint(-7, -60);
//		// bottom-left
//		addPoint(-60, 40);
//		addPoint(-55, 50);
//		// bottom-right
//		addPoint(55, 50);
//		addPoint(60, 40);
		
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
