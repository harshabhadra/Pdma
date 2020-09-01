package com.pdma.pdma.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://bankingpointindia.com/api/"
private const val BANK_URL = "https://www.formaxit.com/partner/api/"
private const val PLAN_URL = "http://planapi.in/api/Mobile/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

class RetrofitClient() {
    companion object {
        fun getClient(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(BASE_URL)
                .build()
        }

        fun getRetrofitClient(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(BANK_URL)
                .build()
        }

        fun getPlanClient(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(PLAN_URL)
                .build()
        }
    }
}

object Api {
    val retrofitService: ApiService by lazy {
        RetrofitClient.getClient().create(ApiService::class.java)
    }

    val retroService: ApiService by lazy {
        RetrofitClient.getRetrofitClient().create(ApiService::class.java)
    }
    val planService:ApiService by lazy{
        RetrofitClient.getPlanClient().create(ApiService::class.java)
    }
}

interface ApiService {

    //Log in user
    @POST("login")
    fun loginAsync(@Body loginRequest: LoginRequest): Deferred<LoginResponse>

    //Sign up user
    @POST("signup")
    fun signUpAsync(@Body singUpRequest: SignUpRequest): Deferred<SignUpResponse>

    //Send Otp
    @POST("send_otp")
    fun sendOtpAsync(@Body otp: SendOtp): Deferred<OtpResponse>

    //Forgot Password
    @POST("reset_password")
    fun resetPasswordAsync(@Body forgotPasswordRequest: ForgotPasswordRequest): Deferred<ForgotPasswordResponse>

    //Aeps
    @POST("aeps_transaction")
    @FormUrlEncoded
    fun aepsTransactionAsync(
        @Field("id") id: String = "",
        @Field("service_type") servieType: String = "",
        @Field("amount") amount: String = "",
        @Field("aadhaar") aadhar: String = "",
        @Field("bank") bank: String = "",
        @Field("mobile") mobile: String = "",
        @Field("pidDataType") pidDataType: String = "",
        @Field("pidData") pidData: String = "",
        @Field("ci") ci: String = "",
        @Field("dc") dc: String = "",
        @Field("dpId") dpId: String = "",
        @Field("errCode") errCode: String = "",
        @Field("errInfo") errInfo: String = "",
        @Field("fCount") fCount: String = "",
        @Field("tType") tType: String = "",
        @Field("hmac") hmac: String = "",
        @Field("iCount") iCount: String = "",
        @Field("mc") mc: String = "",
        @Field("mi") mi: String = "",
        @Field("nmPoints") nmPoints: String = "",
        @Field("pCount") pCount: String = "",
        @Field("pType") pType: String = "",
        @Field("qScore") qScore: String = "",
        @Field("rdsId") rdsId: String = "",
        @Field("rdsVer") rdsVer: String = "",
        @Field("sessionKey") sessionKey: String = "",
        @Field("srno") srno: String = ""
    ): Deferred<AepsResponse>

    //Bank list
    @GET("bank_details")
    fun getBankListAsync(): Deferred<List<BankDetail>>

    //Money transfer
    @POST("remittance_transaction")
    fun moneyTransferAsync(
        @Body moneyTransfer: MoneyTransfer
    ): Deferred<MoneyTransferResponse>

    //Recharge
    @POST("do_recharge")
    fun doRechargeAsync(
        @Body rechargeRequest: RechargeRequest
    ): Deferred<RechargeResponse>

    //Get operators
    @GET("recharge_operator")
    fun getOperatorsAsync(): Deferred<List<RechargeOperator>>

    //Get order id for micro atm
    @POST("matm_transaction")
    fun getMaServiceAsync(@Body maRequest: MaRequest): Deferred<MaResponse>

    //Check wallet balance
    @POST("check_balance")
    fun checkBalanceAsync(@Body profileRequest: ProfileRequest): Deferred<WalletBalance>

    @GET("Operatorplan?")
    fun getPlanDetails(
        @Query("apimember_id") apiMemberId: String,
        @Query("api_password") apiPassword: String,
        @Query("cricle") circleCode: String,
        @Query("operatorcode") operatorCode: String
    ): Call<RechargePlans>

    //Get R-offer list
    @POST("rechargeapi/roffercheck.php")
    @FormUrlEncoded
    fun getRoffer(
        @Field("apimember_id") apiMemberId: String?,
        @Field("api_password") apiPassword: String?,
        @Field("operator_code") opratorCode: Int,
        @Field("mobile_no") mobileno: String?
    ): Call<Roffer>

    //Move money to bank
    @POST("move_to_bank")
    fun moveToBankAsync(@Body moveToBank: MoveToBank):Deferred<MtbResponse>

    //Support
    @POST("support")
    fun getSupportDetailsAsync(@Body support: Support):Deferred<SupportResponse>

    //Profile
    @POST("profile")
    fun getProfileAsync(
        @Body profileRequest: ProfileRequest
    ):Deferred<ProfileResponse>
}