package com.fullsekurity.urbandict.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.databinding.ActivityMainBinding
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.meanings.MeaningsFragment
import com.fullsekurity.urbandict.meanings.MeaningsListViewModel
import com.fullsekurity.urbandict.repository.Repository
import com.fullsekurity.urbandict.services.LongRunningService
import com.fullsekurity.urbandict.services.RemoteService
import com.fullsekurity.urbandict.services.ServiceCallbacks
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.utils.Constants.ROOT_FRAGMENT_TAG
import com.fullsekurity.urbandict.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.urbandict.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Callbacks, ServiceCallbacks {

    lateinit var repository: Repository
    @Inject
    lateinit var uiViewModel: UIViewModel

    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var activityMainBinding: ActivityMainBinding
    private var thumbsStatusMenuItem: MenuItem? = null

    enum class UITheme {
        LIGHT, DARK, NOT_ASSIGNED,
    }

    lateinit var currentTheme: UITheme

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository(this)
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(this))
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.uiViewModel = uiViewModel
        activityMainBinding.mainActivity = this
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        lottieBackgroundView = main_background_lottie
        loadInitialFragment()
        val settings = getSharedPreferences("THEME", Context.MODE_PRIVATE)
        val name: String? = settings.getString("THEME", UITheme.LIGHT.name)
        if (name != null) {
            currentTheme = UITheme.valueOf(name)
        }

        // Start Service

        serviceProgressBar = service_progresss_bar
        timestampText = timestamp_text
        printTimestampButton = print_timestamp
        stopServiceButton = stop_service

        printTimestampButton.setOnClickListener { view ->
            if (remoteServiceConnected) {
                try {
                    val msg = Message.obtain(null, RemoteService.MSG_GET_TIMESTAMP, 0, 0)
                    msg.replyTo = activityMessenger
                    remoteServiceMessenger?.send(msg)
                } catch (e: RemoteException) {
                    LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), e)
                }
            }
        }

        stopServiceButton.setOnClickListener {
            if (remoteServiceConnected) {
                unbindService(remoteServiceConnection)
                remoteServiceConnected = false;
            }
            stopService(Intent(this, RemoteService::class.java))
        }

        // End Service
    }

    // Start Service

    private val TAG = MainActivity::class.java.simpleName
    lateinit var longRunningService: LongRunningService
    private lateinit var serviceProgressBar: ProgressBar
    private var remoteServiceMessenger: Messenger? = null
    private val activityMessenger: Messenger = Messenger(ActivityHandler(this))
    private var remoteServiceConnected = false
    private lateinit var timestampText: TextView
    private lateinit var printTimestampButton: ImageView
    private lateinit var stopServiceButton: ImageView

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("MainActivity: onremoteServiceConnected()"))
            val binder = service as LongRunningService.LocalBinder
            longRunningService = binder.getService()
            longRunningService.setServiceCallbacks(this@MainActivity)
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("MainActivity: onServiceDisconnected()"))
        }
    }

    override fun onResume() {
        super.onResume()
        setupToolbar()
        uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)

        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("MainActivity: bindService in onResume()"))
        val intent = Intent(this, LongRunningService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        val remoteIntent = Intent(this, RemoteService::class.java)
        startService(remoteIntent)
        bindService(remoteIntent, remoteServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        if (remoteServiceConnected) {
            unbindService(remoteServiceConnection);
            remoteServiceConnected = false;
        }
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("MainActivity: onStop unbindService()"))
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, LongRunningService::class.java))
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("MainActivity: onDestroy stopService()"))
    }

    fun startPretendLongRunningTask() {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("MainActivity: startPretendLongRunningTask()"))
        longRunningService.startPretendLongRunningTask()
    }

    fun pausePretendLongRunningTask() {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("MainActivity: pausePretendLongRunningTask()"))
        longRunningService.pausePretendLongRunningTask()
    }

    fun resumePretendLongRunningTask() {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("MainActivity: resumePretendLongRunningTask()"))
        longRunningService.resumePretendLongRunningTask()
    }

    override fun setServiceProgress(progress: Int) {
        serviceProgressBar.progress = progress
    }

    override fun setProgressMaxValue(maxValue: Int) {
        serviceProgressBar.max = maxValue

    }

    private val remoteServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            remoteServiceMessenger = null
            remoteServiceConnected = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            remoteServiceMessenger = Messenger(service)
            remoteServiceConnected = true
        }
    }

    internal class ActivityHandler(activity: MainActivity?) : Handler() {
        private val activity = WeakReference<MainActivity>(activity)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                RemoteService.MSG_GET_TIMESTAMP -> {
                    activity.get()?. let { activity_get ->
                        activity_get.timestampText.text = msg.data.getString("timestamp")
                    }
                }
            }
        }
    }

    // End Service

    fun getMainProgressBar(): ProgressBar {
        return main_progress_bar
    }

    private fun loadInitialFragment() {
        if (supportFragmentManager.findFragmentByTag(ROOT_FRAGMENT_TAG) == null) {
            loadMeaningsFragment()
        }
    }

    private fun loadMeaningsFragment() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, MeaningsFragment.newInstance(), ROOT_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    private fun setupToolbar() {
        supportActionBar?.let { actionBar ->
            actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor(uiViewModel.primaryColor)))
            colorizeToolbarOverflowButton(toolbar, Color.parseColor(uiViewModel.toolbarTextColor))
            val backArrow = ContextCompat.getDrawable(this, R.drawable.toolbar_back_arrow)
            actionBar.setHomeAsUpIndicator(backArrow);
            toolbar.setTitleTextColor(Color.parseColor(uiViewModel.toolbarTextColor))
        }
    }

    private fun colorizeToolbarOverflowButton(toolbar: Toolbar, color: Int): Boolean {
        val overflowIcon = toolbar.overflowIcon ?: return false
        toolbar.overflowIcon = getTintedDrawable(overflowIcon, color)
        return true
    }

    private fun getTintedDrawable(inputDrawable: Drawable, color: Int): Drawable {
        val wrapDrawable = DrawableCompat.wrap(inputDrawable)
        DrawableCompat.setTint(wrapDrawable, color)
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN)
        return wrapDrawable
    }

    private fun setToolbarThumbStatus(resInt: Int) {
        thumbsStatusMenuItem?.let { thumbsStatusMenuItem ->
            runOnUiThread {
                thumbsStatusMenuItem.icon = ContextCompat.getDrawable(this, resInt)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_toggle_theme -> {
            if (currentTheme == UITheme.LIGHT) {
                currentTheme = UITheme.DARK
            } else {
                currentTheme = UITheme.LIGHT
            }
            uiViewModel.currentTheme = currentTheme
            uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
            setupToolbar()
            true
        }
        R.id.action_set_up_sort -> {
            uiViewModel.sortThumbsUp = true
            setToolbarThumbStatus(R.drawable.ic_thumbsup_dark)
            true
        }
        R.id.action_set_down_sort -> {
            uiViewModel.sortThumbsUp = false
            setToolbarThumbStatus(R.drawable.ic_thumbsdown_dark)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        thumbsStatusMenuItem = menu.findItem(R.id.thumb_status)
        setToolbarThumbStatus(R.drawable.ic_thumbsup_dark)
        return true
    }

    override fun fetchActivity(): MainActivity {
        return this
    }

    override fun fetchRootView(): View {
        return activityMainBinding.root
    }

    override fun fetchmeaningsListViewModel() : MeaningsListViewModel? { return null }

}