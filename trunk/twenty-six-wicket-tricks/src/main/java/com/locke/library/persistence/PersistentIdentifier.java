package com.locke.library.persistence;

public class PersistentIdentifier implements IPrimaryKey
{
	private static final long serialVersionUID = -1650658794682492194L;

	private Long id;

	public Long getId()
	{
		return id;
	}
}
