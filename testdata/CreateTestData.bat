-- If you have lame mp3 encoder installed you can recreate all the test files from the single test.wav

-- Resample to higher sampling rate so creates a V1 mp3
lame --resample 44.1 test.wav testV1.mp3
lame --resample 44.1 -b 128  test.wav testV1Cbr128.mp3
lame --resample 44.1 -b 192  test.wav testV1Cbr192.mp3
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

-- Resample to lower sampling rate so creates a V2.5 mp3
lame --resample 12 test.wav testV25.mp3
lame --resample 12 -b 128  test.wav testV2Cbr128.mp3
lame --resample 12 -v -V 0 test.wav testV25vbrOld0.mp3
lame --resample 12 --vbr-new -V 0 test.wav testV25vbrNew0.mp3

-- use Defaults to create the V2 mp3
lame testV2.mp3
lame  -b 128  test.wav testV2Cbr128.mp3
lame -v -V 0 test.wav testV2vbrOld0.mp3
lame --vbr-new -V 0 test.wav testV2vbrNew0.mp3




