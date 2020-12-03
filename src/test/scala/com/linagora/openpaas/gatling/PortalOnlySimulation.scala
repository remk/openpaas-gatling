package com.linagora.openpaas.gatling

import com.linagora.openpaas.gatling.Configuration._
import com.linagora.openpaas.gatling.core.LoginSteps.{login, logout}
import com.linagora.openpaas.gatling.core.UserSteps.getProfile
import com.linagora.openpaas.gatling.core.authentication.lemonldap.LemonLdapSteps
import com.linagora.openpaas.gatling.core.authentication.pkce.PKCESteps
import com.linagora.openpaas.gatling.unifiedinbox.scenari.InboxScenari
import io.gatling.core.Predef._

import scala.concurrent.duration.DurationInt

class PortalOnlySimulation extends Simulation {
  private val feeder = csv("users.csv")

  val scn = scenario("Testing OpenPaaS mix scenarios")
    .feed(feeder.circular())

    .during(ScenarioDuration) {
        group("INBOX")(
          exec(LemonLdapSteps.getPage)
            .exec(LemonLdapSteps.loadLoginTemplates)
        ).pause(7500 milliseconds, 15 seconds)
    }

  setUp(scn.inject(rampUsers(UserCount) during(InjectDuration))).protocols(HttpProtocol)
}
