package first.endtoend;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import first.endtoend.adapters.PortfolioAdapter;
import first.endtoend.facades.PortfolioDetailFacade;
import first.endtoend.helpers.DialogHelper;
import first.endtoend.models.Portfolio;
import first.endtoend.models.PortfolioDetail;

public class PortfolioActivity extends MyActivity {

	//Declaring variables to be used in code
	Portfolio portfolio;
	List<PortfolioDetail> portfolioDetails;
	PortfolioAdapter adapter;
	GridView gridView;
	PortfolioDetailFacade pfdFacade;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portfolio);

		portfolio = BeneficiaryDetail.portfolio;
		

		try {
			pfdFacade = new PortfolioDetailFacade(this);
			portfolioDetails = pfdFacade.findEntitiesByForeignKey(portfolio.getPortfolioId(), Portfolio.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		//Displaying the name of the agent
		TextView agentName = (TextView) findViewById(R.id.agentName);
		agentName.setText("Agent : "+LoginActivity.user.getFirstName().charAt(0)+" "+LoginActivity.user.getLastName());


		//Setting the adapter to the activity view
		adapter = new PortfolioAdapter(PortfolioActivity.this, portfolioDetails);
		gridView = (GridView) findViewById(R.id.gridViewPortfolio);
		gridView.setAdapter(adapter);


		//Action to do when clicking on the check box select all
		CheckBox cb = (CheckBox) findViewById(R.id.checkBoxSelectAll);

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				for(int i=0; i < gridView.getChildCount(); i++){
					LinearLayout itemLayout = (LinearLayout)gridView.getChildAt(i);
					CheckBox chb = (CheckBox)itemLayout.findViewById(R.id.checkBox);
					if(isChecked){
						chb.setChecked(true);
					}
					else{
						chb.setChecked(false);
					}
				}
			}
		});

		//Getting the next button from the layout and adding action on it
		Button nextBtn = (Button) findViewById(R.id.nextBtnPortfolio);
		nextBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				int verif = thereIsOneChecked();
				if(verif > 0){
					portfolio.setProductsSelected(adapter.getProductsSelected());
					Intent intent = new Intent(PortfolioActivity.this, SupplyActivity.class);
					startActivity(intent);
					finish();
				}
				else{
					AlertDialog.Builder alert = new AlertDialog.Builder(PortfolioActivity.this);

					DialogHelper.showErrorDialog(alert, getString(R.string.information), getString(R.string.noProductChoosed));
				}
			}
		});


		//Getting the back button from the layout and adding action on it
		Button backBtn = (Button) findViewById(R.id.backBtnPortfolio);
		backBtn.setOnClickListener(new OnClickListener() {					
			@Override
			public void onClick(View v) {
				onBackPressed();						
			}
		});	
	}


	//method to verify if there is at least on checkbox checked
	public int thereIsOneChecked(){
		int result = 0;
		for(int i=0; i < gridView.getChildCount(); i++){
			LinearLayout itemLayout = (LinearLayout)gridView.getChildAt(i);
			CheckBox chb = (CheckBox)itemLayout.findViewById(R.id.checkBox);
			if(chb.isChecked()){
				result++;				
			}
		}
		return result;		
	}


	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PortfolioActivity.this, BeneficiaryDetail.class);
		startActivity(intent);
		finish();
	}
}
