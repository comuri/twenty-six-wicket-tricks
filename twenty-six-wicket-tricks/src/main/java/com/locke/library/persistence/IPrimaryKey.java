package com.locke.library.persistence;

import java.io.Serializable;

import org.apache.wicket.model.IDetachable;

/**
 * Super-type, for safety, to be implemented by all objects to be used as
 * primary keys.
 * <p>
 * This interface implements {@link Serializable} since all primary keys in
 * Wicket-land must be {@link Serializable} so the id can be stored in the
 * session when using {@link IDetachable} models.
 * 
 * @author jlocke
 */
public interface IPrimaryKey extends Serializable
{
}
