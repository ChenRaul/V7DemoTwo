package com.example.owner.v7demotwo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


/*要使用ToolBar，activity就必须继承AppCompatActivity类，更改主题为NoActionBar的主题
 * 
 * CollapsingToolbarLayout,CollapsingToolbarLayout,AppBarLayout这三个一般是相互结合使用，否则不会出现应该得到的效果额
 * */
public class DetailActivity extends AppCompatActivity {

	
	private static final int EXPANDED = 0;//CollapsingToolbarLayout展开
	private static final int COLLAPSED = 1;//CollapsingToolbarLayout折叠
	private static final int INTERNEDIATE = 2;//CollapsingToolbarLayout中间
	
	private int state = -1;
	
	private AppBarLayout appBarLayout;
	private CollapsingToolbarLayout collapsingToolbar;
	private TextView title;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //使用CollapsingToolbarLayout时必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上不会显示     
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.detail_toolbar);
       title = (TextView) findViewById(R.id.detail_title);
//        toolbar.setTitle("内容详情");
       // toolbar.setNavigationIcon(R.drawable.ic_menu);这一句话会覆盖在下面出现返回的地方的图标

        
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//这句话会出现一个返回的图标
        
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
         collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        
//        collapsingToolbar.setTitle("详情信息");
        
      //通过CollapsingToolbarLayout修改字体颜色
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        collapsingToolbar.setCollapsedTitleTextColor(Color.RED);//设置收缩后Toolbar上字体的
      
        toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println(v);
				finish();
			}
		});
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListener() {
			
			@Override
			public void onOffsetChanged(AppBarLayout arg0, int arg1) {
				System.out.println(arg1+"**"+appBarLayout.getTotalScrollRange()+"--"+"state="+state);
				if(arg1 == 0){
					
					if(state != EXPANDED){
						state = EXPANDED; //0
						collapsingToolbar.setTitle("详情信息");
						
					}
				}else if(Math.abs(arg1) >= appBarLayout.getTotalScrollRange()-50){
					if(state != COLLAPSED){
						collapsingToolbar.setTitle("");
						title.setVisibility(View.VISIBLE);
						state = COLLAPSED;// 1
					}
				}else{
					if(state != INTERNEDIATE){
						if(state == COLLAPSED){
	                       //由折叠变为中间状态时,应该隐藏自定义的标题或者其它view
							title.setVisibility(View.GONE);
	                    }
						collapsingToolbar.setTitle("详情信息");
						state = INTERNEDIATE; //2
					}
				}
				
			}
		});
        
        
        CardView  list_item_card_detail1 = (CardView) findViewById(R.id.list_item_card_detail1);
        CardView  list_item_card_detail2 = (CardView) findViewById(R.id.list_item_card_detail2);
        CardView  list_item_card_detail3 = (CardView) findViewById(R.id.list_item_card_detail3);
        CardView  list_item_card_detail4 = (CardView) findViewById(R.id.list_item_card_detail4);
        CardView  list_item_card_detail5 = (CardView) findViewById(R.id.list_item_card_detail5);
        
        Spinner spinner1 = (Spinner) list_item_card_detail1.findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner) list_item_card_detail2.findViewById(R.id.spinner);
        Spinner spinner3 = (Spinner) list_item_card_detail3.findViewById(R.id.spinner);
        Spinner spinner4 = (Spinner) list_item_card_detail4.findViewById(R.id.spinner);
        Spinner spinner5 = (Spinner) list_item_card_detail5.findViewById(R.id.spinner);
        
        String[] mItems = getResources().getStringArray(R.array.languages);
     // 建立Adapter并且绑定数据源
    	ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mItems);
    	ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, mItems);
        spinner1 .setAdapter(adapter);
        spinner2 .setAdapter(adapter1);
        spinner3 .setAdapter(adapter);
        spinner4 .setAdapter(adapter1);
        spinner5 .setAdapter(adapter);
    }

    public void checkin(View view) {
        Snackbar.make(view, "checkin success!", Snackbar.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overaction, menu);
        return true;
    }
}
