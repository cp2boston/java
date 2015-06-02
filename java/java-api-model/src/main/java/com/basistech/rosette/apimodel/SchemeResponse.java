/******************************************************************************
 ** Copyright (c) 2014-2015 Basis Technology Corporation.
 **
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 **
 **     http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 ******************************************************************************/

package com.basistech.rosette.apimodel;

import java.util.List;

/**
 * Response data for schemes
 */
public final class SchemeResponse extends Response {

    private final List<String> schemes;

    /**
     * constructor for {@code SchemeResponse}
     * @param requestId request id
     * @param schemes list of schemes
     */
    public SchemeResponse(String requestId,
                          List<String> schemes) {
        super(requestId);
        this.schemes = schemes;
    }

    /**
     * get the list of schemes
     * @return the list of schemes
     */
    public List<String> getSchemes() {
        return schemes;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (schemes != null ? schemes.hashCode() : 0);
        return result;
    }

    /**
    * if the param is a {@code SchemeResponse}, compare contents for equality
    * @param o the object
    * @return whether or not the param object is equal to this object
    */
    @Override
    public boolean equals(Object o) {
        if (o instanceof SchemeResponse) {
            SchemeResponse that = (SchemeResponse) o;
            return super.equals(o)
                    && schemes != null ? schemes.equals(that.getSchemes()) : that.schemes == null;
        } else {
            return false;
        }
    }
}