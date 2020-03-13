package com.fullsekurity.urbandict.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.*
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.databinding.ActivityMainBinding
import com.fullsekurity.urbandict.donateproducts.DonateProductsFragment
import com.fullsekurity.urbandict.donateproducts.DonateProductsListViewModel
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.logger.LogUtils.TagFilter.LOT
import com.fullsekurity.urbandict.repository.Repository
import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.utils.Constants.ROOT_FRAGMENT_TAG
import com.fullsekurity.urbandict.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.urbandict.utils.Utils
import com.fullsekurity.urbandict.utils.ViewModelInjectorModule
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Callbacks {

    private val tag = MainActivity::class.java.simpleName

    lateinit var repository: Repository
    @Inject
    lateinit var uiViewModel: UIViewModel

    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var activityMainBinding: ActivityMainBinding

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
        lottieBackgroundView = activityMainBinding.root.findViewById(R.id.main_background_lottie)
        loadInitialFragment()
        getUrbanDictionaryMeanings()
        val settings = getSharedPreferences("THEME", Context.MODE_PRIVATE)
        val name: String? = settings.getString("THEME", UITheme.LIGHT.name)
        if (name != null) {
            currentTheme = UITheme.valueOf(name)
        }
    }

    private fun getUrbanDictionaryMeanings() {
        val progressBar = main_progress_bar
        progressBar.visibility = View.VISIBLE
        repository.getUrbanDictionaryMeanings(progressBar)
    }

    fun loadInitialFragment() {
        supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        loadDonateProductsFragment()
    }

    fun loadDonateProductsFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, DonateProductsFragment.newInstance())
            .commitAllowingStateLoss()
    }

    private fun setupToolbar() {
        supportActionBar?.let { actionBar ->
            actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor(uiViewModel.primaryColor)))
            colorizeToolbarOverflowButton(toolbar, Color.parseColor(uiViewModel.toolbarTextColor))
            val upArrow = ContextCompat.getDrawable(this, R.drawable.toolbar_back_arrow)
            actionBar.setHomeAsUpIndicator(upArrow);
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
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun fetchActivity(): MainActivity {
        return this
    }

    override fun fetchRootView(): View {
        return activityMainBinding.root
    }

    override fun fetchDonateProductsListViewModel() : DonateProductsListViewModel? { return null }

}