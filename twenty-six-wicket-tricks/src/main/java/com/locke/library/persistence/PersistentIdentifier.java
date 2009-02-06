package com.locke.library.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Embeddable
public class PersistentIdentifier implements IPrimaryKey
{
	private static final long serialVersionUID = -1650658794682492194L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	public Long getId()
	{
		return id;
	}
}
