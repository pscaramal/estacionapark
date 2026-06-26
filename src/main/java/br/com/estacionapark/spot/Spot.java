package br.com.estacionapark.spot;

public class Spot {

    private final Long id;
    private final Long sectorId;
    private final Double latitude;
    private final Double longitude;
    private final Boolean occupied;

    public Long getId() {
        return id;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    private Spot (Long id, Long sectorId, Double latitude, Double longitude, Boolean occupied) {
        this.id = id;
        this.sectorId = sectorId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.occupied = occupied;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "id=" + id +
                ", sectorId=" + sectorId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", occupied=" + occupied +
                '}';
    }

    public static SpotBuilder builder() {
        return new SpotBuilder();
    }

    public static class SpotBuilder {
        private Long id;
        private Long sectorId;
        private Double latitude;
        private Double longitude;
        private Boolean occupied;

        public SpotBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SpotBuilder sectorId(Long sectorId) {
            this.sectorId = sectorId;
            return this;
        }

        public SpotBuilder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public SpotBuilder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public SpotBuilder occupied(Boolean occupied) {
            this.occupied = occupied;
            return this;
        }

        public Spot build() {
            return new Spot(this.id, this.sectorId, this.latitude, this.longitude, this.occupied);
        }
    }
}
