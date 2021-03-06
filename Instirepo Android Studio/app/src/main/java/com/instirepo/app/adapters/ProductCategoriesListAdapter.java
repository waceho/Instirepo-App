package com.instirepo.app.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.BaseActivity;
import com.instirepo.app.activities.ProductsListingActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.ProductCategoriesListObject;
import com.instirepo.app.objects.ProductObjectSingle;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.RoundedImageView;

public class ProductCategoriesListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	ProductCategoriesListObject mData;

	public static int ITEM_CATEGORY = 0;
	public static int ITEM_RECENTLY_VIEWED = 1;
	public static int ITEM_TRENDING = 2;

	int widthOfProduct, heightOfCategory;
	MyClickListener clickListerner;

	public ProductCategoriesListAdapter(ProductCategoriesListObject obj,
			Context activity) {
		this.context = activity;
		this.mData = obj;
		heightOfCategory = context.getResources().getDisplayMetrics().widthPixels
				/ 2
				- context.getResources().getDimensionPixelSize(
						R.dimen.z_margin_small);
		clickListerner = new MyClickListener();
	}

	@Override
	public int getItemCount() {
		if (mData.getRecently_viewed().size() == 0) {
			return mData.getCategories().size() + 1;
		} else {
			return mData.getCategories().size() + 2;
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return ITEM_TRENDING;
		} else if (position == mData.getCategories().size() + 1) {
			return ITEM_RECENTLY_VIEWED;
		} else
			return ITEM_CATEGORY;
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCOm, int pos) {
		if (getItemViewType(pos) == ITEM_CATEGORY) {
			pos = pos - 1;
			CategoriesHolder holder = (CategoriesHolder) holderCOm;
			ImageRequestManager.get(context).requestImage(
					context,
					holder.image,
					ZApplication.getImageUrl(mData.getCategories().get(pos)
							.getImage()), 0);
			holder.name.setText(mData.getCategories().get(pos).getName());

			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.container
					.getLayoutParams();
			params.height = heightOfCategory;
			holder.container.setLayoutParams(params);

			holder.container.setTag(pos);
			holder.container.setOnClickListener(clickListerner);
		} else if (getItemViewType(pos) == ITEM_RECENTLY_VIEWED) {
			TrendingProductsHolder holder = (TrendingProductsHolder) holderCOm;
			holder.recyclerView.setLayoutManager(new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false));
			holder.recyclerView.setAdapter(new TrendingProductsListAdapter(
					mData.getRecently_viewed(), getItemViewType(pos)));

			holder.heading.setText("Recently Viewed Products");

			holder.shopByCategoriesText.setVisibility(View.GONE);

			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.viewMore
					.getLayoutParams();
			params.topMargin = context.getResources().getDimensionPixelSize(
					R.dimen.z_margin_mini);
			holder.viewMore.setLayoutParams(params);

			holder.viewMore.setTag(pos);
			holder.viewMore.setOnClickListener(clickListerner);
		} else if (getItemViewType(pos) == ITEM_TRENDING) {
			TrendingProductsHolder holder = (TrendingProductsHolder) holderCOm;
			holder.recyclerView.setLayoutManager(new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false));
			holder.recyclerView.setAdapter(new TrendingProductsListAdapter(
					mData.getTrending_products(), getItemViewType(pos)));

			holder.heading.setText("Trending Products");

			holder.shopByCategoriesText.setVisibility(View.VISIBLE);

			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.viewMore
					.getLayoutParams();
			params.topMargin = 0;
			holder.viewMore.setLayoutParams(params);

			holder.viewMore.setTag(pos);
			holder.viewMore.setOnClickListener(clickListerner);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		if (type == ITEM_CATEGORY) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.product_categories_list_item_category, parent,
					false);
			CategoriesHolder holder = new CategoriesHolder(v);
			return holder;
		} else if (type == ITEM_RECENTLY_VIEWED || type == ITEM_TRENDING) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.product_categories_list_trending_products, parent,
					false);
			TrendingProductsHolder holder = new TrendingProductsHolder(v);
			return holder;
		}
		return null;
	}

	class TrendingProductsHolder extends RecyclerView.ViewHolder {

		RecyclerView recyclerView;
		LinearLayout viewMore;
		TextView heading, shopByCategoriesText;

		public TrendingProductsHolder(View v) {
			super(v);
			recyclerView = (RecyclerView) v
					.findViewById(R.id.postsbyreachersrecyclef);
			viewMore = (LinearLayout) v.findViewById(R.id.viewmorelayout);
			heading = (TextView) v.findViewById(R.id.viewmorehading);
			shopByCategoriesText = (TextView) v
					.findViewById(R.id.shopbyCategories);
		}
	}

	class CategoriesHolder extends RecyclerView.ViewHolder {

		RoundedImageView image;
		TextView name;
		FrameLayout container;

		public CategoriesHolder(View v) {
			super(v);
			image = (RoundedImageView) v.findViewById(R.id.categoryimage);
			name = (TextView) v.findViewById(R.id.categoryname);
			container = (FrameLayout) v.findViewById(R.id.containergridselect);
		}
	}

	class TrendingProductsListAdapter extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {

		List<ProductObjectSingle> mData;
		int itemType;

		public TrendingProductsListAdapter(List<ProductObjectSingle> data,
				int itemType) {
			this.itemType = itemType;
			this.mData = data;
		}

		@Override
		public int getItemCount() {
			return mData.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder holderCOm, int pos) {
			ProductHolder holder = (ProductHolder) holderCOm;
			ImageRequestManager.get(context).requestImage(context,
					holder.image,
					ZApplication.getImageUrl(mData.get(pos).getImage()), -1);
			holder.name.setText(mData.get(pos).getName());
			holder.price.setText("₹ " + mData.get(pos).getPrice());
			holder.mrp.setText("₹ " + mData.get(pos).getMrp());

			holder.mrp.setPaintFlags(holder.mrp.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);

			holder.containerLayout.setTag(R.integer.z_select_post_tag_holder,
					itemType);
			holder.containerLayout.setTag(R.integer.z_select_post_tag_position,
					pos);
			holder.containerLayout.setOnClickListener(clickListerner);
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
			View v = LayoutInflater
					.from(context)
					.inflate(
							R.layout.product_categories_list_trending_products_list_item,
							arg0, false);
			ProductHolder holder = new ProductHolder(v);
			return holder;
		}

		class ProductHolder extends RecyclerView.ViewHolder {

			TextView name, mrp, price;
			RoundedImageView image;
			FrameLayout containerLayout;

			public ProductHolder(View v) {
				super(v);
				name = (TextView) v.findViewById(R.id.productname);
				mrp = (TextView) v.findViewById(R.id.productmrp);
				price = (TextView) v.findViewById(R.id.productprice);
				image = (RoundedImageView) v.findViewById(R.id.productimage);
				containerLayout = (FrameLayout) v
						.findViewById(R.id.productcontainerlayout);
			}
		}
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.containergridselect:
				int pos = (int) v.getTag();
				Intent i = new Intent(context, ProductsListingActivity.class);
				i.putExtra("categoryid", mData.getCategories().get(pos).getId());
				i.putExtra("typeoflisting", LISTING_BY_CATEGORY);
				context.startActivity(i);
				break;
			case R.id.viewmorelayout:
				pos = (int) v.getTag();
				i = new Intent(context, ProductsListingActivity.class);
				if (getItemViewType(pos) == ITEM_RECENTLY_VIEWED) {
					i.putExtra("typeoflisting", LISTING_BY_RECENT);
				} else if (getItemViewType(pos) == ITEM_TRENDING) {
					i.putExtra("typeoflisting", LISTING_BY_TRENDING);
				}
				context.startActivity(i);
				break;
			case R.id.productcontainerlayout:
				int type = (int) v.getTag(R.integer.z_select_post_tag_holder);
				pos = (int) v.getTag(R.integer.z_select_post_tag_position);
				if (type == ITEM_RECENTLY_VIEWED) {
					((BaseActivity) context).openProductDetailActivity(mData
							.getRecently_viewed().get(pos).getId());
				} else if (type == ITEM_TRENDING) {
					((BaseActivity) context).openProductDetailActivity(mData
							.getTrending_products().get(pos).getId());
				}
				break;

			default:
				break;
			}
		}
	}
}
