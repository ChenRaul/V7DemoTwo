package com.example.owner.v7demotwo;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/*该RecycleView的adapter可以添加header或者footer*/
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int ITEM_TYPE_HEADER = 1;//添加header
    private static final int ITEM_TYPE_FOOTER = 2;//添加footer
    private boolean isShowHeader = false;//默认不显示header
    private boolean isShowFooter = false;//默认不显示footer
    private ArrayList<String> data = new ArrayList<String>();
    private int realPosition=0;//实际的item position（当增加headerview时）,防止实际的Item获取data数组List中的内容的出现问题，主要是index和position的换算
    private int realItemCount = 30;//实际的Item内容的总个数，用于标识是加载中还是加载完毕
    

    public RecyclerViewAdapter(Context mContext,ArrayList<String> mData) {
        this.mContext = mContext;
        this.data = mData;
//        setShowFooter(true);
//        setShowHeader(true);
       
    }
    public void setData(ArrayList<String> mData){
    	this.data = mData;
    	notifyDataSetChanged();
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if(viewType == ITEM_TYPE_HEADER){
    	   return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false));
       }else if(viewType == ITEM_TYPE_FOOTER){
    	   return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false));
       }else{
    	   return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false));
       }       
    }

    @TargetApi(Build.VERSION_CODES.BASE)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    	
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
    public int getItemCount() {
    	int count=this.data.size();
    	if (isShowHeader) {//增加header
           count++;
        } 
    	if(isShowFooter) {//增加footer
            count++;
        }
        return count;
    }
    /*一般是position为0就是添加header，position + 1 == getItemCount()时添加footer；
     * 增加多个footer和header时，此处要进行修改,实际上也不需要，多个footer可以成为一个footer；
     * 如果确实要增加多个footer或者header，此处就要根据实际进行修改，同时有几个就要设置几个ItemViewType，以便在onCreateViewHolder等方法中进行区别；
     * 同时getItemCount()方法中的count也需要做相应的修改。
     * */
    @Override
	public int getItemViewType(int position) {
    	if(isShowHeader && isShowFooter){//同时增加header和footer
    		if (position == 0) {
    			return ITEM_TYPE_HEADER;
    		} else if (position+1 == getItemCount()) {
    			return ITEM_TYPE_FOOTER;
    		} else{
				return 0;//正常的item内容
    		}
    	}else{
    		if(isShowHeader){//只增加header
    			if (position == 0) {
        			return ITEM_TYPE_HEADER;
        		} else{
    				return 0;//正常的item内容
        		}
    		}else if(isShowFooter){//只增加footer
    			if (position+1 == getItemCount()) {
        			return ITEM_TYPE_FOOTER;
        		} else{
    				return 0;//正常的item内容
        		}
    		}else{//两个都不增加
    			return 0;
    		}
    	}
	}

	public boolean isShowHeader() {
		return isShowHeader;
	}
	public void setShowHeader(boolean isShowHeader) {
		this.isShowHeader = isShowHeader;
	}

	public boolean isShowFooter() {
		return isShowFooter;
	}
	public void setShowFooter(boolean isShowFooter) {
		this.isShowFooter = isShowFooter;
	}

	public int getRealItemCount() {
		return realItemCount;
	}
	public void setRealItemCount(int realItemCount) {
		this.realItemCount = realItemCount;
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
