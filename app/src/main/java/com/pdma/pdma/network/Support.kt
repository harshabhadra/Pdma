package com.pdma.pdma.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class Support(
    @JsonClass(generateAdapter = true)
    val id:String,
    @JsonClass(generateAdapter = true)
    val auth_key:String
)

data class SupportResponse(
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: List<SupportData>?
)

data class SupportData(
    @Json(name = "id")
    var id: String,
    @Json(name = "user_type")
    var userType: String,
    @Json(name = "user_id")
    var userId: String,
    @Json(name = "name")
    var name: String,
    @Json(name = "business_name")
    var businessName: String,
    @Json(name = "under_master")
    var underMaster: String,
    @Json(name = "under_dist")
    var underDist: String,
    @Json(name = "under_partner")
    var underPartner: String,
    @Json(name = "under_reseller")
    var underReseller: String,
    @Json(name = "username")
    var username: String,
    @Json(name = "password")
    var password: String,
    @Json(name = "package_id")
    var packageId: String,
    @Json(name = "domain_name")
    var domainName: String,
    @Json(name = "website_link")
    var websiteLink: String,
    @Json(name = "login_url")
    var loginUrl: String,
    @Json(name = "key_word")
    var keyWord: String,
    @Json(name = "logo")
    var logo: String,
    @Json(name = "favicon")
    var favicon: String,
    @Json(name = "mobile")
    var mobile: String,
    @Json(name = "phone")
    var phone: String,
    @Json(name = "aadhaar_no")
    var aadhaarNo: String,
    @Json(name = "pan_no")
    var panNo: String,
    @Json(name = "psa_id")
    var psaId: String,
    @Json(name = "state")
    var state: String,
    @Json(name = "gstin")
    var gstin: String,
    @Json(name = "address")
    var address: String,
    @Json(name = "location")
    var location: String,
    @Json(name = "pincode")
    var pincode: String,
    @Json(name = "bank_name")
    var bankName: String,
    @Json(name = "name_in_bank")
    var nameInBank: String,
    @Json(name = "account_no")
    var accountNo: String,
    @Json(name = "ifsc_code")
    var ifscCode: String,
    @Json(name = "account_type")
    var accountType: String,
    @Json(name = "branch")
    var branch: String,
    @Json(name = "status")
    var status: String,
    @Json(name = "kyc_status")
    var kycStatus: String,
    @Json(name = "poi")
    var poi: String,
    @Json(name = "poa")
    var poa: String,
    @Json(name = "poi_status")
    var poiStatus: String,
    @Json(name = "poa_status")
    var poaStatus: String,
    @Json(name = "remark")
    var remark: String,
    @Json(name = "profile_img")
    var profileImg: String,
    @Json(name = "profile_img_status")
    var profileImgStatus: String,
    @Json(name = "created_on")
    var createdOn: String,
    @Json(name = "modified_on")
    var modifiedOn: String,
    @Json(name = "kyc_applied_on")
    var kycAppliedOn: String,
    @Json(name = "enable_psa")
    var enablePsa: String,
    @Json(name = "enable_aeps")
    var enableAeps: String,
    @Json(name = "enable_matm")
    var enableMatm: String,
    @Json(name = "psa_remark")
    var psaRemark: String,
    @Json(name = "aeps_remark")
    var aepsRemark: String,
    @Json(name = "matm_remark")
    var matmRemark: String
)