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

import java.util.concurrent.TimeUnit

import org.camunda.ParserBenchmark.engine
import org.camunda.feel.FeelEngine
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Measurement, Mode, OutputTimeUnit, Scope, State, Threads, Warmup}

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
class ParserBenchmark {

  @Benchmark
  def comparisonNumber(): Any = {
    engine.parseExpression("x = 21")
  }

  @Benchmark
  def intervalNumbers(): Any = {
    engine.parseExpression("x between 10 and 30")
  }

  @Benchmark
  def intervalDateAndTime(): Any = {
    engine.parseExpression("""x between date and time("2021-01-01T00:00:00") and date and time("2021-06-01T00:00:00")""")
  }

  @Benchmark
  def pathExpression(): Any = {
    engine.parseExpression("order.tracking.id")
  }

  @Benchmark
  def filterExpression(): Any = {
    engine.parseExpression("""order[type = "priority"]""")
  }

  @Benchmark
  def listIndexAccess(): Any = {
    engine.parseExpression("[1,2,3][2]")
  }

  @Benchmark
  def contextProjection(): Any = {
    engine.parseExpression("{x:1,y:2,z:3}.x")
  }

  @Benchmark
  def forExpression(): Any = {
    engine.parseExpression("for order in orders return order.id")
  }

  @Benchmark
  def contextLiteral(): Any = {
    engine.parseExpression("""{x:1,y:2,z:3}""")
  }

  @Benchmark
  def listLiteral(): Any = {
    engine.parseExpression("""[1,2,3]""")
  }

  @Benchmark
  def functionInvocation(): Any = {
    engine.parseExpression("""contains(x, "camunda")""")
  }

}

object ParserBenchmark {

  @State(Scope.Benchmark)
  private val engine = new FeelEngine()

}
