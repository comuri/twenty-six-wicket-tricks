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

package com.locke.library.persistence.dao.query;

/**
 * Holds text being built up for a query language statement or clause.
 * 
 * @author jlocke
 */
public class QueryText
{
    private final StringBuilder text = new StringBuilder();

    public void add(final String string)
    {
        if (this.text.length() > 0)
        {
            append(" ");
        }
        append(string);
    }

    public void and(final String string)
    {
        if (this.text.length() > 0)
        {
            append(" and ");
        }
        append(string);
    }

    public void append(final String string)
    {
        this.text.append(string);
    }

    public void clear()
    {
        this.text.delete(0, this.text.length());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return this.text.toString();
    }
}
