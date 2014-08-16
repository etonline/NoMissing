package edu.ntust.cs.idsl.nomissing.model;

import edu.ntust.cs.idsl.nomissing.R;

public enum Region {

	NORTHERN(R.string.northern_region, R.string.northern_region_cities),
	CENTRAL(R.string.central_region, R.string.central_region_cities),
	SOUTHERN(R.string.southern_region, R.string.southern_region_cities),
	EASTERN(R.string.eastern_region, R.string.eastern_region_cities),
	OFFSHORE_ISLANDS(R.string.offshore_islands_region, R.string.offshore_islands_region_cities);
	
	private int name;
	private int cities;
	
	private Region(int name, int cities) {
		this.name = name;
		this.cities = cities;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getCities() {
		return cities;
	}

	public void setCities(int cities) {
		this.cities = cities;
	}
	
}
