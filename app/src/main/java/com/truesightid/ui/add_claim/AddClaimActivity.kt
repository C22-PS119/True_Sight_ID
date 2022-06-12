package com.truesightid.ui.add_claim

import DirtyFilter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.truesightid.R
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.PostClaimRequest
import com.truesightid.databinding.ActivityAddClaimBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.AddClaimAdapter
import com.truesightid.ui.main.MainActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.*
import com.truesightid.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody


class AddClaimActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddClaimBinding
    private lateinit var addPhotoAdapter: AddClaimAdapter
    private lateinit var alertDialog: AlertDialog
    private var listFile = ArrayList<MultipartBody.Part>()

    private var fake: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[AddClaimViewModel::class.java]

        addPhotoAdapter = AddClaimAdapter()

        with(binding.rvEvidenceImage) {
            layoutManager = GridLayoutManager(context, 3)
            adapter = addPhotoAdapter
            setHasFixedSize(true)
        }

        binding.factClaim.setOnClickListener(this)
        binding.fakeClaim.setOnClickListener(this)

        // Setup back button
        binding.ibBackDetail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.btnUpload.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }

        binding.btnSubmit.setOnClickListener { submitClaim(viewModel) }
    }

    private fun submitClaim(viewModel: AddClaimViewModel) {
        val title = binding.edtTitle.text.toString()
        val description = binding.edtDescription.text.toString()
        val url = binding.edtUrl.text.toString()
        var error:String? = null
        showLoading()
        GlobalScope.launch(Dispatchers.Main) {
            delay(500) // Avoiding skip frames error
            error = validateInput(title, description)
        }.invokeOnCompletion {
            if (error != null){
                showErrorDialog(error.toString())
                alertDialog.dismiss()
            }else{
                val postClaim = PostClaimRequest(
                    apiKey = Prefs.getUser()?.apiKey as String,
                    title = title,
                    description = description,
                    fake = if (fake) 1 else 0,
                    url = url,
                    listFile
                )

                viewModel.addClaim(postClaim).observe(this) { response ->
                    when (response.status) {
                        StatusResponse.SUCCESS -> {
                            showSuccessAddClaim { pushActivity(MainActivity::class.java) }
                            alertDialog.dismiss()
                        }
                        StatusResponse.EMPTY -> {
                            toastWarning("Empty: ${response.body}")
                            alertDialog.dismiss()
                        }
                        StatusResponse.ERROR -> {
                            toastError("Error: ${response.body.message}")
                            alertDialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun validateInput(title:String, description:String) : String? {
        if (getTotalWords(description) < 10)
            return resources.getString(R.string.description_must_be, 10)
        if (getTotalWords(description) > 200)
            return resources.getString(R.string.description_must_under, 200)
        if (getTotalWords(title) < 3)
            return resources.getString(R.string.title_must_be, 3)
        if (getTotalWords(title) > 20)
            return resources.getString(R.string.title_must_be_under, 20)
        if (DirtyFilter.isContainDirtyWord(description, DirtyFilter.DirtyWords))
            return resources.getString(R.string.description_dirty_words)
        if(DirtyFilter.isContainDirtyWord(title, DirtyFilter.DirtyWords))
            return resources.getString(R.string.title_dirty_words)
        return null
    }

    override fun onClick(v: View) {
        if (v.id == R.id.fact_claim) {
            fake = false
            with(binding) {
                select.animate().x(0F).duration = 100
                select.setBackgroundResource(R.drawable.fact_claim)
                factClaim.setTextColor(Color.WHITE)
                fakeClaim.setTextColor(
                    ContextCompat.getColor(
                        this@AddClaimActivity,
                        R.color.gray_dark
                    )
                )
            }
        } else if (v.id == R.id.fake_claim) {
            fake = true
            with(binding) {
                val size = fakeClaim.width.toFloat()
                select.animate().x(size).duration = 100
                select.setBackgroundResource(R.drawable.fake_claim)
                factClaim.setTextColor(
                    ContextCompat.getColor(
                        this@AddClaimActivity,
                        R.color.gray_dark
                    )
                )
                fakeClaim.setTextColor(Color.WHITE)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            addPhotoAdapter.setImages(selectedImg)
            addPhotoAdapter.notifyDataSetChanged()
            val file = uriToFile(selectedImg, this)
            val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file-" + listFile.count(),
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )

            listFile.add(filePart)
        }
    }

    private fun showSuccessAddClaim(onConfirmClickListener: () -> Unit) {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText(resources.getString(R.string.add_claim_successful))
            .setContentText(resources.getString(R.string.add_claim_successful_content))
            .setConfirmText(getString(R.string.dialog_ok))
            .setConfirmClickListener {
                it.dismiss()
                onConfirmClickListener()
            }
            .show()
    }

    private fun showLoading() {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.view_loading, null)
        alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(layout)
        alertDialog.setCancelable(false)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }
}