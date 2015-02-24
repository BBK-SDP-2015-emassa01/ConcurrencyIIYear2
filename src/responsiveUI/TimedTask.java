package responsiveUI;

import java.util.Scanner;


public class TimedTask implements Runnable {
	
	public int time2sleep = 0;
	public int idNum;
	
	public TimedTask(int id, int ms){
		if(ms >1000) {time2sleep = 1000;}
		time2sleep = ms;
		idNum = (int) Thread.currentThread().getId();
	}
	
	
	@Override
	public void run(){
		System.out.println("Enter time in ms to sleep <1000 ms please: ");
		int time2sleep = new Scanner(System.in).nextInt();
		if(time2sleep >1000) {time2sleep = 1000;}
		try {
			
			System.out.println("Task " + idNum + " start.");
			
			Thread.sleep(time2sleep);
			
			System.out.println("Task " + idNum + " finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString(){
		return this.idNum + "";
	}
	

}
