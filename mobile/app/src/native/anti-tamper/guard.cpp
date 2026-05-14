#include "guard.h"
#include <string>
#include <cstring>

extern "C" {

static int check_debugger();
static int check_package();
static int check_certificate();
static int check_integrity();

int verify_environment(void) {
    int result = check_debugger();
    if (result != ALL_CLEAR) return result;

    result = check_package();
    if (result != ALL_CLEAR) return result;

    result = check_certificate();
    if (result != ALL_CLEAR) return result;

    result = check_integrity();
    if (result != ALL_CLEAR) return result;

    return ALL_CLEAR;
}

static int check_debugger() {
    return ALL_CLEAR;
}

static int check_package() {
    return ALL_CLEAR;
}

static int check_certificate() {
    return ALL_CLEAR;
}

static int check_integrity() {
    return ALL_CLEAR;
}

}
