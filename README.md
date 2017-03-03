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

Add eAlvaTag to your project
----------------------------
Gradle:
```gradle
compile 'com.ealva:ealvatag:0.0.2'
```

Maven:
```xml
<dependency>
    <groupId>com.ealva</groupId>
    <artifactId>ealvatag</artifactId>
    <version>0.0.2</version>
</dependency>
```

Ensure you have the most recent version by checking [here](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.ealva%22%20AND%20a%3A%22ealvatag%22)

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
    Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
    Copyright (C) 2015 Paul Taylor
    Copyright 2017 Eric A. Snell

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
