package com.example.owner.v7demotwo;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/*该RecycleView的adapter可以添加header或者footer*/
public abstract  class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context mContext;
    public  final int ITEM_TYPE_HEADER = 1;//添加header
    public  final int ITEM_TYPE_FOOTER = 2;//添加footer
    public boolean isShowHeader = false;//默认不显示header
    public boolean isShowFooter = false;//默认不显示footer
    private ArrayList<E> data = new ArrayList<E>();
    public int realPosition=0;//实际的item position（当增加headerview时）,防止实际的Item获取data数组List中的内容的出现问题，主要是index和position的换算
    public int realItemCount = 30;//实际的Item内容的总个数，用于标识是加载中还是加载完毕
    		
    

    public BaseRecyclerViewAdapter(Context mContext,ArrayList<E> mData) {
        this.mContext = mContext;
        this.data = mData;
//        setShowFooter(true);
//        setShowHeader(true);
       
    }
    public void setData(ArrayList<E> mData){
    	this.data = mData;
    	notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {     
    	return onCreateView(parent, viewType);
    }
    protected abstract RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType);
    protected abstract void onBindVie(final RecyclerView.ViewHolder holder, int position);
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    	onBindVie(holder,position);    	
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
}
