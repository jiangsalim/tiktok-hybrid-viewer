#include <jni.h>
#include <string.h>
#include <android/log.h>

#define LOG_TAG "TikTokSigner"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern char* rust_sign_mobile_api(const char* url, const char* device_id, uint64_t timestamp);
extern char* rust_sign_web_api(const char* url, const char* user_agent, uint64_t timestamp);
extern void rust_free_string(char* ptr);

JNIEXPORT jstring JNICALL
Java_com_tiktokviewer_domain_engine_light_SignatureGenerator_nativeSignMobileApi(
    JNIEnv* env,
    jobject thiz,
    jstring url,
    jstring deviceId,
    jlong timestamp) {

    const char* url_str = (*env)->GetStringUTFChars(env, url, NULL);
    const char* device_id_str = (*env)->GetStringUTFChars(env, deviceId, NULL);

    char* result = rust_sign_mobile_api(url_str, device_id_str, (uint64_t)timestamp);

    (*env)->ReleaseStringUTFChars(env, url, url_str);
    (*env)->ReleaseStringUTFChars(env, deviceId, device_id_str);

    jstring jresult = (*env)->NewStringUTF(env, result);
    rust_free_string(result);
    return jresult;
}

JNIEXPORT jstring JNICALL
Java_com_tiktokviewer_domain_engine_light_SignatureGenerator_nativeSignWebApi(
    JNIEnv* env,
    jobject thiz,
    jstring url,
    jstring userAgent,
    jlong timestamp) {

    const char* url_str = (*env)->GetStringUTFChars(env, url, NULL);
    const char* ua_str = (*env)->GetStringUTFChars(env, userAgent, NULL);

    char* result = rust_sign_web_api(url_str, ua_str, (uint64_t)timestamp);

    (*env)->ReleaseStringUTFChars(env, url, url_str);
    (*env)->ReleaseStringUTFChars(env, userAgent, ua_str);

    jstring jresult = (*env)->NewStringUTF(env, result);
    rust_free_string(result);
    return jresult;
}
