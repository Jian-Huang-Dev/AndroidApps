package tinkercoder.budgetcalculator.customdialog;

import tinkercoder.budgetcalculator.budgetcalculator.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog implements OnClickListener {
	Button okButton;
	TextView dialogMsg;
	String mStrDialogMsg;

	public CustomDialog(Context context, String strDialogMsg) {
		super(context);
		mStrDialogMsg = strDialogMsg;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_dialog);
		
		dialogMsg = (TextView) findViewById(R.id.dialog_message);
		dialogMsg.setText(mStrDialogMsg);
		
		okButton = (Button) findViewById(R.id.OkButton);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		if (v == okButton)
			dismiss();
	}

}
