package com.locke.library.persistence.dao;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.PersistentIdentifier;

public interface IDao<T extends IPersistent<PersistentIdentifier>> extends
		IPrimaryKeyedDao<T, PersistentIdentifier>
{
}
