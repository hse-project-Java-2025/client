package org.hse.smartcalendar.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.hse.smartcalendar.view.model.AbstractStatisticsViewModel
import org.hse.smartcalendar.view.model.StatisticsViewModel

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelBindModule {

    @Binds
    abstract fun bindStatisticsVM(
        impl: StatisticsViewModel
    ): AbstractStatisticsViewModel
}