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
import java.util.Objects;

public class DependenciesResponse extends Response {
    private final List<Dependency> dependencies;
    private final List<String> tokens;

    public DependenciesResponse(List<Dependency> dependencies,
                                List<String> tokens) {
        this.dependencies = dependencies;
        this.tokens = tokens;
    }

    public List<Dependency> getDependencies() {
        return this.dependencies;
    }

    public List<String> getTokens() {
        return this.tokens;
    }

    public boolean equals(Object o) {
        if (!(o instanceof DependenciesResponse)) {
            return false;
        } else {
            DependenciesResponse that = (DependenciesResponse) o;
            return Objects.equals(this.dependencies, that.dependencies)
                    && Objects.equals(this.tokens, that.tokens);
        }
    }

    public int hashCode() {
        return Objects.hash(this.dependencies, this.tokens);
    }

}
