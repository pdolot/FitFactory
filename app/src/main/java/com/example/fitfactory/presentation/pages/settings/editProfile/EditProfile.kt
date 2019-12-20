package com.example.fitfactory.presentation.pages.settings.editProfile

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.example.fitfactory.constants.RequestCode
import com.example.fitfactory.data.models.app.*
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.customViews.changePasswordDialog.ChangePasswordDialog
import com.example.fitfactory.presentation.customViews.creditCard.CreditCardDialog
import com.example.fitfactory.utils.SuperValidator
import com.example.fitfactory.utils.addMaskAndTextWatcher
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.firstName
import kotlinx.android.synthetic.main.fragment_edit_profile.userAddressCity
import kotlinx.android.synthetic.main.fragment_edit_profile.userAddressStreet
import kotlinx.android.synthetic.main.fragment_edit_profile.userBirthDate
import kotlinx.android.synthetic.main.fragment_edit_profile.userPhoneNo
import kotlinx.android.synthetic.main.fragment_edit_profile.userZipCode
import kotlinx.android.synthetic.main.fragment_edit_profile.userZipCodeCity
import kotlinx.android.synthetic.main.fragment_payment.*
import javax.inject.Inject

class EditProfile : BaseFragment() {

    @Inject
    lateinit var fm: FragmentManager

    init {
        Injector.component.inject(this)
    }

