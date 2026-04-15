package tinfour;

import jts.Util;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;

/**
 * Check conformal delaunay triangulation using Tinfour.
 * <pre>
 * mvn clean install & j --source 25 -ea src/main/java/tinfour/RandomCDT.java
 * </pre>
 * <p>
 * TODO: convert to unit test?
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-14"
 */
public final class RandomCDT {

  //--------------------------------------------------------------------

  private static final Util util =
    Util.make(0.0,2776201885686L);

  //--------------------------------------------------------------------

  @SuppressWarnings("SameParameterValue")
  private static final void randomDelaunay (final int npoints) {
    final GeometryCollection points = util.randomPoints(npoints);
    final GeometryCollection dtbTriangles = util.dtb(points);
    final int nDtbTriangles = dtbTriangles.getNumGeometries();
    System.out.println("dtbTriangles:");
    util.printAreas(dtbTriangles);
    util.writeWKT(dtbTriangles, "out/wkt/RandomCDTB/dtbTriangles.wkt");
    final GeometryCollection cdtbTriangles = util.cdtb(points,dtbTriangles);
    util.writeWKT(cdtbTriangles, "out/wkt/RandomCDTB/cdtbTriangles.wkt");
    final int nCdtbTriangles = cdtbTriangles.getNumGeometries();
    System.out.println("cdtbTriangles:" + nCdtbTriangles);
    for (int i = 0; i < nCdtbTriangles; ++i) {
      final Geometry triangle = cdtbTriangles.getGeometryN(i);
      final double area = triangle.getArea();
      System.out.println(i + ": " + area);
      if (0.0 == area) { System.out.println("Singular:" + triangle); } }
    assert (nDtbTriangles == nCdtbTriangles)
      : "n dtb != ncdtb"; }

  //--------------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final void main (final String[] args) {
    RandomCDT.randomDelaunay(4); }

  //--------------------------------------------------------------------
  // disabled constructor
  //--------------------------------------------------------------------

  private RandomCDT () {
    throw new UnsupportedOperationException(
      "Can't instantiate " + getClass()); }

  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

