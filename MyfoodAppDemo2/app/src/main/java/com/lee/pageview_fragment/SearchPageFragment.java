package com.lee.pageview_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.lee.content_fragment.ui.localSearch.LocalSearchActivity;
import com.lee.content_fragment.ui.onlineSearch.OnlineSeachActivity;
import com.lee.myfoodappdemo2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class SearchPageFragment extends Fragment {

    private SearchPageViewModel mViewModel;
    private EditText editTextSearch;
    private Button button_search;
    private RadioButton radioButton_01,radioButton_02;
    private RadioGroup radioGroup;
    private LottieAnimationView lottieAnimationView_buttom,lottieAnimationView_top;


    public static SearchPageFragment newInstance() {
        return new SearchPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.search_page_fragment, container, false);
        initView(view);

        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchPageViewModel.class);
        // TODO: Use the ViewModel
    }

    public void initView(View view)
    {
        editTextSearch=view.findViewById(R.id.editText_search);
        button_search=view.findViewById(R.id.button_search);
        radioButton_01=view.findViewById(R.id.radioButtonloc);//本地搜索 默认
        radioButton_02=view.findViewById(R.id.radioButtonOnl);//网络搜索
        radioGroup=view.findViewById(R.id.radiogroup);
        lottieAnimationView_buttom=view.findViewById(R.id.lottile_buttom);
        lottieAnimationView_top=view.findViewById(R.id.lottile_top);

        final int[] i = {0};
        lottieAnimationView_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i[0] ==0)
                {
                    lottieAnimationView_buttom.setAnimation("loader_ball_with_physical_interaction.json");

                    i[0] =1;
                }else
                {
                    lottieAnimationView_buttom.setAnimation("ripple.json");
                    i[0] =0;
                }
                lottieAnimationView_buttom.playAnimation();

            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                System.out.println("选择状态："+i);

            }
        });


        //监听搜索
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //监听软盘搜索键
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH)
                {
                    String text=editTextSearch.getText().toString();
                    if(!text.isEmpty())
                    {
                        System.out.println("软盘按钮提按下值为:"+text);

                    }
                    InputMethodManager imm =(InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                       imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(),0);
                       search(text,radioButton_01.isChecked());

                    return true;
                }
                return false;
            }
        });



        //监听搜索按钮
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=editTextSearch.getText().toString();

                InputMethodManager imm =(InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);//方值软盘打乱布局
                if (text.length()>0)
                {
                    search(text,radioButton_01.isChecked());
                }
                else
                {
                    editTextSearch.setHint("内容不能为空");
                }
            }
        });
    }

    public void search(String keywords,boolean cheched)
    {


        if(cheched)//本地搜索
        {
            Intent intent = new Intent(getContext(), LocalSearchActivity.class);
            intent.putExtra("keyWord",keywords);
            startActivityForResult(intent,1);
        }
        else
        {
            //全网搜索

            Intent intent = new Intent(getContext(), OnlineSeachActivity.class);
            intent.putExtra("keyWord",keywords);
            startActivityForResult(intent,1);
        }


    }



}
