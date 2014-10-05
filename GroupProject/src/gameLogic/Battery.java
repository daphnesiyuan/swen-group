package gameLogic;

public class Battery extends Thread {


	private double batteryLife;

	Avatar avatar;

	boolean charging;


	public Battery(Avatar avatar){

		this.avatar = avatar;

		batteryLife = 100;

		//this.start();

		charging = true;

	}

	private void useBattery(){
		batteryLife--;

	}

	private void chargeBattery(){
		batteryLife++;

	}

	public void iMoved(){


	}

	public boolean isCharging(){
		return charging;
	}

	public void setCharging(boolean charging){
		this.charging = charging;

	}

	public double getBatteryLife(){
		return batteryLife;
	}


	@Override
	public void run() {
		while(true){

			if(charging){
				if(batteryLife<100){

				}
				else{

				}
			}

			else{

			}
		}

	}

}
