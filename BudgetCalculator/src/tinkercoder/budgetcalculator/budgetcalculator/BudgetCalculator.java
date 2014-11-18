package tinkercoder.budgetcalculator.budgetcalculator;

import java.text.DecimalFormat;

import tinkercoder.budgetcalculator.customdialog.CustomDialog;
import tinkercoder.budgetcalculator.navdrawer.DrawerActivity;
import tinkercoder.budgetcalculator.preferenceactivity.PreferenceSetting;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class BudgetCalculator extends DrawerActivity {

	/*
	 * hd: stand for household, uni: stand for university in: income ex: expense
	 */
	InputMethodManager imm;
	SharedPreferences prefs;
	Button household_tab, university_tab, hd_cal_btn, hd_cal_btn2, uni_cal_btn,
			uni_cal_btn2, reset_btn, uni_reset_btn, hdAddCusIn, hdAddCusEx,
			uniAddCusIn, uniAddCusEx, hdDelCusIn, hdDelCusEx, uniDelCusIn,
			uniDelCusEx;
	Button hdRestartBtn, uniRestartBtn;
	FrameLayout household_layout, university_layout;
	TextView result_display, uni_result_display;
	SharedPreferences sharedPref = null;
	PreferenceSetting prefSetting = new PreferenceSetting();
	CustomDialog customDialog = null;
	private static final int ID_IDENTIFIER = 922;
	Context context = this;
	// flag to detect if user successuflly added a custom item
//	private boolean isAdded = true;
	private AdView adView, uniAdView;
	// ID index for custom editText location
	// start with 3 because the first 4 items are predefined
//	private int hdInId = 3;
	// start with 12 because the first 13 items are predefined
//	private int hdExId = 12;
//	private int uniInId = 5, uniExId = 11;
	// promote user input for custom text name
	String inputTxt = "";

	// custom layout elements
	TableLayout hdTableLIn, hdTableLEx, uniTableLIn, uniTableLEx;
	TableRow tr;
	TextView textView;
	EditText editTxt;

	// for household
	EditText edit_emp_income, edit_other_income, edit_grocery, edit_internet,
			edit_cable, edit_phone_bill, edit_utility_water,
			edit_utility_electricity, edit_morgage, edit_house_rental,
			edit_transportation, edit_auto_insurance, edit_gas,
			edit_other_expense;

	// for university
	EditText uni_edit_osap_loan, uni_edit_bursary, uni_edit_scholarship,
			uni_edit_personal_saving, uni_edit_tuition, uni_edit_textbook,
			uni_edit_utility_electricity, uni_edit_grocery,
			uni_edit_house_rental, uni_edit_transportation, uni_edit_internet,
			uni_edit_cable, uni_edit_other_expense, uni_edit_phone_bill,
			uni_edit_utility_water;

	int editTextResource;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
		if (uniAdView != null) {
			uniAdView.resume();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (adView != null) {
			adView.pause();
		}
		if (uniAdView != null) {
			uniAdView.pause();
		}
		super.onPause();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (adView != null) {
			adView.destroy();
		}
		if (uniAdView != null) {
			uniAdView.destroy();
		}
		super.onDestroy();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setInflaterOnView("budgetcalculator");

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		// find and load ads
		AdView adView = (AdView) findViewById(R.id.adView);
		AdView uniAdView = (AdView) findViewById(R.id.uni_adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("98C719A04DF7111D2DDD25D764C88F8E").build();
		if (adView != null) {
			adView.loadAd(adRequest);
		}
		if (uniAdView != null) {
			uniAdView.loadAd(adRequest);
		}

		household_tab = (Button) findViewById(R.id.household_tab);
		university_tab = (Button) findViewById(R.id.university_tab);
		hd_cal_btn = (Button) findViewById(R.id.hd_cal_btn);
		uni_cal_btn = (Button) findViewById(R.id.uni_cal_btn);
		hd_cal_btn2 = (Button) findViewById(R.id.hd_cal_btn2);
		uni_cal_btn2 = (Button) findViewById(R.id.uni_cal_btn2);
		uni_reset_btn = (Button) findViewById(R.id.uni_reset_btn);
		reset_btn = (Button) findViewById(R.id.reset_btn);
		hdAddCusEx = (Button) findViewById(R.id.add_expense_btn);
		hdAddCusIn = (Button) findViewById(R.id.add_income_btn);
		hdDelCusIn = (Button) findViewById(R.id.delete_income_btn);
		hdDelCusEx = (Button) findViewById(R.id.delete_expense_btn);
		uniDelCusEx = (Button) findViewById(R.id.uni_delete_expense_btn);
		uniDelCusIn = (Button) findViewById(R.id.uni_delete_income_btn);
		uniAddCusEx = (Button) findViewById(R.id.uni_add_expense_btn);
		uniAddCusIn = (Button) findViewById(R.id.uni_add_income_btn);

		hdRestartBtn = (Button) findViewById(R.id.restart_btn);
		uniRestartBtn = (Button) findViewById(R.id.uni_restart_btn);

		// custom element
		hdTableLIn = (TableLayout) findViewById(R.id.table_layout_income);
		hdTableLEx = (TableLayout) findViewById(R.id.table_layout_expense);
		uniTableLIn = (TableLayout) findViewById(R.id.uni_table_layout_income);
		uniTableLEx = (TableLayout) findViewById(R.id.uni_table_layout_expense);

		result_display = (TextView) findViewById(R.id.result_display);
		uni_result_display = (TextView) findViewById(R.id.uni_result_display);

		household_layout = (FrameLayout) findViewById(R.id.household_layout);
		university_layout = (FrameLayout) findViewById(R.id.university_layout);

		// initial onCreate
		// if clicked restart button on uniView then should go straight to uniView
		boolean bool = prefs.getBoolean("isUniView", false);
		if (bool) {
			university_tab
					.setBackgroundResource(R.drawable.keyboard_digit_pressed);
			household_layout.setVisibility(View.INVISIBLE);
			getActionBar().setTitle(getString(R.string.actionbar_title_uni));
			// set it to false -> reset to default value (household view)
			prefs.edit().putBoolean("isUniView", false).commit();
		} else {
			household_tab
					.setBackgroundResource(R.drawable.keyboard_digit_pressed);
			university_layout.setVisibility(View.INVISIBLE);
			getActionBar().setTitle(getString(R.string.actionbar_title_hd));
		}
		initEditText();
		setEditTextBackround();

		household_tab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				household_tab
						.setBackgroundResource(R.drawable.keyboard_digit_pressed);
				university_tab
						.setBackgroundResource(R.drawable.keyboard_digit_normal);

				household_layout.setVisibility(View.VISIBLE);
				university_layout.setVisibility(View.INVISIBLE);
				getActionBar().setTitle(getString(R.string.actionbar_title_hd));
			}
		});

		university_tab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				university_tab
						.setBackgroundResource(R.drawable.keyboard_digit_pressed);
				household_tab
						.setBackgroundResource(R.drawable.keyboard_digit_normal);

				household_layout.setVisibility(View.INVISIBLE);
				university_layout.setVisibility(View.VISIBLE);
				getActionBar()
						.setTitle(getString(R.string.actionbar_title_uni));
			}
		});

		reset_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (EditText eTxt : getEditTextGroup(0)) {
					eTxt.setText("");
				}
				for (EditText eTxt : getEditTextGroup(1)) {
					eTxt.setText("");
				}
				result_display.setText("");
			}
		});

		uni_reset_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (EditText eTxt : getEditTextGroup(2)) {
					eTxt.setText("");
				}
				for (EditText eTxt : getEditTextGroup(3)) {
					eTxt.setText("");
				}
				uni_result_display.setText("");
			}
		});

		hdRestartBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, BudgetCalculator.class);
				startActivity(intent);
			}
		});

		uniRestartBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prefs.edit().putBoolean("isUniView", true).commit();
				Intent intent = new Intent(context, BudgetCalculator.class);
				startActivity(intent);
			}
		});

		// onclick listeners for add/delete custom item buttons
		setHdCustomItemClickListener();
		setUniCustomItemClickListener();

		// launcher notification
		sharedPref = prefSetting.getSharedPreferences(this);

		if (sharedPref.getBoolean("disableNotification_budget", false)) {
			// dont send notification
		} else {
			notificationLauncher();
		}

		performCalculation();
	}

	protected void performCalculation() {
		hd_cal_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calculateResult();
				showDialogSummary();
			}
		});

		hd_cal_btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calculateResult();
				showDialogSummary();
			}
		});

		uni_cal_btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calculateResult();
				showDialogSummary();
			}
		});

		uni_cal_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calculateResult();
				showDialogSummary();
			}
		});
	}

	protected void initEditText() {
		// for household
		edit_emp_income = (EditText) findViewById(R.id.edit_emp_income);
		edit_other_income = (EditText) findViewById(R.id.edit_other_income);
		edit_grocery = (EditText) findViewById(R.id.edit_grocery);
		edit_internet = (EditText) findViewById(R.id.edit_internet);
		edit_cable = (EditText) findViewById(R.id.edit_cable);
		edit_phone_bill = (EditText) findViewById(R.id.edit_phone_bill);
		edit_utility_water = (EditText) findViewById(R.id.edit_utility_water);
		edit_utility_electricity = (EditText) findViewById(R.id.edit_utility_elec);
		edit_house_rental = (EditText) findViewById(R.id.edit_house_rental);
		edit_transportation = (EditText) findViewById(R.id.edit_trans);
		edit_auto_insurance = (EditText) findViewById(R.id.edit_auto_ins);
		edit_gas = (EditText) findViewById(R.id.edit_gas);
		edit_other_expense = (EditText) findViewById(R.id.edit_oth_expense);
		edit_morgage = (EditText) findViewById(R.id.edit_mortgage);

		// for university
		uni_edit_osap_loan = (EditText) findViewById(R.id.uni_edit_osap_loan);
		uni_edit_bursary = (EditText) findViewById(R.id.uni_edit_bursary);
		uni_edit_grocery = (EditText) findViewById(R.id.uni_edit_grocery);
		uni_edit_internet = (EditText) findViewById(R.id.uni_edit_internet);
		uni_edit_cable = (EditText) findViewById(R.id.uni_edit_cable);
		uni_edit_phone_bill = (EditText) findViewById(R.id.uni_edit_phone_bill);
		uni_edit_utility_water = (EditText) findViewById(R.id.uni_edit_utility_water);
		uni_edit_utility_electricity = (EditText) findViewById(R.id.uni_edit_utility_elec);
		uni_edit_house_rental = (EditText) findViewById(R.id.uni_edit_house_rental);
		uni_edit_transportation = (EditText) findViewById(R.id.uni_edit_trans);
		uni_edit_other_expense = (EditText) findViewById(R.id.uni_edit_oth_expense);
		uni_edit_personal_saving = (EditText) findViewById(R.id.uni_edit_personal_saving);
		uni_edit_scholarship = (EditText) findViewById(R.id.uni_edit_scholarship);
		uni_edit_textbook = (EditText) findViewById(R.id.uni_edit_textbook);
		uni_edit_tuition = (EditText) findViewById(R.id.uni_edit_tuition);
	}

	protected void setEditTextBackround() {
		editTextResource = R.drawable.custom_edit_text;

		for (EditText eText : getEditTextGroup(0)) {
			eText.setBackgroundResource(editTextResource);
		}

		for (EditText eText : getEditTextGroup(1)) {
			eText.setBackgroundResource(editTextResource);
		}

		for (EditText eText : getEditTextGroup(2)) {
			eText.setBackgroundResource(editTextResource);
		}

		for (EditText eText : getEditTextGroup(3)) {
			eText.setBackgroundResource(editTextResource);
		}
	}

	protected EditText[] getEditTextGroup(int whichEditTxtGroup) {
		// 0: hdIn, 1: hdEx, 2: uniIn, 3: uniEx
		int count = 0;
		EditText[] editTxt = null;

		switch (whichEditTxtGroup) {
		case 0:
			count = getChildCount(hdTableLIn) - 2;
			editTxt = new EditText[count];
			for (int i = 0; i < count; i++) {
				editTxt[i] = getEditText(hdTableLIn, i + 2);
			}
			return editTxt;
		case 1:
			count = getChildCount(hdTableLEx) - 1;
			editTxt = new EditText[count];
			for (int i = 0; i < count; i++) {
				editTxt[i] = getEditText(hdTableLEx, i + 1);
			}
			return editTxt;
		case 2:
			count = getChildCount(uniTableLIn) - 2;
			editTxt = new EditText[count];
			for (int i = 0; i < count; i++) {
				editTxt[i] = getEditText(uniTableLIn, i + 2);
			}
			return editTxt;
		case 3:
			count = getChildCount(uniTableLEx) - 1;
			editTxt = new EditText[count];
			for (int i = 0; i < count; i++) {
				editTxt[i] = getEditText(uniTableLEx, i + 1);
			}
			return editTxt;
		default:
			return null;
		}
		// // eidtText[0][#]: household, editText[1][#]: university
		// editText = new EditText[][] {
		// { edit_emp_income, edit_other_income, edit_grocery,
		// edit_internet, edit_cable, edit_phone_bill,
		// edit_utility_water, edit_utility_electricity,
		// edit_morgage, edit_house_rental, edit_transportation,
		// edit_auto_insurance, edit_gas, edit_other_expense },
		//
		// { uni_edit_osap_loan, uni_edit_bursary, uni_edit_scholarship,
		// uni_edit_personal_saving, uni_edit_tuition,
		// uni_edit_textbook, uni_edit_utility_electricity,
		// uni_edit_grocery, uni_edit_house_rental,
		// uni_edit_transportation, uni_edit_internet,
		// uni_edit_cable, uni_edit_other_expense,
		// uni_edit_phone_bill, uni_edit_utility_water } };
	}

	protected boolean isHdViewVisible() {
		return isViewVisible(household_layout);
	}

	protected boolean isUniViewVisible() {
		return isViewVisible(university_layout);
	}

	protected boolean isViewVisible(View v) {
		if (v.getVisibility() == View.VISIBLE) {
			return true;
		}
		return false;
	}

	protected String setResultText() {
		DecimalFormat format = new DecimalFormat("#.##");
		String textResult;
		double sum = calculateResult()[0];
		double income = calculateResult()[1];
		double expense = calculateResult()[2];

		if (isAllEmptyEntry()) {
			textResult = "Please enter amount for which applicable!";
		} else {
			textResult = (sum < -3000) ? "Your current financial condition is very bad!"
					: (sum < 0 && sum >= -3000) ? "Your current income is not sufficient for you to spend!"
							: (sum >= 0 && sum < 500) ? "Your budget is very tight, spend your money with care!"
									: (sum >= 500 && sum < 1500) ? "Your budget is enough, congratuation!"
											: "You have more than enough budget, congratuation!";
		}

		textResult = textResult + "\n\nYour income: $" + format.format(income)
				+ "\nYour expense: $" + format.format(expense)
				+ "\nYour balance: $" + format.format(sum) + "\n\n";

		if (isHdViewVisible()) {
			result_display.setText(textResult);
		} else if (isUniViewVisible()) {
			uni_result_display.setText(textResult);
		} else {
			Log.e("ERROR", getClass() + " No view is visible or available");
		}
		return textResult;
	}

	protected boolean isAllEmptyEntry() {
		String stringTxt;
		int arrayDimen;

		// 0 and 1 for hd, 2 and 3 for uni
		arrayDimen = (isHdViewVisible()) ? 0 : (isUniViewVisible()) ? 2 : Log
				.e("ERROR", getClass() + " No view is visible or available");

		for (EditText eText : getEditTextGroup(arrayDimen)) {
			stringTxt = eText.getText().toString();
			if (!isInvalidEntry(stringTxt)
					&& Double.parseDouble(stringTxt) != 0) {
				return false;
			}
		}
		for (EditText eText : getEditTextGroup(arrayDimen + 1)) {
			stringTxt = eText.getText().toString();
			if (!isInvalidEntry(stringTxt)
					&& Double.parseDouble(stringTxt) != 0) {
				return false;
			}
		}
		return true;
	}

	protected boolean isInvalidEntry(String string) {// check for invalid double
		return (string.equalsIgnoreCase("") || string.equalsIgnoreCase(".")) ? true
				: false;
	}

	protected double[] calculateResult() {
		String strEditTxt;
		Double sum = 0d;
		EditText editTxt = null;
		// int index = -1;
		double income = 0d, expense = 0d;
		double[] budgetData = null;

		if (isUniViewVisible()) {

			// income
			for (int i = 2; i < getChildCount(uniTableLIn); i++) {
				editTxt = getEditText(uniTableLIn, i);
				strEditTxt = editTxt.getText().toString();
				income += (isInvalidEntry(strEditTxt)) ? 0 : Double
						.parseDouble(strEditTxt);
			}
			// expense
			for (int j = 1; j < getChildCount(uniTableLEx); j++) {
				editTxt = getEditText(uniTableLEx, j);
				strEditTxt = editTxt.getText().toString();
				expense += (isInvalidEntry(strEditTxt)) ? 0 : Double
						.parseDouble(strEditTxt);
			}
			sum = income - expense;

			// for (EditText eText : getEditTextGroup()[1]) {
			// stringEditText = eText.getText().toString();
			// index++;
			//
			// sum += (isInvalidEntry(stringEditText)) ? 0
			// : (index <= 3) ? Double.parseDouble(stringEditText)
			// : -Double.parseDouble(stringEditText);
			// if (index <= 3) {
			// income += (isInvalidEntry(stringEditText)) ? 0 : Double
			// .parseDouble(stringEditText);
			// } else {
			// expense += (isInvalidEntry(stringEditText)) ? 0 : Double
			// .parseDouble(stringEditText);
			// }
			// }
		} else if (isHdViewVisible()) {

			// income
			for (int i = 2; i < getChildCount(hdTableLIn); i++) {
				editTxt = getEditText(hdTableLIn, i);
				strEditTxt = editTxt.getText().toString();
				income += (isInvalidEntry(strEditTxt)) ? 0 : Double
						.parseDouble(strEditTxt);
			}
			// expense
			for (int j = 1; j < getChildCount(hdTableLEx); j++) {
				editTxt = getEditText(hdTableLEx, j);
				strEditTxt = editTxt.getText().toString();
				expense += (isInvalidEntry(strEditTxt)) ? 0 : Double
						.parseDouble(strEditTxt);
			}
			sum = income - expense;

			// for (EditText eText : getEditTextGroup()[0]) {
			// stringEditText = eText.getText().toString();
			// index++;
			//
			// sum += (isInvalidEntry(stringEditText)) ? 0
			// : (index <= 1) ? Double.parseDouble(stringEditText)
			// : -Double.parseDouble(stringEditText);
			// if (index <= 1) {
			// income += (isInvalidEntry(stringEditText)) ? 0 : Double
			// .parseDouble(stringEditText);
			// } else {
			// expense += (isInvalidEntry(stringEditText)) ? 0 : Double
			// .parseDouble(stringEditText);
			// }
			// }
		}
		return budgetData = new double[] { sum, income, expense };
	}

	protected void addTableRow(TableLayout tl) {
		tr = new TableRow(this);
		textView = new TextView(this);
		editTxt = new EditText(this);
		// tr.setId(Id);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		tr.setWeightSum(2f);
		// textView attrs
		textView.setText(inputTxt);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextColor(getResources().getColor(R.color.DarkOrange));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimensionPixelSize(R.dimen.budget_text_size));
		textView.setTypeface(Typeface.DEFAULT_BOLD);
		textView.setLayoutParams(new TableRow.LayoutParams(0,
				TableRow.LayoutParams.WRAP_CONTENT, 1f));
		// editText attrs
		editTxt.setEms(10);
		editTxt.setGravity(Gravity.CENTER_HORIZONTAL);
		editTxt.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		editTxt.setTextColor(getResources().getColor(R.color.DarkOrange));
		editTxt.setTypeface(Typeface.DEFAULT_BOLD);
		editTxt.setBackgroundResource(R.drawable.custom_edit_text);
		editTxt.setLayoutParams(new TableRow.LayoutParams(0,
				TableRow.LayoutParams.WRAP_CONTENT, 1f));

		tr.addView(textView);
		tr.addView(editTxt);
		tl.addView(tr);
	}

	protected void delTableRow(TableLayout tl, int ViewId) {
		tl.removeViewAt(ViewId);
	}

	protected EditText getEditText(TableLayout tl, int viewId) {
		View tableRowView = tl.getChildAt(viewId);
		TableRow tableRow = (TableRow) tableRowView;
		View editTxtView = tableRow.getChildAt(1);
		return ((EditText) editTxtView);
	}

	protected int getChildCount(TableLayout tl) {
		return tl.getChildCount();
	}

	protected void promptUserForTxtName(final TableLayout tl) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		alertDialog.setView(input);

		alertDialog.setTitle(getString(R.string.alert_dialog_title));
		alertDialog.setMessage(getString(R.string.alert_dialog_message));
		alertDialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
						inputTxt = input.getText().toString()
								.equalsIgnoreCase("") ? getString(R.string.custom_default_name)
								: input.getText().toString();
