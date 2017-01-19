ealvatag
========

*ealvatag* is a Java API for reading and writing tag information of audio 
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
be very unstable (no Semantic Versioning until 1.0.0 and later). Currently there is no downloadable jar. Pull requests 
welcome.

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

    ealvatag is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ealvatag is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with ealvatag.  If not, see <http://www.gnu.org/licenses/>.
 
History
-------

This library started as Entagged Audio Tag library which was forked into 
[Jaudiotagger][1]. Jaudiotagger is still under active development at this time 
(January 2017)

Jaudiotagger was forked into this library, ealvatag for 2 primary reasons: to
upgrade the license to GNU LGPLv3 and to focus on Android compatibility. 
GNU LGPLv3 is necessary to link to Apache License Version 2.0 libraries. As for
Android compatibility, the previous library was dependent on java.nio library
components not available on Android versions in current use. Also, quite a bit
of refactoring is needed so the library is more [performant][2] on the Android
platform.



 [1]: https://bitbucket.org/ijabz/jaudiotagger
 [2]: https://en.wiktionary.org/wiki/performant
