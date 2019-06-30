package com.ecimio.xcomm.endpoint;

import com.ecimio.xcomm.model.Template;
import com.ecimio.xcomm.repo.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/template", consumes = APPLICATION_JSON_VALUE)
public class TemplateController {

    private TemplateRepository templateRepository;

    public TemplateController(@Autowired final TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @GetMapping
    public Flux<Template> getAll() {
        return templateRepository.findAll();
    }

    @PostMapping
    public Mono<Template> create(@Valid @RequestBody Template template) {
        return templateRepository.save(template);
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Template>> getById(@PathVariable(value = "id") String id) {
        return templateRepository.findById(id)
                .map(saved -> ResponseEntity.ok(saved))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public Mono<Template> createOrUpdate(@PathVariable(value = "id") String id,
                                         @RequestBody Template template) {
        return templateRepository.save(new Template(id, template.getMessage()));
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable(value = "id") String id) {
        return templateRepository.deleteById(id);
    }

}
