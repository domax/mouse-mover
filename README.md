Mouse Mover
===========

Basic application that automatically moves the mouse on the screen (every 5 seconds by default).
This is useful if you want to prevent the computer from locking screen or run screen saver.
This movement activity is suppressed for some time (30 seconds by default) if the user moves the 
mouse manually.

Multi-screen configuration is supported.

By default, the mouse is moving in the right direction by 5 pixels.
After each movement the direction is changed to the opposite. 

You may change the default setting using command line arguments.
To get help about supported arguments, run application with `-h` option.

Build
-----

To build application you have to have installed JDK 8 or higher and Maven 3.3 or higher:

    mvn clean package

Run
---

To run this app you have to have installed JDK or JRE 8 or higher:

    target/mouse-mover.exe

Or:

    java -jar target/mouse-mover.jar

To run app with logging just put path to [debug.properties](log/debug.properties) file
to `java.util.logging.config.file` Java system variable, e.g:

    java -Djava.util.logging.config.file=log/debug.properties -jar target/mouse-mover.jar 

Support
-------

If you want to support me: <br>
[![Buy me a coffee](./buy_me_a_coffee.png)](https://www.paypal.me/domax/3)
