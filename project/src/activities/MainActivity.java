package activities;
import com.example.missedcallsms.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import app.Preferences;


public class MainActivity extends Activity {

	private EditText messageText;
	private Button saveButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		messageText = (EditText) findViewById(R.id.message_text);
		messageText.setText(Preferences.getResponseMessage(this));
		
		saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Preferences.setResponseMessage(MainActivity.this, messageText.getText().toString());
			}
		});
		
	}
	
}
