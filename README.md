jclouds Prototype
=================

Prototype to demonstrate accessing Rackspace Cloud files using the jclouds library.

Prerequisites
-------------

* Java 6
* Maven (set up as per https://wiki.bright-interactive.com/display/knowhow/Set+up+Maven+on+your+machine)

Building and Running
--------------------

Substitute a Rackspace Cloud UK username and API key in the command below:

    mvn -Dcom.brightinteractive.jclouds.rscloud.identity=<username> -Dcom.brightinteractive.jclouds.rscloud.credential=<api key> test
