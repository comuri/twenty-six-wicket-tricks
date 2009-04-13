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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.apache.wicket.util.lang.Classes;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.dao.IDao;
import com.locke.library.persistence.dao.IQuery;
import com.locke.library.persistence.dao.query.Clause;
import com.locke.library.persistence.dao.query.clauses.Match;
import com.locke.library.utilities.strings.MethodName;

/**
 * Base class for JPA DAO implementations
 * 
 * @author Jonathan Locke
 * @param <T>
 */
public abstract class AbstractJpaDao<T extends IPersistent<PK>, PK extends Serializable>
                                                                                         implements
                                                                                         IDao<T, PK>
{
    /**
     * Map from object class to dao
     */
    private static final Map<Class<?>, AbstractJpaDao<?, ?>> daoForClass =
            new HashMap<Class<?>, AbstractJpaDao<?, ?>>();

    /**
     * Class of object managed by this DAO
     */
    final Class<T> type;

    /**
     * @param type
     *            Type of object managed by this DAO
     */
    public AbstractJpaDao(final Class<T> type)
    {
        this.type = type;
        daoForClass.put(type, this);
    }

    /**
     * {@inheritDoc}
     */
    public void attach(final T object)
    {
        processProperties(object, PropertyProcessingMode.ATTACH);
    }

    /**
     * {@inheritDoc}
     */
    public void create(final T object)
    {
        getEntityManager().persist(object);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final T object)
    {
        getEntityManager().remove(object);
    }

    /**
     * {@inheritDoc}
     */
    public T ensure(final T object)
    {
        final T found = query(new Clause[] { new Match<T>(object) }).firstMatch();
        if (found != null)
        {
            return found;
        }
        processProperties(object, PropertyProcessingMode.ENSURE);
        create(object);
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof AbstractJpaDao)
        {
            final AbstractJpaDao<?, ?> that = (AbstractJpaDao<?, ?>)object;
            return that.type.equals(this.type);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return this.type.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public void lock(final T object, final LockType lockType)
    {
        if (lockType == LockType.READ)
        {
            getEntityManager().lock(object, LockModeType.READ);
        }
        else if (lockType == LockType.WRITE)
        {
            getEntityManager().lock(object, LockModeType.WRITE);
        }
        else
        {
            throw new UnsupportedOperationException("Unsupported lock type " + lockType);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <C extends Clause> IQuery<T> query(final C... clauses)
    {
        return new JpaQuery<T, PK>(this, new ClauseList(clauses));
    }

    /**
     * {@inheritDoc}
     */
    public T read(final PK id)
    {
        return getEntityManager().find(this.type, id);
    }

    /**
     * {@inheritDoc}
     */
    public void update(final T object)
    {
        getEntityManager().persist(object);
    }

    /**
     * @return Entity manager to use
     */
    protected abstract EntityManager getEntityManager();

    /**
     * @return The name of this DAO
     */
    String getName()
    {
        return Classes.simpleName(this.type);
    }

    /**
     * Attaches or ensures all properties of the given object whose getters are
     * annotated with {@link Attachable} or {@link Ensurable}
     * 
     * @param object
     *            The object whose properties should be attached or ensured
     * @param mode
     *            Either Mode.ENSURE or Mode.ATTACH
     */
    private void processProperties(final T object, final PropertyProcessingMode mode)
    {
        for (final Method method : object.getClass().getMethods())
        {
            try
            {
                final boolean isAttachable = method.getAnnotation(Attachable.class) != null;
                final boolean isEnsurable = method.getAnnotation(Ensurable.class) != null;
                if ((isAttachable && mode == PropertyProcessingMode.ATTACH)
                    || (isEnsurable && mode == PropertyProcessingMode.ENSURE))
                {
                    final MethodName methodName = new MethodName(method);
                    if (!methodName.isGetter())
                    {
                        throw new IllegalStateException("Attachable and/or Ensurable method '"
                                                        + method + "' is not a getter");
                    }
                    final Method writeMethod =
                            object.getClass().getMethod(methodName.prefixed("set"),
                                                        method.getReturnType());
                    if (writeMethod == null)
                    {
                        throw new IllegalStateException("No corresponding setter for " + method);
                    }
                    processProperty(object, method, writeMethod, mode);
                }
            }
            catch (final SecurityException e)
            {
                e.printStackTrace();
            }
            catch (final NoSuchMethodException e)
            {
                e.printStackTrace();
            }
            catch (final IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch (final IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (final InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Attaches or ensures the given property of the given object
     * 
     * @param object
     *            The object having the property
     * @param readMethod
     *            The property getter
     * @param writeMethod
     *            The property setter
     * @param mode
     *            Either Mode.ENSURE or Mode.ATTACH
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("unchecked")
    private void processProperty(final T object, final Method readMethod, final Method writeMethod,
                                 final PropertyProcessingMode mode)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
        SecurityException, NoSuchMethodException
    {
        // Get property value from getter
        final Object value = readMethod.invoke(object, (Object[])null);
        if (value instanceof IPersistent)
        {
            // Locate DAO for the value
            final AbstractJpaDao<?, ?> dao = daoForClass.get(value.getClass());

            // Query DB for value
            final JpaQuery<IPersistent<?>, ?> query =
                    new JpaQuery(dao, new ClauseList(new Match((IPersistent)value)));
            final IPersistent<?> found = query.firstMatch();

            // If the value was found
            if (found != null)
            {
                // attach found value to property
                writeMethod.invoke(object, found);
            }
            else
            {
                // If we're saving un-found values
                if (mode == PropertyProcessingMode.ENSURE)
                {
                    // save transient value
                    getEntityManager().persist(value);
                }
            }
        }
    }

    enum PropertyProcessingMode
    {
        ATTACH, ENSURE
    }
}
