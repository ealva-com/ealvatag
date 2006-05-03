JAudiotagger project will eventually become a Java API for as many audio tagging formats as possible, all accessible via a tag independent format known
 as virtual tag as as well in their native format. 
 
 But for now it offers full support of ID3v1 and pretty good but incomplete support of ID3v22,ID3v23 and IDv24.
 
 Directory structure as follows:
 Under source control:
 src                  :source code directory 
 srctest              :source code directory  
 www                  :java doc directory    
 dist:                :contains the latest jar files 
 testdata             :test mp3 files for use by the junit tests
 lib                  :Third party libraries
 
 Created by Build Script:
 classes              :compiled classes directory
 testclasses          :compiled test classes directory
 testdatatmp          :copy of test files used by last invokation of junit
 junit                :junit test output
 
 Other files:
 
 build.xml            :ant build script
 build.properties     :properties likely to differ between users should be in here
 build.bat            :wrapper to run build script on Windows
 
 jaudiotagger.iml     :Intellij Jetbrains Module
 jaudiotagger.ipr     :Intellij Jetbrains Project
 
 jaudiotagger.LICENSE :license file
 
 
 Build details
 
 Install ant, ths build script has been  tested against ant 1.6.2 
 Because of a quirk with ant and junit the junit.jar in the lib directory is required to be copied into ants lib directory
 otherwise the build will not work better, if somebody can come up with a more portable solution to this problem please let me know.
 
 Run build.bat to build the source code, run the tests, and package into jar files 
 
 build -projecthelp shows all the targets
 
 use the 'share' target to copy the contents of the dist to another location, for use in another project. because this would be different for 
 every user of the lib the directory to copy to is defined in build.properties change as required
 
 javadoc target is not run automatically,run as required
 

