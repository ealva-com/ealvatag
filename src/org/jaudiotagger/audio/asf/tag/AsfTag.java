package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.audio.generic.AbstractTag;
import org.jaudiotagger.tag.*;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Christian Laireiter
 *
 */
public final class AsfTag extends AbstractTag
{

    /**
     * The <b>only</b> text encoding for ASF files.
     */
    public final static String TEXT_ENCODING = "UTF-16LE"; // //$NON-NLS-1$

    /**
     * Creates a {@link AsfTagTextField} for use with string content.<br>
     * For now the method is relatively useless. However future common checks can be easily implemented.
     * 
     * @param fieldKey the field identifier for the field.
     * @param content The string content.
     * @return text field for ASF use.
     */
    public static AsfTagTextField createTextField(String fieldKey, String content)
    {
        return new AsfTagTextField(fieldKey, content);
    }

    /**
     * @see #isConvertingFields()    
     */
    private final boolean convertFields;

    /**
     * Creates an empty instance.
     */
    public AsfTag()
    {
        this(false);
    }

    /**
     * Creates an instance and sets the field conversion property.<br>
     * 
     * @param fieldConversion look at {@link #isConvertingFields()}.
     */
    public AsfTag(boolean fieldConversion)
    {
        this.convertFields = fieldConversion;
    }

    /**
     * Creates an instance an copies the fields of the source into the own
     * structure.<br>
     * 
     * @param source source to read tag fields from.
     */
    public AsfTag(Tag source)
    {
        this(false);
        try
        {
            copyFrom(source);
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("This should have been impossible, by class design. Check the Code!!!", e);
        }
    }

