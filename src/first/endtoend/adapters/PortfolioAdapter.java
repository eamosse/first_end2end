package first.endtoend.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import first.endtoend.R;
import first.endtoend.models.PortfolioDetail;

public class PortfolioAdapter extends BaseAdapter {

	private Context context;
	private List<PortfolioDetail> portfolio;
	private ArrayList<PortfolioDetail> productsSelected = new ArrayList<PortfolioDetail>();
	AQuery query;

	public PortfolioAdapter(Context context, List<PortfolioDetail> portfolio) {

		this.context = context;
		System.out.println("the size of portfolio is :"+portfolio.size());
		portfolio = removeBadPortfolioDetails(portfolio);
		this.portfolio = portfolio;
		System.out.println("the size of portfolio is :"+this.portfolio.size());
		query = new AQuery(context);
	}

	public List<PortfolioDetail> removeBadPortfolioDetails(List<PortfolioDetail> list){
		Date currentDate = new Date(System.currentTimeMillis());

		for(Iterator<PortfolioDetail> itr = list.iterator();itr.hasNext();){  
			PortfolioDetail pfd = itr.next();  
			Date startDate = pfd.getAid().getStartDate();
			Date endDate = pfd.getAid().getEndDate();
			boolean dateOk = startDate.before(currentDate) && endDate.after(currentDate);
			if(!dateOk){  
				itr.remove();  
			}  
		}  
		return list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);
			PortfolioDetail pd = portfolio.get(position);

			// get layout from layout_portfolio.xml
			gridView = inflater.inflate(R.layout.layout_portfolio, null);


			// set value into text views
			TextView tv1 = (TextView) gridView.findViewById(R.id.productname);
			tv1.setText(pd.getProduct().getName());

			TextView tv2 = (TextView) gridView.findViewById(R.id.productprice);
			tv2.setText(""+pd.getProduct().getPrice()+" Rps");

			TextView tv3 = (TextView) gridView.findViewById(R.id.productquantity);
			tv3.setText(""+pd.getQuantity()+" "+pd.getProduct().getUnity());

			// set image based on selected text
			ImageView imageView = (ImageView) gridView.findViewById(R.id.gridImage);
			String strPhoto=pd.getProduct().getIconeURL();
			String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+context.getString(R.string.productPhotosOffLine);

			query.id(imageView).image(directoryPath+strPhoto,true, true, 0,R.drawable.icon);

			//Managing the gridview checkbox
			CheckBox checkBox = (CheckBox) gridView.findViewById(R.id.checkBox);

			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					PortfolioDetail pfd = (PortfolioDetail) getItem(position);
					if(isChecked){						
						productsSelected.add(pfd);	
					}
					else{
						productsSelected.remove(pfd);

					}	
				}
			});


		} else {
			gridView = convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return portfolio.size();
	}

	@Override
	public Object getItem(int position) {
		return portfolio.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @return the productSelecteds
	 */
	public ArrayList<PortfolioDetail> getProductsSelected() {
		return productsSelected;
	}

	/**
	 * @param productSelecteds the productSelecteds to set
	 */
	public void setProductsSelected(ArrayList<PortfolioDetail> productsSelected) {
		this.productsSelected = productsSelected;
	}


}
