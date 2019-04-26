package com.domax;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MouseMover {

  private static final Logger LOG = Logger.getLogger(MouseMover.class.getName());

  private static final int SHORT_DELAY = 5000;
  private static final int LONG_DELAY = 30000;
  private static final int MOVE_DIST = 5;

  private static final List<Point> MOVES =
      Arrays.asList(
          new Point(MOVE_DIST, 0),
          new Point(-MOVE_DIST, 0),
          new Point(0, MOVE_DIST),
          new Point(0, -MOVE_DIST));

  public static void main(String... args) throws AWTException {
    final Robot robot = new Robot();
    final Point oldLocation = new Point(-1, -1);
    while (true) {
      try {
        final PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        final Point location = new Point(pointerInfo.getLocation());
        final Rectangle bounds = pointerInfo.getDevice().getDefaultConfiguration().getBounds();
        location.translate(-bounds.x, -bounds.y);
        if (location.x < 0) location.x *= -1;
        if (location.y < 0) location.y *= -1;

        if (location.equals(oldLocation)) {
          final Point newLocation =
              MOVES
                  .stream()
                  .map(d -> new Point(bounds.x + location.x + d.x, bounds.y + location.y + d.y))
                  .filter(bounds::contains)
                  .findAny()
                  .orElseThrow(() -> new IllegalStateException("Cannot compute new location"));

          robot.mouseMove(newLocation.x, newLocation.y);
          LOG.fine("New " + newLocation + " in " + bounds);
          newLocation.translate(-bounds.x, -bounds.y);
          oldLocation.setLocation(newLocation);
          Thread.sleep(SHORT_DELAY);
        } else {
          LOG.fine("Old " + location + " in " + bounds);
          oldLocation.setLocation(location);
          Thread.sleep(LONG_DELAY);
        }
      } catch (Exception e) {
        LOG.log(Level.SEVERE, e.getMessage(), e);
        break;
      }
    }
  }
}
