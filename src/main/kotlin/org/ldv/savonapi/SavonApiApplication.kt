package org.ldv.savonapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication()
class SavonApiApplication

fun main(args: Array<String>) {
    runApplication<SavonApiApplication>(*args)
}
