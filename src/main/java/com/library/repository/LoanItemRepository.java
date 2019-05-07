package com.library.repository;

import com.library.domain.LoanItem;
import org.springframework.data.repository.CrudRepository;

public interface LoanItemRepository extends CrudRepository<LoanItem, Long> {
}
