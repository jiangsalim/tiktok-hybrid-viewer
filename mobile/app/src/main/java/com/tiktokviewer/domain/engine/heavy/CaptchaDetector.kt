package com.tiktokviewer.domain.engine.heavy

class CaptchaDetector {

    fun detect(html: String, pageTitle: String = ""): CaptchaResult {
        val lowerHtml = html.lowercase()
        val lowerTitle = pageTitle.lowercase()

        if (lowerHtml.contains("captcha") ||
            lowerHtml.contains("verify yourself") ||
            lowerHtml.contains("are you a robot") ||
            lowerHtml.contains("press and hold") ||
            lowerHtml.contains("slide to verify") ||
            lowerTitle.contains("captcha") ||
            lowerTitle.contains("verify")
        ) {
            return CaptchaResult.CaptchaDetected
        }

        if (lowerHtml.contains("unusual traffic") ||
            lowerHtml.contains("too many requests") ||
            lowerHtml.contains("access denied") ||
            lowerHtml.contains("blocked") ||
            lowerHtml.contains("try again later") ||
            lowerTitle.contains("access denied")
        ) {
            return CaptchaResult.Blocked
        }

        return CaptchaResult.Clean
    }
}

sealed class CaptchaResult {
    object Clean : CaptchaResult()
    object CaptchaDetected : CaptchaResult()
    object Blocked : CaptchaResult()
}
