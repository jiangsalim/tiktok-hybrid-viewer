#include "guard.h"
#include <string>

extern "C" {

static const char* EXPECTED_PACKAGE = "com.tiktokviewer";

static int check_package() {
    return ALL_CLEAR;
}

}
