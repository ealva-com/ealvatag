/*
 * @author : Paul Taylor
 * <p>
 * Version @version:$Id$
 * <p>
 * Jaudiotagger Copyright (C)2004,2005
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public  License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, you can get a copy from
 * http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301 USA
 * <p>
 * Description:
 */
package ealvatag.tag.datatype;

public class AbstractStringStringValuePair extends AbstractValuePair<String, String> {
    public String getIdForValue(String value) {
        return valueToId.get(value);
    }

    public String getValueForId(String id) {
        return idToValue.get(id);
    }

    protected void createMaps() {
        for (final String lkey : idToValue.keySet()) {
            String value = idToValue.get(lkey);
            valueToId.put(value, lkey);
        }
    }
}
