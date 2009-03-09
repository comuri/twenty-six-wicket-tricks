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
package com.locke.library.utilities.strings;

import java.lang.reflect.Method;

/**
 * Takes a method name and allows extraction of any base name and prefix.
 * 
 * @author jlocke
 */
public class MethodName
{
    private final String fullName;
    private String name;
    private String prefix;

    public MethodName(final Method method)
    {
        this(method.getName());
    }

    public MethodName(String fullName)
    {
        this.fullName = fullName;
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        if (this.name == null)
        {
            extract();
        }
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    public String getPrefix()
    {
        if (this.prefix == null)
        {
            extract();
        }
        return this.prefix;
    }

    private void extract()
    {
        for (final String prefix : new String[] { "get", "set", "as" })
        {
            if (this.fullName.startsWith(prefix))
            {
                this.name = name(this.fullName, prefix);
                this.prefix = prefix;
                break;
            }
        }
        if (this.name == null)
        {
            this.name = this.fullName;
            this.prefix = "";
        }
    }

    /**
     * @param name
     *            The name
     * @param prefix
     *            The prefix to remove
     * @return The base name, without the prefix
     */
    private String name(final String name, final String prefix)
    {
        return name.substring(prefix.length(), prefix.length() + 1).toLowerCase()
               + name.substring(prefix.length() + 1);
    }
}
