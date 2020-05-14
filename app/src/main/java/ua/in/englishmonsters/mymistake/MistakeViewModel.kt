package ua.`in`.englishmonsters.mymistake

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executors

class MistakeViewModel : ViewModel() {

    private val data: MutableLiveData<CardData> = MutableLiveData(CardData())
    private val cardUri: MutableLiveData<Uri?> = MutableLiveData()

    fun getCardData() : LiveData<CardData> = data
    fun getCardUriData() : LiveData<Uri?> = cardUri

    fun setName(name: String){
        data.value = data.value?.also {
            it.name = name
        }
    }

    fun setPhoto(photo: Bitmap?) {
        data.value = data.value?.also {
            it.photo = photo
        }
    }

    fun setUri(uri: Uri?){
        cardUri.value = uri
    }

    fun generate(context: Context){
        Executors.newSingleThreadExecutor().submit {
            val uri = data.value?.generate(context)
            cardUri.postValue(uri)
        }
    }

    fun changeBg(context: Context) {
        data.value = data.value?.also {
            it.bgRes = it.nextBgRes()
        }
        generate(context)
    }

    fun rotate(context: Context) {
        data.value?.rotatePhoto() // TODO move to bg
        generate(context)
    }

}
