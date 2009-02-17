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

	public Long getId()
	{
		return id;
	}
}
