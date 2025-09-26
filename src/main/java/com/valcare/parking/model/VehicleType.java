package com.valcare.parking.model;

public enum VehicleType {
	TWO_WHEELER(20), FOUR_WHEELER(30);

	private final int ratePerHour;

	VehicleType(int ratePerHour) {
		this.ratePerHour = ratePerHour;
	}

	public int getRatePerHour() {
		return ratePerHour;
	}
}
