package gameLogic;

import java.io.Serializable;

public class Cell implements Serializable {

	private static final long serialVersionUID = 7833445205893995697L;

	private int batteryLife;

	private Avatar avatar;

	private boolean charging;


	public Cell(Avatar avatar){

		this.avatar = avatar;
		this.batteryLife = 300;
		this.charging = false;

	}

	public Cell(Avatar avatar, int batteryLife){		//ANTONIA: For use when loading
		this.avatar = avatar;
		this.batteryLife = batteryLife;
		this.charging = false;

	}

	public void decBattery(){
		batteryLife--;
	}

	public void decExtraBattery(){
		batteryLife-=5;
	}


	public void incBattery(){
		batteryLife++;
	}

	public int getBatteryLife(){
		return batteryLife;
	}

	public boolean isCharging(){
		return charging;
	}

	public void setCharging(boolean charging){
		this.charging = charging;

	}

	public void setBatteryLife(int batteryLife) {
		this.batteryLife = batteryLife;
	}


}
