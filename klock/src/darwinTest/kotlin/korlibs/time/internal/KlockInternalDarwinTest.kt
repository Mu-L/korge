package korlibs.time.internal

import korlibs.time.*
import korlibs.time.darwin.*
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import kotlin.test.*

class KlockInternalDarwinTest {
    @Test
    fun test() {
        val names = CFTimeZoneCopyKnownNames().toStrArray().filterNotNull().toList()
        assertEquals(true, "Europe/Madrid" in names)
        val EuropeMadrid = CFTimeZoneCreateWithName(null, CFStringCreateWithCString(null, "Europe/Madrid", kCFStringEncodingUTF8), true)
        //val CET = CFTimeZoneCreateWithName(null, CFStringCreateWithCString(null, "CET", kCFStringEncodingUTF8), true)
        //val CEST = CFTimeZoneCreateWithName(null, CFStringCreateWithCString(null, "CEST", kCFStringEncodingUTF8), true)

        // CEST (UTC +2)
        // CET (UTC +1)
        assertEquals(
            """
                CEST: 120
                CET: 60
            """.trimIndent(),
            """
                CEST: ${getLocalTimezoneOffsetDarwin(EuropeMadrid, DateTime(2023, Month.July, 10)).minutes.toInt()}
                CET: ${getLocalTimezoneOffsetDarwin(EuropeMadrid, DateTime(2023, Month.January, 10)).minutes.toInt()}
            """.trimIndent()
        )
    }

    @Test
    fun testCFAbsoluteTime() {
        assertEquals(0.0, DateTime.fromCFAbsoluteTime(0.0).cfAbsoluteTime())
        assertEquals(1000.0, DateTime.fromCFAbsoluteTime(1000.0).cfAbsoluteTime())
        assertEquals(-1000000.0, DateTime.fromCFAbsoluteTime(-1000000.0).cfAbsoluteTime())
        assertEquals("Mon, 01 Jan 2001 00:00:00 UTC", DateTime.fromCFAbsoluteTime(0.0).toStringDefault())
    }
}

fun CFArrayRef?.toStrArray(): Array<String?> {
    val array = this

    return Array(CFArrayGetCount(array).convert()) {
        val ptr = CFArrayGetValueAtIndex(array, it.convert())
        CFStringGetCStringPtr(ptr?.reinterpret(), kCFStringEncodingUTF8)?.toKStringFromUtf8()
    }
}
