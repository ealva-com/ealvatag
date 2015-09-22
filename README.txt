JAudiotagger is a JAVA API for audio metatagging. Both a common API and format specific APIS are available, currently supports reading and writing
metadata for:
 Mp3, Flac, OggVorbis, Mp4, Aiff, Wav and Wma.

Jaudiotagger reuires Java 1.7 or later

Jaudiotagger welcomes contributors, if you make an improvement or bugf fix we are very likely to merge it back into the master branch with a minimum of fuss.
If you can't contribute code but would like to support this project please consider making a donation - donations can be made at
  http://www.jthink.net/jaudiotagger/donate.jsp

Build is with Maven
 pom.xml              :Maven build file

Directory structure as follows:
 Under source control:
 src                  :source code directory 
 srctest              :source test code directory
 www                  :java doc directory
 testdata             :test files for use by the junit tests, not all tests are included in the distribution because of copyright
 target               :contains the jaudiotagger***.jar built from maven

IDE files:
 jaudiotagger.iml     :JetBrains Intellij Module
 jaudiotagger.ipr     :JetBrains Intellij Project

License:
 license.txt :license file
 
 
Build details:

Install Maven2
Run
  mvn install
to compile, test and install