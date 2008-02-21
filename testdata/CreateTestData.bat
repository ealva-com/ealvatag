rem If you have lame mp3 encoder and Mp2 Enc ( for Layer II encoding) installed you can recreate all the test files from the single test.wav

rem Resample to higher sampling rate so creates a V1 mp3
rem cbr
lame --resample 44.1 test.wav testV1.mp3
lame --resample 44.1 -b 128  test.wav testV1Cbr128.mp3
rem cbr high bit rate
lame --resample 44.1 -b 192  test.wav testV1Cbr192.mp3
rem vbr old method different levels
lame --resample 44.1 -v -V 0 test.wav testV1vbrOld0.mp3
lame --resample 44.1 -v -V 1 test.wav testV1vbrold1.mp3
lame --resample 44.1 -v -V 2 test.wav testV1vbrOld2.mp3
lame --resample 44.1 -v -V 3 test.wav testV1vbrOld3.mp3
lame --resample 44.1 -v -V 4 test.wav testV1vbrOld4.mp3
lame --resample 44.1 -v -V 5 test.wav testV1vbrOld5.mp3
lame --resample 44.1 -v -V 6 test.wav testV1vbrOld6.mp3
lame --resample 44.1 -v -V 6 test.wav testV1vbrOld7.mp3
lame --resample 44.1 -v -V 6 test.wav testV1vbrOld8.mp3
lame --resample 44.1 -v -V 6 test.wav testV1vbrOld9.mp3
rem vbr old method different levels
lame --resample 44.1 --vbr-new -V 0 test.wav testV1vbrNew0.mp3
lame --resample 44.1 --vbr-new -V 1 test.wav testV1vbrNew1.mp3
lame --resample 44.1 --vbr-new -V 2 test.wav testV1vbrNew2.mp3
lame --resample 44.1 --vbr-new -V 3 test.wav testV1vbrNew3.mp3
lame --resample 44.1 --vbr-new -V 4 test.wav testV1vbrNew4.mp3
lame --resample 44.1 --vbr-new -V 5 test.wav testV1vbrNew5.mp3
lame --resample 44.1 --vbr-new -V 6 test.wav testV1vbrNew6.mp3
lame --resample 44.1 --vbr-new -V 6 test.wav testV1vbrNew7.mp3
lame --resample 44.1 --vbr-new -V 6 test.wav testV1vbrNew8.mp3
lame --resample 44.1 --vbr-new -V 6 test.wav testV1vbrNew9.mp3
rem Put in a v1 tag
lame --resample 44.1 -b 128 --tt testtitle --ta testartist --tl testalbum --ty 1999 --tc testcomment --tn 100 --tg 1 test.wav testV1Cbr128ID3v1.mp3
rem Put in a v2 tag
lame --resample 44.1 -b 128 --id3v2-only --tt testtitle --ta testartist --tl testalbum --ty 1999 --tc testcomment --tn 100 --tg 1 test.wav testV1Cbr128ID3v2.mp3
rem Put in a v2 tag with padding
lame --resample 44.1 -b 128 --id3v2-only --tt testtitle --ta testartist --tl testalbum --ty 1999 --tc testcomment --tn 100 --tg 1 test.wav testV1Cbr128ID3v2pad.mp3
rem Put in a v1 and a v2 tag
lame --resample 44.1 -b 128 --add-id3v2 --tt testtitle --ta testartist --tl testalbum --ty 1999 --tc testcomment --tn 100 --tg 1 test.wav testV1Cbr128ID3v1v2.mp3

rem Resample to lower sampling rate so creates a V2.5LIII mp3
lame --resample 12 test.wav testV25.mp3
lame --resample 12 -b 128  test.wav testV2Cbr128.mp3
lame --resample 12 -v -V 0 test.wav testV25vbrOld0.mp3
lame --resample 12 --vbr-new -V 0 test.wav testV25vbrNew0.mp3

rem use Defaults to create the V2LIII mp3
lame testV2.mp3
lame  -b 128  test.wav testV2Cbr128.mp3
lame -v -V 0 test.wav testV2vbrOld0.mp3
lame --vbr-new -V 0 test.wav testV2vbrNew0.mp3

Rem Creating mp2 (Layer II) files
mp2enc -b 192 -m m test.wav testV1L2Mono.mp3
mp2enc -b 192 -m s test.wav testV1L2Stereo.mp3


