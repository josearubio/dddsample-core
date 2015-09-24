package se.citerus.dddsample.domain.model.cargo;

import junit.framework.TestCase;
import static se.citerus.dddsample.application.util.DateTestUtil.toDate;
import static se.citerus.dddsample.domain.model.location.SampleLocations.*;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;

import java.util.Arrays;

public class RouteSpecificationTest extends TestCase {

  final Voyage hongKongTokyoNewYork = new Voyage.Builder(
    new VoyageNumber("V001"), HONGKONG).
    addMovement(TOKYO, toDate("2009-02-01"), toDate("2009-02-05")).
    addMovement(NEWYORK, toDate("2009-02-06"), toDate("2009-02-10")).
    addMovement(HONGKONG, toDate("2009-02-11"), toDate("2009-02-14")).
    build();

  final Voyage dallasNewYorkChicago = new Voyage.Builder(
    new VoyageNumber("V002"), DALLAS).
    addMovement(NEWYORK, toDate("2009-02-06"), toDate("2009-02-07")).
    addMovement(CHICAGO, toDate("2009-02-12"), toDate("2009-02-20")).
    build();

  // TODO:
  // it shouldn't be possible to create Legs that have load/unload locations
  // and/or dates that don't match the voyage's carrier movements.
  final Itinerary itinerary = new Itinerary(Arrays.asList(
      new Leg(hongKongTokyoNewYork, HONGKONG.unLocode(), NEWYORK.unLocode(),
              toDate("2009-02-01"), toDate("2009-02-10")),
      new Leg(dallasNewYorkChicago, NEWYORK.unLocode(), CHICAGO.unLocode(),
              toDate("2009-02-12"), toDate("2009-02-20")))
  );

  public void testIsSatisfiedBy_Success() {
    RouteSpecification routeSpecification = new RouteSpecification(
      HONGKONG.unLocode(), CHICAGO.unLocode(), toDate("2009-03-01")
    );

    assertTrue(routeSpecification.isSatisfiedBy(itinerary));
  }

  public void testIsSatisfiedBy_WrongOrigin() {
    RouteSpecification routeSpecification = new RouteSpecification(
      HANGZOU.unLocode(), CHICAGO.unLocode(), toDate("2009-03-01")
    );

    assertFalse(routeSpecification.isSatisfiedBy(itinerary));
  }

  public void testIsSatisfiedBy_WrongDestination() {
    RouteSpecification routeSpecification = new RouteSpecification(
      HONGKONG.unLocode(), DALLAS.unLocode(), toDate("2009-03-01")
    );

    assertFalse(routeSpecification.isSatisfiedBy(itinerary));
  }

  public void testIsSatisfiedBy_MissedDeadline() {
    RouteSpecification routeSpecification = new RouteSpecification(
      HONGKONG.unLocode(), CHICAGO.unLocode(), toDate("2009-02-15")
    );

    assertFalse(routeSpecification.isSatisfiedBy(itinerary));
  }

}
