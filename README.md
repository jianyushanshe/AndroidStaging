# AndroidStaging
Android kotlin开发脚手架

1.语言：kotlin
2.架构：MVVM+ViewModel+LiveData+Retrofit
3.技术实现：
  1.封装了BaseActivity类，所有Activity需继承该类。该类实现了沉浸式导航栏，重新登录，错误码处理，网络错误页面，actionbar等基础功能的实现。当继承该类后，子类需要重写抽象方法
  
 override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun superBaseOnCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        setActionbar(CustomActionbar.TYPE.ACTIONBAR_TYPE_OF_BACK_CENTERTEXT, "首页", null, 0, 0)
        tv_list.setOnClickListener {
           openActivity<StationListActivity>(this)
        }
    }
