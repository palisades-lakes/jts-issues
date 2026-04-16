package jts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import jts.random.RandomPointsBuilder;

/**
 * Shared JTS functions.
 *
 * @author palisades dot lakes at gmail dot com
 * @version "2026-04-15"
 */
public final class JTS {

  //--------------------------------------------------------------------
  // class methods
  //--------------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final long generateSeed () {
    return System.currentTimeMillis(); }

  //--------------------------------------------------------------------
  // instance slots
  //--------------------------------------------------------------------

  private final double _tolerance;
  public final double getTolerance () { return _tolerance; }

  private final GeometryFactory _factory;
  public final GeometryFactory getFactory () { return _factory; }

  private final RandomPointsBuilder _randomPointsBuilder;
  public final RandomPointsBuilder getRandomPointsBuilder () {
    return _randomPointsBuilder; }

  //--------------------------------------------------------------------
  // instance methods
  //--------------------------------------------------------------------

  public final GeometryCollection randomPoints (final int npoints,
                                                final double xmin,
                                                final double xmax,
                                                final double ymin,
                                                final double ymax) {
    final RandomPointsBuilder builder =
      getRandomPointsBuilder();
    builder.setExtent(new Envelope(xmin, xmax, ymin, ymax));
    builder.setNumPoints(npoints);
    return (GeometryCollection) builder.getGeometry(); }

  public final GeometryCollection randomPoints (final int npoints) {
    return randomPoints(npoints,-1.0,1.0,-1.0,1.0); }

  //--------------------------------------------------------------------

  public static final Geometry readWKT (final String wkt,
                                        final GeometryFactory factory) {
    try { return new WKTReader(factory).read(wkt); }
    catch (final ParseException e) { throw new RuntimeException(e); } }

  public final Geometry readWKT (final String wkt) {
   return readWKT(wkt,getFactory()); }

  //--------------------------------------------------------------------

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static final void writeWKT (final Geometry g,
                              final String dest) {
    final File f = new File(dest);
    f.getParentFile().mkdirs();
    try (FileWriter w = new FileWriter(f)) {
      new WKTWriter().writeFormatted(g, w); }
    catch (final IOException e) { throw new RuntimeException(e); } }

  //--------------------------------------------------------------------
  public static final void printAreas (final GeometryCollection geometries) {
    final int n = geometries.getNumGeometries();
    System.out.println("n:" + n);
    for (int i = 0; i < n; ++i) {
      final Geometry g = geometries.getGeometryN(i);
      final double area = g.getArea();
      System.out.println(i + ": " + area);
      if (0.0 == area) {
        System.out.println("Singular geometry:" + g); } } }
  //--------------------------------------------------------------------

  public final GeometryCollection dtb (final Geometry sites,
                                       final double tolerance) {
    final DelaunayTriangulationBuilder dtb =
      new DelaunayTriangulationBuilder();
    dtb.setTolerance(tolerance);
    dtb.setSites(sites);
    return (GeometryCollection) dtb.getTriangles(getFactory()); }

  public final GeometryCollection cdtb (final Geometry sites,
                                        final Geometry constraints,
                                        final double tolerance) {
    final ConformingDelaunayTriangulationBuilder cdtb =
      new ConformingDelaunayTriangulationBuilder();
    cdtb.setTolerance(tolerance);
    cdtb.setSites(sites);
    cdtb.setConstraints(constraints);
    return (GeometryCollection) cdtb.getTriangles(getFactory()); }

  //-------------------------------------------------------------------

  public final void checkCdtb (final String name,
                               final Geometry sites,
                               final Geometry constraints,
                               final double tolerance,
                               final int expectedTriangles) {
    final GeometryCollection triangles =
      cdtb(sites,constraints,tolerance);
    System.out.println("\n" + name + ": " + tolerance);
    final int nTriangles = triangles.getNumGeometries();
    if (nTriangles != expectedTriangles) {
      System.out.println("nTriangles != expected: " +
                           expectedTriangles + " != " + nTriangles); }
    for (int i = 0; i < nTriangles; ++i) {
      final Geometry triangle = triangles.getGeometryN(i);
      final double area = triangle.getArea();
      //System.out.println(i + ": " + area);
      if (0.0 == area) {
        System.out.println("Singular:" + triangle); } } }

  //--------------------------------------------------------------------
  // constructor
  //--------------------------------------------------------------------

  private JTS (final GeometryFactory factory,
               final double tolerance,
               final long seed) {
    _factory = factory;
    _tolerance = tolerance;
    _randomPointsBuilder = new RandomPointsBuilder(factory);
    _randomPointsBuilder.setRandomGenerator(new Random(seed)); }

  public static final JTS make (final GeometryFactory factory,
                                final double tolerance,
                                final long seed) {
    return new JTS(factory, tolerance, seed); }

  public static final JTS make (final double tolerance,
                                final long seed) {
    return make(new GeometryFactory(), tolerance,seed); }

  public static final JTS make (final long seed) {
    return make(0.0,seed); }

  //--------------------------------------------------------------------
} // end class
//--------------------------------------------------------------------

