package org.jaudiotagger.audio.generic;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;


public class UtilsTest  extends TestCase {

    public void testGetExtension () {
        assertEquals ("jpeg", Utils.getExtension (new File("_12XYZ.jpeg")));
    }
    
    public void testReadUInt16 () {
        try {
            byte[] maxUnsignedBuf = { (byte) 0XFF, (byte) 0XFF };
            ByteArrayInputStream ins = new ByteArrayInputStream (maxUnsignedBuf);
            DataInputStream dis = new DataInputStream (ins);
            int val = Utils.readUint16(dis);
            assertEquals (val, 0XFFFF);

            byte[] smallIntBuf = { (byte) 0X01, (byte) 0X10 };
            ins = new ByteArrayInputStream (smallIntBuf);
            dis = new DataInputStream (ins);
            val = Utils.readUint16(dis);
            assertEquals (val, 0X0110);
        }
        catch (IOException e) {
            fail("IOException in testReadUInt16");  // huh?
        }
    }

    public void testReadUInt32 () {
        try {
            byte[] maxUnsignedBuf = { (byte) 0XFF, (byte) 0XFF, (byte) 0XFF, (byte) 0XFF };
            ByteArrayInputStream ins = new ByteArrayInputStream (maxUnsignedBuf);
            DataInputStream dis = new DataInputStream (ins);
            long val = Utils.readUint32(dis);
            assertEquals (val, 0XFFFFFFFFL);

            byte[] smallIntBuf = { (byte) 0X03, (byte) 0XFF, (byte) 0X01, (byte) 0X12 };
            ins = new ByteArrayInputStream (smallIntBuf);
            dis = new DataInputStream (ins);
            val = Utils.readUint32(dis);
            assertEquals (val, 0X03FF0112);
        }
        catch (IOException e) {
            fail("IOException in testReadUInt32");  // huh?
        }
    }
    
    public void testReadString () {
        try {
            byte [] strBuf = { (byte) 'A', (byte) '=', (byte) '1', (byte) '+', (byte) '7' };
            ByteArrayInputStream ins = new ByteArrayInputStream (strBuf);
            DataInputStream dis = new DataInputStream (ins);
            String s = Utils.readString(dis,  5);
            assertEquals ("A=1+7", s);
        }
        catch (IOException e) {
            fail("IOException in testReadString");  // huh?
        }
    }
    
    public void testGetSizeLEInt32 () {
        byte[] bytes = Utils.getSizeLEInt32(0X010203F4);
        assertEquals (bytes.length, 4);
        // need to AND with FF to avoid sign extension of byte
        assertEquals (bytes[0] & 0XFF, 0XF4);
        assertEquals (bytes[1] & 0XFF, 0X03);
        assertEquals (bytes[2] & 0XFF, 0X02);
        assertEquals (bytes[3] & 0XFF, 0X01);
    }

    public void testGetSizeBEInt32 () {
        byte[] bytes = Utils.getSizeBEInt32(0X010203F4);
        assertEquals (bytes.length, 4);
        // need to AND with FF to avoid sign extension of byte
        assertEquals (bytes[3] & 0XFF, 0XF4);
        assertEquals (bytes[2] & 0XFF, 0X03);
        assertEquals (bytes[1] & 0XFF, 0X02);
        assertEquals (bytes[0] & 0XFF, 0X01);
    }

    public void testGetSizeBEInt16 () {
        byte[] bytes = Utils.getSizeBEInt16((short) 0X0182);
        assertEquals (bytes.length, 2);
        // need to AND with FF to avoid sign extension of byte
        assertEquals (bytes[1] & 0XFF, 0X82);
        assertEquals (bytes[0] & 0XFF, 0X01);
    }

    public void testgetLongLE () {
        ByteBuffer bb = ByteBuffer.allocate(4);
        byte[] bytes = new byte[] {0X32, 0X01, (byte) 0XF0, 0X18};
        bb.put(bytes);
        long val = Utils.getLongLE (bb, 0, 3);
        assertEquals (0X18F00132L, val);
    }

    public void testgetLongBE () {
        ByteBuffer bb = ByteBuffer.allocate(4);
        byte[] bytes = new byte[] {0X32, 0X01, (byte) 0XF0, 0X18};
        bb.put(bytes);
        long val = Utils.getLongBE (bb, 0, 3);
        assertEquals (0X3201F018L, val);
        
        bytes = new byte[] {0X01, 0X02, 0X03, 0X04, 0X05, 0X06, 0X07, 0X08};
        bb = ByteBuffer.allocate(8);
        bb.put(bytes);
        val = Utils.getLongBE (bb, 0, 7);
        assertEquals (0X0102030405060708L, val);
    }
    
    public void testGetIntLE () {
        byte[] bytes = new byte[] {(byte) 0XFF, 0X01, 0X11, 0X21};
        int val = Utils.getIntLE (bytes);
        assertEquals (0X211101FF, val);
        
        bytes = new byte[] {0, (byte) 0XFF, 0X01, 0X11, 0X31, 0};
        val = Utils.getIntLE (bytes, 1, 4);
        assertEquals (0X311101FF, val);
    }

    public void testGetIntBE () {
        // But there is one with a ByteBuffer, a start, and an end
        ByteBuffer bb = ByteBuffer.allocate(5);
        byte[] bytes = new byte[] {0, 0X32, (byte) 0X80, 0X70, 0X18};
        bb.put(bytes);
        int val = Utils.getIntBE(bb, 1, 4);
        assertEquals (0X32807018, val);
    }

    public void testGetShortBE()
    {
        ByteBuffer bb = ByteBuffer.allocate(8);
        byte[] bytes = new byte[] {(byte)0xFF, (byte)0XFF};
        bb.put(bytes);
        bb.rewind();
        short val = Utils.getShortBE(bb,1,2);
        System.out.println(val);
    }

}
