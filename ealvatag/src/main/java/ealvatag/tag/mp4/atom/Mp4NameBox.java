package ealvatag.tag.mp4.atom;

import ealvatag.audio.Utils;
import ealvatag.audio.mp4.atom.AbstractMp4Box;
import ealvatag.audio.mp4.atom.Mp4BoxHeader;
import okio.Buffer;

import java.io.EOFException;
import java.nio.ByteBuffer;

/**
 * This box is used within ---- boxes to hold the data name/descriptor
 */
public class Mp4NameBox extends AbstractMp4Box {
    public static final String IDENTIFIER = "name";

    private String name;

    //TODO Are these misnamed, are these version flag bytes or just null bytes
    public static final int VERSION_LENGTH = 1;
    public static final int FLAGS_LENGTH = 3;
    public static final int PRE_DATA_LENGTH = VERSION_LENGTH + FLAGS_LENGTH;

    /**
     * @param header     parentHeader info
     * @param dataBuffer data of box (doesnt include parentHeader data)
     */
    public Mp4NameBox(Mp4BoxHeader header, ByteBuffer dataBuffer) {
        this.header = header;

        //Double check
        if (!header.getId().equals(IDENTIFIER)) {
            throw new RuntimeException("Unable to process name box because identifier is:" + header.getId());
        }

        //Make slice so operations here don't effect position of main buffer
        this.dataBuffer = dataBuffer.slice();

        //issuer
        this.name = Utils.getString(this.dataBuffer, PRE_DATA_LENGTH, header.getDataLength() - PRE_DATA_LENGTH, header.getEncoding());
    }

    public Mp4NameBox(final Mp4BoxHeader nameBoxHeader, final Buffer buffer) throws EOFException {
        header = nameBoxHeader;

        //Double check
        if (!header.getId().equals(IDENTIFIER)) {
            throw new RuntimeException("Unable to process name box because identifier is:" + header.getId());
        }

        buffer.skip(PRE_DATA_LENGTH);

        //descriptor
        name = buffer.readString(header.getDataLength() - PRE_DATA_LENGTH, header.getEncoding());
    }

    public String getName() {
        return name;
    }
}
