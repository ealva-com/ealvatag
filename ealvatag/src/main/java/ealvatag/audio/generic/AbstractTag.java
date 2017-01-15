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
package ealvatag.audio.generic;

import com.google.common.collect.ImmutableList;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.KeyNotFoundException;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagTextField;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.images.Artwork;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the default implementation for
 * {@link ealvatag.tag.Tag} and introduces some more useful
 * functionality to be implemented.<br>
 *
 * @author Raphaël Slinckx
 */
public abstract class AbstractTag implements Tag {
    /**
     * Stores the amount of {@link TagField} with {@link TagField#isCommon()}
     * <code>true</code>.
     */
    protected int commonNumber = 0;

    /**
     * This map stores the {@linkplain TagField#getId() ids} of the stored
     * fields to the {@linkplain TagField fields} themselves. Because a linked hashMap is used the order
     * that they are added in is preserved, the only exception to this rule is when two fields of the same id
     * exist, both will be returned according to when the first item was added to the file. <br>
     */
    protected Map<String, List<TagField>> fields = new LinkedHashMap<>();

    public List<String> getAll(String id) {
        List<String> fields = new ArrayList<>();
        List<TagField> tagFields = getFields(id);
        for (int i = 0, size = tagFields.size(); i < size; i++) {
            fields.add(tagFields.get(i).toString());
        }
        return fields;
    }

    public String getItem(String id, int index) {
        List<TagField> l = getFields(id);
        return (l.size() > index) ? l.get(index).toString() : "";
    }

    public List<TagField> getAll() {
        List<TagField> fieldList = new ArrayList<>();
        for (List<TagField> listOfFields : fields.values()) {
            for (TagField next : listOfFields) {
                fieldList.add(next);
            }
        }
        return fieldList;
    }

    /**
     * Determines whether the given charset encoding may be used for the
     * represented tagging system.
     *
     * @param enc charset encoding.
     *
     * @return <code>true</code> if the given encoding can be used.
     */
    protected abstract boolean isAllowedEncoding(Charset enc);

    /**
     * Create new field and set it in the tag
     *
     * @param genericKey
     * @param value
     *
     * @throws KeyNotFoundException
     * @throws FieldDataInvalidException
     */
    @Override
    public void setField(FieldKey genericKey, String... value) throws KeyNotFoundException, FieldDataInvalidException {
        TagField tagfield = createField(genericKey, value);
        setField(tagfield);
    }

    /**
     * Create new field and add it to the tag
     *
     * @param genericKey
     * @param value
     *
     * @throws KeyNotFoundException
     * @throws FieldDataInvalidException
     */
    @Override
    public void addField(FieldKey genericKey, String... value) throws KeyNotFoundException, FieldDataInvalidException {
        TagField tagfield = createField(genericKey, value);
        addField(tagfield);
    }

