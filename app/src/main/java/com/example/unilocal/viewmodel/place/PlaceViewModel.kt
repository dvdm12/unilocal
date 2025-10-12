package com.example.unilocal.viewmodel.place

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.unilocal.R
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceCategory
import com.example.unilocal.model.Schedule
import com.example.unilocal.model.User
import com.example.unilocal.model.buildPlace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL

/**
 * ViewModel responsible for managing the creation, validation, and image handling
 * of Place entities within the UniLocal app.
 *
 * Responsibilities:
 * - Manages reactive UI states for all Place fields.
 * - Validates user inputs before creating a Place.
 * - Builds a valid Place instance ready to be saved through UserViewModel.
 */
class PlaceViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    // -------------------------------------------------------------------------
    // 🔹 STATE MANAGEMENT
    // -------------------------------------------------------------------------
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _category = MutableStateFlow(PlaceCategory.RESTAURANT)
    val category: StateFlow<PlaceCategory> = _category

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: StateFlow<List<Schedule>> = _schedules

    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    val imageUrls: StateFlow<List<String>> = _imageUrls

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // -------------------------------------------------------------------------
    // 🔹 FIELD UPDATES
    // -------------------------------------------------------------------------
    fun updateName(value: String) = _name.update { value }

    fun updateDescription(value: String) = _description.update { value }

    fun updateCategory(value: PlaceCategory) = _category.update { value }

    fun updatePhone(value: String) {
        if (value.all { it.isDigit() }) _phone.update { value }
        else _message.value = context.getString(R.string.msg_phone_digits_only)
    }

    fun updateAddress(value: String) = _address.update { value }

    fun setSchedules(list: List<Schedule>) {
        _schedules.value = list
    }

    fun addSchedule(schedule: Schedule) {
        _schedules.update { it + schedule }
    }

    fun clearSchedules() {
        _schedules.value = emptyList()
    }

    fun addImage(url: String) {
        _imageUrls.update { it + url }
        _message.value = context.getString(R.string.msg_image_added)
    }

    fun removeImage(index: Int) {
        if (index in _imageUrls.value.indices) {
            val updated = _imageUrls.value.toMutableList().apply { removeAt(index) }
            _imageUrls.value = updated
            _message.value = context.getString(R.string.msg_image_removed)
        }
    }

    fun clearImages() {
        _imageUrls.value = emptyList()
    }

    fun clearMessage() {
        _message.value = null
    }

    // -------------------------------------------------------------------------
    // 🔹 VALIDATION LOGIC
    // -------------------------------------------------------------------------
    /**
     * Validates that all fields have valid values before creating a Place.
     * Returns `true` if all validations pass, otherwise `false`.
     */
    private fun validateFields(): Boolean {
        return when {
            _name.value.isBlank() -> {
                _message.value = context.getString(R.string.msg_validation_name); false
            }
            _description.value.isBlank() -> {
                _message.value = context.getString(R.string.msg_validation_description); false
            }
            _address.value.isBlank() -> {
                _message.value = context.getString(R.string.msg_validation_address); false
            }
            _phone.value.isBlank() -> {
                _message.value = context.getString(R.string.msg_validation_phone); false
            }
            !_phone.value.all { it.isDigit() } -> {
                _message.value = context.getString(R.string.msg_phone_digits_only); false
            }
            _phone.value.length < 7 -> {
                _message.value = context.getString(R.string.msg_validation_phone); false
            }
            _schedules.value.isEmpty() -> {
                _message.value = context.getString(R.string.msg_validation_schedule); false
            }
            else -> true
        }
    }

    // -------------------------------------------------------------------------
    // 🔹 PLACE CREATION
    // -------------------------------------------------------------------------
    /**
     * Builds and returns a valid Place instance if all fields are valid.
     * Returns `null` if validation fails.
     */
    fun createPlace(owner: User): Place? {
        if (!validateFields()) return null

        return try {
            buildPlace {
                this.name = _name.value
                this.description = _description.value
                this.category = _category.value
                this.address = _address.value
                this.phone = _phone.value
                this.owner = owner
                this.schedules = _schedules.value
                setImageUrls(_imageUrls.value.mapNotNull { runCatching { URL(it) }.getOrNull() })
            }.also {
                _message.value = context.getString(R.string.msg_place_created)
            }
        } catch (e: Exception) {
            _message.value = e.message ?: "Error desconocido al crear el lugar."
            null
        }
    }

    // ---------------------------------------------------------
    // Reset logic
    // ---------------------------------------------------------

    /**
     * Resets all form fields and UI state to their default values.
     *
     * This method is typically invoked after successfully creating a place,
     * allowing the user to start entering a new one without residual data.
     */
    fun resetPlaceForm() {
        viewModelScope.launch {
            _name.value = ""
            _description.value = ""
            _category.value = PlaceCategory.RESTAURANT
            _phone.value = ""
            _address.value = ""
            _imageUrls.value = emptyList()
            _schedules.value = emptyList()
            _message.value = null
        }
    }

    /**
     * Companion object providing a [ViewModelProvider.Factory] for creating instances
     * of [PlaceViewModel] with an [Application] context.
     */
    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PlaceViewModel(application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}


