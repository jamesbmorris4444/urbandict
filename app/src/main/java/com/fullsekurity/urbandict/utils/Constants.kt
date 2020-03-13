package com.fullsekurity.urbandict.utils

import com.fullsekurity.urbandict.repository.storage.Meaning

object Constants {

    const val URBANDICT_ARRAY_DATA_TAG = "list"
    const val ROOT_FRAGMENT_TAG = "root fragment"
    val URBANDICT_LIST_CLASS_TYPE = (ArrayList<Meaning>()).javaClass
    const val URBANDICT_BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com/"
    const val URBANDICT_RAPID_API_HOST = "x-rapidapi-host: mashape-community-urban-dictionary.p.rapidapi.com"
    const val URBANDICT_RAPID_API_KEY = "x-rapidapi-key: f674a66b81msh663e5810b0cdd7ep162635jsnb30c4b837dd9"
    const val URBANDICT_TERM = "term"
    const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/"
    const val SMALL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w300"
    const val API_KEY_REQUEST_PARAM = "api_key"
    const val LANGUAGE_REQUEST_PARAM = "language"
    const val PAGE_REQUEST_PARAM = "page"
    const val API_KEY = "17c5889b399d3c051099e4098ad83493"
    const val LANGUAGE = "en"
    const val MAIN_DATABASE_NAME = "TheatreBlood.db"
    const val MODIFIED_DATABASE_NAME = "TheatreBloodModified.db"
    const val STANDARD_LEFT_AND_RIGHT_MARGIN = 20f
    const val STANDARD_EDIT_TEXT_SMALL_MARGIN = 10f
    const val STANDARD_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_GRID_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_BUTTON_HEIGHT = 50f
    const val STANDARD_GRID_HEIGHT = 120f
    const val EDIT_TEXT_TO_BUTTON_RATIO = 3  // 3:1
    const val REQUEST_CODE_ASK_PERMISSIONS = 123

    // toolbar

    const val DONATE_PRODUCTS_TITLE = "Donate Products"
    const val CREATE_PRODUCTS_TITLE = "Create Products"
    const val MANAGE_DONOR_TITLE = "Manage Meaning"
    const val REASSOCIATE_DONATION_TITLE = "Reassociate Donation"
    const val UPDATE_TEST_RESULTS_TITLE = "Update Test Results"
    const val VIEW_DONOR_LIST_TITLE = "View Meaning List"

}