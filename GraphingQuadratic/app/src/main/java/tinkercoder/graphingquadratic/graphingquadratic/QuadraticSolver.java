package tinkercoder.graphingquadratic.graphingquadratic;

import java.text.DecimalFormat;

import tinkercoder.graphingquadratic.navdrawer.DrawerActivity;
import tinkercoder.graphingquadratic.preferenceactivity.PreferenceSetting;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

public class QuadraticSolver extends DrawerActivity {

	InputMethodManager imm;
	EditText editText_a, editText_b, editText_c;
	EditText x1, x2, resultDisplay;
	Button clc;
	RadioButton vertex, roots, opening, symetry;
	RadioGroup radioGroup, radioGroup2;
	TextView title;
	LinearLayout layout;
	DecimalFormat format;
	private AdView adView;
	SharedPreferences sharedPref = null;
	PreferenceSetting prefSetting = new PreferenceSetting();
	int editTextDrawable;
	int orangeColor, blueColor, redColor, darkOrangeColor;
	public double A, B, C, ans1, ans2, val, vertexX, vertexY;
	// valueYRange: max Y value within range (opposite direction of vertex Y)
	// valueYRangeTop and valueYRangeBottom: max and min y value within range
	private double valueYRange = 0, valueYRangeTop = 0, valueYRangeBottom;

	private boolean isQuadratic = true;

	private static final double STEP_VALUE = 0.05;
	private static final int ID_IDENTIFIER = 913;
	
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setInflaterOnView("quadraticSolver");
		super.getDrawerList().setItemChecked(0, true);

		// find and load ads
		AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("98C719A04DF7111D2DDD25D764C88F8E").build();
		if (adView != null) {
			adView.loadAd(adRequest);
		}

		layout = (LinearLayout) findViewById(R.id.graph_layout);

		orangeColor = getResources().getColor(R.color.Orange);
		blueColor = getResources().getColor(R.color.BlueViolet);
		redColor = getResources().getColor(R.color.DarkRed);
		darkOrangeColor = getResources().getColor(R.color.DarkOrange);

		x1 = (EditText) findViewById(R.id.editText_x1);
		x2 = (EditText) findViewById(R.id.editText_x2);
		resultDisplay = (EditText) findViewById(R.id.display_text);

		editTextDrawable = R.drawable.custom_edit_text;

		editText_a = (EditText) findViewById(R.id.a);
		editText_b = (EditText) findViewById(R.id.b);
		editText_c = (EditText) findViewById(R.id.c);

		editText_a.setBackgroundResource(editTextDrawable);
		editText_b.setBackgroundResource(editTextDrawable);
		editText_c.setBackgroundResource(editTextDrawable);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		format = new DecimalFormat("#.####");

		vertex = (RadioButton) findViewById(R.id.vertex);
		roots = (RadioButton) findViewById(R.id.roots);
		opening = (RadioButton) findViewById(R.id.opening);
		symetry = (RadioButton) findViewById(R.id.symetry);

		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		radioGroup2 = (RadioGroup) findViewById(R.id.radio_group2);

