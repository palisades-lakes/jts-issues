package tinfour;

import org.locationtech.jts.geom.*;
import org.tinfour.common.*;
import org.tinfour.demo.utils.TinRenderingUtility;
import org.tinfour.standard.IncrementalTin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Shared Tinfour functions.
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-16"
 */
public final class Tinfour {

  //--------------------------------------------------------------------
  // class methods
  //--------------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final long generateSeed () {
    return System.currentTimeMillis();
  }

  //--------------------------------------------------------------------

//  public static final Coordinate
//  toJTS (final Vertex v) {
//    return new Coordinate(v.getX(), v.getY(), v.getZ());
//  }

//  public static final Polygon
//  toJTS (final SimpleTriangle t,
//         final GeometryFactory factory) {
//    final Coordinate[] coords = new Coordinate[] {
//      toJTS(t.getVertexA()),
//      toJTS(t.getVertexB()),
//      toJTS(t.getVertexC()),
//      //toJTS(t.getVertexA()),
//    };
//    return factory.createPolygon(coords);
//  }

//  public static final GeometryCollection
//  toJTS (final Iterable<SimpleTriangle> triangles,
//         final GeometryFactory factory) {
//    final List<Geometry> polygons = new ArrayList<>();
//    for (final SimpleTriangle t : triangles) {
//      polygons.add(toJTS(t, factory));
//    }
//    return factory.createGeometryCollection(
//      polygons.toArray(new Geometry[0]));
//  }

  //--------------------------------------------------------------------

  public static final Vertex toTinfour (final Coordinate p) {
    return new Vertex(p.getX(), p.getY(), p.getZ());
  }

  public static final Vertex toTinfour (final Point p) {
    return new Vertex(p.getX(), p.getY(), 0.0);
  }

  public static final LinearConstraint toTinfour (final LineString l) {
    final int n = l.getNumPoints();
    final Vertex[] vertices = new Vertex[n];
    for (int i = 0; i < n; i++) {
      vertices[i] = toTinfour(l.getCoordinateN(i)); }
    return new LinearConstraint(Arrays.asList(vertices)); }

  /**
   * Fails if not a collection of Points
   */
  public static final List<Vertex> toSites (
    final GeometryCollection gc) {
    final List<Vertex> tmp = new ArrayList<>();
    for (int i = 0; i < gc.getNumGeometries(); i++) {
      tmp.add(toTinfour((Point) gc.getGeometryN(i))); }
    return Collections.unmodifiableList(tmp); }

  /**
   * Fails if not a collection of LineStrings
   */
  public static final List<IConstraint> toConstraints (
    final GeometryCollection gc) {
    final List<IConstraint> tmp = new ArrayList<>();
    for (int i = 0; i < gc.getNumGeometries(); i++) {
      tmp.add(toTinfour((LineString) gc.getGeometryN(i))); }
    return Collections.unmodifiableList(tmp); }

  //--------------------------------------------------------------------

  public static final IncrementalTin
  conformingDT (final List<Vertex> sites,
                final List<IConstraint> constraints,
                final double estimatedPointSpacing) {
    final IncrementalTin tin = new IncrementalTin(estimatedPointSpacing);
    tin.add(sites, null);
    if ((null != constraints) && (!constraints.isEmpty())) {
      tin.addConstraints(constraints, true); }
    return tin; }

  //--------------------------------------------------------------------

  public static final void checkCdt (final String name,
                                     final List<Vertex> sites,
                                     final List<IConstraint> constraints,
                                     final double estimatedPointSpacing,
                                     final int expectedTriangles) {
    final IncrementalTin tin =
      conformingDT(sites, constraints, estimatedPointSpacing);

    System.out.println("\n" + name + ": " + estimatedPointSpacing);
    final TriangleCount stats = tin.countTriangles();
    final int nTriangles = stats.getCount();
    try {
      TinRenderingUtility.drawTin(
        tin, 1024, 1024,
        new File("out/png/tinfour/" + name + ".png")); }
    catch (final IOException e) { throw new RuntimeException(e); }
    if (nTriangles == expectedTriangles) {
      System.out.println("Success."); }
    else {
      System.out.println("expected != nTriangles: " +
                           expectedTriangles + " != " + nTriangles);
      System.out.println("areas: [" + stats.getAreaMin() +
                           ", " + stats.getAreaMax() + "]");
      if (0.0 == stats.getAreaMin()) {
          System.out.println("Singular!"); }

      for (final SimpleTriangle triangle : tin.triangles()) {
        if (0.0 == triangle.getArea()) {
          System.out.println("Singular:" + triangle); } } } }

  //--------------------------------------------------------------------
  // disabled constructor
  //--------------------------------------------------------------------

  private Tinfour () {
    throw new UnsupportedOperationException(
      "can't construct instances of" + Tinfour.class.getSimpleName());
  }

  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

