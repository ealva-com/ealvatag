# Jaudiotagger

[![Coverage Status](https://coveralls.io/repos/ijabz/jaudiotagger/badge.svg?branch=master&service=bitbucket)](https://coveralls.io/bitbucket/ijabz/jaudiotagger?branch=master)
[![Build Status](https://drone.io/bitbucket.org/ijabz/jaudiotagger/status.png)](https://drone.io/bitbucket.org/ijabz/jaudiotagger/latest)

*Jaudiotagger* is a Java API for audio metatagging. Both a common API and format
specific APIs are available, currently supports reading and writing metadata for:

- Mp3
- Flac
- OggVorbis
- Mp4
- Aiff
- Wav
- Wma
- Dsf

The main project page is http://www.jthink.net/jaudiotagger/ and you can contact the main developer via email:paultaylor@jthink.net

## Requirements

*Jaudiotagger* requires Java 1.8 for a full build and install, but the code is Java 1.7 compatible
(You can run mvn package successfully with Java 1.7)

## Contributing

*Jaudiotagger* welcomes contributors, if you make an improvement or bug fix we are
very likely to merge it back into the master branch with a minimum of fuss.
If you can't contribute code but would like to support this project please consider
making a donation—donations can be made at
[here](http://www.jthink.net/jaudiotagger/donate.jsp).

## Build

Build is with [Maven](http://maven.apache.org).

- `pom.xml` : Maven build file

Directory structure as follows:

### Under source control

- `src`                  : source code directory
- `srctest`              : source test code directory
- `www`                  : java doc directory
- `testdata`             : test files for use by the junit tests, not all tests are included in the distribution because of copyright
- `target`               : contains the `jaudiotagger***.jar` built from maven

### IDE files

- `jaudiotagger.iml`     : JetBrains Intellij Module
- `jaudiotagger.ipr`     : JetBrains Intellij Project

### License

- `license.txt` : license file
 
 
### Build details

Run

    mvn install

to compile, test, build javadocs and install into your local repository.

Run

    mvn site

to generate a website for *Jaudiotagger* including code coverage reports,
they will be found in `target/site/index.html`.

Your test coverage can be seen at `target/site/cobertura/index.html`.

Periodically we upload latest Code Coverage to:
http://www.jthink.net/jaudiotagger/maven/cobertura/index.html

Jaudiotagger uses http://drone.io for continuous integration, the latest artifacts can be downloaded from

https://drone.io/bitbucket.org/ijabz/jaudiotagger/files

To use in your own project starting with Jaudiotagger 2.2.3 as final versions are
released they are now available in a maven repository on bintray.

I.e update your `pom.xml` as follows to use release 2.2.3:

    <repositories>
        <repository>
            <id>jaudiotagger-repository</id>
            <url>https://dl.bintray.com/ijabz/maven</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>net.jthink</groupId>
            <artifactId>jaudiotagger</artifactId>
            <version>2.2.3</version>
        </dependency>
    </dependencies>
