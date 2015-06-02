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

/**
 * Response data model for name matcher
 */
public final class NameMatcherResponse extends Response {
    private final NameMatcherResult result;

    /**
     * Constructor for {@code NameMatcherResponse}
     * @param requestId request id
     * @param result name matcher result
     */
    public NameMatcherResponse(String requestId,
                               NameMatcherResult result
    ) {
        super(requestId);
        this.result = result;
    }

    /**
     * Gets the name matcher result
     * @return name matcher result
     */
    public NameMatcherResult getResult() {
        return result;
    }

    @Override
    public int hashCode() {
        return result != null ? result.hashCode() : 0;
    }

    /**
     * if the param is a {@code NameMatcherResponse}, compare contents for equality
     * @param o the object
     * @return whether or not the param object is equal to this object
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof NameMatcherResponse) {
            NameMatcherResponse that = (NameMatcherResponse) o;
            return result != null ? result.equals(that.getResult()) : that.result == null;
        } else {
            return false;
        }
    }
}