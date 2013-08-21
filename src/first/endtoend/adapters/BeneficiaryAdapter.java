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
		TextView text;
		ImageView image;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LinearLayout layoutItem;
		MyView view;
		if (convertView == null){
			layoutItem = (LinearLayout) mInflater.inflate(R.layout.layout_beneficiary, parent, false);
			view = new MyView();
			view.text= (TextView) layoutItem.findViewById(R.id.beneficiaryname);
			view.image = (ImageView) layoutItem.findViewById(R.id.benefImage);
			Beneficiary beneficiary = listBeneficiaries.get(position);
			
			System.out.println(JsonHelper.createJsonObject(beneficiary));

			view.text.setText(beneficiary.getFirstName()+" - "+beneficiary.getLastName());

			// set image based on selected text

			String strPhoto=beneficiary.getPhotoURL();
			String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+context.getString(R.string.benefPhotosOffLine);
			
			query.id(view.image).progress(R.id.progress).image(directoryPath+strPhoto,true, true, 0,R.drawable.ic_launcher);
		} else {
			layoutItem = (LinearLayout) convertView;
			view = (MyView)convertView.getTag();

		}

		return layoutItem;
	}
}