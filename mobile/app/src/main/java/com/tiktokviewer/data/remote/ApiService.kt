package com.tiktokviewer.data.remote

import com.tiktokviewer.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("health")
    suspend fun healthCheck(): Response<HealthResponse>

    @POST("peer/heartbeat")
    suspend fun peerHeartbeat(
        @Header("X-Device-Id") deviceId: String,
        @Body request: PeerHeartbeatRequest
    ): Response<PeerHeartbeatResponse>

    @POST("peer/poll")
    suspend fun peerPoll(
        @Header("X-Device-Id") deviceId: String,
        @Body request: PeerPollRequest
    ): Response<PeerPollResponse>

    @POST("peer/submit")
    suspend fun peerSubmit(
        @Header("X-Device-Id") deviceId: String,
        @Body request: PeerSubmitRequest
    ): Response<PeerSubmitResponse>

    @POST("peer/request")
    suspend fun peerRequest(
        @Header("X-Device-Id") deviceId: String,
        @Body request: PeerRequestRequest
    ): Response<PeerRequestResponse>

    @POST("peer/status")
    suspend fun peerStatus(
        @Header("X-Device-Id") deviceId: String,
        @Body request: PeerStatusRequest
    ): Response<PeerStatusResponse>

    @POST("proxy/search")
    suspend fun proxySearch(
        @Header("X-Device-Id") deviceId: String,
        @Body request: ProxySearchRequest
    ): Response<ProxySearchResponse>

    @GET("proxy/thumbnail")
    suspend fun proxyThumbnail(
        @Header("X-Device-Id") deviceId: String,
        @Query("url") imageUrl: String
    ): Response<okhttp3.ResponseBody>

    @GET("trending/global")
    suspend fun trendingGlobal(
        @Header("X-Device-Id") deviceId: String
    ): Response<TrendingResponse>

    @GET("trending/country/{code}")
    suspend fun trendingCountry(
        @Header("X-Device-Id") deviceId: String,
        @Path("code") countryCode: String
    ): Response<TrendingResponse>

    @GET("trending/category/{name}")
    suspend fun trendingCategory(
        @Header("X-Device-Id") deviceId: String,
        @Path("name") categoryName: String
    ): Response<TrendingResponse>

    @POST("signature/check")
    suspend fun signatureCheck(
        @Header("X-Device-Id") deviceId: String,
        @Body request: SignatureCheckRequest
    ): Response<SignatureCheckResponse>

    @POST("signature/proxy-sign")
    suspend fun signatureProxySign(
        @Header("X-Device-Id") deviceId: String,
        @Body request: SignatureProxySignRequest
    ): Response<SignatureProxySignResponse>

    @POST("telemetry/failure")
    suspend fun telemetryFailure(
        @Header("X-Device-Id") deviceId: String,
        @Body request: TelemetryFailureRequest
    ): Response<TelemetryResponse>

    @POST("telemetry/success")
    suspend fun telemetrySuccess(
        @Header("X-Device-Id") deviceId: String,
        @Body request: TelemetrySuccessRequest
    ): Response<TelemetryResponse>

    @POST("config")
    suspend fun getConfig(
        @Header("X-Device-Id") deviceId: String,
        @Body request: ConfigRequest
    ): Response<ConfigResponse>
}
