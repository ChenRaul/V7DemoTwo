package com.example.owner.v7demotwo;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseRecyclerViewAdapter<String> {

	private ArrayList<String> data;
	public MyAdapter(Context mContext, ArrayList<String> mData) {
		super(mContext, mData);
		this.data = mData;
	}
	@Override
	protected void onBindVie(ViewHolder holder, int position) {
		if (holder instanceof HeaderHolder) {//headerview的操作
          HeaderHolder headerHolder = (HeaderHolder) holder;
      } else if (holder instanceof FooterHolder) {//可以根据数据量的总大小或者设置标志位来确定是否加载完毕
          FooterHolder footHolder = (FooterHolder) holder;
          if(data.size() >= realItemCount){
          	footHolder.tv.setText("加载完毕");
          }else{
          	footHolder.tv.setText("正在加载中....");
          }
      } else {//具体内容
    	  final ItemHolder itemHolder = (ItemHolder) holder;
    	  
      	if(isShowHeader){//如果要显示头部，需要将list数据中index是position-1，需要注意添加几个头部，就是减去几个头部
      		realPosition = position-1;
      	}else{
      		realPosition = position;
      	}
      	VolleyRequestDemo.imageLoader(itemHolder.imageView,data.get(realPosition));
      	itemHolder.tv.setText("美文"+(realPosition));
      //点击事件，由于RecycView没有设置click事件和Long Click事件，所以需要自己在此处添加
      	itemHolder.mView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
              builder.setMessage("是否进入详情界面?")
              		.setNegativeButton("取消", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog, int which) {
								ObjectAnimator animator = ObjectAnimator.ofFloat(itemHolder.mView, "translationZ", 100, 0);
				                animator.addListener(new AnimatorListenerAdapter() {
				                    @Override
				                    public void onAnimationEnd(Animator animation) {				                    	
				                    	mContext.startActivity(new Intent(mContext, DetailActivity.class));
				                    	dialog.dismiss();
				                    }
				                });
				                animator.start();
							}
						}).setTitle("V7中最新dialog展示：")
						.setCancelable(false)
						.show();
          }
      });
      //设置长点击事件
  	itemHolder.mView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				//TODO
				return true;
			}
		});
      }
		
	}
	@Override
	protected ViewHolder onCreateView(ViewGroup parent, int viewType) {
		  if(viewType == ITEM_TYPE_HEADER){
	    	   return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false));
	       }else if(viewType == ITEM_TYPE_FOOTER){
	    	   return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false));
	       }else{
	    	   return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false));
	       }  
	
	}
	
	 class ItemHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public  ImageView imageView;
        public TextView tv;
        public ItemHolder(View root) {
            super(root);
            this.mView = root;
            this.imageView = (ImageView) root.findViewById(R.id.item_image);
            this.tv = (TextView) root.findViewById(R.id.item_tv);
        }
    }
	 class HeaderHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView tv;
        public HeaderHolder(View root) {
            super(root);
            this.mView = root;
            this.tv = (TextView) root.findViewById(R.id.list_item_header_tv);
            
        }
    }
	 class FooterHolder extends RecyclerView.ViewHolder {
		 public final View mView;
		public TextView tv;
        public FooterHolder(View root) {
            super(root);
            this.mView = root;
            this.tv = (TextView) root.findViewById(R.id.list_item_header_tv);
        }
    }

}
