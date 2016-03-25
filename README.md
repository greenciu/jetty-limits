jetty-limits
============

Testing Camel-Jetty component as proxy/bridge to another endpoint

### Build

    mvn clean install

### Deploy [Karaf]

    install -s mvn:com.playground.camel/jetty-limits-server/1.0.0-SNAPSHOT
    install -s mvn:com.playground.camel/jetty-limits-bridge/1.0.0-SNAPSHOT

### Test

Bridge:

    http://localhost:7071/limits
=> IndexOutOfBoundsException for large GET/POST requests

Server:

    http://localhost:7070/serviceX
=> 200 OK!
