package edu.knoldus.implement.impl

import akka.{Done, NotUsed}
import com.example.hello.api.{HellolagomService, Posting, UserData}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import edu.knoldus.implement.api.{ImplementlagomService, UserInfo}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class ImplementlagomServiceImpl(serviceImport: HellolagomService)(implicit ec: ExecutionContext) extends ImplementlagomService {
 /**
   * for adding users through external service
   * @return successfully completion
   */
 override def postService: ServiceCall[UserInfo, String] = ServiceCall { request =>
  val newUser = Posting(request.name, request.id)
  val externalResult = serviceImport.usingPostOperation().invoke(newUser)
  val result = Await.result(externalResult, Duration.Inf)
  println(result)
  Future.successful("added")
 }

 /**
   * reading all users records through external service
   * @return list of all users
   */
 override def getService: ServiceCall[NotUsed, ListBuffer[UserInfo]] = ServiceCall { request =>
  val externalResult = serviceImport.readUser.invoke()
  val result = Await.result(externalResult, Duration.Inf)
  val users = result.map(user => UserInfo(user.name, user.id))
  Future.successful(users)
 }
}
