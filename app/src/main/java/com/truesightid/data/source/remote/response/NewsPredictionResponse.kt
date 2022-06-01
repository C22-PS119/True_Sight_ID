package com.truesightid.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class NewsPredictionResponse(
	@field:SerializedName("prediction")
	val prediction: String? = null,

	@field:SerializedName("claim")
	val claim: String? = null,

	@field:SerializedName("val_prediction")
	val valPrediction: String? = null
)
