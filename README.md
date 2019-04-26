Mouse Mover
===========

Very simple application that automatically moves mouse in screen every 5 seconds.
This is useful if you want to prevent computer to lock screen or run screen saver.
This movement activity is suppressed for 30 seconds if user moves mouse manually.

Multi-screen configuration is supported.

By default mouse is moving to the right direction by 5 pixels. 
In case if cursor reaches edge of current screen then this motion changes direction. 

Build
-----

To build application you have to have installed JDK 8 or higher and Maven 3.3 or higher:

    mvn clean package

Run
---

To run this app you have to have installed JDK or JRE 8 or higher:

    java -jar target/mouse-mover.jar

To run app with logging just put path to [debug.properties](log/debug.properties) file 
to `java.util.logging.config.file` Java system variable, e.g:

    java -Djava.util.logging.config.file=log/debug.properties -jar target/mouse-mover.jar 
