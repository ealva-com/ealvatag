package ealvatag.audio.mp4.atom;

import com.google.common.base.Preconditions;
import ealvatag.audio.mp4.EncoderType;
import ealvatag.audio.mp4.Mp4AtomIdentifier;
import ealvatag.audio.mp4.Mp4AudioHeader;
import ealvatag.utils.Buffers;
import okio.Buffer;
import okio.BufferedSource;

import java.io.EOFException;
import java.io.IOException;

/**
 * DrmsBox Replaces mp4a box on drm files
 * <p>
 * Need to skip over data in order to find esds atom
 * <p>
 * Specification not known, so just look for byte by byte 'esds' and then step back four bytes for size
 */
class Mp4DrmsBox extends AbstractMp4Box {
  Mp4DrmsBox(final Mp4BoxHeader drmsBoxHeader,
             final BufferedSource bufferedSource,
             final Mp4AudioHeader audioHeader) throws IOException {
    Preconditions.checkArgument(Mp4AtomIdentifier.DRMS.matches(drmsBoxHeader.getId()));

    int dataSize = drmsBoxHeader.getDataLength();
    Buffer buffer = Buffers.makeBufferFrom(bufferedSource, dataSize);

    // have no idea where next block starts so look for identifier and step back to beginning of box to read the header
    int childHeaderMark = getChildHeaderMark(buffer);
    if (childHeaderMark != -1) {
      buffer.skip(childHeaderMark);
      Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
      @SuppressWarnings("unused")
      Mp4EsdsBox esdsBox = new Mp4EsdsBox(childHeader,
                                          bufferedSource,
                                          audioHeader,
                                          EncoderType.DRM_AAC);
    }
  }

  /**
   * Find the beginning of the esds header. We don't have the spec so we just search for the identifier
   * <p>
   * The outer loop will move ahead {@link Mp4BoxHeader#HEADER_LENGTH} bytes at a time, looking for an 'e'. If an e is found, the "sds"
   * is searched. If "sds" is found we have the position, else move ahead to just after the 'e'. During the loops we
   * {@link BufferedSource#require(long)} data to be present in the buffer, otherwise an EOFException is thrown
   *
   * @param buffer buffer to search
   *
   * @return the position of the esds child header or -1 if not found
   *
   * @throws EOFException if hit the end of the buffer
   */
  private int getChildHeaderMark(final Buffer buffer) throws EOFException {
    int childHeaderMark = -1;
    int pos = 0;
    while (pos < buffer.size() - Mp4BoxHeader.HEADER_LENGTH && childHeaderMark == -1) {
      buffer.require(pos + Mp4BoxHeader.HEADER_LENGTH);   // do this to ensure data available
      for (int i = 0; i < Mp4BoxHeader.HEADER_LENGTH; i++) {
        final int innerPos = pos + i;
        if (buffer.getByte(innerPos) == (byte)'e') {
          buffer.require(innerPos + 3);
          if (buffer.getByte(innerPos + 1) == (byte)'s' &&
              buffer.getByte(innerPos + 2) == (byte)'d' &&
              buffer.getByte(innerPos + 2) == (byte)'s') {
            childHeaderMark = innerPos - 4;
          }
        } else {
          pos = innerPos + 1;
          break;
        }
      }
      pos += Mp4BoxHeader.HEADER_LENGTH;
    }
    return childHeaderMark;
  }

//    /**
//     * Process direct data
//     *
//     * @throws CannotReadException
//     */
//    public void processData() throws CannotReadException {
//        while (dataBuffer.hasRemaining()) {
//            byte next = dataBuffer.get();
//            if (next != (byte)'e') {
//                continue;
//            }
//
//            //Have we found esds identifier, if so adjust buffer to start of esds atom
//            ByteBuffer tempBuffer = dataBuffer.slice();
//            if ((tempBuffer.get() == (byte)'s') & (tempBuffer.get() == (byte)'d') & (tempBuffer.get() == (byte)'s')) {
//                dataBuffer.position(dataBuffer.position() - 1 - Mp4BoxHeader.OFFSET_LENGTH);
//                return;
//            }
//        }
//    }
}
