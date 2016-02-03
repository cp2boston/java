/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.basistech.rosette.apimodel;

import java.util.List;

/** 
 * Simple api response data model for relationship extraction
 */
public final class RelationshipsResponse extends Response {

    private final List<Relationship> relationships;

    /**
     * Constructor for {@code EntitiesResponse}
     * @param relationships list of extracted relationships
     */
    public RelationshipsResponse(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    /**
     * get the list of extracted relationships
     * @return the list of extracted relationships
     */
    public List<Relationship> getRelationships() {
        return relationships;
    }

    @Override
    public int hashCode() {
        return relationships != null ? relationships.hashCode() : 0;
    }

    /**
     * if the param is a {@code RelationshipsResponse}, compare contents for equality
     * @param o the object
     * @return whether or not the param object is equal to this object
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RelationshipsResponse)) {
            return false;
        }

        RelationshipsResponse that = (RelationshipsResponse) o;
        return relationships != null ? relationships.equals(that.getRelationships()) : that.relationships == null;
    }
}
