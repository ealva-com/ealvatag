package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 */
public class FrameBodyUSLTTest extends AbstractTestCase
{

    public static final String UTF16_REQUIRED = "\u2026";

    public void testWriteUnicodeBody() throws IOException
    {
        FrameBodyUSLT fb = new FrameBodyUSLT(TextEncoding.UTF_16, "eng", "", UTF16_REQUIRED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fb.write(baos);
        FileOutputStream fos = new FileOutputStream("testdatatmp/TEST.TXT");
        fos.write(baos.toByteArray());
        byte[] frameBody = baos.toByteArray();
        byte[] correctBits = makeByteArray(new int[]{0x01, 'e', 'n', 'g', 0xff, 0xfe, 0x00, 0x00, 0xff, 0xfe, 0x26, 0x20});
        String s = cmp(correctBits, frameBody);
        if (s != null)
        {
            fail(s);
        }
    }

    private String cmp(byte[] a, byte[] b)
    {
        if (a.length != b.length)
        {
            return "length of byte arrays differ (" + a.length + "!=" + b.length + ")";
        }
        for (int i = 0; i < a.length; i++)
        {
            if (a[i] != b[i])
            {
                return "byte arrays differ at offset " + i + " (" + a[i] + "!=" + b[i] + ")";
            }
        }
        return null;
    }

    private byte[] makeByteArray(int[] ints)
    {
        byte[] bs = new byte[ints.length];
        for (int i = 0; i < ints.length; i++)
        {
            bs[i] = (byte) ints[i];
        }
        return bs;
    }
}