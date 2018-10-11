package com.example.owner.v7demotwo;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshView;//v4扩展包中的下拉刷新组件
	private OnRefreshListener refreshListener;
	private ArrayList<String> data = new ArrayList<String>();
	private ArrayList<String> changeData = new ArrayList<String>();
    private boolean isLoadingMore = false;
	private RecyclerViewAdapter adapter; //第一种直接new原生态的adapter
//    private MyAdapter adapter;//第二种继承经过提炼的base的adapter，感觉第一种代码更少,其实用原生的，需要时直接new，再根据情况设置头部和footer显示就可以了
	private int realItemCount = 30;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				data.clear();
				data.addAll(changeData);
				adapter.setShowFooter(true);//显示footer
				adapter.setRealItemCount(realItemCount);//设置数据的总条数
				adapter.setData(data);
				refreshView.setRefreshing(false);
				isLoadingMore = false;
			}else if(msg.what == 1){
				isLoadingMore = false;
				data.addAll(changeData);
				adapter.notifyDataSetChanged();
				if(data.size() >= 30){
					isLoadingMore = true;
				}
			}
		};
	};
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       
        if(refreshView == null){
        	refreshView =(SwipeRefreshLayout) inflater.inflate(R.layout.list_fragment, container, false);
        	System.out.println("onCreateView");
        	mRecyclerView = (RecyclerView) refreshView.findViewById(R.id.recycler_view);
            /*setColorSchemeColors() 设置进度条颜色，可设置多个值，进度条颜色在这多个颜色值之间变化
    			setSize() 设置下拉出现的圆形进度条的大小，有两个值：SwipeRefreshLayout.DEFAULT 和 SwipeRefreshLayout.LARGE
    			setProgressBackgroundColorSchemeColor()设置圆形进度条背景颜色。
    			setDistanceToTriggerSync() 设置手势操作下拉多少距离之后开始刷新数据*/
           refreshListener = new OnRefreshListener() {//下拉刷新
    			@Override
    			public void onRefresh() {
    				System.out.println("刷新");
    				new Thread(){
    					@Override
						public void run() {
    						try {
    							Thread.sleep(3000);
    						} catch (InterruptedException e) {
    							e.printStackTrace();
    						}
    						changeData.clear();
    						for(int i=0;i<10;i++){
    							if(i%2 == 0){
    								changeData.add("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg");
    					        }else{
    					        	changeData.add("http://192.168.11.223:8080/image/QQ20160920143938.png");
    					        }
    						}
    						handler.sendEmptyMessage(0);
    					};
    				}.start();
    			}
    		};
            refreshView.setOnRefreshListener(refreshListener);           
            refreshView.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN,Color.YELLOW);
            refreshView.setSize(SwipeRefreshLayout.DEFAULT);
            refreshView.setProgressBackgroundColorSchemeColor(Color.GRAY);
            refreshView.setDistanceToTriggerSync(50);
            
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
//          mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),2));
          /*使用瀑布流的方式布局，StaggeredGridLayoutManager.VERTICAL代表有3列；
           * StaggeredGridLayoutManager.HORIZONTAL代表有3行，这样一来此时就是横向滑动的GridView了，
           * 
          */
//          mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
          
            adapter = new RecyclerViewAdapter(getActivity(),data);
//            adapter = new MyAdapter(getActivity(),data);
          mRecyclerView.setAdapter(adapter);
          /*设置Item的增加、移除动画，
          如果使用了动画，增加和删除数据更新ITem，则需要使用notifyItemInserted(position)与notifyItemRemoved(position)方法，
         前者是增加，后者是删除，目测好像只能用position一个一个的添加、删除，一般是先增加或删除数据集的position，再执行这两个方法
         当然如果没有使用动画模式，则跟listview是一样的
          */
          mRecyclerView.setItemAnimator(new DefaultItemAnimator());
          mRecyclerView.addOnScrollListener(new OnScrollListener() {

          	@Override
				public void onScrollStateChanged(RecyclerView recyclerView,
						int newState) {
          		/*
          		 * recyclerView: 当前在滚动的RecyclerView;
          		 * newState: 当前滚动状态,有三种状态：SCROLL_STATE_IDLE(静止没有滚动)  SCROLL_STATE_DRAGGING(正在滑动) SCROLL_STATE_SETTLING(自动滚动) 
          		 * */
					super.onScrollStateChanged(recyclerView, newState);
					
					/* public boolean canScrollVertically (int direction)
						这个方法是判断View在竖直方向是否还能向上，向下滑动。
						其中，direction为 -1 表示手指向下滑动（屏幕向上滑动）， 1 表示手指向上滑动（屏幕向下滑动）。
						返回false，表明已经滚动到底部或者顶部；
						
						public boolean canScrollHorizontally (int direction)
						这个方法用来判断 水平方向的滑动
					
					* 
					*/
					/*此处也可以添加自动加载代码，跟在onScrolled方法里面是一样的，随便选择哪一种；
					 * 
					 * 至于是recyclerView.canScrollVertically(1)是false还是true再开始加载更多的数据，取决于是否想快速的进行加载，
					 * true表示只要屏幕向下滑动就会开始加载数据。
					 * false表示只有滑动到底部时才开始加载数据，这可能导致速度比较慢
					 * */
					if(!isLoadingMore && !recyclerView.canScrollVertically(1)){
						isLoadingMore = true;
//						//加载代码
	                    new LoadMoreThread().start();
	                    
					}
					
				}

				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);
					/*recyclerView : 当前滚动的view
						dx : 水平滚动距离
						dy : 垂直滚动距离
						dx > 0时为手指向左滚动, 列表滚动显示右面的内容
						dx < 0时为手指向右滚动, 列表滚动显示左面的内容
						dy > 0时为手指向上滚动, 列表滚动显示下面的内容
						dy < 0时为手指向下滚动, 列表滚动显示上面的内容
					*/
					//第一个可见的item的位置
					int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
	               //item的总个数
					int totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
	                //当前可见的Item的个数
	                int visibleItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getChildCount();
					
	                System.out.println(firstVisibleItem+"-------"+visibleItemCount+"------"+totalItemCount+"******"+dy);
	                //firstVisibleItem+visibleItemCount >= totalItemCount时，表明已经达到底部了
//					if (!isLoadingMore && firstVisibleItem+visibleItemCount >= totalItemCount && dy > 0) {//
//						System.out.println("加载代码线程运行");
//                       isLoadingMore = true;
//                       new LoadMoreThread().start();
//	                }
				}

          	
			});
          
          
           /*要添加下面两句话才能让这个刷新组建主动调用出来，并消失*/
            refreshView.post(new Runnable() {			
    			@Override
    			public void run() {
    				refreshView.setRefreshing(true);			
    			}
    		});  
            refreshListener.onRefresh();
        }
        ViewGroup parent = (ViewGroup) refreshView.getParent();
	    if (parent != null){
	    	parent.removeView(refreshView);
	    }
        return refreshView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置布局管理器
        System.out.println("onActivityCreated");
        
       
       
        
    }
class LoadMoreThread extends Thread{
	@Override
	public void run() {
		new Thread(){
				@Override
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					changeData.clear();
					for(int i=0;i<10;i++){
						if(i%2 == 0){
							changeData.add("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg");
				        }else{
				        	changeData.add("http://192.168.11.223:8080/image/QQ20160920143938.png");
				        }
						System.out.println("添加前："+data.size()+"----"+adapter.getItemCount());
					}
					handler.sendEmptyMessage(1);
				};
			}.start();
	}
}
	
}
