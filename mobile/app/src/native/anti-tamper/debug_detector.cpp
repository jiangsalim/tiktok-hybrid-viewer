#include "guard.h"
#include <fstream>
#include <string>
#include <unistd.h>
#include <sys/ptrace.h>

extern "C" {

static int check_debugger() {
    std::ifstream status("/proc/self/status");
    if (status.is_open()) {
        std::string line;
        while (std::getline(status, line)) {
            if (line.find("TracerPid:") == 0) {
                size_t pos = line.find(':');
                if (pos != std::string::npos) {
                    std::string pid_str = line.substr(pos + 1);
                    int tracer_pid = 0;
                    for (char c : pid_str) {
                        if (c >= '0' && c <= '9') {
                            tracer_pid = tracer_pid * 10 + (c - '0');
                        }
                    }
                    if (tracer_pid != 0) {
                        return DEBUGGER_DETECTED;
                    }
                }
            }
        }
        status.close();
    }

    if (ptrace(PTRACE_TRACEME, 0, nullptr, 0) == -1) {
        return DEBUGGER_DETECTED;
    }

    return ALL_CLEAR;
}

}
