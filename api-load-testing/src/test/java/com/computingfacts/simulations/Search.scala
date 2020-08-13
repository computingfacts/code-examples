package com.computingfacts.simulations
import io.gatling.core.Predef._ // required for Gatling core structure DSL
import io.gatling.http.Predef._ // required for Gatling HTTP DSL
import scala.concurrent.duration._  // used for specifying duration unit, eg "1 second"
import scala.language.postfixOps

class Search extends Simulation {
  before {
    println("Simulation is about to start!")
  }

  after {
    println("Simulation is finished!")
  }
  object WebClientSearch {
    val feeder = csv("data/search.csv").random
    val search =   feed(feeder).exec(
        http("Nonblocking WebClient Search")
          .get("/service/webclient/search")
          .queryParam("dataType","json")
          .queryParam("query","${Name}")
          .check(status.is(200))
      ).pause(5 seconds)
      .exec(
        http("Nonblocking Autocomplete Search (POST)")
          .post("/service/webclient/auto/search")
          .formParam("query","pyruva")
          .check(status.is(200))
          .check(jsonPath("$..suggestion").is("pyruvate"))
    ).pause(5 seconds)
  .feed(feeder).exec(
    http("Nonblocking WebClient Search - returns Flux ")
      .get("/service/webclient/nonblocking/search")
      .queryParam("dataType","json")
      .queryParam("query","${Name}")
      .check(status.is(200)))
  }
  object OkHttpSearch{
    val feeder = csv("data/search.csv").random
    val search = exec(
      http("Autocomplete Search - with OkHttp ")
        .get("/service/okhttp/auto/search")
        .queryParam("dataType","json")
        .queryParam("query","silden")
        .check(status.is(200))
        .check(jsonPath("$..suggestion").is("sildenafil")))
      .pause(5 seconds)
    .feed(feeder)
      .exec(
        http("Entry Search - with OkHttp ")
          .get("/service/okhttp/search")
          .queryParam("dataType","json")
          .queryParam("query","${Name}")
          .check(status.is(200)))
  }

  object BlockingWebClientSearch {
    val feeder = csv("data/search.csv").random
    val search = feed(feeder).exec(
      http("Blocking WebClient Search")
        .get("/service/blocking/search")
        .queryParam("dataType","json")
        .queryParam("query","${Name}")
        .check(status.is(200))
    ).pause(5 seconds)
  }

  val httpProtocol = http
    .baseUrl("http://localhost:9090/api")
    .disableCaching
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-UK,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val searchScenario = scenario("Search")
    .exec(BlockingWebClientSearch.search,WebClientSearch.search,OkHttpSearch.search)

  setUp(
    searchScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(1),
      rampUsersPerSec(100) to(500) during(10 seconds)
    ).protocols(httpProtocol)
  ).maxDuration(1 minute)
    .assertions(global.responseTime.max.lte(60000))  //assert that the max response time of all requests is less than 60_000 ms
    .assertions(global.responseTime.percentile(75).lte(40000))
    .assertions(global.successfulRequests.percent.gte(80))
    .assertions(forAll.failedRequests.percent.lte(50))   //assert that every request has no more than 50% of failing requests
    .assertions(global.failedRequests.count.lte(50))
}
