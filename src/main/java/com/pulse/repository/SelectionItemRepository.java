package com.pulse.repository;

import com.pulse.model.SelectionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionItemRepository extends JpaRepository<SelectionItem, Long> {
}
