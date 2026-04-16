package jts;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;

/**
 * Check ConformingDelaunayTriangulationBuilder with random
 * Delaunay triangulations as input.
 * Windows:
 * <pre>
 * mvn clean install & j --source 25 -ea src/main/java/jts/RandomCDTB.java
 * </pre>
 * <p>
 * TODO: convert to unit test?
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-16"
 */
public final class RandomCDTB {

  //--------------------------------------------------------------------

  private static final JTS util =
    JTS.make(0.0, 2776201805686L);

  //--------------------------------------------------------------------

  @SuppressWarnings("SameParameterValue")
  private static final void randomDelaunay (final int npoints) {
    final GeometryCollection points = util.randomPoints(npoints);
    JTS.writeWKT(points, "out/wkt/RandomCDTB/points.wkt");
    final GeometryCollection dtbTriangles = util.dtb(points,0.0);
    final int nDtbTriangles = dtbTriangles.getNumGeometries();
    System.out.println("dtbTriangles:");
    JTS.printAreas(dtbTriangles);
    JTS.writeWKT(dtbTriangles, "out/wkt/RandomCDTB/dtbTriangles.wkt");
    final GeometryCollection cdtbTriangles = util.cdtb(points,dtbTriangles,0.0);
    JTS.writeWKT(cdtbTriangles, "out/wkt/RandomCDTB/cdtbTriangles.wkt");
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
    RandomCDTB.randomDelaunay(4); }

  //--------------------------------------------------------------------
  // disabled constructor
  //--------------------------------------------------------------------

  private RandomCDTB () {
    throw new UnsupportedOperationException(
      "Can't instantiate " + getClass()); }

  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

