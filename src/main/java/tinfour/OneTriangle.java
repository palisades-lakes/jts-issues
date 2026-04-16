package tinfour;

import jts.JTS;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.tinfour.common.IConstraint;
import org.tinfour.common.Vertex;

import java.util.List;

/**
 *  Compare results from Tinfour to JTS.
 *  <p>>
 * Windows:
 * <pre>
 * mvn clean install & j --source 25 -ea src/main/java/tinfour/OneTriangle.java
 * </pre>
 * <p>
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-16
 */
public final class OneTriangle {

  //--------------------------------------------------------------------
  /** Giving 3 points and the corresponding 3 edges as constraints to
   * ConformingDelaunayTriangulationBuilder
   * produces 3 triangles, one of which is singular.
   */

  private static final void threePointsEdges (final double estimatedPointSpacing) {
    final GeometryFactory factory = new GeometryFactory();
    final GeometryCollection points = (GeometryCollection)
        JTS.readWKT(
          "GEOMETRYCOLLECTION(" +
            "  POINT (-221.72957795130824 -26.56505117707799)," +
            "  POINT (-149.72957795130824 -26.56505117707799)," +
            "  POINT (0 -90))",
          factory);
    final GeometryCollection edges = (GeometryCollection)
        JTS.readWKT(
          """
          GEOMETRYCOLLECTION (
            LINESTRING (-221.72957795130824 -26.56505117707799,
                        -149.72957795130824 -26.56505117707799),
            LINESTRING (-221.72957795130824 -26.56505117707799,
                        0 -90),
            LINESTRING (-149.72957795130824 -26.56505117707799,
                        0 -90))
          """,
          factory);
    final List<Vertex> sites = Tinfour.toSites(points);
    final List<IConstraint> constraints = Tinfour.toConstraints(edges);
    Tinfour.checkCdt( "threePointsEdges",
                    sites, constraints,
                    estimatedPointSpacing, 1); }

  //--------------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final void main (final String[] args) {
    for (int i = 0; i < 8; i++) {
      final double estimatedPointSpacing =
        Double.parseDouble("1.0e-" + i);
      threePointsEdges(estimatedPointSpacing);
    } }

  //--------------------------------------------------------------------
  // disabled constructor
  //--------------------------------------------------------------------
  private OneTriangle () {
    throw new UnsupportedOperationException(
      "Can't instantiate " + getClass());
  }
  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

