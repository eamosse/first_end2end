package first.endtoend;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import first.endtoend.adapters.BeneficiaryAdapter;
import first.endtoend.facades.AddressFacade;
import first.endtoend.facades.BeneficiaryFacade;
import first.endtoend.models.Address;
import first.endtoend.models.Beneficiary;
import first.endtoend.models.Family;

public class ListBeneficiariesActivity extends MyActivity {

	//Variables
	List<Beneficiary> beneficiaries;
	BeneficiaryAdapter adapter;
	TextView tv;
	Button btn;
	ProgressDialog progressDialog;	
	BeneficiaryFacade benefFacade;
	AlertDialog.Builder alert;
	AddressFacade adrFacade;
	Address adr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setting Layout to the activity
		setContentView(R.layout.activity_listing_beneficiaries);
		try {
			benefFacade = new BeneficiaryFacade(this);
			adrFacade = new AddressFacade(this);
			//Getting the beneficiaries list
			beneficiaries = benefFacade.findEntitiesByForeignKey(TagActivity.family.getFamilyId(), Family.class);
			adr = adrFacade.findEntityByForeignKey(TagActivity.family.getFamilyId(), Family.class);
			((TextView)findViewById(R.id.familyName)).setText(TagActivity.family.getFamilyName()); 
			((TextView)findViewById(R.id.familyAddress)).setText(adr.toString()); 
			((TextView)findViewById(R.id.familyPhone)).setText(TagActivity.family.getPhoneNumber()); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		progressDialog = new ProgressDialog(ListBeneficiariesActivity.this);		
		adapter = new BeneficiaryAdapter(ListBeneficiariesActivity.this, beneficiaries);
		final ListView listeViewBeneficiaries = (ListView) findViewById(R.id.listViewBeneficiaries);
		listeViewBeneficiaries.setAdapter(adapter);
		listeViewBeneficiaries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,int i, long l) {
				Beneficiary b = (Beneficiary) listeViewBeneficiaries.getItemAtPosition(i);
				TagActivity.family.setBeneficiarySelected(b);
				Intent intent = new Intent(ListBeneficiariesActivity.this, BeneficiaryDetail.class);
				startActivity(intent);
				finish();
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.beneficiary_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
