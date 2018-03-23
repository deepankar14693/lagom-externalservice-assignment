package edu.knoldus.implement.api
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

import scala.collection.mutable.ListBuffer

trait ImplementlagomService extends Service{

 def postService: ServiceCall[UserInfo, String]
 def getService: ServiceCall[NotUsed,ListBuffer[UserInfo]]

 override final def descriptor = {
  import Service._
  // @formatter:off
  named("implement-service")
   .withCalls(
    restCall(Method.POST,"/api/add", postService _),
    pathCall("/api/read",getService _)
   ).withAutoAcl(true)
  // @formatter:on
 }
}

case class UserInfo(name: String,id: Int)
object UserInfo {

 implicit val format: Format[UserInfo] = Json.format[UserInfo]

}
