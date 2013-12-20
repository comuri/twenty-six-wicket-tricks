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

package com.locke.library.persistence.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import com.locke.library.persistence.dao.query.Clause;

/**
 * @author jlocke
 */
public class ClauseList extends ArrayList<Clause>
{
    private static final long serialVersionUID = -6862554357767675069L;

    public ClauseList()
    {
    }

    public ClauseList(final Clause... clauses)
    {
        for (final Clause clause : clauses)
        {
            add(clause);
        }
    }

    /**
     * Finds a given clause by type if it was passed in to the constructor
     * 
     * @param <C>
     *            Clause type
     * @param type
     *            The type of clause desired
     * @return The clause
     */
    public <C extends Clause> C find(final Class<C> type)
    {
        final List<C> found = findAll(type);
        if (found.size() == 0)
        {
            return null;
        }
        if (found.size() > 1)
        {
            throw new IllegalStateException("More than one clause of type " + type + " found");
        }
        return found.get(0);
    }

    /**
     * Finds a given clause by type if it was passed in to the constructor
     * 
     * @param <C>
     *            Clause type
     * @param type
     *            The type of clause desired
     * @return The clause
     */
    @SuppressWarnings("unchecked")
    public <C extends Clause> List<C> findAll(final Class<C> type)
    {
        final List<C> list = new ArrayList<C>();
        for (final Clause clause : this)
        {
            if (clause.getClass().equals(type))
            {
                list.add((C)clause);
            }
        }
        return list;
    }
}
