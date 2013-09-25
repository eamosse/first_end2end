package first.endtoend;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.AQuery;

import first.endtoend.facades.AddressFacade;
import first.endtoend.facades.PortfolioDetailFacade;
import first.endtoend.facades.PortfolioFacade;
import first.endtoend.helpers.DialogHelper;
import first.endtoend.models.Address;
import first.endtoend.models.Beneficiary;
import first.endtoend.models.Family;
import first.endtoend.models.Portfolio;
import first.endtoend.models.PortfolioDetail;

public class BeneficiaryDetail extends MyActivity {

	Beneficiary beneficiary;
	ProgressDialog progressDialog;
	AQuery query;
	AddressFacade adrFacade;
	Address adr;
	PortfolioFacade portfolioFacade;
	public static Portfolio portfolio;
	PortfolioDetailFacade pfdFacade;
	List<PortfolioDetail> pfDetails;




	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beneficiarydetail);
		progressDialog = new ProgressDialog(BeneficiaryDetail.this);
		final Date currentDate = new Date(System.currentTimeMillis());

		try {
			adrFacade = new AddressFacade(this);
			adr = adrFacade.findEntityByForeignKey(TagActivity.family.getFamilyId(), Family.class);
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
					System.out.println("Start : "+startDate+" End : "+endDate);
					System.out.println(startDate.before(currentDate) && endDate.after(currentDate));
					boolean dateOk = startDate.before(currentDate) && endDate.after(currentDate);
					if(!dateOk){  
						itr.remove();  
					}  
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		//Displaying the name of the agent
		TextView agentName = (TextView) findViewById(R.id.agentName);
		agentName.setText("Agent : "+LoginActivity.user.getFirstName().charAt(0)+" "+LoginActivity.user.getLastName());

		//getting the beneficiary selected from the family
		beneficiary = TagActivity.family.getBeneficiarySelected();


		AQuery aq = new AQuery(this);

		String str = beneficiary.getPhotoURL();
		String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+getString(R.string.benefPhotosOffLine);

		aq.id(R.id.beneficiaryImage).image(directoryPath+str,true, true, 0,R.drawable.ic_launcher);


		//Setting values to Layout text views		
		TextView fName = (TextView) findViewById(R.id.valuefirstName);
		fName.setText(beneficiary.getFirstName());

		TextView lName = (TextView) findViewById(R.id.valuelastName);
		lName.setText(beneficiary.getLastName());

		TextView address = (TextView) findViewById(R.id.valueaddress);
		if(adr != null){
			address.setText(adr.getStreet()+", "+adr.getZipCode());
		}else{
			address.setText("NA");
		}

		TextView phone = (TextView) findViewById(R.id.valuephone);
		phone.setText(TagActivity.family.getPhoneNumber());

		//Getting the next button from the layout and adding action on it
		Button nextBtn = (Button) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
				if(portfolio == null || pfDetails.size() == 0){
					AlertDialog.Builder alert = new AlertDialog.Builder(BeneficiaryDetail.this);
					alert.setTitle(R.string.information);
					alert.setIcon(android.R.drawable.ic_dialog_info);
					alert.setMessage(R.string.empty_portfolio);
					alert.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {	
							DialogHelper.processingDisconnection(progressDialog, BeneficiaryDetail.this);
						}
					});

					alert.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(BeneficiaryDetail.this, TagActivity.class);
							startActivity(intent);
							finish();
						}
					});
					alert.show();

				}
				else{
					Intent intent = new Intent(BeneficiaryDetail.this, PortfolioActivity.class);
					startActivity(intent);
					finish();

				}
			}
		});

		//Getting the back button from the layout and adding action on it
		Button backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});	
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(BeneficiaryDetail.this, ListBeneficiariesActivity.class);
		startActivity(intent);
		finish();
	}
}
