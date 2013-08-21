package first.endtoend;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import first.endtoend.adapters.SupplyAdapter;
import first.endtoend.helpers.DialogHelper;

/**
 * 
 * This activity is used to process only one product to beneficiary
 *
 */
public class SupplyActivity extends MyActivity{

	//Variables
	EditText editText;
	Float quantityWished;
	Button validateBtn, backBtn;
	ListView listview;
	SupplyAdapter adapter;	



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//set the layout to the activity
		setContentView(R.layout.activity_supply);


		//Displaying the name of the agent
		TextView agentName = (TextView) findViewById(R.id.agentName);
		agentName.setText("Agent : "+LoginActivity.user.getFirstName().charAt(0)+" "+LoginActivity.user.getLastName());


		validateBtn = (Button) findViewById(R.id.validBtnSupply);

		backBtn = (Button) findViewById(R.id.backBtnSupply);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		listview = (ListView)findViewById(R.id.listView1);

		adapter = new SupplyAdapter(BeneficiaryDetail.portfolio.getProductsSelected(), SupplyActivity.this);
		listview.setAdapter(adapter);

		validateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int verif = thereIsEmptyEditText();
				if(verif != 1){
					Intent intent = new Intent(SupplyActivity.this, ConfirmationSupplyActivity.class);
					startActivity(intent);
					finish();

				}
				else{
					AlertDialog.Builder alert = new AlertDialog.Builder(SupplyActivity.this);
					DialogHelper.showErrorDialog(alert, getString(R.string.information), getString(R.string.emptyFields));
				}
			}
		});
	}

	//method to check if there is one editText empty in the layout
	public int thereIsEmptyEditText(){
		int result = 0;
		for(int i=0; i < listview.getChildCount(); i++){
			LinearLayout itemLayout = (LinearLayout)listview.getChildAt(i);
			editText = (EditText)itemLayout.findViewById(R.id.quantityWished);
			if(editText.getText().toString().equals("")){			
				return 1;
			}}

		return result;		

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(SupplyActivity.this, PortfolioActivity.class);
		startActivity(intent);
		finish();
	}
}
