package com.ecimio.xcomm.repo;

import com.ecimio.xcomm.model.Settings;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SettingsRepository extends ReactiveCrudRepository<Settings, String> {
}
