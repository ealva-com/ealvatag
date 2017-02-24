package ealvatag.tag.id3;

import ealvatag.tag.InvalidFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ealvatag.logging.ErrorMessage.ID3_UNABLE_TO_DECOMPRESS_FRAME;
import static ealvatag.logging.ErrorMessage.exceptionMsg;

import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * compresses frame data
 * <p>
 * Is currently required for V23Frames and V24Frames
 */
//TODO also need to support compress framedata
@SuppressWarnings("Duplicates")
class ID3Compression {
  private static Logger LOG = LoggerFactory.getLogger(ID3Compression.class);

  /**
   * Decompress realFrameSize bytes to decompressedFrameSize bytes and return as ByteBuffer
   */
  static ByteBuffer uncompress(String identifier,
                               String filename,
                               ByteBuffer byteBuffer,
                               int decompressedFrameSize,
                               int realFrameSize) throws InvalidFrameException {
    LOG.debug("{}:About to decompress {} bytes, expect result to be:{} bytes", filename, realFrameSize, decompressedFrameSize);
    // Decompress the bytes into this buffer, size initialized from header field
    byte[] result = new byte[decompressedFrameSize];
    byte[] input = new byte[realFrameSize];

    //Store position ( just after frame header and any extra bits)
    //Read frame data into array, and then put buffer back to where it was
    int position = byteBuffer.position();
    byteBuffer.get(input, 0, realFrameSize);
    byteBuffer.position(position);

    Inflater decompresser = new Inflater();
    decompresser.setInput(input);
    try {
      int inflatedTo = decompresser.inflate(result);
      LOG.debug("{}:Decompressed to {} bytes", inflatedTo);
    } catch (DataFormatException dfe) {
      LOG.debug("Unable to decompress this frame:{}", identifier, dfe);

      //Update position of main buffer, so no attempt is made to reread these bytes
      byteBuffer.position(byteBuffer.position() + realFrameSize);
      throw new InvalidFrameException(exceptionMsg(ID3_UNABLE_TO_DECOMPRESS_FRAME,
                                                   identifier,
                                                   filename),
                                      dfe);
    }
    decompresser.end();
    return ByteBuffer.wrap(result);
  }

//  protected static Buffer uncompress(String identifier,
//                                     String filename,
//                                     Buffer byteBuffer,
//                                     int decompressedFrameSize,
//                                     int realFrameSize) throws InvalidFrameException {
//    LOG.debug("{}:About to decompress {} bytes, expect result to be:{} bytes", filename, realFrameSize, decompressedFrameSize);
//    // Decompress the bytes into this buffer, size initialized from header field
//    byte[] result = new byte[decompressedFrameSize];
//    byte[] input = new byte[realFrameSize];
//
//    byteBuffer.read(input, 0, realFrameSize);
//
//    Inflater decompresser = new Inflater();
//    decompresser.setInput(input);
//    try {
//      int inflatedTo = decompresser.inflate(result);
//      LOG.debug(filename + ":Decompressed to " + inflatedTo + " bytes");
//    } catch (DataFormatException dfe) {
//      LOG.debug("Unable to decompress this frame:" + identifier, dfe);
//
//      //Update position of main buffer, so no attempt is made to reread these bytes
////            byteBuffer.position(byteBuffer.position() + realFrameSize);
//      throw new InvalidFrameException(exceptionMsg(ID3_UNABLE_TO_DECOMPRESS_FRAME, identifier, filename),
//                                      dfe);
//    }
//    decompresser.end();
//    final Buffer decompressedBuffer = new Buffer();
//    decompressedBuffer.write(result);
//    return decompressedBuffer;
//  }
}
