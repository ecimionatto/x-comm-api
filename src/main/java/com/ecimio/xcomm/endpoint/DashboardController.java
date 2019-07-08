package com.ecimio.xcomm.endpoint;

import com.ecimio.xcomm.model.Dashboard;
import com.ecimio.xcomm.repo.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping(value = "/dashboard", consumes = APPLICATION_JSON_VALUE)
public class DashboardController {

    private DashboardRepository dashboardRepository;

    public DashboardController(@Autowired final DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @GetMapping
    public Flux<Dashboard> getAll() {
        return dashboardRepository.findAll().take(1);
    }

}

