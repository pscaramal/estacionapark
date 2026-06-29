package br.com.estacionapark.parking.domain;

public record ParkingOccupancy(
        int occupied,
        int total
) {
    public int percentage() {
        return (int) ((occupied * 100.0) / total);
    }
}
