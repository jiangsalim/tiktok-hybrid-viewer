#ifndef TIKTOK_GUARD_H
#define TIKTOK_GUARD_H

#ifdef __cplusplus
extern "C" {
#endif

enum GuardResult {
    ALL_CLEAR = 0,
    DEBUGGER_DETECTED = 1,
    PACKAGE_MISMATCH = 2,
    CERTIFICATE_INVALID = 3,
    INTEGRITY_FAILED = 4
};

int verify_environment(void);

#ifdef __cplusplus
}
#endif

#endif
