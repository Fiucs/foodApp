package com.lee.content_fragment.ui.content01;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lee.content_fragment.ui.content01.view.CommentExpandableListView;
import com.lee.myadapter.CommentExpandAdapter;
import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.repository.javabean.CommentBean;
import com.lee.repository.javabean.CommentDetailBean;
import com.lee.repository.javabean.FoodsBean;
import com.lee.repository.javabean.MsgInfo;
import com.lee.repository.javabean.ReplyDetailBean;
import com.lee.repository.network.CommentsRepository;
import com.lee.repository.network.FoodOneThread;
import com.lee.repository.network.OtherRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class Content01Fragment extends Fragment {

    private Content01ViewModel mViewModel;
    private WebView webview;
//    private  String title_url;
    private ImageView imageView;
    final static int width= BaseDataModel.getWidth();//宽

    final static int height= BaseDataModel.getHeight();//高
    Content01ViewModel content01ViewModel;
    private ImageView collectImageView;
    private TextView collectStatus;
    private OtherRepository otherRepository;


    public static Content01Fragment newInstance() {

        return new Content01Fragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View inflate = inflater.inflate(R.layout.content01_fragment, container, false);
        otherRepository=new OtherRepository();
        content01ViewModel=new Content01ViewModel();
        FoodsBean foodsBean = getData();
        System.out.println(foodsBean);
//        imageView = inflate.findViewById(R.id.titleImageview);//获取标题图片
        initView(inflate,foodsBean,inflate);//webview 显示菜谱数据
        initReView(inflate,foodsBean.getFoodid());
        updateCollectStatus(foodsBean,inflate,"aaaa");
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Content01ViewModel.class);
        // TODO: Use the ViewModel
    }

    public String getUsername()
    {
        SharedPreferences account = getActivity().getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS );
        String username = account.getString("username", "_null");

        return username;

    }
    /**
     * webview 部分
     */
    public void initData()
    {

            try {
                Bundle arguments = getArguments();


                String foodId =(String) arguments.get("foodId");
//                String foodId=foodid;
                Log.d("msg01",foodId);
                 FoodOneThread oneThread = new FoodOneThread(foodId);
                Thread thread1=new Thread(oneThread);//开启子线程 获取bean 放置断网重连 无法刷新的bug
                thread1.start();//开启子线程  获取详细数据
                thread1.join();

            }
            catch (Exception e)
            {
                System.out.println("参数为空000000000");
            }

    }

    public FoodsBean getData()
    {
        boolean flag=true;//默认 进入循环
        FoodOneThread oneThread = null;


        while (flag)
        {
            initData();
            //获取网络状态
            boolean isNetWorkOk = BaseDataModel.isIsNetWorkOk();//true 网络正常 false 网络异常
            if (isNetWorkOk)
            {
                flag=false;//网络正常 获得数据
            }
            else
            {
                flag=true;//网络异常 继续尝试连接或 其他操作
            }

        }
       return FoodOneThread.getFoodBean();//得到bean数据

    }

    public void initView(View inflate,FoodsBean foodsBean,View view)
    {

        webview = view.findViewById(R.id.webview);
        String str=getFoodDemoHtml(foodsBean);
        System.out.println("转换前"+str);
        Document parse = Jsoup.parse(str);
        Elements imgs = parse.getElementsByTag("img");
        if (!imgs.isEmpty()) {
            for (Element e : imgs) {
                imgs.attr("width", "100%");
//                imgs.attr("height", "auto");

            }
        }

        Elements body = parse.getElementsByTag("body");
        body.attr("style","display:block;padding:0 ;margin:0;");

        Elements head = parse.getElementsByTag("head");
        head.attr("style","display:none");


        Element foodpic = parse.getElementById("foodpic");
        foodpic.attr("width","100%");

        String content = parse.toString();
        System.out.println("转换后"+content);
        WebSettings settings = webview.getSettings();
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
//        webview.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress==100)
//                {
//
//                    Log.e("webviewProgress","加载情况-100/100");
//                }
//                newProgress=0;
//            }
//        });

        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);



    }

    private String getFoodDemoHtml(FoodsBean foodsBean) {

        String foodname = foodsBean.getFoodname();
        String foodpic = foodsBean.getFoodpic();
        String introduce = foodsBean.getIntroduce();
        String ingredients = foodsBean.getIngredients();
        String[] ingredientsList = ingredients.split(",");
        Map<String, List<String>> methods = foodsBean.getMethods();
        int height = BaseDataModel.getHeight()/5;//得到手机高度
        String roothtml="";

//        title_url=foodpic;//设置图片url

        roothtml+="<img alt=\"资源不见了\" id='foodpic' height='"+ height+"'  src='"+foodpic+"'/>";
        roothtml+="<h2 align=\" center\" >"+foodname+"</h2>";//添加图片名

        roothtml+="<h3 style=\"margin-left:2%\" >简介:</h3>\n" +
                "<div style=\"margin-left:2%;margin-right:2%;box-shadow:2px 2px 5px #909090;border-radius：30px；\" >"+
                "\t\t\t\t <p>"+introduce+"</p>"
        +"</div>";

        roothtml+="<h3 style=\"margin-left:2%\">原料:</h3>\n";
        for (String s : ingredientsList) {
            String[] split = s.split(":");
            if (split.length>=2)
            {
                roothtml+="\t\t\t<div style=\"display: inline;font-size: 15px;\">\n" +
                        "\t\t\t\t<div style=\"float:left;padding-left: 20px;\">"+split[0]+"</div>\n" +
                        "\t\t\t\t<div style=\"float: right;padding-right: 20px;\">"+split[1]+"</div>\n" +
                        "\t\t\t</div>\n" +
                        "\t\t\t<br />\n" +
                        "\t\t\t<hr style=\"margin-left:2%;margin-right:2%\" />";
            }

        }
        roothtml+="<h3 style=\"margin-left:2%\" >步骤:</h3>\n";
        roothtml+="<div align='center' style='max-width::100%' >";
        for(int i=0;i<methods.get("imgUrl").size();i++)
        {
            String url = (String)methods.get("imgUrl").get(i);
            String stepname01=(String)methods.get("stepname").get(i);
            if(stepname01!=null)
            {
                roothtml+="<div align='center' style=\"width: 80%;\" >" +
                        "<p>"+stepname01+"</p>";
            }
            if (url!=null)
            {
                roothtml+="<img alt=\"资源不见了\" height='"+ height/2+" '  style=\"border-radius: 5%;\" src='"+url+"'/>\n" +
                        "\t\t\t</div>" ;
            }
        }
        roothtml+="</div>";
        return roothtml;
    }

    /**
     * 评论部分
     */
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;

    private int nTotalPages=1;//总页数
    private int currentPages=1;//当前页
    private int commentId;//每条评论的Id

