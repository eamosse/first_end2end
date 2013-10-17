package first.endtoend;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import first.endtoend.adapters.BeneficiaryAdapter;
import first.endtoend.facades.AddressFacade;
import first.endtoend.facades.BeneficiaryFacade;
import first.endtoend.facades.PortfolioDetailFacade;
import first.endtoend.facades.PortfolioFacade;
import first.endtoend.helpers.DialogHelper;
import first.endtoend.models.Address;
import first.endtoend.models.Beneficiary;
import first.endtoend.models.Family;
import first.endtoend.models.Portfolio;
import first.endtoend.models.PortfolioDetail;

public class ListBeneficiariesActivity extends MyActivity {

	//Variables
	List<Beneficiary> beneficiaries;
	BeneficiaryAdapter adapter;
	TextView tv;
	Button backBtn, nextBtn;
	ProgressDialog progressDialog;	
	BeneficiaryFacade benefFacade;
	AlertDialog.Builder alert;
	AddressFacade adrFacade;
	Address adr;
	Beneficiary b;
	boolean beneficiaryIsSelected = false;
	PortfolioFacade portfolioFacade;
	public static Portfolio portfolio;
	PortfolioDetailFacade pfdFacade;
	List<PortfolioDetail> pfDetails;
	ListView listeViewBeneficiaries;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setting Layout to the activity
		setContentView(R.layout.activity_listing_beneficiaries);
		final Date currentDate = new Date(System.currentTimeMillis());
		
		try {
			benefFacade = new BeneficiaryFacade(this);
			adrFacade = new AddressFacade(this);
			//Getting the beneficiaries list
			beneficiaries = benefFacade.findEntitiesByForeignKey(TagActivity.family.getFamilyId(), Family.class);
			adr = adrFacade.findEntityByForeignKey(TagActivity.family.getFamilyId(), Family.class);
			((TextView)findViewById(R.id.familyName)).setText(TagActivity.family.getFamilyName()); 
			((TextView)findViewById(R.id.familyAddress)).setText(adr.toString()); 
			((TextView)findViewById(R.id.familyPhone)).setText(TagActivity.family.getPhoneNumber()); 

			portfolioFacade = new PortfolioFacade(this);
			portfolio = portfolioFacade.findEntityByForeignKey(TagActivity.family.getFamilyId(), Family.class);
			pfdFacade = new PortfolioDetailFacade(this);
			pfDetails = pfdFacade.findEntitiesByForeignKey(portfolio.getPortfolioId(), Portfolio.class);			

			if(pfDetails != null){
				System.out.println(pfDetails.size());
				for(Iterator<PortfolioDetail> itr = pfDetails.iterator();itr.hasNext();){  
					PortfolioDetail pfd = itr.next();  
					Date startDate = pfd.getAid().getStartDate();

					Date endDate = pfd.getAid().getEndDate();
					boolean dateOk = startDate.before(currentDate) && endDate.after(currentDate);
					if(!dateOk){  
						itr.remove();  
					}  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		progressDialog = new ProgressDialog(ListBeneficiariesActivity.this);		
		adapter = new BeneficiaryAdapter(ListBeneficiariesActivity.this, beneficiaries);
		listeViewBeneficiaries = (ListView) findViewById(R.id.listViewBeneficiaries);
		listeViewBeneficiaries.setAdapter(adapter);
		listeViewBeneficiaries.setSelector(R.drawable.selector);
		listeViewBeneficiaries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,int i, long l) {
				b = (Beneficiary) listeViewBeneficiaries.getItemAtPosition(i);
				TagActivity.family.setBeneficiarySelected(b);
				beneficiaryIsSelected = true;
			}
		});
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		nextBtn = (Button) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(beneficiaryIsSelected){
					if(portfolio == null || pfDetails.size() == 0){
						AlertDialog.Builder alert = new AlertDialog.Builder(ListBeneficiariesActivity.this);
						alert.setTitle(R.string.information);
						alert.setIcon(android.R.drawable.ic_dialog_info);
						alert.setMessage(R.string.empty_portfolio);
						alert.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {	
								DialogHelper.processingDisconnection(progressDialog, ListBeneficiariesActivity.this);
							}
						});

						alert.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(ListBeneficiariesActivity.this, TagActivity.class);
								startActivity(intent);
								finish();
							}
						});
						alert.show();

					}
					else{
						Intent intent = new Intent(ListBeneficiariesActivity.this, PortfolioActivity.class);
						startActivity(intent);
						finish();

					}

				}else{
					AlertDialog.Builder alert = new AlertDialog.Builder(ListBeneficiariesActivity.this);
					DialogHelper.showErrorDialog(alert, getString(R.string.information), "Select one recipient to continue");
				}
			}
		});
		
	}
	

	@Override
	public void onBackPressed() {
		alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.warning);
		alert.setMessage(R.string.cancelToTag);
		alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(ListBeneficiariesActivity.this, TagActivity.class);
				startActivity(intent);
				finish();
			}
		});

		alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		alert.show();

	}
}
