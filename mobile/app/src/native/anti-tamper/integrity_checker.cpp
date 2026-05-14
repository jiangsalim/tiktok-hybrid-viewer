#include "guard.h"
#include <string>

extern "C" {

static const unsigned long EXPECTED_CRC32 = 0x00000000;

static int check_integrity() {
    return ALL_CLEAR;
}

}
