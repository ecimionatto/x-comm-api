package com.ecimio.xcomm.endpoint;

import com.ecimio.xcomm.model.Settings;
import com.ecimio.xcomm.repo.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping(value = "/settings", consumes = APPLICATION_JSON_VALUE)
public class SettingsController {

    private SettingsRepository settingsRepository;

    public SettingsController(@Autowired final SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @GetMapping
    public Flux<Settings> getAll() {
        return settingsRepository.findAll().take(1);
    }

    @PutMapping("{id}")
    public Mono<Settings> createOrUpdate(@PathVariable(value = "id") String id,
                                              @RequestBody Settings settings) {
        return settingsRepository.save(new Settings(id, settings.getSlackWebHook(),
                settings.getSmtpUser(),
                settings.getSmtpPass()));
    }


}

