package com.example.unilocal.viewmodel.place

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.unilocal.R
import com.example.unilocal.model.*
import com.example.unilocal.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL

/**
 * ViewModel responsible for managing [Place] creation, validation, and persistence.
 *
 * Responsibilities:
 * - Manages reactive UI states for place creation/editing.
 * - Validates user input and constructs [Place] entities via the [buildPlace] DSL.
 * - Delegates all persistence operations to [PlaceRepository].
 *
 * The builder [buildPlace] automatically initializes missing attributes
 * with default values defined in [PlaceBuilderDSL].
 */
class PlaceViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    private val placeRepository = PlaceRepository

    // -------------------------------------------------------------------------
    // STATE MANAGEMENT
    // -------------------------------------------------------------------------

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

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

    // -------------------------------------------------------------------------
    // FIELD UPDATES
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
    // VALIDATION LOGIC
    // -------------------------------------------------------------------------

    /**
     * Validates the current field values before creating or updating a [Place].
     * @return `true` if all required fields are valid; otherwise, `false`.
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
    // PLACE CREATION
    // -------------------------------------------------------------------------

    /**
     * Creates and registers a [Place] for the given [User].
     * The [buildPlace] DSL automatically fills in default values
     * for unspecified attributes.
     *
     * @param currentOwner The [User] who owns this place.
     * @return The created [Place], or `null` if validation fails.
     */
    fun createPlace(currentOwner: User): Place? {
        if (!validateFields()) return null

        return try {
            val newPlace = buildPlace {
                name = _name.value
                description = _description.value
                category = _category.value
                address = _address.value
                phone = _phone.value
                owner = currentOwner
                schedules = _schedules.value
                setImageUrls(_imageUrls.value.mapNotNull { runCatching { URL(it) }.getOrNull() })
            }

            viewModelScope.launch {
                placeRepository.addPlace(newPlace)
                _places.value = placeRepository.getAllPlaces()
                _message.value = context.getString(R.string.msg_place_created)
            }

            newPlace
        } catch (e: Exception) {
            _message.value = e.message ?: "Unknown error while creating the place."
            null
        }
    }

    // -------------------------------------------------------------------------
    // CRUD OPERATIONS
    // -------------------------------------------------------------------------

    /** Updates an existing [Place]. */
    fun updatePlace(place: Place) {
        viewModelScope.launch {
            val updated = placeRepository.updatePlace(place)
            if (updated) {
                _places.value = placeRepository.getAllPlaces()
                _message.value = context.getString(R.string.msg_place_updated)
            } else {
                _message.value = context.getString(R.string.msg_place_not_found)
            }
        }
    }

    /** Removes a [Place] by its unique ID. */
    fun removePlace(placeId: String) {
        viewModelScope.launch {
            val removed = placeRepository.removePlace(placeId)
            if (removed) {
                _places.value = placeRepository.getAllPlaces()
                _message.value = context.getString(R.string.msg_place_deleted)
            } else {
                _message.value = context.getString(R.string.msg_place_not_found)
            }
        }
    }

    /** Updates the status of a [Place] (e.g., approved, rejected). */
    fun updatePlaceStatus(placeId: String, newStatus: PlaceStatus) {
        viewModelScope.launch {
            val updated = placeRepository.updatePlaceStatus(placeId, newStatus)
            if (updated) {
                _places.value = placeRepository.getAllPlaces()
                _message.value = "Place status updated to $newStatus"
            } else {
                _message.value = context.getString(R.string.msg_place_not_found)
            }
        }
    }

    /** Refreshes the list of places from the repository. */
    fun refreshPlaces() {
        viewModelScope.launch {
            _places.value = placeRepository.getAllPlaces()
        }
    }

    // -------------------------------------------------------------------------
    // RESET LOGIC
    // -------------------------------------------------------------------------

    /** Resets all input fields to their default state. */
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

    // -------------------------------------------------------------------------
    // FACTORY
    // -------------------------------------------------------------------------

    companion object {
        /** Factory for creating [PlaceViewModel] instances with an [Application] context. */
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
