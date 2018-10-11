package com.example.owner.v7demotwo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;



/*别同时使用onCreatView和onCreatDialog方法，他们仅仅是为了完成同样一个目的的两条路而已*/
public class CustomDialogFragment extends DialogFragment {

	private DialogFragmentClick dialogFragmentClick;
	private View view;
	public static final int ONCREATEVIEW = 0; 
	public static final int ONCREATDIALOG = 1; 
	/*静态实例调用生成CustomDialogFragment*/
	public static CustomDialogFragment newInstance(int style,int type) {
		CustomDialogFragment dialogFragment = new CustomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("style", style);
        bundle.putInt("type", type);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，
        //则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true 
//        setCancelable(true); 
        //可以设置dialog的显示风格
		int styleNum = getArguments().getInt("style", 0);
        int style = 0;
        switch (styleNum) {
            case 0:
                style = DialogFragment.STYLE_NORMAL;//默认样式
                break;
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;//无标题样式
                break;
            case 2:
                style = DialogFragment.STYLE_NO_FRAME;//无边框样式
                break;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;//不可输入，不可获得焦点样式
                break;
        }
        setStyle(style, 0);//设置样式
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if(getArguments().getInt("type") == ONCREATDIALOG){
			android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("复写onCreateDialog方法，在其中创建AlertDialog来创建对话框：是否退出?")
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialogFragmentClick.negClick();
					dialog.dismiss();
				}
			})
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationZ", 100, 0);
	                animator.addListener(new AnimatorListenerAdapter() {
	                    @Override
	                    public void onAnimationEnd(Animator animation) {				                    	
//	                    	mContext.startActivity(new Intent(mContext, DetailActivity.class));
	                    	dialogFragmentClick.posClick();
	                    	dialog.dismiss();
	                    }
	                });
	                animator.start();
	                
					
				}
			}).setTitle("使用DialogFragment来显示最新dialog：")
			.setCancelable(false);
			
			return  builder.show();
		}else{
			return super.onCreateDialog(savedInstanceState);
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/*也可以在此处自定一个对话框View，但同时就不能复写onCreateDialog方法*/
		if(getArguments().getInt("type") == ONCREATEVIEW){
			view = inflater.inflate(R.layout.dialog_login, container);
			view.findViewById(R.id.cancle).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogFragmentClick.negClick();
				}
			});
			view.findViewById(R.id.login).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogFragmentClick.posClick();
				}
			});
			return view;
		}else{
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}
	public interface DialogFragmentClick {

		public abstract void posClick( );
		public abstract void negClick( );
	}
	public void setDialogFragmentClick(DialogFragmentClick dialogFragmentClick){
		this.dialogFragmentClick = dialogFragmentClick;
	}
}

