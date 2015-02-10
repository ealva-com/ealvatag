JAudiotagger is a JAVA API for audio metatagging. Both a common API and format specific APIS are available, currently supports
 ID3,Flac,OggVorbis,Wma and Mp4.

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
 jaudiotagger.LICENSE :license file
 
 
Build details:

Install Maven2
Run
  mvn install
to compile, test and install