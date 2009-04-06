/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.locke.library.persistence.dao.query.clauses;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.dao.query.AbstractClause;

/**
 * AbstractClause for query-by-example matching
 * 
 * @author Jonathan Locke
 */
public class Match<T extends IPersistent<?>> extends AbstractClause
{
    /**
     * The example object
     */
    private final T object;

    /**
     * @param object
     *            The object with properties that must match
     */
    public Match(T object)
    {
        this.object = object;
    }

    /**
     * @return The object with properties that must match
     */
    public T getObject()
    {
        return this.object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "[Match " + this.object.getClass() + "]";
    }
}
