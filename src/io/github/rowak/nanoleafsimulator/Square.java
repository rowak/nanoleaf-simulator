package io.github.rowak.nanoleafsimulator;
import java.awt.Polygon;

public class Square extends Polygon
{
	public Square()
	{
		// top-left
		addPoint(0, 0);
		// top-right
		addPoint(90, 0);
		// bottom-right
		addPoint(90, 90);
		// bottom-left
		addPoint(0, 90);
	}
}
