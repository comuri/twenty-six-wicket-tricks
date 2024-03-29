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
package com.locke.tricks.u;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.parse.metapattern.IntegerGroup;
import org.apache.wicket.util.parse.metapattern.MetaPattern;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;

import com.locke.Trick;

/**
 * Trick 'U'
 * 
 * @author Jonathan Locke
 */
@SuppressWarnings("serial")
public class U extends Trick
{

    IDataProvider<Utility> utilitiesDataProvider = new IDataProvider<Utility>()
    {

        public void detach()
        {
        }

        public Iterator<? extends Utility> iterator(final int first, final int count)
        {
            return U.this.utilities.subList(first, count).iterator();
        }

        public IModel<Utility> model(final Utility object)
        {
            return new Model<Utility>(object);
        }

        public int size()
        {
            return U.this.utilities.size();
        }
    };

    private final List<Utility> utilities = new ArrayList<Utility>();

    @SuppressWarnings("unchecked")
    public U()
    {

        final IColumn<Utility>[] columns = new IColumn[2];
        columns[0] = new PropertyColumn<Utility>(new Model<String>("Code"), "code", "code");
        columns[1] = new PropertyColumn<Utility>(new Model<String>("Output"), "output", "output");

        final DataTable<Utility> dataTable =
                new DataTable<Utility>("utilities", columns, this.utilitiesDataProvider,
                                       Integer.MAX_VALUE);
        dataTable.addTopToolbar(new HeadersToolbar(dataTable, new ISortStateLocator()
        {

            private ISortState sortState = new SingleSortState();

            public ISortState getSortState()
            {
                return this.sortState;
            }

            public void setSortState(final ISortState state)
            {
                this.sortState = state;
            }
        }));
        add(dataTable);

        this.utilities.add(new Utility("Time.now().toString()")
        {

            @Override
            public String getOutput()
            {
                return Time.now().toString();
            }
        });
        this.utilities.add(new Utility("Duration.ONE_WEEK.toString()")
        {

            @Override
            public String getOutput()
            {
                return Duration.ONE_WEEK.toString();
            }
        });
        this.utilities.add(new Utility("Duration.ONE_WEEK.add(Duration.ONE_DAY).toString()")
        {

            @Override
            public String getOutput()
            {
                return Duration.ONE_WEEK.add(Duration.ONE_DAY).toString();
            }
        });
        this.utilities.add(new Utility("Time.now().add(Duration.ONE_WEEK).toString()")
        {

            @Override
            public String getOutput()
            {
                return Time.now().add(Duration.ONE_WEEK).toString();
            }
        });
        this.utilities.add(new Utility("Bytes.valueOf(\"512K\") + Bytes.megabytes(1.3)")
        {

            @Override
            public String getOutput()
            {
                return Bytes.bytes(Bytes.valueOf("512K").bytes() + Bytes.megabytes(1.3).bytes())
                        .toString();
            }
        });
        this.utilities.add(new Utility("Parsing \'13 + 13\' using MetaPattern")
        {

            @Override
            public String getOutput()
            {
                final IntegerGroup a = new IntegerGroup(MetaPattern.DIGITS);
                final IntegerGroup b = new IntegerGroup(MetaPattern.DIGITS);
                final MetaPattern sum =
                        new MetaPattern(new MetaPattern[] { a, MetaPattern.OPTIONAL_WHITESPACE,
                                                           MetaPattern.PLUS,
                                                           MetaPattern.OPTIONAL_WHITESPACE, b });
                final Matcher matcher = sum.matcher("13 + 13");
                if (matcher.matches())
                {
                    return Integer.toString(a.getInt(matcher) + b.getInt(matcher));
                }
                return "Failed to match.";
            }
        });
    }

    private abstract class Utility implements Serializable
    {

        @SuppressWarnings("unused")
        String code;

        public Utility(final String code)
        {
            this.code = code;
        }

        public abstract String getOutput();
    }
}