    /**
     * Creates an instance an copies the fields of the source into the own
     * structure.<br>
     * 
     * @param source source to read tag fields from.
     * @param fieldConversion look at {@link #isConvertingFields()}.
     * @throws UnsupportedEncodingException {@link TagField#getRawContent()} which may be called 
     */
    public AsfTag(Tag source, boolean fieldConversion) throws UnsupportedEncodingException
    {
        this(fieldConversion);
        copyFrom(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(TagField field)
    {
        try
        {
            TagField copy = copyFrom(field);
            if (AsfFieldKey.isMultiValued(copy.getId()))
            {
                super.add(copy);
            }
            else
            {
                super.set(copy);
            }
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Cannot get raw content.", e);
        }
    }

    /**
     * Creates a field for copyright and adds it.<br>
     * @param copyRight copyright content
     */
    public void addCopyright(String copyRight)
    {
        add(createCopyrightField(copyRight));
    }

    /**
     * This method copies tag fields from the source.<br>
     * 
     * @param source source to read tag fields from.
     * @throws UnsupportedEncodingException {@link TagField#getRawContent()} which may be called
     */
    private void copyFrom(Tag source) throws UnsupportedEncodingException
    {
        if (source == null)
        {
            throw new NullPointerException();
        }
        final Iterator<TagField> fieldIterator = source.getFields();
        // iterate over all fields 
        while (fieldIterator.hasNext())
        {
            TagField copy = copyFrom(fieldIterator.next());
            if (copy != null)
            {
                super.add(copy);
            }
        }
    }

    /**
     * If {@link #isConvertingFields()} is <code>true</code>,
     * yCreates a copy of <code>source</code>, if its not empty-<br>
     * However, plain {@link TagField} objects can only be transformed into binary fields using their 
     * {@link TagField#getRawContent()} method.<br>
     * 
     * @param source source field to copy.
     * @return A copy, which is as close to the source as possible, or <code>null</code> if the field is empty 
     *         (empty byte[] or blank string}.
     * @throws UnsupportedEncodingException upon {@link TagField#getRawContent()}.
     */
    private TagField copyFrom(TagField source) throws UnsupportedEncodingException
    {
        TagField result = null;
        if (isConvertingFields())
        {
            // Get the ASF internal key, where it applies
            String internalId = AsfFieldKey.convertId(source.getId());
            if (source instanceof TagTextField)
            {
                String content = ((TagTextField) source).getContent();
                if (!Utils.isBlank(content))
                {
                    // If its a non blank text field, value copy is simple
                    result = createTextField(internalId, content);
                }
            }
            else if (source instanceof AsfTagField)
            {
                if (!((AsfTagField) source).isEmpty())
                {
                    // If its a AsfTagField, the value copy is simple too, since all possible data types and their content
                    // are managed by ContentDescriptor objects. 
                    result = new AsfTagField(((AsfTagField) source).getDescriptor());
                }
            }
            else
            {
                /*
                 * Well, now what to do with Tagfields. The interface only allows access to raw content (byte[]).
                 * It depends on the audio formats implementation whether there is for example the raw picture data,
                 * or additionally the field identifier contained within.
                 */
                /*
                 * Well simply convert take it as plain binary data for now, maybe there will be some changes in future. 
                 */
                byte[] content = source.getRawContent();
                if (content != null && content.length > 0)
                {
                    ContentDescriptor desc = new ContentDescriptor(internalId, ContentDescriptor.TYPE_BINARY);
                    desc.setBinaryValue(content);
                    result = new AsfTagField(desc);
                }
            }
        }
        else
        {
            result = source;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createAlbumField(String content)
    {
        return createTextField(getAlbumId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createArtistField(String content)
    {
        return createTextField(getArtistId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createCommentField(String content)
    {
        return createTextField(getCommentId(), content);
    }

    /**
     * Creates a field for storing the copyright.<br>
     * @param content Copyright value.
     * @return {@link AsfTagTextField}
     */
    public TagField createCopyrightField(String content)
    {
        return createTextField(AsfFieldKey.COPYRIGHT.getPublicFieldId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createGenreField(String content)
    {
        return createTextField(getGenreId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createTagField(TagFieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createTextField(AsfFieldKey.convertId(genericKey), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createTitleField(String content)
    {
        return createTextField(getTitleId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createTrackField(String content) throws FieldDataInvalidException
    {
        return createTextField(getTrackId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createYearField(String content)
    {
        return createTextField(getYearId(), content);
    }

    /**
     * Removes all fields with the specified field key.<br>
     * 
     * @param fieldKey the key of the fields to remove.<br> 
     */
    public void deleteTagField(String fieldKey)
    {
        super.deleteField(fieldKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTagField(TagFieldKey tagFieldKey) throws KeyNotFoundException
    {
        super.deleteField(AsfFieldKey.convertId(tagFieldKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TagField> get(String id)
    {
        return super.get(AsfFieldKey.convertId(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TagField> get(TagFieldKey id) throws KeyNotFoundException
    {
        return super.get(AsfFieldKey.convertId(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getAlbumId()
    {
        return AsfFieldKey.ALBUM.getPublicFieldId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getArtistId()
    {
        return AsfFieldKey.ARTIST.getPublicFieldId();
    }

    public <F extends AsfTagField> Iterator<F> getAsfFields()
    {
        if (!isConvertingFields())
        {
            throw new IllegalStateException("Since the field conversion is not enabled, this method cannot be executed");
        }
        final Iterator<TagField> it = getFields();
        return new Iterator<F>()
        {

            public boolean hasNext()
            {
                return it.hasNext();
            }

            @SuppressWarnings("unchecked")
            public F next()
            {
                return (F) it.next();
            }

            public void remove()
            {
                it.remove();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommentId()
    {
        return AsfFieldKey.COMMENT.getPublicFieldId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirst(TagFieldKey genericKey) throws KeyNotFoundException
    {
        return getFirst(AsfFieldKey.convertId(genericKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getGenreId()
    {
        return AsfFieldKey.GENRE.getPublicFieldId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTitleId()
    {
        return AsfFieldKey.TITLE.getPublicFieldId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTrackId()
    {
        return AsfFieldKey.TRACK.getPublicFieldId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getYearId()
    {
        return AsfFieldKey.YEAR.getPublicFieldId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAllowedEncoding(String enc)
    {
        return TEXT_ENCODING.equals(enc);
    }

    /**
     * If <code>true</code>, the {@link #copyFrom(TagField)} method creates a new {@link AsfTagField} instance and
     * copies the content from the source.<br>
     * This method is utilized by {@link #add(TagField)} and {@link #set(TagField)}.<br>
     * So if <code>true</code> it is ensured that the {@link AsfTag} instance has its own copies of fields, which cannot
     * be modified after assignment (which could pass some checks), and it just stores {@link AsfTagField} objects.<br>
     * Only then {@link #getAsfFields()} can work. otherwise {@link IllegalStateException} is thrown.
     *    
     * @return state of field conversion.
     */
    public boolean isConvertingFields()
    {
        return this.convertFields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(TagField field)
    {
        try
        {
            super.set(copyFrom(field));
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Cannot get raw content.", e);
        }
    }

    /**
     * Sets the copyright.<br>
     * @param CopyRight the copyright to set.
     */
    public void setCopyRight(String Copyright)
    {
        set(createCopyrightField(Copyright));
    }

}
