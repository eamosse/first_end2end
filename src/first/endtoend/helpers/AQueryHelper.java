package first.endtoend.helpers;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;

import first.endtoend.R;
import first.endtoend.facades.AddressFacade;
import first.endtoend.facades.AidFacade;
import first.endtoend.facades.BeneficiaryFacade;
import first.endtoend.facades.FamilyFacade;
import first.endtoend.facades.PortfolioDetailFacade;
import first.endtoend.facades.PortfolioFacade;
import first.endtoend.facades.ProductFacade;
import first.endtoend.facades.RationCardFacade;
import first.endtoend.facades.TraceDetailFacade;
import first.endtoend.facades.TraceFacade;
import first.endtoend.models.Address;
import first.endtoend.models.Aid;
import first.endtoend.models.Beneficiary;
import first.endtoend.models.Category;
import first.endtoend.models.Family;
import first.endtoend.models.Portfolio;
import first.endtoend.models.PortfolioDetail;
import first.endtoend.models.Product;
import first.endtoend.models.RationCard;
import first.endtoend.models.Trace;
import first.endtoend.models.TraceDetail;

public class AQueryHelper {


	public static void download(AQuery aquery, String url,String imageName, String location){

		File ext = Environment.getExternalStorageDirectory();
		File target = new File(ext,location+imageName);              		
		aquery.progress(null).download(url+imageName, target, new AjaxCallback<File>(){
			@Override
			public void callback(String url, File file, AjaxStatus status) {
				if(file == null)
					System.out.println("Failed to load file");
			}
		});
	}


