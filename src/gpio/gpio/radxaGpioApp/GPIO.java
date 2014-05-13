package gpio.gpio.radxaGpioApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;




public class GPIO{
	
	public String port;
	public int pin;	
	
	

		
	//get direction of gpio
	public String getInOut()
	{
		String command = String.format("cat /sys/class/gpio/%s/direction",this.port);		
		try {
			Log.d("debugLed", "la commande "+command);
			Process p = Runtime.getRuntime().exec(new String[] {"su", "-c", command});
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder text = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
				text.append(line);
				text.append("\n");
			}
			String retour= text.toString();
			Log.d("debugLed", "le retour "+retour);
			return retour;
		} catch (IOException e) {
			return "";
		}
	}
	
	// get state of gpio for input and output
	//test if gpio is configurate
	public int getState()
	{
		String command = String.format("cat /sys/class/gpio/%s/value",this.port);		
		try {
			Log.d("debugLed", "la commande "+command);
			Process p = Runtime.getRuntime().exec(new String[] {"su", "-c", command});
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder text = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
				text.append(line);
				text.append("\n");
			}
			try{
				
				String retour= text.toString();
				Log.d("debugLed", "le retour sans modif "+retour);
				if(retour.equals("")){
					Log.d("debugLed", "vide");
					return -1;
				}
				else
				{
				Log.d("debugLed", "le retour "+retour.substring(0, 1));
				return Integer.parseInt(retour.substring(0, 1));
				}
			}catch(NumberFormatException nfe) {
				return -1;
			}
			
		
			
		} catch (IOException e) {
			return -1;
		}
	}
	
	//set value of the output
	public boolean setState(int value){	
		String command = String.format("echo %d > /sys/class/gpio/%s/value", value,this.port);		
		Log.d("debugLed", "la commande "+command);
		try {
			String[] test = new String[] {"su", "-c", command};
			Log.d("debugLed",test.toString());
			Runtime.getRuntime().exec(test);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	// set direction
	public boolean setInOut(String direction){	
		String command = String.format("echo %s > /sys/class/gpio/%s/direction", direction,this.port);	
		Log.d("debugLed", "la commande "+command);
		try {
			Runtime.getRuntime().exec(new String[] {"su", "-c", command});
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	
	//export gpio
	public boolean activationPin(){
		String command = String.format("echo %d > /sys/class/gpio/export", this.pin);		
		try {
			Runtime.getRuntime().exec(new String[] {"su", "-c", command});
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	// unexport gpio
	public boolean desactivationPin(){
		String command = String.format("echo %d > /sys/class/gpio/unexport", this.pin);		
		try {
			Runtime.getRuntime().exec(new String[] {"su", "-c", command});
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	
	//init the pin 
	public int initPin(String direction){
		int retour=0;
		boolean ret=true;
		
		// see if gpio is already set
		retour=getState();
		Log.d("debugLed","retour state "+retour);
		if (retour==-1)
		{
			// unexport the gpio
			ret=desactivationPin();
			if(ret==false){ retour=-1; Log.d("debugLed", "erreur lors de la desactivation");}
			
			//export the gpio
			ret=activationPin();
			if(ret==false){ retour=-2; Log.d("debugLed", "erreur lors de l'activation"); }
		}
		
		
		// get If gpio direction is define
		String ret2 = getInOut();
		Log.d("debugLed", "retour inout ret2"+ret2+" direction"+direction);
		if (!ret2.contains(direction))
		{	
			// set the direction (in or out)
			ret=setInOut(direction);
			if(ret==false){ retour=-3; Log.d("debugLed", "erreur lors de la definition de la direction");}
		}
		
		return retour;
	}
	
	//Constructor
	public GPIO( int pin){
		this.port = "gpio"+pin;
		this.pin = pin;
	}


}