		// radio buttons only one is able checked
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId != -1 && vertex.isChecked()
						|| symetry.isChecked()) {
					radioGroup2.clearCheck();
				}
			}
		});

		radioGroup2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId != -1 && roots.isChecked() || opening.isChecked()) {
					radioGroup.clearCheck();
				}
			}
		});

		// startup graph
		drawGraph(2.0, 8.0, 2.0);

		clc = (Button) findViewById(R.id.clc);
		clc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isEmptyEntry()) {
					resultDisplay
							.setText("Please enter numbers for a, b and c");
					// if aX^2 equal zero, then perform straigh light
					// calculation
				} else if (Double.parseDouble(editText_a.getText().toString()) == 0) {
					isQuadratic = false;
					B = getInput("b");
					C = getInput("c");
					performComputation("straight", 0.0, B, C);
					imm.hideSoftInputFromWindow(editText_a.getWindowToken(), 0);
				}

				else {
					isQuadratic = true;
					A = getInput("a");
					B = getInput("b");
					C = getInput("c");
					performComputation("parabola", A, B, C);
					drawGraph(A, B, C);
					// Hide soft keyboard right after button is clicked
					imm.hideSoftInputFromWindow(editText_a.getWindowToken(), 0);
				}

			}
		});

		// launcher notification
		sharedPref = prefSetting.getSharedPreferences(this);

		if (sharedPref.getBoolean("disableNotification_quad", false)) {
			// dont send notification
		} else {
			notificationLauncher();
		}
	}

	protected void performComputation(String graphType, double a, double b,
			double c) {
		val = b * b - 4 * a * c;
		vertexX = getVertexX(a, b);
		vertexY = getVertexY(a, b, c);

		if (graphType.equalsIgnoreCase("parabola")) {
			// no roots
			if (val < 0) {
				x1.setText("N/A");
				x2.setText("N/A");
				if (roots.isChecked()) {
					displayRoots(0);
				} else if (vertex.isChecked()) {
					displayVertex(0);
				} else if (opening.isChecked()) {
					displayDirOfOpen();
				} else if (symetry.isChecked()) {
					displayLineOfSym();
				}
			} else if (val == 0) {// one root
				ans1 = ((-b) / (2 * a));
				x1.setText(String.valueOf(format.format(ans1)));
				x2.setText(String.valueOf(format.format(ans1)));
				if (roots.isChecked()) {
					displayRoots(1);
				} else if (vertex.isChecked()) {
					displayVertex(1);
				} else if (opening.isChecked()) {
					displayDirOfOpen();
				} else if (symetry.isChecked()) {
					displayLineOfSym();
				}
			} else if (val > 0) {// two roots
				ans1 = ((-b) + (Math.sqrt(b * b - 4 * a * c))) / (2 * a);
				ans2 = ((-b) - (Math.sqrt(b * b - 4 * a * c))) / (2 * a);
				x1.setText(String.valueOf(format.format(ans1)));
				x2.setText(String.valueOf(format.format(ans2)));
				if (roots.isChecked()) {
					displayRoots(2);
				} else if (vertex.isChecked()) {
					displayVertex(2);
				} else if (opening.isChecked()) {
					displayDirOfOpen();
				} else if (symetry.isChecked()) {
					displayLineOfSym();
				}
			}
		} else if (graphType.equalsIgnoreCase("straight")) {
			ans1 = ((-c) / b);
			x1.setText(String.valueOf(ans1));
			drawGraph(0, b, c);
		} else
			resultDisplay
					.setText("system error, please restart this application!");
	}

	protected void displayRoots(int condition) {
		switch (condition) {
		case 0:// no roots
			resultDisplay.setText("This Equation Has NO Roots");
			break;
		case 1:// one root
			resultDisplay.setText("This Equation Has Only ONE Root" + "\n( " + format.format(ans1) + " )");
			break;
		case 2:// two roots
			resultDisplay.setText("This Equation Has TWO Roots" + "\n( " + format.format(ans1) + " , " + format.format(ans2) + " )");
			break;
		}
	}

	protected void displayDirOfOpen() {
		if (A < 0) {
			resultDisplay.setText("The Parabola Opens DOWN");
		} else if (A > 0) {
			resultDisplay.setText("The Parabola Opens UP");
		} else {
			resultDisplay.setText("This Equation is not Quadratic");
		}
	}

	protected void displayLineOfSym() {
		resultDisplay.setText("Line Of Symetry: x="
				+ String.valueOf(format.format(vertexX)));
	}

	protected void displayVertex(int condition) {
		switch (condition) {
		case 0:// no roots
			resultDisplay.setText("Vertex: ( " + format.format(vertexX)
					+ " , " + format.format(vertexY) + " )");
			break;
		case 1:// one root
			resultDisplay.setText("Vertex: ( "
					+ String.valueOf(format.format(vertexX)) + " , "
					+ String.valueOf(format.format(vertexY)) + " )");
			break;
		case 2:// two roots
			resultDisplay.setText("Vertex: ( "
					+ String.valueOf(format.format(vertexX)) + " , "
					+ String.valueOf(format.format(vertexY)) + " )");
			break;
		}
	}

	protected double getInput(String string) {
		double value;
		if (string.equalsIgnoreCase("a")) {
			value = Double.parseDouble(editText_a.getText().toString());
		} else if (string.equalsIgnoreCase("b")) {
			value = Double.parseDouble(editText_b.getText().toString());
		} else {
			value = Double.parseDouble(editText_c.getText().toString());
		}
		return value;
	}

	protected boolean isEmptyEntry() {
		if (editText_a.getText().toString().equals("")
				|| editText_b.getText().toString().equals("")
				|| editText_c.getText().toString().equals("")) {
			return true;
		}
		return false;
	}

	protected void drawGraph(double a, double b, double c) {
		// remove previous view before adding a new view
		layout.removeAllViews();

		LineGraphView graphView = new LineGraphView(this, "Graph");

		if (isQuadratic()) {
			// parabola
			graphView.addSeries(new GraphViewSeries("Parabola",
					new GraphViewSeriesStyle(orangeColor, 4), getParabolaData(
							a, b, c)));
			// line of symetry
			graphView.addSeries(new GraphViewSeries("Line Of Symetry",
					new GraphViewSeriesStyle(redColor, 2), getLineSymetryData(
							a, b, c)));
			// Vertex
			// graphView.addSeries(new GraphViewSeries("Vertex",
			// new GraphViewSeriesStyle(blueColor, 6), getVertexData(a,
			// b, c)));
		} else {
			// straight line
			graphView.addSeries(new GraphViewSeries("Straight Line",
					new GraphViewSeriesStyle(orangeColor, 4),
					getStraightLineData(b, c)));
		}

		// X Axis
		graphView
				.addSeries(new GraphViewSeries("X Axis",
						new GraphViewSeriesStyle(orangeColor, 7), getXAxisData(
								a, b, c)));
		// Y Axis
		graphView
				.addSeries(new GraphViewSeries("Y Axis",
						new GraphViewSeriesStyle(orangeColor, 7), getYAxisData(
								a, b, c)));

		setXAxisBounds(graphView, a, b, c);
		setYAxisBounds(graphView, a, b, c);

		graphView.setScrollable(true);
		graphView.setScalable(true);
		graphView.getGraphViewStyle().setGridColor(darkOrangeColor);
		graphView.getGraphViewStyle().setVerticalLabelsColor(darkOrangeColor);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(darkOrangeColor);

		layout.addView(graphView);
	}

	protected GraphViewData[] getParabolaData(double a, double b, double c) {
		double valueY = 0;
		double leftBound = 0, rightBound = 0;
		int dataPosition = 0;
		vertexX = getVertexX(a, b);

		if (val < 0 || val == 0) {
			leftBound = vertexX - 10;
			rightBound = vertexX + 10;
		} else {// > 0
			leftBound = getLeftRoot(a, b, c) - getMarginSpace(a, b, c);
			rightBound = getRighttRoot(a, b, c) + getMarginSpace(a, b, c);
		}

		GraphViewData[] data = new GraphViewData[getHorizontnalArraySize(a, b,
				c)];
		for (double x = leftBound; x < rightBound; x += getStepValue(leftBound,
				rightBound)) {
			valueY = a * x * x + b * x + c;
			data[dataPosition] = new GraphViewData(x, valueY);
			dataPosition++;
		}
		valueYRange = valueY;
		// resultDisplay.setText(Double.toString(valueYRange));
		return data;
	}

	protected GraphViewData[] getStraightLineData(double b, double c) {
		double valueY = 0;
		int dataPosition = 0;

		GraphViewData[] data = new GraphViewData[201];

		for (double x = -10.0; x < 10.0; x += 0.1) {
			valueY = b * x + c;
			data[dataPosition] = new GraphViewData(x, valueY);
			dataPosition++;
		}
		valueYRangeBottom = (b > 0) ? (b * (-10.0) + c) : valueY;
		valueYRangeTop = (b > 0) ? valueY : (b * (-10.0) + c);
		return data;
	}

	protected GraphViewData[] getLineSymetryData(double a, double b, double c) {
		GraphViewData[] data = new GraphViewData[2];
		data[0] = new GraphViewData(getVertexX(a, b),
				getYAxisBounds(a, b, c)[0]);
		data[1] = new GraphViewData(getVertexX(a, b),
				getYAxisBounds(a, b, c)[1]);
		return data;
	}

	protected GraphViewData[] getVertexData(double a, double b, double c) {
		// draw a diamond shape
		GraphViewData[] data = new GraphViewData[6];
		data[0] = new GraphViewData(getVertexX(a, b), getVertexY(a, b, c));
		data[1] = new GraphViewData(getVertexX(a, b), getVertexY(a, b, c) - 0.1);
		data[2] = new GraphViewData(getVertexX(a, b) - 0.1, getVertexY(a, b, c));
		data[3] = new GraphViewData(getVertexX(a, b), getVertexY(a, b, c) + 0.1);
		data[4] = new GraphViewData(getVertexX(a, b) + 0.1, getVertexY(a, b, c));
		data[5] = new GraphViewData(getVertexX(a, b), getVertexY(a, b, c) - 0.1);
		return data;
	}

	protected GraphViewData[] getXAxisData(double a, double b, double c) {
		GraphViewData[] data = new GraphViewData[2];
		double marginSpace = 0, leftMargin = 0, rightMargin = 0;

		if (isQuadratic()) {
			if (val < 0 || val == 0) {
				leftMargin = getVertexX(a, b) - 10;
				rightMargin = getVertexX(a, b) + 10;
			} else {// > 0
				marginSpace = getMarginSpace(a, b, c);
				leftMargin = getLeftRoot(a, b, c) - marginSpace;
				rightMargin = getRighttRoot(a, b, c) + marginSpace;
			}
		} else {
			leftMargin = -10;
			rightMargin = 10;
		}

		data[0] = new GraphViewData(leftMargin, 0);
		data[1] = new GraphViewData(rightMargin, 0);
		return data;
	}

	protected GraphViewData[] getYAxisData(double a, double b, double c) {
		GraphViewData[] data = new GraphViewData[2];

		if (isQuadratic()) {
			data[0] = new GraphViewData(0, getYAxisBounds(a, b, c)[0]);
			data[1] = new GraphViewData(0, getYAxisBounds(a, b, c)[1]);
		} else {
			data[0] = new GraphViewData(0, valueYRangeBottom
					- getVerticalMarginSpace());
			data[1] = new GraphViewData(0, valueYRangeTop
					+ getVerticalMarginSpace());
		}

		return data;
	}

	protected double getVertexX(double a, double b) {
		return -b / (2 * a);
	}

	protected double getVertexY(double a, double b, double c) {
		return (4 * a * c - b * b) / (4 * a);
	}

	protected double[] getRoot(double a, double b, double c) {
		double[] root = new double[2];
		root[0] = ((-b) + (Math.sqrt(b * b - 4 * a * c))) / (2 * a);
		root[1] = ans1 = ((-b) - (Math.sqrt(b * b - 4 * a * c))) / (2 * a);
		return root;
	}

	protected double getLeftRoot(double a, double b, double c) {
		double root;
		if (a > 0) {// parabola opens up, second root value at left
			root = getRoot(a, b, c)[1];
		} else {// opens down, first root value at left
			root = getRoot(a, b, c)[0];
		}
		return root;
	}

	protected double getRighttRoot(double a, double b, double c) {
		double root;
		if (a > 0) {// parabola opens up, first root value at right
			root = getRoot(a, b, c)[0];
		} else {// opens down, second root value at right
			root = getRoot(a, b, c)[1];
		}
		return root;
	}

	protected void setXAxisBounds(GraphView graphView, double a, double b,
			double c) {
		double deltaValue = 0;
		double marginSpace = 0;

		if (isQuadratic()) {
			if (val < 0 || val == 0) {
				graphView.setViewPort(getVertexX(a, b) - 10, 20);
			} else if (val > 0) {
				// difference between two roots
				deltaValue = getRootsDeltaValue(a, b, c);
				marginSpace = getMarginSpace(a, b, c);
				graphView.setViewPort(getLeftRoot(a, b, c) - marginSpace,
						deltaValue + 2 * marginSpace);
			}
		} else {// for straight line
			graphView.setViewPort(-10, 20);
		}
	}

	protected void setYAxisBounds(GraphView graphView, double a, double b,
			double c) {
		boolean level = (getVertexY(a, b, c) > 0) ? true : false;
		double vertexY = getVertexY(a, b, c);
		double verticalMarginSpace = getVerticalMarginSpace();

		if (isQuadratic()) {
			if (a > 0 && level) {
				graphView.setManualYAxisBounds(valueYRange, vertexY
						- verticalMarginSpace);
			} else if (a > 0 && !level) {
				graphView.setManualYAxisBounds(valueYRange, vertexY
						- verticalMarginSpace);
			} else if (a < 0 && level) {
				graphView.setManualYAxisBounds(getVertexY(a, b, c)
						+ verticalMarginSpace, valueYRange);
			} else {// (a < 0 && !level
				graphView.setManualYAxisBounds(getVertexY(a, b, c)
						+ verticalMarginSpace, valueYRange);
			}
		}

		else {// straight line
			graphView.setManualYAxisBounds(valueYRangeTop
					+ getVerticalMarginSpace(), valueYRangeBottom
					- getVerticalMarginSpace());
		}
	}

	protected int getHorizontnalArraySize(double a, double b, double c) {
		int count = 0;
		double leftBound = 0, rightBound = 0;

		if (val < 0 || val == 0) {
			leftBound = vertexX - 10;
			rightBound = vertexX + 10;
		} else {// > 0
			leftBound = getLeftRoot(a, b, c) - getMarginSpace(a, b, c);
			rightBound = getRighttRoot(a, b, c) + getMarginSpace(a, b, c);
		}

		for (double x = leftBound; x < rightBound; x += getStepValue(leftBound,
				rightBound)) {
			count++;
		}
		return count;
	}

	protected int getVerticalArraySize(double a, double b, double c) {
		int count = 0;
		int start = (int) Math.round(Math.abs(getYAxisBounds(a, b, c)[0]));
		int end = (int) Math.round(Math.abs(getYAxisBounds(a, b, c)[1]));
		for (int i = start; i < end; i++) {
			count++;
		}
		return count;
	}

	protected double getStepValue(double leftBound, double rightBound) {
		double deltaValue = Math.abs(leftBound - rightBound);

		if (val < 0 || val == 0) {
			return STEP_VALUE;
		} else {
			return deltaValue / 100.0;
		}
	}

	protected double[] getYAxisBounds(double a, double b, double c) {
		boolean level = (getVertexY(a, b, c) > 0) ? true : false;
		double value[] = new double[2];
		double vertexY = getVertexY(a, b, c);
		double verticalMarginSpace = getVerticalMarginSpace();

		if (a > 0 && level) {
			value[1] = valueYRange;
			value[0] = vertexY - verticalMarginSpace;
		} else if (a > 0 && !level) {
			value[1] = valueYRange;
			value[0] = vertexY - verticalMarginSpace;
		} else if (a < 0 && level) {
			value[1] = vertexY + verticalMarginSpace;
			value[0] = valueYRange;
		} else {// (a < 0 && !level
			value[1] = vertexY + verticalMarginSpace;
			value[0] = valueYRange;
		}
		return value;
	}

	protected double getRootsDeltaValue(double a, double b, double c) {
		double deltaValue = Math.abs(getRoot(a, b, c)[0] - getRoot(a, b, c)[1]);
		return deltaValue;
	}

	// the margin space between X range and Roots
	protected double getMarginSpace(double a, double b, double c) {
		double deltaValue = Math.abs(getRoot(a, b, c)[0] - getRoot(a, b, c)[1]);
		return deltaValue / 5.0;
	}

	// the margin space between vertex X and Y range
	protected double getVerticalMarginSpace() {
		if (isQuadratic()) {
			return Math.abs((valueYRange - vertexY)) / 10.0;
		} else {
			return Math.abs(valueYRangeTop - valueYRangeBottom) / 10.0;
		}
	}

	protected boolean isQuadratic() {
		return isQuadratic;
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