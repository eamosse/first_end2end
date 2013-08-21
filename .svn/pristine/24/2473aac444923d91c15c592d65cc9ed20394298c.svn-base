package first.endtoend.adapters;

import java.io.File;
import java.util.List;

import com.androidquery.AQuery;

import android.content.Context;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import first.endtoend.R;
import first.endtoend.models.PortfolioDetail;

public class SupplyAdapter extends BaseAdapter {
	private List<PortfolioDetail> portfolioDetails;
	private Context context;
	private LayoutInflater mInflater;
	//variable to store the quantity seized in the layout
	private float quantityWished;
	AQuery aquery;


	public SupplyAdapter(List<PortfolioDetail> portfolioDetails,Context context) {
		this.portfolioDetails = portfolioDetails;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		aquery = new AQuery(context);
	}

	@Override
	public int getCount() {
		return portfolioDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return portfolioDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		LinearLayout layoutItem;
		if (convertView == null){
			layoutItem = (LinearLayout) mInflater.inflate(R.layout.layout_supply, parent, false);
		} else {
			layoutItem = (LinearLayout) convertView;
		}
		final PortfolioDetail portfolioDetail = portfolioDetails.get(position);

		TextView name = (TextView) layoutItem.findViewById(R.id.productNameSupply);			
		name.setText(portfolioDetail.getProduct().getName());

		TextView unity = (TextView) layoutItem.findViewById(R.id.productUnitySupply);
		unity.setText(portfolioDetail.getProduct().getUnity());

		// set image based on selected text
		ImageView imageView = (ImageView) layoutItem.findViewById(R.id.productImageSupply);
		String strPhoto=portfolioDetail.getProduct().getIconeURL();
		String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+context.getString(R.string.productPhotosOffLine);

		aquery.id(imageView).image(directoryPath+strPhoto,true, true, 0,R.drawable.icon);

		final EditText editText = (EditText) layoutItem.findViewById(R.id.quantityWished);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(!s.toString().equals("")){
					quantityWished = Float.parseFloat(s.toString());
					if(quantityWished <= portfolioDetail.getQuantity()){
						portfolioDetail.setQuantityWished(quantityWished);
						editText.setError(null);						
					}
					else{
						editText.setError(context.getString(R.string.quantity_error) +portfolioDetail.getQuantity());
					}
				}

			}
		});		
		return layoutItem;
	}

}
