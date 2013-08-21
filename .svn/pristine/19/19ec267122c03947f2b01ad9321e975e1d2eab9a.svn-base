package first.endtoend.adapters;

import java.io.File;
import java.util.List;

import android.content.Context;
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
import first.endtoend.models.PortfolioDetail;

public class ConfirmationAdapter extends BaseAdapter {
	List<PortfolioDetail> productsSelected;
	private Context context;
	private LayoutInflater mInflater;
	AQuery aquery;

	public ConfirmationAdapter(Context context, List<PortfolioDetail> productsSelected) {
		this.context = context;
		this.productsSelected = productsSelected;
		this.mInflater = LayoutInflater.from(context);
		aquery = new AQuery(context);
	}


	@Override
	public int getCount() {
		return productsSelected.size();
	}

	@Override
	public Object getItem(int position) {
		return productsSelected.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layoutItem;
		if (convertView == null){
			layoutItem = (LinearLayout) mInflater.inflate(R.layout.layout_confirm, parent, false);
		} else {
			layoutItem = (LinearLayout) convertView;
		}
		final PortfolioDetail portfolioDetail = productsSelected.get(position);

		TextView productName = (TextView) layoutItem.findViewById(R.id.confirmProductName);
		productName.setText(portfolioDetail.getProduct().getName());

		TextView price = (TextView) layoutItem.findViewById(R.id.confirmPrice);
		price.setText(""+portfolioDetail.getProduct().getPrice()+" Rps");

		TextView quantityWished = (TextView) layoutItem.findViewById(R.id.confirmValueQuantityWished);
		quantityWished.setText(""+portfolioDetail.getQuantityWished()+" "+portfolioDetail.getProduct().getUnity());

		TextView quantityRemained = (TextView) layoutItem.findViewById(R.id.confirmValueQuantityRemained);
		float remained = portfolioDetail.getQuantity() - portfolioDetail.getQuantityWished();
		quantityRemained.setText(""+remained+" "+portfolioDetail.getProduct().getUnity());

		// set image based on selected text
		ImageView imageView = (ImageView) layoutItem.findViewById(R.id.confirmImage);
		String strPhoto=portfolioDetail.getProduct().getIconeURL();
		String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+context.getString(R.string.productPhotosOffLine);

		aquery.id(imageView).image(directoryPath+strPhoto,true, true, 0,R.drawable.icon);

		return layoutItem;
	}

}
