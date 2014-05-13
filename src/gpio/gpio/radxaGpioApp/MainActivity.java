package gpio.gpio.radxaGpioApp;


import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		final ToggleButton button = (ToggleButton) findViewById(R.id.button1);
		final GPIO gpio175 = new GPIO(175);	
		
		//set direction pin to "out" 
		//can be replace by "in" if you want getsatate  of an input
		gpio175.initPin("out");
		
		// get state to init button
		int retourState=gpio175.getState();
		if (retourState!=-1)
		{
			if(retourState==0)
			{
				button.setChecked(false);
			}
			else
			{
				button.setChecked(true);
			}
		}
	
		//on click change state of gpio
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Read output state and toggle it
				gpio175.getInOut();
				if(button.isChecked()){
					gpio175.setState(1);
					Log.d("debugLed", "on");
				}
				else
				{
					gpio175.setState(0);
					Log.d("debugLed", "off");
				}
				
					
			}
		});
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
