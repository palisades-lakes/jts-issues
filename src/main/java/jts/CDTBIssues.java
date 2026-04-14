package jts;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;

/**
 * Demonstrate edge case in ConformingDelaunayTriangulationBuilder.
 * Windows:
 * <pre>
 * mvn clean install & j --source 25 -ea src/main/java/jts/CDTBIssues.java
 * </pre>
 * <p>
 * TODO: convert to unit test?
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-14
 */
public final class CDTBIssues {

  //--------------------------------------------------------------------
  private static final Util util = Util.make(1776201860794L);
  //--------------------------------------------------------------------
  /** Giving 3 points and no constraints to
   * ConformingDelaunayTriangulationBuilder produces the expected
   * single triangle.
   */

  private static final void threePoints () {
    final GeometryCollection points =
      (GeometryCollection)
        util.readWKT(
          "GEOMETRYCOLLECTION(" +
            "  POINT (-221.72957795130824 -26.56505117707799)," +
            "  POINT (-149.72957795130824 -26.56505117707799)," +
            "  POINT (0 -90))");
    final ConformingDelaunayTriangulationBuilder cdtb =
      new ConformingDelaunayTriangulationBuilder();
    cdtb.setTolerance(util.getTolerance());
    cdtb.setSites(points);
    final GeometryCollection triangles =
      (GeometryCollection) cdtb.getTriangles(util.getFactory());
    util.writeWKT(triangles, "out/wkt/threePoints.wkt");
    final int nTriangles = triangles.getNumGeometries();
    System.out.println("\nthreePoints:");
    for (int i = 0; i < nTriangles; ++i) {
      final Geometry triangle = triangles.getGeometryN(i);
      final double area = triangle.getArea();
      System.out.println(i + ": " + area);
      if (0.0 == area) {
        System.out.println("Singular:" + triangle); } }
    assert (1 == triangles.getNumGeometries())
      : "n geometries > 1: " + triangles.getNumGeometries();
    assert (0.0 < triangles.getGeometryN(0).getArea())
      : "singular triangle: " + triangles.getGeometryN(0);
  }
  //--------------------------------------------------------------------
  /** Giving 3 points and the corresponding 3 edges as constraints to
   * ConformingDelaunayTriangulationBuilder
   * produces 3 triangles, one of which is singular.
   */

  private static final void threePointsEdges () {
    final GeometryCollection points =
      (GeometryCollection)
        util.readWKT(
          """
          GEOMETRYCOLLECTION(
                      POINT (-221.72957795130824 -26.56505117707799),
                      POINT (-149.72957795130824 -26.56505117707799),
                      POINT (0 -90))
          """);
    final GeometryCollection edges =
      (GeometryCollection)
        util.readWKT(
          """
          GEOMETRYCOLLECTION (
            LINESTRING (-221.72957795130824 -26.56505117707799,
                        -149.72957795130824 -26.56505117707799),
            LINESTRING (-221.72957795130824 -26.56505117707799,
                        0 -90),
            LINESTRING (-149.72957795130824 -26.56505117707799,
                        0 -90))
          """);
    final ConformingDelaunayTriangulationBuilder cdtb =
      new ConformingDelaunayTriangulationBuilder();
    cdtb.setTolerance(util.getTolerance());
    cdtb.setSites(points);
    cdtb.setConstraints(edges);
    final GeometryCollection triangles =
      (GeometryCollection) cdtb.getTriangles(util.getFactory());
    util.writeWKT(triangles, "out/wkt/threePointsEdges.wkt");
    final int nTriangles = triangles.getNumGeometries();
    assert (3 == nTriangles)
      : "n geometries != 3: " + nTriangles;
    System.out.println("\nthreePointsEdges:");
    for (int i = 0; i < nTriangles; ++i) {
      final Geometry triangle = triangles.getGeometryN(i);
      final double area = triangle.getArea();
      System.out.println(i + ": " + area);
      if (0.0 == area) {
        System.out.println("Singular:" + triangle); } } }

  //--------------------------------------------------------------------
  /** Giving 3 edges which are the sides of a triangle as both sites and
   * constraints to
   * ConformingDelaunayTriangulationBuilder
   * produces 3 triangles, one of which is singular.
   */

  private static final void threeEdges () {
    final GeometryCollection edges =
      (GeometryCollection)
        util.readWKT(
          """
          GEOMETRYCOLLECTION (
            LINESTRING (-221.72957795130824 -26.56505117707799,
                        -149.72957795130824 -26.56505117707799),
            LINESTRING (-221.72957795130824 -26.56505117707799,
                        0 -90),
            LINESTRING (-149.72957795130824 -26.56505117707799,
                        0 -90))
          """);
    final ConformingDelaunayTriangulationBuilder cdtb =
      new ConformingDelaunayTriangulationBuilder();
    cdtb.setTolerance(util.getTolerance());
    cdtb.setSites(edges);
    cdtb.setConstraints(edges);
    final GeometryCollection triangles =
      (GeometryCollection) cdtb.getTriangles(util.getFactory());
    util.writeWKT(triangles, "out/wkt/threeEdges.wkt");
    final int nTriangles = triangles.getNumGeometries();
    assert (3 == nTriangles)
      : "n geometries != 3: " + nTriangles;
    System.out.println("\nthreeEdges:");
    for (int i = 0; i < nTriangles; ++i) {
      final Geometry triangle = triangles.getGeometryN(i);
      final double area = triangle.getArea();
      System.out.println(i + ": " + area);
      if (0.0 == area) {
        System.out.println("Singular:" + triangle); } } }
  //--------------------------------------------------------------------
  /** Giving a Polygon which is a triangle as both sites and
   * constraints to
   * ConformingDelaunayTriangulationBuilder
   * produces 3 triangles, one of which is singular.
   */

  private static final void oneTriangle () {
    final GeometryCollection polygon =
      (GeometryCollection)
        util.readWKT(
          """
          GEOMETRYCOLLECTION (
            POLYGON ((-221.72957795130824 -26.56505117707799,
                      0 -90,
                      -149.72957795130824 -26.56505117707799,
                      -221.72957795130824 -26.56505117707799)))
          """);
    final ConformingDelaunayTriangulationBuilder cdtb =
      new ConformingDelaunayTriangulationBuilder();
    cdtb.setTolerance(util.getTolerance());
    cdtb.setSites(polygon);
    cdtb.setConstraints(polygon);
    final GeometryCollection triangles =
      (GeometryCollection) cdtb.getTriangles(util.getFactory());
    util.writeWKT(triangles, "out/wkt/oneTriangle.wkt");
    final int nTriangles = triangles.getNumGeometries();
    assert (3 == nTriangles)
      : "n geometries != 3: " + nTriangles;
    System.out.println("\noneTriangle:");
    for (int i = 0; i < nTriangles; ++i) {
      final Geometry triangle = triangles.getGeometryN(i);
      final double area = triangle.getArea();
      System.out.println(i + ": " + area);
      if (0.0 == area) {
        System.out.println("Singular:" + triangle); } } }

  //--------------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final void main (final String[] args) {
    CDTBIssues.threePoints();
    CDTBIssues.threePointsEdges();
    CDTBIssues.threeEdges();
    CDTBIssues.oneTriangle();
  }

  //--------------------------------------------------------------------
  // disabled constructor
  //--------------------------------------------------------------------
  private CDTBIssues () {
    throw new UnsupportedOperationException(
      "Can't instantiate " + getClass());
  }
  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

