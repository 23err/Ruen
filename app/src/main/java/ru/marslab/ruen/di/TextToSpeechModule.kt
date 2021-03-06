package ru.marslab.ruen.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.marslab.ruen.wordrepetition.utilities.ITextToSpeech
import ru.marslab.ruen.wordrepetition.utilities.TTS

@Module
@InstallIn(ViewModelComponent::class)
object TextToSpeechModule {
    @Provides
    fun provideTTS(@ApplicationContext context: Context): ITextToSpeech =
        TTS(context)
}