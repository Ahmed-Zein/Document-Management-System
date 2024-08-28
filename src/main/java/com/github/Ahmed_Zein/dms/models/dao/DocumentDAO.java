package com.github.Ahmed_Zein.dms.models.dao;

import com.github.Ahmed_Zein.dms.models.Document;
import org.springframework.data.repository.ListCrudRepository;

public interface DocumentDAO extends ListCrudRepository<Document, Long> {
}
