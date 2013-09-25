package first.endtoend.adapters;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import first.endtoend.R;
import first.endtoend.helpers.JsonHelper;
import first.endtoend.models.Beneficiary;

public class BeneficiaryAdapter extends BaseAdapter {
	private List<Beneficiary> listBeneficiaries;
	private Activity context;
	private LayoutInflater mInflater;
	AQuery query;

	public BeneficiaryAdapter(Activity context, List<Beneficiary> listBeneficiaries) {
		this.context = context;
		this.listBeneficiaries = listBeneficiaries;
		this.mInflater = LayoutInflater.from(context);
		query = new AQuery(context);
	}

	@Override
	public int getCount() {
		return listBeneficiaries.size();
	}

	@Override
	public Object getItem(int position) {
		return listBeneficiaries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class MyView{
		TextView firstName;
		TextView lastName;
		ImageView image;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		RelativeLayout layoutItem;
		MyView view;
		if (convertView == null){
			layoutItem = (RelativeLayout) mInflater.inflate(R.layout.layout_beneficiary, parent, false);
			view = new MyView();
			view.firstName= (TextView) layoutItem.findViewById(R.id.beneficiaryFirstName);
			view.lastName= (TextView) layoutItem.findViewById(R.id.beneficiaryLastName);
			view.image = (ImageView) layoutItem.findViewById(R.id.benefImage);
			Beneficiary beneficiary = listBeneficiaries.get(position);
			
			System.out.println(JsonHelper.createJsonObject(beneficiary));

			view.firstName.setText(beneficiary.getFirstName());
			view.lastName.setText(beneficiary.getLastName());
			// set image based on selected text

			String strPhoto=beneficiary.getPhotoURL();
			String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+context.getString(R.string.benefPhotosOffLine);
			
			query.id(view.image).image(directoryPath+strPhoto,true, true, 0,R.drawable.ic_launcher);
		} else {
			layoutItem = (RelativeLayout) convertView;
			view = (MyView)convertView.getTag();

		}

		return layoutItem;
	}
}