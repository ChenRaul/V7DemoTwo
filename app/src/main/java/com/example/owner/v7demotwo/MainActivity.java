package com.example.owner.v7demotwo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



/*要使用ToolBar，activity就必须继承AppCompatActivity类，更改主题为NoActionBar的主题
 * 
 * CollapsingToolbarLayout,CollapsingToolbarLayout,AppBarLayout这三个一般是相互结合使用，否则不会出现应该得到的效果额
 * */

/*对于要隐藏或者要消失的组件最好使用ViewStub*/
public class MainActivity extends AppCompatActivity implements CustomDialogFragment.DialogFragmentClick {

	public static MainActivity main_this;
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    public Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	main_this = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("V7新增view组件展示");
        toolbar.setSubtitle("小标题");
        toolbar.setTitleTextColor(Color.RED);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawerLayout.openDrawer(GravityCompat.START);
			}
		});
        toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
//					Toast.makeText(MainActivity.this, "点击菜单", 0).show();
				
					if(arg0.getTitle().equals("设置")){
						showListPopupWindow(toolbar);//展示listPopupWindow
					}
				return false;
			}
		});
        
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        NavigationView navigationView =
                (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R
//                .string.app_name, R.string.app_name);
//        mDrawerLayout.setDrawerListener(toggle);//添加监听，自动帮我们处理事项
//        toggle.syncState();//异步启动
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Snackbar comes out", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Toast comes out",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();
        VolleyRequestDemo.simpleRequest(ApplicationTest.TAG);
        VolleyRequestDemo.getJsonObjectRequest();
        VolleyRequestDemo.getJsonArrayRequest();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu = menu;
       getMenuInflater().inflate(R.menu.menu_overaction, menu);
        Log.d("V7Demo","餐单的大小"+menu.getItem(2));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置Tablayout的模式，可滑动 Tab，不是固定的
      
        List<String> titles = new ArrayList<String>();
        titles.add("Page One");
        titles.add("Page Two");
        titles.add("Page Three");
        titles.add("Page Four");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(3)));
        
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new ListFragment());
        fragments.add(new ListFragment());
        fragments.add(new ListFragment());
        fragments.add(new ListFragment());
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        /*如果添加了上面这句代码，就不要添加下面的监听，否则会导致点击tab时，viewpager不会切换；
         * 如果确实需要以下监听，则需要屏蔽上面的 mTabLayout.setTabsFromPagerAdapter(adapter);
         * 
         * 同时需要在onTabSelected方法中添加mViewPager.setCurrentItem(arg0.getPosition())代码；
         * 才能在点击Tab时切换viewpager页面
         * */
//        mTabLayout.setOnTabSelectedListener(new OnTabSelectedListener() {
//			
//			@Override
//			public void onTabUnselected(Tab arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onTabSelected(Tab arg0) {
//				// TODO Auto-generated method stub
//				System.out.println("点击："+arg0.getPosition());
//				mViewPager.setCurrentItem(arg0.getPosition());
//			}
//			
//			@Override
//			public void onTabReselected(Tab arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
    }

    private void setupDrawerContent(NavigationView navigationView) {//侧滑窗口中的item事件
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                    	System.out.println(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }); 
        //获取头部的VIew并设置监听
        navigationView.getHeaderView(0).setOnClickListener(new OnClickListener() {
						@Override
			public void onClick(View v) {
				 Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				 MainActivity.this.startActivity(intent);
				 mDrawerLayout.closeDrawers();
				
			}
		});  
    }
    private void showListPopupWindow(View view){
    	final String items[]={"LPWitem1","LPWitem2","LPWitem3","LPWitem4","LPWitem5"};
    	final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
    	listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
    	listPopupWindow.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this, "点击"+items[position], 0).show();	
				listPopupWindow.dismiss();
				CustomDialogFragment customDialogFragment = CustomDialogFragment.newInstance(0,CustomDialogFragment.ONCREATEVIEW);
		    	customDialogFragment.setDialogFragmentClick(MainActivity.this);
		    	customDialogFragment.show(getSupportFragmentManager(), "ITEM");
			}
		});
        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        int height1 = wm1.getDefaultDisplay().getHeight();
    	//设置ListPopupWindow的锚点,也就是弹出框的位置是相对当前参数View的位置来显示，
    	listPopupWindow.setAnchorView(view);
    	//ListPopupWindow 距锚点的距离，也就是相对锚点View的位置
    	listPopupWindow.setHorizontalOffset(width1*2);
//    	listPopupWindow.setVerticalOffset(-20);//垂直
    	listPopupWindow.setWidth(200);
    	listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
    	listPopupWindow.setModal(true);//这句让点击其它地方时，listPopupWIndow消失，同时也不会触发其它的触摸事件
    	//listPopupWindow.setForceIgnoreOutsideTouch(true);这句话在有上面这句话时好像不起作用
    	listPopupWindow.show();
    }
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	//第一种创建方法
//    	CustomDialogFragment customDialogFragment = new CustomDialogFragment();
//    	customDialogFragment.setDialogFragmentClick(this);
//    	customDialogFragment.show(getSupportFragmentManager(), "EXIT");
    	//第二种创建方法
    	CustomDialogFragment customDialogFragment = CustomDialogFragment.newInstance(0,CustomDialogFragment.ONCREATDIALOG);
    	customDialogFragment.setDialogFragmentClick(this);
    	customDialogFragment.show(getSupportFragmentManager(), "EXIT");
//    	super.onBackPressed();
    }
    
	@Override
	public void posClick() {
		if(getSupportFragmentManager().findFragmentByTag("ITEM") != null){
			CustomDialogFragment fragment = (CustomDialogFragment) getSupportFragmentManager().findFragmentByTag("ITEM");
			fragment.dismiss();
			
		}else{
			this.finish();
		}
		
		
	}

	@Override
	public void negClick() {
		/*其实可以把CustomDialogFragment定义成一个全局变量*/
		
		if(getSupportFragmentManager().findFragmentByTag("ITEM") != null){
			CustomDialogFragment fragment = (CustomDialogFragment) getSupportFragmentManager().findFragmentByTag("ITEM");
			fragment.dismiss();
		}else{
			CustomDialogFragment fragment = (CustomDialogFragment) getSupportFragmentManager().findFragmentByTag("EXIT");
			fragment.dismiss();
		}
	}
}
