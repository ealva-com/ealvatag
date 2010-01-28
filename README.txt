JAudiotagger is a JAVA API for audio metatagging. Both a common API and format specific APIS are available, currently supports
 ID3,Flac,OggVorbis,Wma and Mp4.

 Build is with Maven, but was previously with ant. The ant files have not been deleted in case they are useful to those
  not familiar with Maven.

 Directory structure as follows:
 Under source control:
 src                  :source code directory 
 srctest              :source code directory  
 www                  :java doc directory
 testdata             :test files for use by the junit tests, not all tests are included in the distribution because of copyright
 target               :contains the jadiotagger***.jar built from maven

 Other files:
 pom.xml              :Maven build file

 Defunct ant build files:
 build.xml
 build.properties     :properties likely to differ between users should be in here
 build.bat            :wrapper to run build script on Windows
 lib                  :Third party libraries

 IDE files:
 jaudiotagger.iml     :Intellij Jetbrains Module
 jaudiotagger.ipr     :Intellij Jetbrains Project

 License:
 jaudiotagger.LICENSE :license file
 
 
 Build details:

 Install Maven2
 Run
    mvn package
 to compile and test

 Admin only
 Run
    mvn deploy
 to upload to the java.net maven repository
