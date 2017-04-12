/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.jfoenix.skins;

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.CacheMomento;
import com.jfoenix.transitions.CachedTransition;
import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 * <h1>Material Design Popup Skin</h1>
 *
 * @author Shadi Shaheen
 * @version 2.0
 * @since 2017-03-01
 */
public class JFXPopupSkin implements Skin<JFXPopup> {

    private final JFXPopup control;
    private final StackPane container = new StackPane();
    private Node root;
    private PopupTransition t;
    private Scale scale;
    private Region popupContent;

    public JFXPopupSkin(JFXPopup control) {
        this.control = control;
        scale = new Scale(1, 0, 0, 0);
        popupContent = control.getPopupContent();
        container.getStyleClass().add("jfx-popup-container");
        container.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        container.getChildren().add(popupContent);
        container.getTransforms().add(scale);
        container.setOpacity(0);
        root = JFXDepthManager.createMaterialNode(container, 4);
        t = new PopupTransition();
    }


    public void reset(PopupVPosition vAlign, PopupHPosition hAlign, double offsetX, double offsetY) {
        // postion the popup according to its animation
        scale.setPivotX(hAlign == PopupHPosition.RIGHT ? container.getWidth() : 0);
        scale.setPivotY(vAlign == PopupVPosition.BOTTOM ? container.getHeight() : 0);
        root.setTranslateX(hAlign == PopupHPosition.RIGHT ? -container.getWidth() + offsetX : offsetX);
        root.setTranslateY(vAlign == PopupVPosition.BOTTOM ? -container.getHeight() + offsetY : offsetY);
    }

    public void animate() {
        if (t.getStatus() == Status.STOPPED) {
            t.play();
        }
    }

    @Override
    public JFXPopup getSkinnable() {
        return control;
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void dispose() {
    }

    private final class PopupTransition extends CachedTransition {
        PopupTransition() {
            super(root, new Timeline(
                    new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(popupContent.opacityProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(scale.xProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(scale.yProperty(), 0, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(700),
                        new KeyValue(scale.xProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(popupContent.opacityProperty(), 0, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(1000),
                        new KeyValue(popupContent.opacityProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(scale.yProperty(), 1, Interpolator.EASE_BOTH)
                    )
                )
                , new CacheMomento(popupContent));
            setCycleDuration(Duration.seconds(.4));
            setDelay(Duration.seconds(0));
        }

        @Override
        protected void starting() {
            container.setOpacity(1);
            super.starting();
        }
    }

    public void init() {
        t.stop();
        container.setOpacity(0);
        scale.setX(0);
        scale.setY(0);
    }
}
