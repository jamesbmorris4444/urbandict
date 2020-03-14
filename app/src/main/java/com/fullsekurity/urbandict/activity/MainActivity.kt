package com.fullsekurity.urbandict.activity

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.*
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.databinding.ActivityMainBinding
import com.fullsekurity.urbandict.meanings.MeaningsFragment
import com.fullsekurity.urbandict.meanings.MeaningsListViewModel
import com.fullsekurity.urbandict.repository.Repository
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.utils.Constants.ROOT_FRAGMENT_TAG
import com.fullsekurity.urbandict.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.urbandict.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Callbacks {

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

    override fun onResume() {
        super.onResume()
        setupToolbar()
        uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
    }

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
    }

    fun getMainProgressBar(): ProgressBar {
        return main_progress_bar
    }

    private fun loadInitialFragment() {
        supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        loadMeaningsFragment()
    }

    private fun loadMeaningsFragment() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, MeaningsFragment.newInstance())
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

    override fun fetchFragment(): Fragment? {
        return null
    }

    override fun fetchRootView(): View {
        return activityMainBinding.root
    }

    override fun fetchmeaningsListViewModel() : MeaningsListViewModel? { return null }

}