package tinkercoder.tipcalculator.tipcalculator;

import java.text.DecimalFormat;

import tinkercoder.tipcalculator.navdrawer.DrawerActivity;
import tinkercoder.tipcalculator.preferenceactivity.PreferenceSetting;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TipCalculator extends DrawerActivity implements TextWatcher {

	EditText editText4, editText5, editText6, editTaxRate;
	TextView editText, editText2, editText3, editTheBill;
	int editTextResouce, numOfPpl;
	double tipPerPerson = 0, totalPerPerson = 0, billTotal = 0, theBill = 0,
			tipPercent, taxPercent;
	TextWatcher textWatcher = null;
	String theBillString;
	DecimalFormat format;
	CheckBox roundUpward;
	Spinner taxSelector;
	PreferenceSetting prefSetting = new PreferenceSetting();
	SharedPreferences sharedPref = null;
	Button longClick, btnReset;
	ArrayAdapter<CharSequence> spinnerArrayAdapter;

	private AdView adView;

	private static final int ID_IDENTIFIER = 911;
	private static final int MAX_LENGTH = 18;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (adView != null) {
			adView.pause();
		}
		super.onPause();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setInflaterOnView("tipcalculator");

		// find and load ads
		AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("98C719A04DF7111D2DDD25D764C88F8E").build();
		if (adView != null) {
			adView.loadAd(adRequest);
		}

		longClick = (Button) findViewById(R.id.buttonDel);
		btnReset = (Button) findViewById(R.id.btn_reset);

		format = new DecimalFormat("#.##");
		editTextResouce = R.drawable.custom_edit_text;

		editText = (TextView) findViewById(R.id.edit_text);
		editText2 = (TextView) findViewById(R.id.edit_text2);
		editText3 = (TextView) findViewById(R.id.edit_text3);
		editTheBill = (TextView) findViewById(R.id.the_bill);
		editText4 = (EditText) findViewById(R.id.edit_text4);
		editText5 = (EditText) findViewById(R.id.edit_text5); 
		editText6 = (EditText) findViewById(R.id.edit_text6);
		editTaxRate = (EditText) findViewById(R.id.edit_tax_rate);

		editTaxRate.setBackgroundResource(editTextResouce);
		editTaxRate.setVisibility(View.INVISIBLE);
		editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENGTH)});
		editText2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENGTH)});
		editText3.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENGTH)});
		editText4.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENGTH - 10)});
		// avoid some extreme number such as 999... to exceed the limitation of numbers
		editText6.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENGTH - 3)});
		editText4.setBackgroundResource(editTextResouce);
		editText5.setBackgroundResource(editTextResouce);
		editText6.setBackgroundResource(editTextResouce);

		// hide keyboard
		editText4.setInputType(0);
		editText5.setInputType(0);
		editText6.setInputType(0);
		editTaxRate.setInputType(0);

		roundUpward = (CheckBox) findViewById(R.id.round_upwards);
		taxSelector = (Spinner) findViewById(R.id.tax_selector);

		initPageData();

		textWatcher = createTextWatcher();
		editText4.addTextChangedListener(textWatcher);
		editText5.addTextChangedListener(textWatcher);
		editText6.addTextChangedListener(textWatcher);
		editTaxRate.addTextChangedListener(textWatcher);

		roundUpward.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				displayResult();
			}
		});

		taxSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 1) {// if before_tax is selected, then set
									// editText visible
					editTheBill.setText("The Bill (Before Tax)");
					editTaxRate.setVisibility(View.VISIBLE);
				} else {
					editTheBill.setText("The Bill (After Tax)");
					editTaxRate.setVisibility(View.INVISIBLE);
				}

				if (numOfPpl == 0) {// numOfPpl is in denominator, can not be
									// zero
					tipPerPerson = 0.00;
					totalPerPerson = 0.00;
					billTotal = 0.00;
				} else {
					calculateResult();
				}
				displayResult();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		longClick.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (editText4.isFocused()) {
					editText4.setText("");
				} else if (editText5.isFocused()) {
					editText5.setText("");
				} else if (editText6.isFocused()) {
					editText6.setText("");
				} else if (editTaxRate.isFocused()) {
					editTaxRate.setText("");
				}
				return true;
			}
		});

		btnReset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initPageData();
			}
		});

		spinnerArrayAdapter = ArrayAdapter.createFromResource(this,
				R.array.tax_selector, R.layout.text_view);
		spinnerArrayAdapter.setDropDownViewResource(R.layout.text_view);
		taxSelector.setAdapter(spinnerArrayAdapter);

		// launcher notification
		sharedPref = prefSetting.getSharedPreferences(this);

		if (sharedPref.getBoolean("disableNotification_tipCalculator", false)) {
			Log.e("ERROR", "true");
			// dont send notification
		} else {
			Log.e("ERROR", "false");
			notificationLauncher();
		}
	}

	public TextWatcher createTextWatcher() {
		Log.e("ERROR", "textwatacher");
		TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

				checkForEmptyString();

				if (isStartWithDot()) {
					// do nothing, else will thrown exception invalid double ""
				} else if (numOfPpl == 0) {
					// numOfPpl is in denominator, can not set numOfPpl equal 0
					tipPerPerson = 0.00;
					totalPerPerson = 0.00;
					billTotal = 0.00;
				} else {
					calculateResult();
				}
				// display result
				displayResult();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		};
		return textWatcher;
	}

	protected void checkForEmptyString() {
		boolean startWithDot = isStartWithDot();
		tipPercent = (editText4.getText().toString().equalsIgnoreCase("") || startWithDot) ? 0
				: Double.parseDouble(editText4.getText().toString());
		numOfPpl = (editText5.getText().toString().equalsIgnoreCase("") || startWithDot) ? 0
				: Integer.parseInt(editText5.getText().toString());
		theBill = (editText6.getText().toString().equalsIgnoreCase("") || startWithDot) ? 0
				: Double.parseDouble(editText6.getText().toString());
		taxPercent = (editTaxRate.getText().toString().equalsIgnoreCase("") || startWithDot) ? 0
				: Double.parseDouble(editTaxRate.getText().toString());
	}

	protected boolean isStartWithDot() {
		if (editText4.getText().toString().contains(".")
				&& editText4.getText().toString().length() == 1
				|| editText5.getText().toString().contains(".")
				&& editText5.getText().toString().length() == 1
				|| editText6.getText().toString().contains(".")
				&& editText6.getText().toString().length() == 1
				|| editTaxRate.getText().toString().contains(".")
				&& editTaxRate.getText().toString().length() == 1) {
			return true;
		}
		return false;
	}

	protected void calculateResult() {
		int position = taxSelector.getSelectedItemPosition();

		if (position == 0) {// after tax
			// calculate result
			tipPerPerson = (theBill / numOfPpl) * (tipPercent / 100.0);
			totalPerPerson = tipPerPerson + (theBill / numOfPpl);
			billTotal = totalPerPerson * numOfPpl;
		} else {// before tax
			// calculate result
			tipPerPerson = ((theBill * (taxPercent / 100 + 1)) / numOfPpl)
					* (tipPercent / 100.0);
			totalPerPerson = tipPerPerson
					+ ((theBill * (taxPercent / 100 + 1)) / numOfPpl);
			billTotal = totalPerPerson * numOfPpl;
		}
	}

	protected void displayResult() {
		if (roundUpward.isChecked()) {
			if (theBill == 0.0 || numOfPpl == 0) {
				// if any of user input is zero, then no need to round, jsut set
				// to zero
				editText.setText("$"
						+ Double.toString(Math.ceil(tipPerPerson)));
				editText2.setText("$"
						+ Double.toString(Math.ceil(totalPerPerson)));
				editText3.setText("$" + Double.toString(Math.ceil(billTotal)));
			} else {
				// if tip is zero percent, then should not display amount on
				// tip/person session
				if (tipPercent == 0.0) {
					editText.setText("$"
							+ Double.toString(Math.ceil(tipPerPerson)));
				} else {
					editText.setText("$"
							+ Double.toString(Math.ceil(tipPerPerson)));
				}
				editText2.setText("$"
						+ Double.toString(Math.ceil(totalPerPerson)));
				editText3.setText("$"
						+ Double.toString(Math.ceil(billTotal)));
			}
		} else {
			editText.setText("$" + format.format(tipPerPerson));
			editText2.setText("$" + format.format(totalPerPerson));
			editText3.setText("$" + format.format(billTotal));
		}
	}

	protected void notificationLauncher() {
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, PreferenceSetting.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setContentTitle(getString(R.string.notification_title))
				.setContentText(getString(R.string.notification_text))
				.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
				.setContentIntent(contentIntent);

		NotificationManager notifManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.notify(ID_IDENTIFIER, mBuilder.build());
	}

	public void clickButtonOne(View v) {
		if (editText4.isFocused()) {
			editText4.append("1");
		} else if (editText5.isFocused()) {
			editText5.append("1");
		} else if (editText6.isFocused()) {
			editText6.append("1");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("1");
		}
	}

	public void clickButtonTwo(View v) {
		if (editText4.isFocused()) {
			editText4.append("2");
		} else if (editText5.isFocused()) {
			editText5.append("2");
		} else if (editText6.isFocused()) {
			editText6.append("2");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("2");
		}
	}

	public void clickButtonThree(View v) {
		if (editText4.isFocused()) {
			editText4.append("3");
		} else if (editText5.isFocused()) {
			editText5.append("3");
		} else if (editText6.isFocused()) {
			editText6.append("3");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("3");
		}
	}

	public void clickButtonFour(View v) {
		if (editText4.isFocused()) {
			editText4.append("4");
		} else if (editText5.isFocused()) {
			editText5.append("4");
		} else if (editText6.isFocused()) {
			editText6.append("4");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("4");
		}
	}

	public void clickButtonFive(View v) {
		if (editText4.isFocused()) {
			editText4.append("5");
		} else if (editText5.isFocused()) {
			editText5.append("5");
		} else if (editText6.isFocused()) {
			editText6.append("5");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("5");
		}
	}

	public void clickButtonSix(View v) {
		if (editText4.isFocused()) {
			editText4.append("6");
		} else if (editText5.isFocused()) {
			editText5.append("6");
		} else if (editText6.isFocused()) {
			editText6.append("6");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("6");
		}
	}

	public void clickButtonSeven(View v) {
		if (editText4.isFocused()) {
			editText4.append("7");
		} else if (editText5.isFocused()) {
			editText5.append("7");
		} else if (editText6.isFocused()) {
			editText6.append("7");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("7");
		}
	}

	public void clickButtonEight(View v) {
		if (editText4.isFocused()) {
			editText4.append("8");
		} else if (editText5.isFocused()) {
			editText5.append("8");
		} else if (editText6.isFocused()) {
			editText6.append("8");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("8");
		}
	}

	public void clickButtonNine(View v) {
		if (editText4.isFocused()) {
			editText4.append("9");
		} else if (editText5.isFocused()) {
			editText5.append("9");
		} else if (editText6.isFocused()) {
			editText6.append("9");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("9");
		}
	}

	public void clickButtonZero(View v) {
		if (editText4.isFocused()) {
			editText4.append("0");
		} else if (editText5.isFocused()) {
			editText5.append("0");
		} else if (editText6.isFocused()) {
			editText6.append("0");
		} else if (editTaxRate.isFocused()) {
			editTaxRate.append("0");
		}
	}

	public void clickButtonDot(View v) {
		if (editText4.isFocused()) {
			if (editText4.getText().toString().contains(".")) {
				// avoid adding more than one decimal points
			} else {
				editText4.append(".");
			}
		} else if (editText5.isFocused()) {
			// editText5 is numOfPeople field, it is interger, skip this
		} else if (editText6.isFocused()) {
			if (editText6.getText().toString().contains(".")) {
				// avoid adding more than one decimal points
			} else {
				editText6.append(".");
			}
		} else if (editTaxRate.isFocused()) {
			if (editTaxRate.getText().toString().contains(".")) {
				// avoid adding more than one devimal points
			} else {
				editTaxRate.append(".");
			}
		}
	}

	public void clickButtonDel(View v) {
		String str4 = editText4.getText().toString();
		String str5 = editText5.getText().toString();
		String str6 = editText6.getText().toString();
		String strTaxRate = editTaxRate.getText().toString();

		if (editText4.isFocused()) {
			if (str4.length() > 0) {
				str4 = str4.substring(0, str4.length() - 1);
				editText4.setText(str4);
			}
		} else if (editText5.isFocused()) {
			if (str5.length() > 0) {
				str5 = str5.substring(0, str5.length() - 1);
				editText5.setText(str5);
			}
		} else if (editText6.isFocused()) {
			if (str6.length() > 0) {
				str6 = str6.substring(0, str6.length() - 1);
				editText6.setText(str6);
			}
		} else if (editTaxRate.isFocused()) {
			if (strTaxRate.length() > 0) {
				strTaxRate = strTaxRate.substring(0, strTaxRate.length() - 1);
				editTaxRate.setText(strTaxRate);
			}
		}
	}

	protected void initPageData() {
		editText.setText("0.00");
		editText2.setText("0.00");
		editText3.setText("0.00");
		editText4.setText("15");
		editText5.setText("1");
		editText6.setText("");
		editTaxRate.setText("");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}
}