    /**
     * @param genericKey the field to delete
     *
     * @throws IllegalArgumentException if {@code fieldKey} is null
     * @throws UnsupportedFieldException if the tag instance does not support the type of {@link FieldKey}
     */
    public abstract void deleteField(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException;

    public void deleteField(String id) {
        fields.remove(id);
    }

    @Override
    public ImmutableList<TagField> getFields(String id) {
        final List<TagField> tagFields = fields.get(id);
        if (tagFields == null) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf(tagFields);
    }

    @Override
    public Iterator<TagField> getFields() {
        final Iterator<Map.Entry<String, List<TagField>>> it = this.fields.entrySet().iterator();
        return new Iterator<TagField>() {
            private Iterator<TagField> fieldsIt;

            private void changeIt() {
                if (!it.hasNext()) {
                    return;
                }

                Map.Entry<String, List<TagField>> e = it.next();
                List<TagField> l = e.getValue();
                fieldsIt = l.iterator();
            }

            @Override
            public boolean hasNext() {
                if (fieldsIt == null) {
                    changeIt();
                }
                return it.hasNext() || (fieldsIt != null && fieldsIt.hasNext());
            }

            @Override
            public TagField next() {
                if (!fieldsIt.hasNext()) {
                    changeIt();
                }

                return fieldsIt.next();
            }

            @Override
            public void remove() {
                fieldsIt.remove();
            }
        };
    }

    @Override
    public String getFirst(String id) {
        List<TagField> l = getFields(id);
        return (l.size() != 0) ? l.get(0).toString() : "";
    }

    /**
     * Retrieve the first value that exists for this generic key
     *
     * @param genericKey
     *
     * @return
     */
    @Override
    public String getFirst(FieldKey genericKey) throws KeyNotFoundException {
        return getValue(genericKey, 0);
    }

    @Override
    public TagField getFirstField(String id) {
        List<TagField> l = getFields(id);
        return (l.size() != 0) ? l.get(0) : null;
    }

    /**
     * @param genericKey
     *
     * @return
     *
     * @throws KeyNotFoundException
     */
    public abstract TagField getFirstField(FieldKey genericKey) throws KeyNotFoundException;

    /**
     * Does this tag contain any comon fields
     *
     * @see ealvatag.tag.Tag#hasCommonFields()
     */
    @Override
    public boolean hasCommonFields() {
        return commonNumber != 0;
    }

    @Override
    public boolean hasField(FieldKey fieldKey) {
        return hasField(fieldKey.name());
    }

    /**
     * Does this tag contain a field with the specified id
     *
     * @see ealvatag.tag.Tag#hasField(java.lang.String)
     */
    @Override
    public boolean hasField(String id) {
        return getFields(id).size() != 0;
    }

    /**
     * Is this tag empty
     *
     * @see ealvatag.tag.Tag#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return fields.size() == 0;
    }

    /**
     * Return field count
     * <p>
     * TODO:There must be a more efficient way to do this.
     *
     * @return field count
     */
    @Override
    public int getFieldCount() {
        getAll().size();
        Iterator it = getFields();
        int count = 0;
        while (it.hasNext()) {
            count++;
            it.next();
        }
        return count;
    }

    @Override
    public int getFieldCountIncludingSubValues() {
        return getFieldCount();
    }

    /**
     * Set or add encoding
     */
    public boolean setEncoding(final Charset enc) {
        if (!isAllowedEncoding(enc)) {
            return false;
        }

        Iterator it = getFields();
        while (it.hasNext()) {
            TagField field = (TagField)it.next();
            if (field instanceof TagTextField) {
                ((TagTextField)field).setEncoding(enc);
            }
        }

        return true;
    }

    public Artwork getFirstArtwork() {
        List<Artwork> artwork = getArtworkList();
        if (artwork.size() > 0) {
            return artwork.get(0);
        }
        return null;
    }

    /**
     * Delete all instance of artwork Field
     *
     * @throws KeyNotFoundException
     */
    public void deleteArtworkField() throws KeyNotFoundException {
        this.deleteField(FieldKey.COVER_ART);
    }

    /**
     * Create field and then set within tag itself
     *
     * @param artwork
     *
     * @throws FieldDataInvalidException
     */
    public void setField(Artwork artwork) throws FieldDataInvalidException {
        this.setField(createField(artwork));
    }

    /**
     * Create field and then add within tag itself
     *
     * @param artwork
     *
     * @throws FieldDataInvalidException
     */
    public void addField(Artwork artwork) throws FieldDataInvalidException {
        this.addField(createField(artwork));
    }

    /**
     * Set field
     * <p>
     * Changed:Just because field is empty it doesn't mean it should be deleted. That should be the choice
     * of the developer. (Or does this break things)
     *
     * @see ealvatag.tag.Tag#setField(ealvatag.tag.TagField)
     */
    @Override
    public void setField(TagField field) {
        if (field == null) {
            return;
        }

        // If there is already an existing field with same id
        // and both are TextFields, we replace the first element
        List<TagField> list = fields.get(field.getId());
        if (list != null) {
            list.set(0, field);
            return;
        }

        // Else we put the new field in the fields.
        list = new ArrayList<>();
        list.add(field);
        fields.put(field.getId(), list);
        if (field.isCommon()) {
            commonNumber++;
        }
    }

    /**
     * Add field
     *
     * @see ealvatag.tag.Tag#addField(ealvatag.tag.TagField)
     * <p>
     * Changed so add empty fields
     */
    @Override
    public void addField(TagField field) {
        if (field == null) {
            return;
        }
        List<TagField> list = fields.get(field.getId());

        // There was no previous item
        if (list == null) {
            list = new ArrayList<TagField>();
            list.add(field);
            fields.put(field.getId(), list);
            if (field.isCommon()) {
                commonNumber++;
            }
        } else {
            // We append to existing list
            list.add(field);
        }
    }

    /**
     * @param genericKey the field to create
     * @param value      the value of the the newly created field
     *
     * @return tag field of the type {@code genericKey} containing the given {@code value}
     *
     * @throws IllegalArgumentException  if genericKey is null or if no value is passed
     * @throws FieldDataInvalidException if the value is incorrect for the given field
     * @throws UnsupportedFieldException if the {@link FieldKey} is not supported
     */
    public abstract TagField createField(FieldKey genericKey, String... value) throws IllegalArgumentException,
                                                                                      FieldDataInvalidException,
                                                                                      UnsupportedFieldException;

    /**
     * (overridden)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("Tag content:\n");
        Iterator it = getFields();
        while (it.hasNext()) {
            TagField field = (TagField)it.next();
            out.append("\t");
            out.append(field.getId());
            out.append(":");
            out.append(field.toString());
            out.append("\n");
        }
        return out.toString().substring(0, out.length() - 1);
    }


}
