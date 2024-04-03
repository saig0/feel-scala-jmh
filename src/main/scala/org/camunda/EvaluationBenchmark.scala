/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.camunda

import org.camunda.EvaluationBenchmark._
import org.camunda.feel.api.FeelEngineBuilder
import org.camunda.feel.syntaxtree.ParsedExpression
import org.openjdk.jmh.annotations._

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
class EvaluationBenchmark {

  @Benchmark
  def comparisonNumber(): Any = {
    engine.evaluate(
      expression = expressionComparisonNumber,
      variables = Map("x" -> 21)
    )
  }

  @Benchmark
  def intervalNumbers(): Any = {
    engine.evaluate(
      expression = expressionIntervalNumbers,
      variables = Map("x" -> 21)
    )
  }

  @Benchmark
  def intervalDateAndTime(): Any = {
    engine.evaluate(
      expression = expressionIntervalDateAndTime,
      variables = Map("x" -> LocalDateTime.parse("2021-04-01T00:00:00"))
    )
  }

  @Benchmark
  def pathExpression(): Any = {
    engine.evaluate(
      expression = expressionPath,
      variables = Map("order" ->
        Map("tracking" ->
          Map("id" -> "order-123")
        )
      )
    )
  }

  @Benchmark
  def filterExpression(): Any = {
    engine.evaluate(
      expression = expressionFilter,
      variables = Map("order" ->
        List(
          Map("type" -> "priority")
        )
      )
    )
  }

  @Benchmark
  def listIndexAccess(): Any = {
    engine.evaluate(
      expression = expressionListIndexAccess,
      variables = Map[String, Any]()
    )
  }

  @Benchmark
  def contextProjection(): Any = {
    engine.evaluate(
      expression = expressionContextProjection,
      variables = Map[String, Any]()
    )
  }

  @Benchmark
  def forExpression(): Any = {
    engine.evaluate(
      expression = expressionFor,
      variables = Map("orders" ->
        List(
          Map("id" -> "order-123")
        )
      )
    )
  }

  @Benchmark
  def contextLiteral(): Any = {
    engine.evaluate(
      expression = expressionContextLiteral,
      variables = Map[String, Any]()
    )
  }

  @Benchmark
  def listLiteral(): Any = {
    engine.evaluate(
      expression = expressionListLiteral,
      variables = Map[String, Any]()
    )
  }

  @Benchmark
  def functionInvocation(): Any = {
    engine.evaluate(
      expression = expressionFunctionInvocation,
      variables = Map("x" -> "Hello camunda")
    )
  }

}

object EvaluationBenchmark {

  @State(Scope.Benchmark)
  private val engine = FeelEngineBuilder().build()

  @State(Scope.Benchmark)
  private val expressionComparisonNumber = parseExpression("x = 21")

  @State(Scope.Benchmark)
  private val expressionIntervalNumbers = parseExpression("x between 10 and 30")

  @State(Scope.Benchmark)
  private val expressionIntervalDateAndTime = parseExpression("""x between date and time("2021-01-01T00:00:00") and date and time("2021-06-01T00:00:00")""")

  @State(Scope.Benchmark)
  private val expressionPath = parseExpression("order.tracking.id")

  @State(Scope.Benchmark)
  private val expressionFilter = parseExpression("""order[type = "priority"]""")

  @State(Scope.Benchmark)
  private val expressionListIndexAccess = parseExpression("[1,2,3][2]")

  @State(Scope.Benchmark)
  private val expressionContextProjection = parseExpression("{x:1,y:2,z:3}.x")

  @State(Scope.Benchmark)
  private val expressionFor = parseExpression("for order in orders return order.id")

  @State(Scope.Benchmark)
  private val expressionContextLiteral = parseExpression("{x:1,y:2,z:3}")

  @State(Scope.Benchmark)
  private val expressionListLiteral = parseExpression("[1,2,3]")

  @State(Scope.Benchmark)
  private val expressionFunctionInvocation = parseExpression("""contains(x, "camunda")""")

  private def parseExpression(expression: String): ParsedExpression = engine.parseExpression(expression).parsedExpression

}
