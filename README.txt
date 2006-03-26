JAudiotagger project will eventually become a Java API for as many audio tagging formats as possible, all accessible via a tag independent format known
 as virtual tag as as well in their native format. 
 
 But for now it offers full support of ID3v1 and pretty good but incomplete support of ID3v22,ID3v23 and IDv24.
 
 Directory structure as follows:
 
 src                  :source code directory (under cvs source control)
 www                  :java doc directory    (under cvs source control)
 classes              :compiled classes directory
 dist:                :contains the latest jar files ((under cvs source control)
 
 Other files:
 
 build.xml            :ant build script
 build.properties     :properties likely to differ between users should be in here
 build.bat            :wrapper to run build script on Windows
 
 jaudiotagger.iml     :Intellij Jetbrains Module
 jaudiotagger.ipr     :Intellij Jetbrains Project
 
 jaudiotagger.LICENSE :license file
 
 
 Build details
 
 Run build.bat to build the source code (requires ant 1.6.5 to be installed)
 
 build -projecthelp shows all the targets
 
 use the 'share' target to copy the contents of the dist to another location, for use in another project. because this would be different for 
 every user of the lib the directory to copy to is defined in build.properties change as required
 
 javadoc target is not run automtically,run as required
 

