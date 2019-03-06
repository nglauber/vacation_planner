package br.com.nglauber.twvacationplanner.domain

import io.reactivex.Scheduler

interface PostExecutionThread {
    val scheduler: Scheduler
}