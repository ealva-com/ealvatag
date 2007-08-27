/*
 * jaudiotagger library
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.generic;

import java.util.*;

import org.jaudiotagger.tag.*;

/**
 * This class is the default implementation for
 * {@link org.jaudiotagger.tag.Tag} and introduces some more useful
 * functionality to be implemented.<br>
 *
 * @author Raphaël Slinckx
 */
public abstract class AbstractTag implements Tag
{

    /**
     * Stores the amount of {@link TagField} with {@link TagField#isCommon()}
     * <code>true</code>.
     */
    protected int commonNumber = 0;

    /**
     * This map stores the {@linkplain TagField#getId() ids} of the stored
     * fields to the {@linkplain TagField fields} themselves.<br>
     */
    protected HashMap fields = new HashMap();

    /**
     * (overridden) The use of this method is not recommended. One must know the
     * underlying audio files implementation.
     *
     * @see org.jaudiotagger.tag.Tag#add(org.jaudiotagger.tag.TagField)
     */
    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#add(org.jaudiotagger.tag.TagField)
     *      <p/>
     *      Changed so add empty fields
     */
    public void add(TagField field)
    {
        if (field == null)
        {
            return;
        }

        List list = (List) fields.get(field.getId());

        // There was no previous item
        if (list == null)
        {
            list = new ArrayList();
            list.add(field);
            fields.put(field.getId(), list);
            if (field.isCommon())
            {
                commonNumber++;
            }
        }
        else
        {
            // We append to existing list
            list.add(field);
        }
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#addAlbum(java.lang.String)
     */
    public void addAlbum(String s)
    {
        add(createAlbumField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#addArtist(java.lang.String)
     */
    public void addArtist(String s)
    {
        add(createArtistField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#addComment(java.lang.String)
     */
    public void addComment(String s)
    {
        add(createCommentField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#addGenre(java.lang.String)
     */
    public void addGenre(String s)
    {
        add(createGenreField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#addTitle(java.lang.String)
     */
    public void addTitle(String s)
    {
        add(createTitleField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#addTrack(java.lang.String)
     */
    public void addTrack(String s)
    {
        add(createTrackField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#addYear(java.lang.String)
     */
    public void addYear(String s)
    {
        add(createYearField(s));
    }

    /**
     * Creates a field which represents the &quot;album&quot;.<br>
     * The field will already contain the given content.
     *
     * @param content The content of the created field.
     * @return tagfield representing the &quot;album&quot;
     */
    protected abstract TagField createAlbumField(String content);

    /**
     * Creates a field which represents the &quot;artist&quot;.<br>
     * The field will already contain the given content.
     *
     * @param content The content of the created field.
     * @return tagfield representing the &quot;artist&quot;
     */
    protected abstract TagField createArtistField(String content);

    /**
     * Creates a field which represents the &quot;comment&quot;.<br>
     * The field will already contain the given content.
     *
     * @param content The content of the created field.
     * @return tagfield representing the &quot;comment&quot;
     */
    protected abstract TagField createCommentField(String content);

    /**
     * Creates a field which represents the &quot;genre&quot;.<br>
     * The field will already contain the given content.
     *
     * @param content The content of the created field.
     * @return tagfield representing the &quot;genre&quot;
     */
    protected abstract TagField createGenreField(String content);

    /**
     * Creates a field which represents the &quot;title&quot;.<br>
     * The field will already contain the given content.
     *
     * @param content The content of the created field.
     * @return tagfield representing the &quot;title&quot;
     */
    protected abstract TagField createTitleField(String content);

    /**
     * Creates a field which represents the &quot;track&quot;.<br>
     * The field will already contain the given content.
     *
     * @param content The content of the created field.
     * @return tagfield representing the &quot;track&quot;
     */
    protected abstract TagField createTrackField(String content);

    /**
     * Creates a field which represents the &quot;year&quot;.<br>
     * The field will already contain the given content.
     *
     * @param content The content of the created field.
     * @return tagfield representing the &quot;year&quot;
     */
    protected abstract TagField createYearField(String content);

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#get(java.lang.String)
     */
    public List get(String id)
    {
        List list = (List) fields.get(id);

        if (list == null)
        {
            return new ArrayList();
        }

        return list;
    }

    /**
     * @param id
     * @return
     */

    //Needs to be overridden
    //TODO remove
    public List get(TagFieldKey id)
    {
        List list = (List) fields.get(id.name());

        if (list == null)
        {
            return new ArrayList();
        }

        return list;
    }


    public String getFirst(String id)
    {
        List l = get(id);
        return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getAlbum()
     */
    public List getAlbum()
    {
        return get(getAlbumId());
    }

    /**
     * Returns the identifier for a field representing the &quot;album&quot;<br>
     *
     * @return identifier for the &quot;album&quot; field.
     * @see TagField#getId()
     */
    protected abstract String getAlbumId();

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getArtist()
     */
    public List getArtist()
    {
        return get(getArtistId());
    }

    /**
     * Returns the identifier for a field representing the &quot;artist&quot;<br>
     *
     * @return identifier for the &quot;artist&quot; field.
     * @see TagField#getId()
     */
    protected abstract String getArtistId();

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getComment()
     */
    public List getComment()
    {
        return get(getCommentId());
    }

    /**
     * Returns the identifier for a field representing the &quot;comment&quot;<br>
     *
     * @return identifier for the &quot;comment&quot; field.
     * @see TagField#getId()
     */
    protected abstract String getCommentId();

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getFields()
     */
    public Iterator getFields()
    {
        final Iterator it = this.fields.entrySet().iterator();
        return new Iterator()
        {
            private Iterator fieldsIt;

            private void changeIt()
            {
                if (!it.hasNext())
                {
                    return;
                }

                List l = (List) ((Map.Entry) it.next()).getValue();
                fieldsIt = l.iterator();
            }

            public boolean hasNext()
            {
                if (fieldsIt == null)
                {
                    changeIt();
                }
                return it.hasNext() || (fieldsIt != null && fieldsIt.hasNext());
            }

            public Object next()
            {
                if (!fieldsIt.hasNext())
                {
                    changeIt();
                }

                return fieldsIt.next();
            }

            public void remove()
            {
                fieldsIt.remove();
            }
        };
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getFirstAlbum()
     */
    public String getFirstAlbum()
    {
        List l = get(getAlbumId());
        return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getFirstArtist()
     */
    public String getFirstArtist()
    {
        List l = get(getArtistId());
        return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getFirstComment()
     */
    public String getFirstComment()
    {
        List l = get(getCommentId());
        return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getFirstGenre()
     */
    public String getFirstGenre()
    {
        List l = get(getGenreId());
        return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getFirstTitle()
     */
    public String getFirstTitle()
    {
        List l = get(getTitleId());
        return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getFirstTrack()
     */
    public String getFirstTrack()
    {
        List l = get(getTrackId());
        return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getFirstYear()
     */
    public String getFirstYear()
    {
        List l = get(getYearId());
        return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getGenre()
     */
    public List getGenre()
    {
        return get(getGenreId());
    }

    /**
     * Returns the identifier for a field representing the &quot;genre&quot;<br>
     *
     * @return identifier for the &quot;genre&quot; field.
     * @see TagField#getId()
     */
    protected abstract String getGenreId();

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getTitle()
     */
    public List getTitle()
    {
        return get(getTitleId());
    }

    /**
     * Returns the identifier for a field representing the &quot;title&quot;<br>
     *
     * @return identifier for the &quot;title&quot; field.
     * @see TagField#getId()
     */
    protected abstract String getTitleId();

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getTrack()
     */
    public List getTrack()
    {
        return get(getTrackId());
    }

    /**
     * Returns the identifier for a field representing the &quot;track&quot;<br>
     *
     * @return identifier for the &quot;track&quot; field.
     * @see TagField#getId()
     */
    protected abstract String getTrackId();

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#getYear()
     */
    public List getYear()
    {
        return get(getYearId());
    }

    /**
     * Returns the identifier for a field representing the &quot;year&quot;<br>
     *
     * @return identifier for the &quot;year&quot; field.
     * @see TagField#getId()
     */
    protected abstract String getYearId();

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#hasCommonFields()
     */
    public boolean hasCommonFields()
    {
        return commonNumber != 0;
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#hasField(java.lang.String)
     */
    public boolean hasField(String id)
    {
        return get(id).size() != 0;
    }

    /**
     * Determines whether the given charset encoding may be used for the
     * represented tagging system.
     *
     * @param enc charset encoding.
     * @return <code>true</code> if the given encoding can be used.
     */
    protected abstract boolean isAllowedEncoding(String enc);

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#isEmpty()
     */
    public boolean isEmpty()
    {
        return fields.size() == 0;
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#merge(org.jaudiotagger.tag.Tag)
     */
    public void merge(Tag tag)
    {
        // FIXME: Improve me, for the moment,
        // it overwrites this tag with other values
        // FIXME: TODO: an abstract method that merges particular things for
        // each
        // format
        if (getTitle().size() == 0)
        {
            setTitle(tag.getFirstTitle());
        }
        if (getArtist().size() == 0)
        {
            setArtist(tag.getFirstArtist());
        }
        if (getAlbum().size() == 0)
        {
            setAlbum(tag.getFirstAlbum());
        }
        if (getYear().size() == 0)
        {
            setYear(tag.getFirstYear());
        }
        if (getComment().size() == 0)
        {
            setComment(tag.getFirstComment());
        }
        if (getTrack().size() == 0)
        {
            setTrack(tag.getFirstTrack());
        }
        if (getGenre().size() == 0)
        {
            setGenre(tag.getFirstGenre());
        }
    }

    /**
     * (overridden)
     * Changed:Just because field is empty it doesnt mesn it should be deleted. That should be the choice
     * of the developer. (Or does this break things)
     *
     * @see org.jaudiotagger.tag.Tag#set(org.jaudiotagger.tag.TagField)
     */
    public void set(TagField field)
    {
        if (field == null)
        {
            return;
        }

        // If there is already an existing field with same id
        // and both are TextFields, we update the first element
        List l = (List) fields.get(field.getId());
        if (l != null)
        {
            TagField f = (TagField) l.get(0);
            f.copyContent(field);
            return;
        }

        // Else we put the new field in the fields.
        l = new ArrayList();
        l.add(field);
        fields.put(field.getId(), l);
        if (field.isCommon())
        {
            commonNumber++;
        }
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#setAlbum(java.lang.String)
     */
    public void setAlbum(String s)
    {
        set(createAlbumField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#setArtist(java.lang.String)
     */
    public void setArtist(String s)
    {
        set(createArtistField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#setComment(java.lang.String)
     */
    public void setComment(String s)
    {
        set(createCommentField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#setEncoding(java.lang.String)
     */
    public boolean setEncoding(String enc)
    {
        if (!isAllowedEncoding(enc))
        {
            return false;
        }

        Iterator it = getFields();
        while (it.hasNext())
        {
            TagField field = (TagField) it.next();
            if (field instanceof TagTextField)
            {
                ((TagTextField) field).setEncoding(enc);
            }
        }

        return true;
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#setGenre(java.lang.String)
     */
    public void setGenre(String s)
    {
        set(createGenreField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#setTitle(java.lang.String)
     */
    public void setTitle(String s)
    {
        set(createTitleField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#setTrack(java.lang.String)
     */
    public void setTrack(String s)
    {
        set(createTrackField(s));
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.tag.Tag#setYear(java.lang.String)
     */
    public void setYear(String s)
    {
        set(createYearField(s));
    }

    /**
     * (overridden)
     *
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        out.append("Tag content:\n");
        Iterator it = getFields();
        while (it.hasNext())
        {
            TagField field = (TagField) it.next();
            out.append("\t");
            out.append(field.getId());
            out.append(" : ");
            out.append(field.toString());
            out.append("\n");
        }
        return out.toString().substring(0, out.length() - 1);
    }

    //Should be overridden by all subclasses but provided empty impl
    //as working one format at a time
    //TODO remove
    public TagField createTagField(TagFieldKey genericKey, String value)
    {
        return null;
    }

    //Should be overridden by all subclasses but provided empty impl
    //as working one format at a time
    //TODO remove
    public String getFirst(TagFieldKey genericKey)
    {
        return null;
    }

    //Should be overridden by all subclasses but provided empty impl
    //as working one format at a time
    //TODO remove
    public void deleteTagField(TagFieldKey tagFieldKey)
    {
        ;
    }


    /**
     * Delete all ocurrences of field.
     *
     * @param key
     */
    protected void deleteField(String key)
    {
        Object removed = fields.remove(key);
        //if (removed != null && field.isCommon())
        //    commonNumber--;
        return;
    }
}