//    private String testJson = "{\n" +
//            "\t\"code\": 1000,\n" +
//            "\t\"message\": \"查看评论成功\",\n" +
//            "\t\"data\": {\n" +
//            "\t\t\"total\": 3,\n" +
//            "\t\t\"list\": [{\n" +
//            "\t\t\t\t\"id\": 42,\n" +
//            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
//            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
//            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
//            "\t\t\t\t\"replyTotal\": 1,\n" +
//            "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
//            "\t\t\t\t\"replyList\": [{\n" +
//            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
//            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\t\"id\": 40,\n" +
//            "\t\t\t\t\t\"commentId\": \"42\",\n" +
//            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
//            "\t\t\t\t\t\"status\": \"01\",\n" +
//            "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
//            "\t\t\t\t}]\n" +
//            "\t\t\t},\n" +
//            "\t\t\t{\n" +
//            "\t\t\t\t\"id\": 41,\n" +
//            "\t\t\t\t\"nickName\": \"设计狗\",\n" +
//            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
//            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
//            "\t\t\t\t\"replyTotal\": 1,\n" +
//            "\t\t\t\t\"createDate\": \"一天前\",\n" +
//            "\t\t\t\t\"replyList\": [{\n" +
//            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
//            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\t\"commentId\": \"41\",\n" +
//            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
//            "\t\t\t\t\t\"status\": \"01\",\n" +
//            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
//            "\t\t\t\t}]\n" +
//            "\t\t\t},\n" +
//            "\t\t\t{\n" +
//            "\t\t\t\t\"id\": 40,\n" +
//            "\t\t\t\t\"nickName\": \"产品喵\",\n" +
//            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。电话号\uD83D\uDE0A\uD83D\uDE0A\uD83D\uDE0A\",\n" +
//            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
//            "\t\t\t\t\"replyTotal\": 0,\n" +
//            "\t\t\t\t\"createDate\": \"三天前\",\n" +
//            "\t\t\t\t\"replyList\": []\n" +
//            "\t\t\t}\n" +
//            "\t\t]\n" +
//            "\t}\n" +
//            "}";
    NestedScrollView nestedScrollView;
    private void initReView(View view,String foodId) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        nestedScrollView=view.findViewById(R.id.nestedScrollView);
        expandableListView = (CommentExpandableListView) view.findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) view.findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.detail_page_do_comment){

                    showCommentDialog(foodId);
                }
            }
        });
