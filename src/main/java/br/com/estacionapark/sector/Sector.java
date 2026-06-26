package br.com.estacionapark.sector;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Sector {

    private final Long id;
    private final String code;
    private final BigDecimal basePrice;
    private final Integer maxCapacity;
    private final LocalTime openHour;
    private final LocalTime closeHour;
    private final Integer durationLimitMinutes;

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public LocalTime getOpenHour() {
        return openHour;
    }

    public LocalTime getCloseHour() {
        return closeHour;
    }

    public Integer getDurationLimitMinutes() {
        return durationLimitMinutes;
    }

    private Sector (Long id, String code, BigDecimal basePrice, Integer maxCapacity,
                    LocalTime openHour, LocalTime closeHour, Integer durationLimitMinutes) {
        this.id = id;
        this.code = code;
        this.basePrice = basePrice;
        this.maxCapacity = maxCapacity;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.durationLimitMinutes = durationLimitMinutes;
    }

    public static SectorBuilder builder() {
        return new SectorBuilder();
    }

    public static class SectorBuilder {
        private Long id;
        private String code;
        private BigDecimal basePrice;
        private Integer maxCapacity;
        private LocalTime openHour;
        private LocalTime closeHour;
        private Integer durationLimitMinutes;

        public SectorBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SectorBuilder code(String code) {
            this.code = code;
            return this;
        }

        public SectorBuilder basePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public SectorBuilder maxCapacity(Integer maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public SectorBuilder openHour(LocalTime openHour) {
            this.openHour = openHour;
            return this;
        }

        public SectorBuilder closeHour(LocalTime closeHour) {
            this.closeHour = closeHour;
            return this;
        }

        public SectorBuilder durationLimitMinutes(Integer durationLimitMinutes) {
            this.durationLimitMinutes = durationLimitMinutes;
            return this;
        }

        public Sector build () {
            return new Sector(this.id, this.code, this.basePrice, this.maxCapacity, this.openHour, this.closeHour,
                    this.durationLimitMinutes);
        }
    }
}
