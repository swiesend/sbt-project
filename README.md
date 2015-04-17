# sbt-project

This is a basic sbt project with **preconfigured dependencies** for:

* [Logback](http://logback.qos.ch/)
* [ScalaTest](http://www.scalatest.org/)
* [Config](https://github.com/typesafehub/config)
* [Akka actors](http://akka.io/)
* [Play](https://www.playframework.com/)
* [Slick](http://slick.typesafe.com/)

local **sbt plugins** support for:

* [Eclipse](https://github.com/typesafehub/sbteclipse)
* [IntelliJ](https://github.com/mpeltonen/sbt-idea)
* [Play](https://www.playframework.com/)


## Source code

The source code is the at standard location for sbt */src/main* and */src/test*. If you add Play nature the sources are changing to */app* and */test*.

## Configuration File

The *application.conf* is located to *./conf* and not to */src/main/resources*. 

## Logback configuration

The *logback.xml*is located to */src/main/resources*.





