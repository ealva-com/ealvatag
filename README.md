eAlvaTag
========

*eAlvaTag* is a Java API for reading and writing tag information of audio 
files. The follow types are currently supported:

- Mp3
- Flac
- OggVorbis
- Mp4
- Aiff
- Wav
- Wma
- Dsf

Until the version of this library reaches 1.0, you should consider the API to 
be very unstable (no Semantic Versioning until 1.0.0 and later). Pull requests 
welcome.

Quick Start
-----------
```java
 class Test {
  public void test() {
      File inputFile = new File("MyFavoriteSong.mp3");
      AudioFile audioFile = AudioFileIO.read(inputFile);
    
      final AudioHeader audioHeader = audioFile.getAudioHeader();
      final int channels = audioHeader.getChannelCount();
      final int bitRate = audioHeader.getBitRate();
      final String encodingType = audioHeader.getEncodingType();
    
      Tag tag = audioFile.getTag().or(NullTag.INSTANCE);
      final String title = tag.getValue(FieldKey.TITLE).or("");
      if ("".equals(title)) {
        if (tag == NullTag.INSTANCE) {
          // there was no tag. set a new default tag for the file type
          tag = audioFile.setNewDefaultTag();
        }
      }
    
      tag.setField(FieldKey.TITLE, "My New Title");
      audioFile.save();
    
      final ImmutableSet<FieldKey> supportedFields = tag.getSupportedFields();
      if (supportedFields.contains(FieldKey.COVER_ART)) {
        System.out.println("File type supports Artwork");
      }
    }
  }
```

eAlvaLog is a very thin facade used by eAlvaTag for logging purposes. Each Logger instance created by eAlvaTag logs all output with a 
Marker found at ealvatag.logging.Log.MARKER. This allows for easy filtering so log output can be directed and/or filtered as needed. 
eAlvaLog provides a very light Android specific LoggerFactory if you only need log to the Android log. If using the java.util.logging 
logger framework, you may also add a handler at Loggers.INSTANCE.get("ealvatag") to add a Handler specifically for eAlvaTag library log output. 
See [eAlvaLog](https://github.com/ealva-com/ealvalog) for more details on configuring logging. 

Add eAlvaTag to your project
----------------------------
Gradle:
```gradle
compile 'com.ealva:ealvatag:0.4.3'
```

Maven:
```xml
<dependency>
    <groupId>com.ealva</groupId>
    <artifactId>ealvatag</artifactId>
    <version>0.4.3</version>
</dependency>
```

Ensure you have the most recent version by checking [here](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.ealva%22%20AND%20a%3A%22ealvatag%22)

For the latest SNAPSHOT check [here](https://oss.sonatype.org/content/repositories/snapshots/com/ealva/ealvatag/)

Dependencies
------------
 [Square's Okio](https://github.com/square/okio)
    Used for reading mp3 and mp4 files and will be further integrated. The segment pooling dramatically increased read performance over Jdk stream/file IO.
    
    implementation 'com.squareup.okio:okio:2.4.1'

 [Google's Guava](https://github.com/google/guava)
    Used for Optional<>, Immutable collections, and general utilities
     
    implementation 'com.google.guava:guava:20.0'

 [eAlvaLog](https://github.com/ealva-com/ealvalog)
    Thin logging facade. Only the API is used in this library. For users of this library it currently supports the java.util.logging 
    Logger and Android logging. It should be very easy to write a facade implementation for another logging framework if it's not already
    provided.
    
    implementation 'com.ealva:ealvalog:0.5.1-SNAPSHOT'
    implementation 'com.ealva:ealvalog-core:0.5.1-SNAPSHOT'
    implementation 'com.ealva:ealvalog-java:0.5.1-SNAPSHOT'


Android
-------

ProGuard

    -keep class ealvatag.tag.id3.framebody.** { *; }
    -keep class ealvatag.tag.datatype.** { *; }
    -dontwarn java.awt.geom.AffineTransform
    -dontwarn java.awt.Graphics2D
    -dontwarn java.awt.Image
    -dontwarn java.awt.image.BufferedImage
    -dontwarn javax.imageio.ImageIO
    -dontwarn javax.imageio.ImageWriter
    -dontwarn javax.imageio.stream.ImageInputStream
    -dontwarn javax.swing.filechooser.FileFilter
    -dontwarn sun.security.action.GetPropertyAction
    -dontwarn java.nio.file.Paths
    -dontwarn java.nio.file.OpenOption
    -dontwarn java.nio.file.Files


License
-------
    Copyright 2018 Eric A. Snell

    eAlvaTag is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    eAlvaTag is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with eAlvaTag.  If not, see <http://www.gnu.org/licenses/>.
    
 Some files copyright by the original authors (see file headers)    
 
History
-------

This library started as Entagged Audio Tag library which was forked into 
[Jaudiotagger][1]. Jaudiotagger is still under active development at this time 
(January 2017)

Jaudiotagger was forked into this library, eAlvaTag for 2 primary reasons: to
upgrade the license to GNU LGPLv3 and to focus on Android compatibility. 
GNU LGPLv3 is necessary to link to Apache License Version 2.0 libraries. As for
Android compatibility, the previous library was dependent on java.nio library
components not available on Android versions in current use. Also, quite a bit
of refactoring was needed to make the library more [performant][2] on the Android
platform.



 [1]: https://bitbucket.org/ijabz/jaudiotagger
 [2]: https://en.wiktionary.org/wiki/performant
