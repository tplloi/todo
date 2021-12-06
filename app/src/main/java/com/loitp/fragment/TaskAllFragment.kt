package com.loitp.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.adapter.HeaderAdapter
import com.loitp.adapter.TaskAdapter
import com.loitp.model.MessageEvent
import com.loitp.model.Task
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_task_all.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@LogTag("AllFragment")
class TaskAllFragment : BaseFragment() {
    private var mainViewModel: MainViewModel? = null
    private var concatAdapter = ConcatAdapter()
    private var headerAdapter = HeaderAdapter()
    private var taskAdapter = TaskAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupViewModels()
        getListTaskAll()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_task_all
    }

    private fun getListTaskAll() {
        mainViewModel?.getListTaskAll()
    }

    private fun setupViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        headerAdapter.setData(getString(R.string.all))
        concatAdapter.addAdapter(headerAdapter)
        taskAdapter.onClickCbCompleteListener = { task ->
            handleCheckboxComplete(task)
        }
        taskAdapter.onClickDeleteListener = { task ->
            handleDeleteTask(task)
        }
        concatAdapter.addAdapter(taskAdapter)
        recyclerView.adapter = concatAdapter
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { vm ->
            vm.getListTaskAllActionLiveData.observe(this, { actionData ->
                val isDoing = actionData.isDoing
                val isSuccess = actionData.isSuccess

                if (isDoing == true) {
                    progressBar.showProgressBar()
                } else {
                    progressBar.hideProgressBar()
                    if (isSuccess == true) {
                        actionData.data?.let { list ->
                            taskAdapter.setData(list)
                            if (list.isNullOrEmpty()) {
                                recyclerView.isVisible = false
                                tvNoData.isVisible = true
                            } else {
                                recyclerView.isVisible = true
                                tvNoData.isVisible = false
                            }
                        }
                    }
                }
            })
            vm.updateTaskActionLiveData.observe(this, { actionData ->
                val isDoing = actionData.isDoing
                if (isDoing == true) {
                    progressBar.showProgressBar()
                } else {
                    progressBar.hideProgressBar()
                }
            })
            vm.deleteTaskActionLiveData.observe(this, { actionData ->
                val isDoing = actionData.isDoing
                if (isDoing == true) {
                    progressBar.showProgressBar()
                } else {
                    progressBar.hideProgressBar()
                }
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        event?.msg?.let {
            logD(">>> onMessageEvent $it")
            if (it == MessageEvent.CREATE_TASK || it == MessageEvent.DELETE_TASK) {
                getListTaskAll()
            }
        }
    }

    private fun handleCheckboxComplete(task: Task) {
        task.isComplete = !task.isComplete
        mainViewModel?.updateTask(task)
    }

    private fun handleDeleteTask(task: Task) {
        mainViewModel?.deleteTask(task)
    }
}