	public static void load_products(final Context ctx, Map<String, ?> params){
		final AQuery aquery = new AQuery(ctx);
		if (InternetHelper. isOnline(ctx)) {
			System.out.println(">>>>>>< Loading Products >><<<<<<<");
			String url = ctx.getString(R.string.url) + ctx.getString(R.string.load_Product);
			AjaxCallback<String> cb = new AjaxCallback<String>() {
				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch (status.getCode()){
					case Constant.REQUEST_OK:
						int code = JsonHelper.getResponseCodeFromJson(json);
						switch (code) {
						case Constant.REQUEST_OK:	
							Type listType = new TypeToken<ArrayList<Product>>() {}.getType();
							ArrayList<Product> products = JsonHelper.getListObjectFromJson(json,Constant.LIST_PRODUCTS,listType);

							if(!products.isEmpty())

								for(Product p : products)
									try {
										ProductFacade productFacade = new ProductFacade(ctx);
										if(p.getIconeURL() == null){
											p.setIconeURL("");
										}
										if(productFacade.findById(p.getProductId())!= null){
											productFacade.update(p);
										}
										else{
											productFacade.insert(p);
										}
										String path = ctx.getString(R.string.productPhotosOffLine)+p.getIconeURL() ;
										File file = new File(Environment.getExternalStorageDirectory(),path);

										if(!file.exists())
											download(aquery, ctx.getString(R.string.url)+ctx.getString(R.string.productPhotosOnLine), 
													p.getIconeURL(), ctx.getString(R.string.productPhotosOffLine));

									} catch (Exception e) {
										e.printStackTrace();
									}
							System.out.println(">>>>>>>>>>>  End Loading Product<<<<<<<<<<<<");
							break;

						default:
							break;

						}
						break;

					default:
						break;
					}
				}
			};
			aquery.ajax(url, params, String.class, cb);
		}				    
	}


	public static void load_aids(final Context ctx, Map<String, ?> params){
		AQuery aquery = new AQuery(ctx);

		if (InternetHelper. isOnline(ctx)) {
			System.out.println(">>>>>>> Loading Aids <<<<<<<<<<<<<<<");
			String url = ctx.getString(R.string.url) + ctx.getString(R.string.load_Aid);

			AjaxCallback<String> cb = new AjaxCallback<String>() {
				@Override
				public void callback(String url, String json, AjaxStatus status) {
					System.out.println(json);
					switch (status.getCode()){
					case Constant.REQUEST_OK:

						int code = JsonHelper.getResponseCodeFromJson(json);

						switch (code) {
						case Constant.REQUEST_OK:	
							Type listType = new TypeToken<ArrayList<Aid>>() {}.getType();
							ArrayList<Aid> aids = JsonHelper.getListObjectFromJson(json,Constant.LIST_AIDS,listType);

							if(!aids.isEmpty())
								for(Aid a : aids)
									try {
										AidFacade aidFacade = new AidFacade(ctx);
										if(aidFacade.findById(a.getAidId())!=null){
											aidFacade.update(a);
										}
										else{
											aidFacade.insert(a);
										}
										List<Family> familiesToUpdate = new FamilyFacade(ctx).findEntitiesByForeignKey(a.getCategory().getCategoryId(), Category.class);
										List<Integer> idFamilies = new ArrayList<Integer>();
										for(Family f : familiesToUpdate){
											idFamilies.add(f.getFamilyId());
										}


										JSONArray array = new JSONArray(idFamilies);
										Map<String, Object> parameters = new HashMap<String, Object>();
										JSONObject jo = new JSONObject();
										jo.put("code", Constant.CODE_GCM_1);
										jo.put("data", array);
										parameters.put("notif",jo.toString());

										load_families(ctx, parameters);

									} catch (Exception e) {
										e.printStackTrace();
									}

							System.out.println(">>>>>> End Loading Aids >>>>>><<<<<<<");
							break;

						default:
							break;
						}
						break;

					default:
						break;
					}
				}
			}; 
			aquery.ajax(url, params, String.class, cb);
		}
	}


	public static void load_families(final Context ctx, Map<String, ?> params){
		final AQuery aquery = new AQuery(ctx);

		if (InternetHelper. isOnline(ctx)) {
			System.out.println(">>>>> Loading Families <<<<<<<<<<<<");
			String url = ctx.getString(R.string.url) + ctx.getString(R.string.load_Family);

			AjaxCallback<String> cb = new AjaxCallback<String>() {

				@Override
				public void callback(String url, String json, AjaxStatus status) {

					System.out.println(json);
					switch (status.getCode()){

					case Constant.REQUEST_OK:
						int code = JsonHelper.getResponseCodeFromJson(json);
						switch (code) {

						case Constant.REQUEST_OK:	
							Type listType = new TypeToken<ArrayList<Family>>() {}.getType();
							ArrayList<Family> families = JsonHelper.getListObjectFromJson(json,Constant.LIST_FAMILIES,listType);
							if(!families.isEmpty())

								for(Family f : families){
									try {
										FamilyFacade familyFacade = new FamilyFacade(ctx);
										if(familyFacade.findById(f.getFamilyId()) != null){
											familyFacade.update(f);
										}else{
											familyFacade.insert(f);
										}


										List<Beneficiary> newBeneficiaries = f.getBeneficiaries();
										if(newBeneficiaries.size() != 0){
											for(Beneficiary fib : newBeneficiaries){
												fib.setFamily(f);
												BeneficiaryFacade beneficiaryFacade = new BeneficiaryFacade(ctx);
												if(beneficiaryFacade.findById(fib.getBeneficiaryId()) != null){
													beneficiaryFacade.update(fib);
												}else{
													beneficiaryFacade.insert(fib);
												}

												String path = ctx.getString(R.string.benefPhotosOffLine)+fib.getPhotoURL() ;
												File file = new File(Environment.getExternalStorageDirectory(),path);
												if(!file.exists())
													download(aquery, ctx.getString(R.string.url)+ctx.getString(R.string.benefPhotosOnLine),
															fib.getPhotoURL(), ctx.getString(R.string.benefPhotosOffLine));
											}

										}

										Portfolio p = f.getPortfolio();
										if(p != null){
											p.setFamily(f);
											PortfolioFacade portfolioFacade = new PortfolioFacade(ctx);
											if(portfolioFacade.findById(p.getPortfolioId()) != null){
												portfolioFacade.update(p);
											}else{
												portfolioFacade.insert(p);
											}


											for(PortfolioDetail pfd : p.getDetails()){
												pfd.setPortfolio(p);
												PortfolioDetailFacade portfolioDetailFacade = new PortfolioDetailFacade(ctx);
												if(portfolioDetailFacade.findById(pfd.getPortfolioDetailId()) != null){
													portfolioDetailFacade.update(pfd);
												}else{
													portfolioDetailFacade.insert(pfd);	
												}
											}
										}

										RationCard r = f.getRationCard();
										if(r != null){
											r.setFamily(f);
											RationCardFacade rationCardFacade = new RationCardFacade(ctx);
											if(rationCardFacade.findById(r.getRationCardId()) != null){
												rationCardFacade.update(r);
											}
											else{
												rationCardFacade.insert(r);
											}
										}

										Address adr = f.getAddress();
										if(adr != null){
											adr.setFamily(f);
											AddressFacade addressFacade = new AddressFacade(ctx);
											if(addressFacade.findById(adr.getAddressId()) != null){
												addressFacade.update(adr);
											}
											else{
												addressFacade.insert(adr);
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							System.out.println(">>>>>>>> End Loading all Families <<<<<<<<<<");

						}
						break;

					default:
						break;
					}
				}
			};
			aquery.ajax(url, params, String.class, cb);
		}
	}


	public static void delete_family(Context context, int familyId){
		try {
			//delete rationCard of family
			RationCardFacade rcFacade = new RationCardFacade(context);
			RationCard rc = rcFacade.findEntityByForeignKey(familyId, Family.class);
			if(rc !=null)
				rcFacade.delete(rc.getRationCardId());

			//delete beneficiaries of family
			BeneficiaryFacade bFacade = new BeneficiaryFacade(context);
			List<Beneficiary> beneficiaries = bFacade.findEntitiesByForeignKey(familyId, Family.class);

			for(Beneficiary b : beneficiaries){
				delete_beneficiary(context, b.getBeneficiaryId());
			}

			//delete Portfolio and portfolioDetail of family
			PortfolioFacade pfFacade = new PortfolioFacade(context);
			Portfolio p = pfFacade.findEntityByForeignKey(familyId, Family.class);
			if(p != null){

				PortfolioDetailFacade pfdFacade = new PortfolioDetailFacade(context);
				List<PortfolioDetail> pfdetails = pfdFacade.findEntitiesByForeignKey(p.getPortfolioId(), Portfolio.class);
				if(pfdetails != null)
					for(PortfolioDetail pfd : pfdetails)
						pfdFacade.delete(pfd.getPortfolioDetailId());

				pfFacade.delete(p.getPortfolioId());
			}

			//delete address of family
			AddressFacade adrFacade = new AddressFacade(context);
			Address adr = adrFacade.findEntityByForeignKey(familyId, Family.class);
			if(adr != null)
				adrFacade.delete(adr.getAddressId());

			//finally delete family
			new FamilyFacade(context).delete(familyId);

		} catch (Exception e) {
			System.err.println("No Family with id :"+familyId);
			e.printStackTrace();
		}

	}

	public static void delete_product(Context context, int productId){
		try {

			ProductFacade pFacade = new ProductFacade(context);
			Product p = pFacade.findById(productId);

			//delete all portfolio Details related to this product
			PortfolioDetailFacade pfdFacade = new PortfolioDetailFacade(context);
			List<PortfolioDetail> pfDetails = pfdFacade.findEntitiesByForeignKey(productId, Product.class);
			if(pfDetails != null)
				for(PortfolioDetail pfd : pfDetails)
					pfdFacade.delete(pfd.getPortfolioDetailId());

			//delete all trace Details related to this product
			TraceDetailFacade trFacade = new TraceDetailFacade(context);
			List<TraceDetail> trDetails = trFacade.findEntitiesByForeignKey(productId, Product.class);
			if(trDetails != null)
				for(TraceDetail td : trDetails)
					trFacade.delete(td.getTraceDetailId());

			String iconURL=p.getIconeURL();
			String directoryPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath()+File.separator+context.getString(R.string.productPhotosOffLine);
			new File(directoryPath+iconURL).delete();

			//delete the product
			pFacade.delete(productId);

		} catch (Exception e) {
			System.err.println("No product with id :"+productId);
			e.printStackTrace();
		}
	}

	public static void delete_aid(Context context, int aidId){
		System.out.println("deleting aid");
		try {
			//delete all portfolio details related to this aid
			PortfolioDetailFacade pfdFacade = new PortfolioDetailFacade(context);
			List<PortfolioDetail> pfDetails = pfdFacade.findEntitiesByForeignKey(aidId, Aid.class);
			if(pfDetails != null){
				System.out.println("pfdetails not null");
				for(PortfolioDetail pfd : pfDetails)
					pfdFacade.delete(pfd.getPortfolioDetailId());
			}
			else{
			}	
			//delete the aid
			new AidFacade(context).delete(aidId);

		} catch (Exception e) {
			System.err.println("No Aid with id :"+aidId);
			e.printStackTrace();
		}

	}


	public static void delete_beneficiary(Context context, int beneficiaryId){
		try{
			BeneficiaryFacade facade = new BeneficiaryFacade(context);
			Beneficiary b = facade.findById(beneficiaryId);		

			if(b != null){
				//delete all traces of beneficiary b
				TraceFacade trFacade= new TraceFacade(context);
				List<Trace> traces = trFacade.findEntitiesByForeignKey(beneficiaryId, Beneficiary.class);
				if(traces != null)
					for(Trace t : traces){

						//delete all trace details of trace t
						TraceDetailFacade trDetailFacade = new TraceDetailFacade(context);
						List<TraceDetail> tDetails = trDetailFacade.findEntitiesByForeignKey(t.getTraceId(), Trace.class);
						if(tDetails != null)
							for(TraceDetail td : tDetails){
								trDetailFacade.delete(td.getTraceDetailId());
							}

						trFacade.delete(t.getTraceId());
					}
				String strPhoto=b.getPhotoURL();
				String directoryPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath()+File.separator+context.getString(R.string.benefPhotosOffLine);
				new File(directoryPath+strPhoto).delete();

				facade.delete(b.getBeneficiaryId());
			}
		}
		catch (Exception e) {
			System.err.println("Cannot delete beneficiary with id : "+beneficiaryId);
			e.printStackTrace();
		}
	}

}
