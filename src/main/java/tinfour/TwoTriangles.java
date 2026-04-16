package tinfour;

import jts.JTS;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.tinfour.common.IConstraint;
import org.tinfour.common.Vertex;

import java.util.List;

/**
 * Compare results from Tinfour to JTS.
 * <p>
 * Windows:
 * <pre>
 * mvn install & j --source 25 -ea src/main/java/tinfour/TwoTriangles.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-16
 */
public final class TwoTriangles {

  //--------------------------------------------------------------------
  /** Giving 4 points as sites and the 5 edges of a Delaunay
   * triangulation of those site as constraints to
   * <code>IncrementalTin</code>
   * produces ?.
   */

  private static final void fourPointsFiveEdges (final double estimatedPointSpacing) {
    final GeometryFactory factory = new GeometryFactory();
    final GeometryCollection points = (GeometryCollection)
        JTS.readWKT(
          """
          MULTIPOINT ((-0.5681021283174366 -0.8360713525676495),
                      (-0.3420493618720524 -0.7052562923760732),
                      (-0.6006199815294515 0.2586678428458957),
                      (-0.1177819608189914 0.6301871424709511))
          """,
          factory);
    final GeometryCollection edges = (GeometryCollection)
        JTS.readWKT(
          """
          GEOMETRYCOLLECTION (
          LINESTRING (-0.6006199815294515  0.2586678428458957,
                      -0.5681021283174366 -0.8360713525676495),
          LINESTRING (-0.5681021283174366 -0.8360713525676495,
                      -0.3420493618720524 -0.7052562923760732),
          LINESTRING (-0.3420493618720524 -0.7052562923760732,
                      -0.6006199815294515  0.2586678428458957),
          LINESTRING (-0.3420493618720524 -0.7052562923760732,
                      -0.1177819608189914  0.6301871424709511),
          LINESTRING (-0.1177819608189914  0.6301871424709511,
                      -0.6006199815294515  0.2586678428458957))
          """,
          factory);
    final List<Vertex> sites = Tinfour.toSites(points);
    final List<IConstraint> constraints = Tinfour.toConstraints(edges);
    Tinfour.checkCdt( "fourPointsFiveEdges",
                    sites, constraints,
                    estimatedPointSpacing, 2); }

  //--------------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final void main (final String[] args) {
    for (int i = 0; i < 8; i++) {
      final double estimatedPointSpacing =
        Double.parseDouble("1.0e-" + i);
      fourPointsFiveEdges(estimatedPointSpacing);
    } }

  //--------------------------------------------------------------------
  // disabled constructor
  //--------------------------------------------------------------------
  private TwoTriangles () {
    throw new UnsupportedOperationException(
      "Can't instantiate " + getClass());
  }
  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

