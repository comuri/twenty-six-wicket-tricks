package com.locke.library.persistence;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Embeddable
public class PersistentIdentifier implements IPrimaryKey
{
	private static final long serialVersionUID = -1650658794682492194L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object)
	{
		if (object instanceof PersistentIdentifier)
		{
			PersistentIdentifier that = (PersistentIdentifier) object;
			return that.id.equals(this.id);
		}
		return false;
	}
	
	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return id.hashCode();
	}
}
