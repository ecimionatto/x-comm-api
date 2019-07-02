package com.ecimio.xcomm.repo;

import com.ecimio.xcomm.model.Template;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TemplateRepository extends ReactiveCrudRepository<Template, String> {

}
