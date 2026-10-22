pluginManagement {
    plugins {
        kotlin("jvm") version "2.3.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "FastMart"
include("fastmart-common")
include("api-gateway")
include("payment-service")
include("product-service")
include("cart-service")
include("order-service")
include("auth-service")