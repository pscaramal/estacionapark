package br.com.estacionapark.api;

import br.com.estacionapark.api.response.RevenueResponse;
import br.com.estacionapark.revenue.RevenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class RevenueController {

    private static final Logger logger = LoggerFactory.getLogger(RevenueController.class);

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("/revenue")
    public RevenueResponse revenue(@RequestParam LocalDate date,
                                   @RequestParam String sector) {
        logger.info("getting revenue, date={}, sector={}", date, sector);

        RevenueResponse revenue = revenueService.getRevenue(date, sector);
        logger.info("revenue calculated = {}", revenue);

        return revenue;
    }
}