//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("详情");
        CommentsRepository.GetComments comments = content01ViewModel.getComments;
        comments.execute(foodId, String.valueOf(1));
        comments.setGetCommentsCallBack(new CommentsRepository.CommentsCallBack() {
            @Override
            public void getCommentsCallBack(CommentBean commentBean) {
                //此处获取初次加载的数据
                if (commentBean!=null)
                {
                    List<CommentDetailBean> detailBeanList = commentBean.getData().getList();
                    nTotalPages = commentBean.getData().getTotalpages();//获取总页数;
                    currentPages=commentBean.getData().getCurrentPage();//当前页
                    commentsList=detailBeanList;
                    initExpandableListView(commentsList);//初始化

                }
                else
                {
                    Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                }

            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    System.out.println(i+" "+i1+"  "+i2+"  "+i3);
                    if(nestedScrollView.getScrollY() == nestedScrollView.getChildAt(0).getMeasuredHeight()- nestedScrollView.getMeasuredHeight()){
                        //滑动到了底部,注意如果有padding设置还需要减去一个padding的数值
                        System.out.println("滑动到地步了");

//                        进行 网络请求获取数据
                        if (currentPages<=nTotalPages)
                        {
                            currentPages++;
                            content01ViewModel.getCommentDetailBeanList(foodId,currentPages,adapter,expandableListView);
                        }
                        else
                        {
//                            到底了
                            Toast.makeText(getContext(),"没有更多了",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }


    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(getContext(), commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                TextView cid = view.findViewById(R.id.commentId);
                Log.e(TAG,"onGroupClick: 当前的评论commentId>>>"+cid.getText().toString());
                showReplyDialog(groupPosition,cid.getText().toString());
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(getContext(),"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

//    /**
//     * by moos on 2018/04/20
//     * func:生成测试数据
//     * @return 评论数据
//     */
//    private List<CommentDetailBean> generateTestData(){
//        Gson gson = new Gson();
//        commentBean = gson.fromJson(testJson, CommentBean.class);
//        List<CommentDetailBean> commentList = commentBean.getData().getList();
//        return commentList;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onClick(View view) {
//
//    }

    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private String userName;

    private void showCommentDialog(String foodid){
        dialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetEdit);
        View commentView = LayoutInflater.from(getContext()).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());
//        behavior.setPeekHeight();

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
//                    CommentDetailBean detailBean = new CommentDetailBean("小明", commentContent,"刚刚");
//                    ////////////////////////////////////
//                    ReplyDetailBean replyDetailBean = new ReplyDetailBean("asdasd", "阿萨大大");
//                    ReplyDetailBean replyDetailBean1 = new ReplyDetailBean("asdasd", "阿萨大大");
//                    ReplyDetailBean replyDetailBean2= new ReplyDetailBean("asdasd", "阿萨大大");
//                    ReplyDetailBean replyDetailBean3= new ReplyDetailBean("asdasd", "阿萨大大");
//                    List<ReplyDetailBean> beans = new ArrayList<>();
//                    beans.add(replyDetailBean);
//                    beans.add(replyDetailBean1);
//                    beans.add(replyDetailBean2);
//                    beans.add(replyDetailBean3);
//                    detailBean.setReplyList(beans);
//                    此处调用viewModel进行获取数据
                    SharedPreferences account = getActivity().getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS );
                    //获取用户名
                    userName = account.getString("username", BaseDataModel.getDeviceId());//设备号
                    Log.e("username",userName);
//                    adapter.addTheCommentData(detailBean,expandableListView);//添加评论数据  此处应该通过网络及交给服务器
//                    content01ViewModel.getCommentDetailBeanList();//添加评论
                    CommentDetailBean commentDetailBean = new CommentDetailBean();
                    commentDetailBean.setFoodid(foodid);
                    commentDetailBean.setContent(commentContent);
                    commentDetailBean.setNickName(userName);
                    CommentsRepository.UpdateCmRply updateCmRply = content01ViewModel.getUpdateCmRply();
                    updateCmRply.execute(commentDetailBean,null);
                    updateCmRply.setUpDateReultCallBack(new CommentsRepository.UpDateReultCallBack() {
                        @Override
                        public void upDateResult(MsgInfo msgInfo) {
                            if (msgInfo==null)
                            {
                                System.out.println("评论失败");
                            }
                            else
                            {
                                adapter.addTheCommentData(commentDetailBean,expandableListView);//更新
                            }
                        }
                    });

                    System.out.println("评论内容："+commentContent);
                    Toast.makeText(getContext(),"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getContext(),"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框 adapter
     */
    private void showReplyDialog(final int position,String cid){
        dialog = new BottomSheetDialog(getContext(),R.style.BottomSheetEdit);
        View commentView = LayoutInflater.from(getContext()).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);


        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
//                    ReplyDetailBean detailBean = new ReplyDetailBean("小红",replyContent);
                    /////////////////
                    SharedPreferences account = getActivity().getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS );
                    //获取用户名
                    userName = account.getString("username", BaseDataModel.getDeviceId());
                    ReplyDetailBean replyDetailBean = new ReplyDetailBean();
                    replyDetailBean.setContent(replyContent);
                    replyDetailBean.setCommentId(Integer.valueOf(cid));
                    replyDetailBean.setNickName(userName);
                    CommentsRepository.UpdateCmRply updateCmRply = content01ViewModel.getUpdateCmRply();
                    updateCmRply.execute(null,replyDetailBean);
                    updateCmRply.setUpDateReultCallBack(new CommentsRepository.UpDateReultCallBack() {
                        @Override
                        public void upDateResult(MsgInfo msgInfo) {
                            if (msgInfo==null)
                            {
                                System.out.println("回复失败");
                                Toast.makeText(getContext(),"回复失败",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                adapter.addTheReplyData(replyDetailBean, position);//添加回复数据  此处通过网络提交到服务器
                                expandableListView.expandGroup(position);
                                Toast.makeText(getContext(),"回复成功",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

//                    adapter.addTheReplyData(detailBean, position);//添加回复数据  此处通过网络提交到服务器

//                    expandableListView.expandGroup(position);

                }else {
                    Toast.makeText(getContext(),"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


    /**
     * 收藏功能
     */


    public void updateCollectStatus(FoodsBean foodsBean,View view,String userName)
    {

        //初始化红星状态
        collectImageView=view.findViewById(R.id.image_collect);
        collectStatus=view.findViewById(R.id.collectionStatus);

//        String username="aaaa";
        String food_id=foodsBean.getFoodid();

        if (!userName.equals("_null"))
        {
            String url ="/app/findCollectionStatus?user_id="+userName+"&food_id="+food_id;
            OtherRepository.UpdateFavo updateFavo = otherRepository.getUpdateFavo();
            updateFavo.execute(url);
            updateFavo.setFavoriteCallback(new OtherRepository.FavoriteCallback() {
                @Override
                public void updateFavorite(MsgInfo msgInfo) {
                    if (msgInfo.isSuccess())
                    {
                        imageView.setImageResource(R.drawable.ic_favorite_red_24dp);//设置为红星
                        collectStatus.setText("1");

                    }

                }
            });
        }

        collectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                if (!userName.equals("_null"))
                {
//                    url="/app/updateFavorites?user_id="+userName+"&food_id="+food_id;
                    if (collectStatus.getText().equals("1"))
                    {
                        //取消收藏状态
                        url="/app/updateFavorites?user_id="+userName+"&food_id="+food_id+"&favorite=0";
                    }else
                    {
//                        url+="&favorite=1";//新增收藏状态
                        url="/app/updateFavorites?user_id="+userName+"&food_id="+food_id+"&favorite=1";

                    }
                    //修改状态
                    OtherRepository.UpdateFavo updateFavo = otherRepository.getUpdateFavo();
                    updateFavo.execute(url);
                    updateFavo.setFavoriteCallback(new OtherRepository.FavoriteCallback() {
                        @Override
                        public void updateFavorite(MsgInfo msgInfo) {
                            if (msgInfo.isSuccess())
                            {
                                //修改成功
                                if (collectStatus.getText().equals("1"))
                                {
                                    collectStatus.setText("0");
                                    collectImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                                }else
                                {
                                    collectStatus.setText("1");
                                    collectImageView.setImageResource(R.drawable.ic_favorite_red_24dp);

                                }
                            }
                        }
                    });


                }else
                {
                    Toast.makeText(getContext(),"你尚未登陆",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }






}