    private val viewModel by lazy { EditProfileViewModel() }
    private val textWatchers = ArrayList<Pair<TextWatcher, EditText>>()

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.editProfile)
    override fun topBarEnabled() = true
    override fun backButtonEnabled() = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setOwner(this)
        bindData()
        setListeners()

        editProfileImage.setOnClickListener {
            EditProfileImageBottomSheet.newInstance().apply {
                onGallerySelect = ::checkReadExternalStoragePermission
                onCameraSelect = ::checkCameraPermission
            }.show(fm)
        }

        viewModel.photoServiceResult.observe(viewLifecycleOwner, Observer {
            deleteImage.visibility = if (it != null) View.VISIBLE else View.GONE

            setProfileImage(it ?: viewModel.localStorage.getUser()?.profileImage)
        })

        viewModel.passwordChanged.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.mapFragment)
                viewModel.localStorage.setToken(null)
            }
        })

        viewModel.updateState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is StateInProgress -> {
                    editUserButton.isEnabled = false
                }
                is StateError -> {
                    editUserButton.isEnabled = true
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is StateComplete -> {
                    editUserButton.isEnabled = true
                    Toast.makeText(context, "Pomyślnie zedytowano", Toast.LENGTH_SHORT).show()
                    topBar?.setProfileImage(viewModel.localStorage.getUser()?.profileImage)
                    navigationDrawer?.setProfileView()
                }
            }
        })

        deleteImage.visibility =
            if (viewModel.user?.profileImage != null) View.VISIBLE else View.GONE

        viewModel.creditCardDao.getCreditCard(viewModel.user?.id ?: 0).observe(viewLifecycleOwner, Observer {
            viewModel.creditCard = it
            bindCreditCard(it)
        })

    }

    override fun onResume() {
        super.onResume()
        addTextWatchers()
    }

    fun setListeners() {
        deleteImage.setOnClickListener {
            viewModel.takePhotoService.removePhoto()
            viewModel.user?.profileImage = null
            setProfileImage(null)
            deleteImage.visibility = View.GONE
        }

        addCreditCard.setOnClickListener {
            showCreditCardDialog("Dodaj")
        }

        editCreditCard.setOnClickListener {
            showCreditCardDialog("Edytuj")
        }

        deleteCreditCard.setOnClickListener {
            viewModel.deleteCreditCard()
        }

        changePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        editUserButton.setOnClickListener {
            if (validate()) {
                viewModel.user?.firstName = firstName.text.toString()
                viewModel.user?.secondName = secondName.text.toString()
                viewModel.user?.lastName = lastName.text.toString()
                viewModel.user?.identityNumber = personalIdentity.text.toString()
                viewModel.user?.birthDate = userBirthDate.text.toString()
                viewModel.user?.phoneNumber = userPhoneNo.text.toString()
                viewModel.user?.address = Address(
                    street = userAddressStreet.text.toString(),
                    city = userAddressCity.text.toString(),
                    zipCode = userZipCode.text.toString(),
                    zipCodeCity = userZipCodeCity.text.toString()
                )
                viewModel.editUserProfile()
            }
        }
    }

    private fun validate(): Boolean {
        if (!SuperValidator.Builder()
                .on(firstName)
                .canBeEmpty(false)
                .enableError()
                .build()
                .validate()
        ) return false

        if (!SuperValidator.Builder()
                .on(lastName)
                .canBeEmpty(false)
                .enableError()
                .build()
                .validate()
        ) return false

        if (!SuperValidator.Builder()
                .on(personalIdentity)
                .canBeEmpty(true)
                .minLength(9)
                .enableError()
                .build()
                .validate()
        ) return false

        if (!SuperValidator.Builder()
                .on(userBirthDate)
                .canBeEmpty(false)
                .withExactLength(10)
                .enableError()
                .build()
                .validate()
        ) return false

        val addressCanBeEmpty =
            userAddressStreet.text.toString().isBlank() && userAddressCity.text.toString().isBlank() && userZipCode.text.toString().isBlank() && userZipCodeCity.text.toString().isBlank()

        if (!SuperValidator.Builder()
                .on(userAddressStreet)
                .canBeEmpty(addressCanBeEmpty)
                .minLength(6)
                .enableError()
                .build()
                .validate()
        ) return false

        if (!SuperValidator.Builder()
                .on(userAddressCity)
                .canBeEmpty(addressCanBeEmpty)
                .minLength(2)
                .enableError()
                .build()
                .validate()
        ) return false

        if (!SuperValidator.Builder()
                .on(userZipCode)
                .canBeEmpty(addressCanBeEmpty)
                .withExactLength(6)
                .enableError()
                .build()
                .validate()
        ) return false

        if (!SuperValidator.Builder()
                .on(userZipCodeCity)
                .canBeEmpty(addressCanBeEmpty)
                .minLength(2)
                .enableError()
                .build()
                .validate()
        ) return false

        return true
    }

    private fun showCreditCardDialog(positiveButtonText: String) {
        activity?.let {
            val view = CreditCardDialog(it).apply {
                creditCard = viewModel.creditCard
            }
            MaterialDialog(it).show {
                noAutoDismiss()
                title(text = "Podaj dane karty")
                customView(view = view)
                positiveButton(text = positiveButtonText) {
                    val creditCard = view.getCard()
                    if (creditCard != null) {
                        viewModel.addCreditCard(creditCard)
                        dismiss()
                    }
                }
                negativeButton(text = "Anuluj") {
                    dismiss()
                }
            }
        }
    }

    private fun showChangePasswordDialog() {
        activity?.let {
            val view = ChangePasswordDialog(it)
            MaterialDialog(it).show {
                noAutoDismiss()
                title(text = "Zmień hasło")
                message(text = "Uwaga! Po zmianie hasła zostaniesz automatycznie wylogowany")
                customView(view = view)
                positiveButton(text = "Zmień") {
                    if (view.checkCorrectness()) {
                        viewModel.changePassword(view.getPasswords(), this)
                    }
                }
                negativeButton(text = "Anuluj") {
                    dismiss()
                }
            }
        }
    }

    private fun addTextWatchers() {
        textWatchers.add(Pair(userBirthDate.addMaskAndTextWatcher("##/##/####"), userBirthDate))
        textWatchers.add(Pair(userPhoneNo.addMaskAndTextWatcher("### ### ###"), userBirthDate))
        textWatchers.add(Pair(userZipCode.addMaskAndTextWatcher("##-###"), userBirthDate))
    }

    private fun bindData() {
        val user = viewModel.user
        user?.let {
            firstName.setText(user.firstName)
            secondName.setText(user.secondName)
            lastName.setText(user.lastName)
            email.setText(user.email)
            personalIdentity.setText(user.identityNumber)
            userBirthDate.setText(user.birthDate)
            userPhoneNo.setText(user.phoneNumber)
            userAddressStreet.setText(user.address?.street)
            userAddressCity.setText(user.address?.city)
            userZipCode.setText(user.address?.zipCode)
            userZipCodeCity.setText(user.address?.zipCodeCity)
            setProfileImage(it.profileImage)
        }
    }

    private fun bindCreditCard(creditCard: CreditCard?) {
        val user = viewModel.user
        if (creditCard != null) {
            creditCardView.bindData(creditCard, user?.firstName + " " + user?.lastName)
            creditCardView.visibility = View.VISIBLE
            deleteCreditCard.visibility = View.VISIBLE
            editCreditCard.visibility = View.VISIBLE

            creditCardIcon.visibility = View.GONE
            addCreditCard.visibility = View.GONE
            noCreditCard.visibility = View.GONE

        } else {
            creditCardView.visibility = View.GONE
            deleteCreditCard.visibility = View.GONE
            editCreditCard.visibility = View.GONE

            creditCardIcon.visibility = View.VISIBLE
            addCreditCard.visibility = View.VISIBLE
            noCreditCard.visibility = View.VISIBLE
        }
    }

    private fun checkReadExternalStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RequestCode.READ_EXTERNAL_STORAGE_REQUEST_CODE
        )
    }

    private fun checkCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            RequestCode.CAMERA_REQUEST_CODE
        )
    }

    private fun setProfileImage(source: String?) {
        Glide.with(context ?: return)
            .load(source)
            .placeholder(R.drawable.user_image)
            .fitCenter()
            .into(profileImage)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        viewModel.onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.takePhotoService.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        viewModel.takePhotoService.removePhoto()
        textWatchers.forEach {
            it.second.removeTextChangedListener(it.first)
        }
        super.onDestroyView()
    }

}
