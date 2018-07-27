package com.popalay.cardme

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class KotlinJUnitTest : StringSpec({

    "message" {
        val helloWorld = "ssss"
        helloWorld.length shouldBe 4
    }
})