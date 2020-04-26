package io.github.rowak.nanoleafsimulator;
import java.awt.Polygon;

public class InvertedTriangle extends Polygon
{
	public InvertedTriangle()
	{
		// top-left
		addPoint(-55, -15);
		addPoint(-50, -25);
		// top-right
		addPoint(50, -25);
		addPoint(55, -15);
		// bottom
		addPoint(5, 80);
		addPoint(-5, 80);
	}
}
