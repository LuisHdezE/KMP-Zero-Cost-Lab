package dev.eliasworks.kmpzerocostlab

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedSmokeTest {
    @Test
    fun markerIdentifiesReferencePilot() {
        assertEquals("KMP Zero-Cost Lab", LabMarker.name)
        assertTrue(LabMarker.phase.startsWith("Phase "))
    }
}
