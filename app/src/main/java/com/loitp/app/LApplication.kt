package com.loitp.app

import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.common.Constants
import com.core.utilities.LSharedPrefsUtil
import com.core.utilities.LUIUtil
import com.data.ActivityData
import com.data.AdmobData
import com.loitp.R

//TODO debug const

//done
//TODO id admob
//TODO link link_cover string

@LogTag("LApplication")
class LApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        Constants.setIsDebug(true)

        //config admob id
        AdmobData.instance.idAdmobFull = getString(R.string.str_f)

        //config activity transition default
        ActivityData.instance.type = Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT

        //config font
        LUIUtil.fontForAll = Constants.FONT_PATH

        //set dark theme
        LSharedPrefsUtil.instance.putBoolean(Constants.KEY_IS_DARK_THEME, true)
    }
}
