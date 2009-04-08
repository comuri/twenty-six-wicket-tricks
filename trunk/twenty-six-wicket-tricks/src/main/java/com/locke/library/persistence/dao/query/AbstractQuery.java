package com.locke.library.persistence.dao.query;

import java.io.Serializable;

import com.locke.library.persistence.IPersistent;

public abstract class AbstractQuery<T extends IPersistent<PK>, PK extends Serializable>
{
    /**
     * @return Number of objects matching this query
     */
    public abstract int countMatches();

    /**
     * Delete all objects matching this query
     */
    public abstract void delete();

    /**
     * @return First matching object
     */
    public abstract T firstMatch();

    /**
     * @param clauses
     *            Clauses to use in query
     * @return All matching objects
     */
    public abstract Iterable<T> matches();

    /**
     * @return The query text (for debugging)
     */
    public abstract String query();
}
