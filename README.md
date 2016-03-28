jetty-limits
============

Testing Camel-Jetty component as proxy/bridge to another endpoint

### Build

    mvn clean install

### Deploy [JBossFuse 6.2.x]

    features:install camel-http4
    install -s mvn:com.playground.camel/jetty-limits-server/1.0.0-SNAPSHOT
    install -s mvn:com.playground.camel/jetty-limits-bridge/1.0.0-SNAPSHOT

### Test

Run the test in the bridge module: com.playground.camel.jetty.limits.test.BridgeTest

Bridge:

    http://localhost:7071/bridgeWIthJetty
=> IndexOutOfBoundsException for large GET/POST requests

    http://localhost:7071/bridgeWIthHttp4
=> 200 OK!

Server:

    http://localhost:7070/serviceX
=> 200 OK!
