package jts;

import org.locationtech.jts.geom.GeometryCollection;

/**
 * Demonstrate edge case in ConformingDelaunayTriangulationBuilder.
 * Check effect of varying <code>tolerance</code>.
 * <p>
 * Windows:
 * <pre>
 * mvn clean install & j --source 25 -ea src/main/java/jts/Tolerance.java  > tolerance.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-16
 */
public final class Tolerance {

  //--------------------------------------------------------------------
  private static final Util util = Util.make(1775201860794L);
  //--------------------------------------------------------------------
  /** Giving 3 points and no constraints to
   * ConformingDelaunayTriangulationBuilder produces the expected
   * single triangle.
   */

  private static final void threePoints (final double tolerance) {
    final GeometryCollection points =
      (GeometryCollection)
        util.readWKT(
          "GEOMETRYCOLLECTION(" +
            "  POINT (-221.72957795130824 -26.56505117707799)," +
            "  POINT (-149.72957795130824 -26.56505117707799)," +
            "  POINT (0 -90))");
    util.checkCdtb( "threePoints",
                    points, null,
                   tolerance, 1); }
  //--------------------------------------------------------------------
  /** Giving 3 points and the corresponding 3 edges as constraints to
   * ConformingDelaunayTriangulationBuilder
   * produces 3 triangles, one of which is singular.
   */

  private static final void threePointsEdges (final double tolerance) {
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
    util.checkCdtb( "threePointsEdges",
                    points, edges,
                    tolerance, 1); }


  //--------------------------------------------------------------------
  /** Giving 3 edges which are the sides of a triangle as both sites and
   * constraints to
   * ConformingDelaunayTriangulationBuilder
   * produces 3 triangles, one of which is singular.
   */

  private static final void threeEdges (final double tolerance) {
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
    util.checkCdtb( "threeEdges",
                    edges, edges,
                    tolerance, 1); }

  //--------------------------------------------------------------------
  /** Giving a Polygon which is a triangle as both sites and
   * constraints to
   * ConformingDelaunayTriangulationBuilder
   * produces 3 triangles, one of which is singular.
   */

  private static final void oneTriangle (final double tolerance) {
    final GeometryCollection triangle =
      (GeometryCollection)
        util.readWKT(
          """
          GEOMETRYCOLLECTION (
            POLYGON ((-221.72957795130824 -26.56505117707799,
                      0 -90,
                      -149.72957795130824 -26.56505117707799,
                      -221.72957795130824 -26.56505117707799)))
          """);
    util.checkCdtb( "oneTriangle",
                    triangle, triangle,
                    tolerance, 1); }

  //--------------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final void main (final String[] args) {
    for (int i = 0; i < 8; i++) {
      final double tolerance = Double.parseDouble("1.0e-" + i);
      Tolerance.threePoints(tolerance);
      Tolerance.threePointsEdges(tolerance);
      Tolerance.threeEdges(tolerance);
      Tolerance.oneTriangle(tolerance); } }

  //--------------------------------------------------------------------
  // disabled constructor
  //--------------------------------------------------------------------
  private Tolerance () {
    throw new UnsupportedOperationException(
      "Can't instantiate " + getClass()); }
  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

