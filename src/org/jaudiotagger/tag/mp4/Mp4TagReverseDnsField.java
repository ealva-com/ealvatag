package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;
import org.jaudiotagger.audio.generic.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Represents reverse dns field, used for custom information
 *
 * These fields have a more complex setup
 *      Box ----  shows this is a reverse dns metadata field
 *      Box mean  the issuer in the form of reverse DNS domain (e.g com.apple.iTunes)
 *      Box name  descriptor identying the type of contents
 *      Box data  contents
 *
 * The raw data passed starts from the mean box
 */
public class Mp4TagReverseDnsField extends Mp4TagField implements TagTextField
{
    public static final String IDENTIFIER       = "----";
   
    protected int    dataSize;

    //Issuer
    private String issuer;

    //Descriptor
    private String descriptor;

    //Data Content
    protected String content;

    public Mp4TagReverseDnsField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        super(id, data);
    }

    public Mp4TagReverseDnsField(String id, String content)
    {
        super(id);
        this.content = content;
    }

    protected void build(ByteBuffer data) throws UnsupportedEncodingException
    {
        //Read mean box, set the issuer and skip over data
        Mp4BoxHeader meanBoxHeader  = new Mp4BoxHeader(data);
        Mp4MeanBox   meanBox        = new Mp4MeanBox(meanBoxHeader,data);
        setIssuer(meanBox.getIssuer());
        data.position(data.position()+meanBoxHeader.getDataLength());

        //Read name box, identify what type of field it is
        Mp4BoxHeader nameBoxHeader = new Mp4BoxHeader(data);
        Mp4NameBox   nameBox       = new Mp4NameBox(nameBoxHeader,data);
        setDescriptor(nameBox.getName());
        data.position(data.position()+nameBoxHeader.getDataLength());

        //Read data box, identify the data
        Mp4BoxHeader dataBoxHeader = new Mp4BoxHeader(data);
        Mp4DataBox   dataBox       = new Mp4DataBox(dataBoxHeader,data);
        setContent(dataBox.getContent());
        data.position(data.position()+dataBoxHeader.getDataLength());

        //TODO We need to do this so the id to uniquely reference this field however probably need another field
        //because we are overloading the meaning of id now
        id=id+":"+issuer+":"+descriptor;

    }


    public void copyContent(TagField field)
    {
        if (field instanceof Mp4TagReverseDnsField)
        {
            this.content = ((Mp4TagReverseDnsField) field).getContent();
        }
    }

    /**
     *
     * @return content
     */
    public String getContent()
    {
        return content;
    }

    // This is overriden in the number data box
    protected byte[] getDataBytes() throws UnsupportedEncodingException
    {
        return content.getBytes(getEncoding());
    }

    public String getEncoding()
    {
        return "ISO-8859-1";
    }

    //TODO
    public byte[] getRawContent() throws UnsupportedEncodingException
    {
        return null;
    }

    public boolean isBinary()
    {
        return false;
    }

    public boolean isEmpty()
    {
        return this.content.trim().equals("");
    }

    public void setContent(String s)
    {
        this.content = s;
    }

    public void setEncoding(String s)
    {
        /* Not allowed */
    }

    public String toString()
    {
        return "Issuer:"+issuer+":Descriptor:"+descriptor+":Data:"+content;
    }

    /**
     *
     * @return the issuer
     */
    public String getIssuer()
    {
        return issuer;
    }

    /**
     *
     * @return the descriptor
     */
    public String getDescriptor()
    {
        return descriptor;
    }

    /**
     * Set the issuer, usually reverse dns of the Companies domain
     *
     * @param issuer
     */
    public void setIssuer(String issuer)
    {
        this.issuer = issuer;
    }

    /**
     * Set the descriptor for the data (what type of data it is)
     *
     * @param descriptor
     */
    public void setDescriptor(String descriptor)
    {
        this.descriptor = descriptor;
    }

    
}
