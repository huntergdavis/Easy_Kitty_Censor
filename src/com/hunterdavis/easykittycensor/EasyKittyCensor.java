package com.hunterdavis.easykittycensor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class EasyKittyCensor extends Activity {

	int SELECT_PICTURE = 22;
	Panel mypanel = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mypanel = (Panel) findViewById(R.id.SurfaceView01);

		// Create an anonymous implementation of OnClickListener
		OnClickListener loadButtonListner = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked

				// in onCreate or any event where your want the user to
				// select a file
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Source Photo"),
						SELECT_PICTURE);
			}
		};

		// Create an anonymous implementation of OnClickListener
		OnClickListener saveButtonListner = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				// Boolean didWeSave = saveImage(v.getContext());
				mypanel.saveImage(v.getContext(),v);
			}
		};
		
		// Create an anonymous implementation of OnClickListener
		OnClickListener undoButtonListner = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				// Boolean didWeSave = saveImage(v.getContext());
				mypanel.undo();
			}
		};

		Button loadButton = (Button) findViewById(R.id.loadButton);
		loadButton.setOnClickListener(loadButtonListner);

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(saveButtonListner);
		
		Button undoButton = (Button) findViewById(R.id.undoButton);
		undoButton.setOnClickListener(undoButtonListner);
		
		
		// our seek bar stuff
		// implement a seekbarchangelistener for this class
		SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				mypanel.setScaleValue(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		};

		SeekBar onlySeekBar = (SeekBar) findViewById(R.id.kittySize);
		onlySeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
		

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

	} // end of oncreate

	protected void onPause() {
		super.onPause();
		mypanel.terminateThread();
		System.gc();
	}

	protected void onResume() {
		super.onResume();
		if (mypanel.surfaceCreated == true) {
			mypanel.createThread(mypanel.getHolder());
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				mypanel.setUri(selectedImageUri);
			}
		}
	}

}