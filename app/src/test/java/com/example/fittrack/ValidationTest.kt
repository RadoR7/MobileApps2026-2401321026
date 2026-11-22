package com.example.fittrack

import com.example.fittrack.util.Validation
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {
    @Test fun validDate() = assertTrue(Validation.validDate("2026-11-21"))
    @Test fun invalidDate() = assertFalse(Validation.validDate("2026-02-30"))
    @Test fun workoutAcceptsValid() = assertNull(Validation.workout("Run", "Running", 30, 250, "2026-11-21"))
    @Test fun workoutRejectsEmptyTitle() = assertNotNull(Validation.workout("", "Running", 30, 250, "2026-11-21"))
    @Test fun workoutRejectsZeroDuration() = assertNotNull(Validation.workout("Run", "Running", 0, 250, "2026-11-21"))
    @Test fun validFood() = assertNull(Validation.food("Rice", "Lunch", 320, 7.0, 67.0, 1.0, "2026-11-21"))
    @Test fun invalidFood() = assertNotNull(Validation.food("Rice", "Lunch", -3, 7.0, 67.0, 1.0, "2026-11-21"))
    @Test fun invalidMacro() = assertNotNull(Validation.food("Rice", "Lunch", 320, -1.0, 67.0, 1.0, "2026-11-21"))
    @Test fun validSleep() = assertNull(Validation.sleep("2026-11-21", "23:00", "07:00", 5))
    @Test fun badSleepQuality() = assertNotNull(Validation.sleep("2026-11-21", "23:00", "07:00", 8))
    @Test fun badSleepTime() = assertNotNull(Validation.sleep("2026-11-21", "28:00", "07:00", 4))
    @Test fun equalSleepTimeIsInvalid() = assertNotNull(Validation.sleep("2026-11-21", "10:00", "10:00", 4))
    @Test fun zeroWaterRejected() = assertNotNull(Validation.water(0))
    @Test fun waterAccepted() = assertNull(Validation.water(250))
    @Test fun goalsAccepted() = assertNull(Validation.goals(2200, 45, 8.0, 2000))
    @Test fun goalsRejectTooLowCalories() = assertNotNull(Validation.goals(10, 45, 8.0, 2000))
}
