/*
 * Copyright (c) 2016 Martin Davis.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v20.html
 * and the Eclipse Distribution License is available at
 *
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package jts.random;

import org.locationtech.jts.algorithm.locate.IndexedPointInAreaLocator;
import org.locationtech.jts.algorithm.locate.PointOnGeometryLocator;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.shape.GeometricShapeBuilder;

import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Creates random point sets contained in a 
 * region defined by either a rectangular or a polygonal extent. 
 * 
 * @author mbdavis
 *
 * add setRandomGenerator() method .
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2026-04-14
 */
public class RandomPointsBuilder 
extends GeometricShapeBuilder
{
  private RandomGenerator randomGenerator;
  /**
   * Set the pseudo-random number generator used to generate coordinates.
   *
   * @param prng instance of java.util.RandomGenerator
   */
  public void setRandomGenerator(RandomGenerator prng)
  {
    randomGenerator = prng;
  }

  protected Geometry maskPoly = null;
  private PointOnGeometryLocator extentLocator;

  /**
   * Create a shape factory which will create shapes using the default
   * {@link GeometryFactory}.
   */
  public RandomPointsBuilder ()
  {
    super(new GeometryFactory());
    randomGenerator = new Random();
  }

  /**
   * Create a shape factory which will create shapes using the given
   * {@link GeometryFactory}.
   *
   * @param geomFact the factory to use
   */
  public RandomPointsBuilder (GeometryFactory geomFact)
  {
  	super(geomFact);
    randomGenerator = RandomGenerator.getDefault();
    // or, to be more consistent with existing code:
    // randomGenerator = new Random();
  }

  /**
   * Sets a polygonal mask.
   *
   * @param mask
   * @throws IllegalArgumentException if the mask is not polygonal
   */
  public void setExtent(Geometry mask)
  {
    if (! (mask instanceof Polygonal))
      throw new IllegalArgumentException("Only polygonal extents are supported");
    this.maskPoly = mask;
    setExtent(mask.getEnvelopeInternal());
    extentLocator = new IndexedPointInAreaLocator(mask);
  }

  public Geometry getGeometry()
  {
  	Coordinate[] pts = new Coordinate[numPts];
  	int i = 0;
  	while (i < numPts) {
  		Coordinate p = createRandomCoord(getExtent());
  		if (extentLocator != null && ! isInExtent(p))
  			continue;
  		pts[i++] = p;
  	}
  	return geomFactory.createMultiPointFromCoords(pts);
  }
  
  protected boolean isInExtent(Coordinate p)
  {
  	if (extentLocator != null) 
  		return extentLocator.locate(p) != Location.EXTERIOR;
  	return getExtent().contains(p);
  }
  
  protected Coordinate createCoord(double x, double y)
  {
  	Coordinate pt = new Coordinate(x, y);
  	geomFactory.getPrecisionModel().makePrecise(pt);
    return pt;
  }
  
  protected Coordinate createRandomCoord(Envelope env)
  {
    double x = env.getMinX() + env.getWidth() * randomGenerator.nextDouble();
    double y = env.getMinY() + env.getHeight() * randomGenerator.nextDouble();
    return createCoord(x, y);
  }

}