//						isAdded = true;
						addTableRow(tl);
					}
				});

		alertDialog.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
//						isAdded = false;
						// cancel, do nothing
					}
				});
		alertDialog.show();
	}

	protected void setHdCustomItemClickListener() {
		hdAddCusIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				hdInId++;
				promptUserForTxtName(hdTableLIn);
			}
		});

		hdDelCusIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				hdInId -= isAdded ? 0 : 1;
				if ((getChildCount(hdTableLIn) - 1) <= 1) {
					// do nothing, no more items available
				} else {
					delTableRow(hdTableLIn, (getChildCount(hdTableLIn) - 1));
//					hdInId--;
				}
			}
		});

		hdAddCusEx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				hdExId++;
				promptUserForTxtName(hdTableLEx);
			}
		});

		hdDelCusEx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				hdExId -= isAdded ? 0 : 1;
				if ((getChildCount(hdTableLEx) - 1) <= 0) {
					// do nothing, no more items available
				} else {
					delTableRow(hdTableLEx, (getChildCount(hdTableLEx) - 1));
//					hdExId--;
				}
			}
		});
	}

	protected void setUniCustomItemClickListener() {
		uniAddCusIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				uniInId++;
				promptUserForTxtName(uniTableLIn);
			}
		});

		uniDelCusIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				uniInId -= isAdded ? 0 : 1;
				if ((getChildCount(uniTableLIn) - 1) <= 1) {
					// do nothing, no more items available
				} else {
					delTableRow(uniTableLIn, (getChildCount(uniTableLIn) - 1));
//					uniInId--;
				}
			}
		});

		uniAddCusEx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				uniExId++;
				promptUserForTxtName(uniTableLEx);
			}
		});

		uniDelCusEx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				uniExId -= isAdded ? 0 : 1;
				if ((getChildCount(uniTableLEx) - 1) <= 0) {
					// do nothing, no more items available
				} else {
					delTableRow(uniTableLEx, (getChildCount(uniTableLEx) - 1));
//					uniExId--;
				}
			}
		});
	}

	protected void showDialogSummary() {
		customDialog = new CustomDialog(context, setResultText());
		customDialog.show();
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
}
