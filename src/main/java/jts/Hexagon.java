package jts;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;

/**
 * Demonstrate edge case in ConformingDelaunayTriangulationBuilder.
 * Windows:
 * <pre>
 * mvn install & j --source 25 -ea src/main/java/jts/Hexagon.java
 * </pre>
 * <p>
 * TODO: convert to unit test?
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-17
 */
public final class Hexagon {

  //--------------------------------------------------------------------
  private static final JTS jts = JTS.make(1776203860794L);
  //--------------------------------------------------------------------

  private static final void emptyHexagon (final double tolerance) {
    final Geometry hexagon = jts.hexagon();
    jts.checkCdtb(
      "emptyHexagon",
      hexagon,hexagon,tolerance,4); }

  private static final void triangulatedHexagon (final double tolerance) {
    final Geometry triangles = jts.readWKT(
      """
      GEOMETRYCOLLECTION (
        POLYGON ((-0.5000000000000001 0.8660254037844386, 
                  -1 0, 
                   0.5000000000000001 0.8660254037844386, 
                  -0.5000000000000001 0.8660254037844386)),
        POLYGON ((0.5000000000000001 0.8660254037844386,
                  -1 0,
                   0.5000000000000001 -0.8660254037844386, 
                   0.5000000000000001 0.8660254037844386)),
        POLYGON ((0.5000000000000001 0.8660254037844386, 
                  0.5000000000000001 -0.8660254037844386, 
                  1 0, 
                  0.5000000000000001 0.8660254037844386)),
        POLYGON ((0.5000000000000001 -0.8660254037844386, 
                  -1 0, 
                  -0.5000000000000001 -0.8660254037844386,
                   0.5000000000000001 -0.8660254037844386)))
      """
                                          );
    jts.checkCdtb(
      "triangulatedHexagon",
      triangles,triangles,tolerance,4); }

  private static final void hulledHexagon (final double tolerance) {
    final Geometry sites = jts.readWKT(
      """
      GEOMETRYCOLLECTION(
        POINT (-0.5000000000000001 0.86602540378443869),
        POINT ( 1 0),
        POINT (-0.5000000000000001 -0.86602540378443869))
        """
                                      );
    final Geometry constraints = jts.readWKT(
      """
      GEOMETRYCOLLECTION (
        LINESTRING (0.5000000000000001 0.8660254037844386, -1 0), 
        LINESTRING (-1 0, 0.5000000000000001 -0.8660254037844386), 
        LINESTRING (0.5000000000000001 -0.8660254037844386, 
                    0.5000000000000001 0.8660254037844386))
      """);
    jts.checkCdtb(
      "hulledHexagon",
      sites,constraints,tolerance,4); }

  //--------------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final void main (final String[] args) {
   // Hexagon.emptyHexagon(0.0);
    //Hexagon.triangulatedHexagon(0.0);
    Hexagon.hulledHexagon(0.0);
//    for (int i = 0; i < 8; i++) {
//      final double tolerance = Double.parseDouble("1.0e-" + i);
//      Hexagon.hexagon(tolerance); }
  }

  //--------------------------------------------------------------------
  // disabled constructor
  //--------------------------------------------------------------------
  private Hexagon () {
    throw new UnsupportedOperationException(
      "Can't instantiate " + getClass());
  }
  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

