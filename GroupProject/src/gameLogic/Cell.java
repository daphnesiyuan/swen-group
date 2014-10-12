package gameLogic;

import java.io.Serializable;

public class Cell implements Serializable {

	private static final long serialVersionUID = 7833445205893995697L;

	private int batteryLife;

	Avatar avatar;

	boolean charging;


	public Cell(Avatar avatar){

		this.avatar = avatar;

		batteryLife = 500;

		charging = false;


	}

	public Cell(Avatar avatar, int batteryLife){		//ANTONIA: For use when loading
		this.avatar = avatar;
		this.batteryLife = batteryLife;
		charging = false;

	}

	public void useBattery(){
		batteryLife--;
	}

	public void useExtraBattery(){
		batteryLife-=5;
	}


	public void chargeBattery(){
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
