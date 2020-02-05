package com.domax;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.stream.Stream;
import lombok.extern.java.Log;
import lombok.val;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

@Log
public class MouseMover {

  public static void main(String... args) throws AWTException {
    val parser =
        ArgumentParsers.newFor(MouseMover.class.getSimpleName())
            .build()
            .defaultHelp(true)
            .description("Very simple application that automatically moves mouse in screen");
    val argShortDelay =
        parser
            .addArgument("-s", "--short-delay")
            .type(Integer.class)
            .setDefault(5)
            .help("Short delay in seconds between moves");
    val argLongDelay =
        parser
            .addArgument("-l", "--long-delay")
            .type(Integer.class)
            .setDefault(30)
            .help("Long delay in seconds before first move after mouse inactivity");
    val argDistance =
        parser
            .addArgument("-d", "--distance")
            .type(Integer.class)
            .setDefault(5)
            .help("Distance in pixels of mouse moving");
    int shortDelay, longDelay, distance;
    try {
      val ns = parser.parseArgs(args);
      shortDelay = ns.getInt("short_delay");
      longDelay = ns.getInt("long_delay");
      distance = ns.getInt("distance");
      if (shortDelay < 1)
        throw new ArgumentParserException(
            "Short delay cannot be less than 1", parser, argShortDelay);
      if (longDelay < 1)
        throw new ArgumentParserException("Long delay cannot be less than 1", parser, argLongDelay);
      if (shortDelay > longDelay)
        throw new ArgumentParserException("Short delay cannot be less than long one", parser);
      if (distance == 0)
        throw new ArgumentParserException("Distance cannot be 0", parser, argDistance);
      log.info(
          String.format(
              "Short Delay: %d; Long Delay: %d; Distance: %d", shortDelay, longDelay, distance));
    } catch (ArgumentParserException e) {
      parser.handleError(e);
      System.exit(1);
      return;
    }

    val robot = new Robot();
    val oldLocation = new Point(-1, -1);
    while (true) {
      try {
        val pointerInfo = MouseInfo.getPointerInfo();
        val location = new Point(pointerInfo.getLocation());
        val bounds = pointerInfo.getDevice().getDefaultConfiguration().getBounds();
        location.translate(-bounds.x, -bounds.y);
        if (location.x < 0) location.x *= -1;
        if (location.y < 0) location.y *= -1;

        if (location.equals(oldLocation)) {
          val newLocation =
              Stream.of(new Point(distance, 0), new Point(-distance, 0))
                  .map(d -> new Point(bounds.x + location.x + d.x, bounds.y + location.y + d.y))
                  .filter(bounds::contains)
                  .findAny()
                  .orElseThrow(() -> new IllegalStateException("Cannot compute new location"));
          robot.mouseMove(newLocation.x, newLocation.y);
          log.fine("New " + newLocation + " in " + bounds);
          newLocation.translate(-bounds.x, -bounds.y);
          oldLocation.setLocation(newLocation);
          distance = -distance;
          Thread.sleep(shortDelay * 1000);
        } else {
          log.fine("Old " + location + " in " + bounds);
          oldLocation.setLocation(location);
          Thread.sleep(longDelay * 1000);
        }
      } catch (Exception e) {
        log.log(Level.SEVERE, e.getMessage(), e);
        break;
      }
    }
  }
}
