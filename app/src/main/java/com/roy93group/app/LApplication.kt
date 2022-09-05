package com.roy93group.app

import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseApplication
import com.loitpcore.core.common.Constants
import com.loitpcore.core.utilities.LUIUtil
import com.loitpcore.data.ActivityData
import com.roy93group.db.TaskDatabase

//TODO new keystore
//TODO refactor codes

//DONE
//change pkg name
//TODO ic_launcher

@LogTag("LApplication")
class LApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        // config activity transition default
        ActivityData.instance.type = Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT
        // config font
        LUIUtil.fontForAll = Constants.FONT_PATH
        TaskDatabase.getInstance(this)
    }
}
