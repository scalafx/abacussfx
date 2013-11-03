package org.scalafx.abacussfx

/*
 * Copyright (c) 2013, ScalaFX Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the ScalaFX Project nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE SCALAFX PROJECT OR ITS CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.scene.input.MouseEvent
import scalafx.animation.TranslateTransition
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import scalafx.util.Duration

/**
 * This program shows how to draw rails using Rectangle shape and using text.
 *
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
object Abacus5PushNeighbors extends JFXApp with AbacusCommons {

  val rails = makeRails()
  var texts: Seq[Text] = Seq.empty

  val circles = for (row <- 0 to ROW_COUNT - 1) yield {
    var previousBall: Circle = null
    for (col <- 0 to COL_COUNT - 1) yield {
      val currentBall = new Circle {
        thisBall =>
        radius = RADIUS - 1
        centerX = OFFSET + (col * DIAMETER)
        centerY = OFFSET + (row * DIAMETER)
        onMouseClicked = (e: MouseEvent) => {
          // Toggle between `0` and `MOVE_WAY` locations.
          new TranslateTransition {
            node = thisBall
            toX = if (translateX() > 1) 0 else MOVE_WAY
            duration = Duration(200)
          }.playFromStart()
        }
      }

      texts = texts :+ makeText(((COL_COUNT - col) % COL_COUNT) + "", currentBall)

      if (previousBall != null) {
        // We need a `value` reference to the current currentBall, so it is properly referenced to during
        // later invocation in `onChange` event handler.
        val leftBall = previousBall

        // If the ball on the left moved to the right, make sure that current ball toggles to the right too
        leftBall.translateX.onChange((_, _, newValue) => {
          val newX = newValue.doubleValue
          if (newX > currentBall.translateX()) currentBall.translateX = newX
        })
        // If current current ball toggled to the left, toggle ball on the left to the left too
        currentBall.translateX.onChange((_, _, newValue) => {
          val newX = newValue.doubleValue
          if (newX < leftBall.translateX()) leftBall.translateX = newX
        })

      }
      previousBall = currentBall
      currentBall
    }
  }

  private def makeRails(): Seq[Rectangle] = for (row <- 0 to ROW_COUNT - 1) yield new Rectangle {
    width = WIDTH
    height = RAIL_HEIGHT
    x = PADDING
    y = OFFSET - (RAIL_HEIGHT / 2) + (row * DIAMETER)
  }

  private def makeText(label: String, ball: Circle) = new Text {
    x = ball.centerX() - 3
    y = ball.centerY() + 4
    text = label //"" + ((COL_COUNT - col) % COL_COUNT)
    translateX <== ball.translateX
    onMouseClicked = ball.onMouseClicked()
    fill = WHITE
  }

  stage = new PrimaryStage {
    title = "Abacus 5 Push Neighbors"
    scene = new Scene(WIDTH + 2 * PADDING, HEIGHT + 2 * PADDING) {
      content = rails ++ circles.flatten ++ texts
    }
  }
}