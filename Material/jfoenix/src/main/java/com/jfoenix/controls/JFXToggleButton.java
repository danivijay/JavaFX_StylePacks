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

package com.jfoenix.controls;

import com.jfoenix.skins.JFXToggleButtonSkin;
import com.sun.javafx.css.converters.PaintConverter;
import javafx.css.*;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.Skin;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JFXToggleButton is the material design implementation of a toggle button.
 * important CSS Selectors:
 * <p>
 * .jfx-toggle-button{
 * -fx-toggle-color: color-value;
 * -fx-untoggle-color: color-value;
 * -fx-toggle-line-color: color-value;
 * -fx-untoggle-line-color: color-value;
 * }
 * <p>
 * To change the rippler color when toggled:
 * <p>
 * .jfx-toggle-button .jfx-rippler{
 * -fx-rippler-fill: color-value;
 * }
 * <p>
 * .jfx-toggle-button:selected .jfx-rippler{
 * -fx-rippler-fill: color-value;
 * }
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class JFXToggleButton extends ToggleButton {

    /**
     * {@inheritDoc}
     */
    public JFXToggleButton() {
        initialize();
        // init in scene builder workaround ( TODO : remove when JFoenix is well integrated in scenebuilder by gluon )
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraceElements.length && i < 15; i++) {
            if (stackTraceElements[i].getClassName().toLowerCase().contains(".scenebuilder.kit.fxom.")) {
                this.setText("ToggleButton");
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new JFXToggleButtonSkin(this);
    }

    private void initialize() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        toggleColor.addListener((o, oldVal, newVal) -> {
            // update line color in case not set by the user
            toggleLineColor.set(((Color) getToggleColor()).desaturate().desaturate().brighter());
        });
    }

    /***************************************************************************
     *                                                                         *
     * styleable Properties                                                    *
     *                                                                         *
     **************************************************************************/

    /**
     * Initialize the style class to 'jfx-toggle-button'.
     * <p>
     * This is the selector class from which CSS can be used to style
     * this control.
     */
    private static final String DEFAULT_STYLE_CLASS = "jfx-toggle-button";

    /**
     * default color used when the button is toggled
     */
    private StyleableObjectProperty<Paint> toggleColor = new SimpleStyleableObjectProperty<>(StyleableProperties.TOGGLE_COLOR,
        JFXToggleButton.this,
        "toggleColor",
        Color.valueOf(
            "#009688"));

    public Paint getToggleColor() {
        return toggleColor == null ? Color.valueOf("#009688") : toggleColor.get();
    }

    public StyleableObjectProperty<Paint> toggleColorProperty() {
        return this.toggleColor;
    }

    public void setToggleColor(Paint color) {
        this.toggleColor.set(color);
    }

    /**
     * default color used when the button is not toggled
     */
    private StyleableObjectProperty<Paint> untoggleColor = new SimpleStyleableObjectProperty<>(StyleableProperties.UNTOGGLE_COLOR,
        JFXToggleButton.this,
        "unToggleColor",
        Color.valueOf(
            "#FAFAFA"));

    public Paint getUnToggleColor() {
        return untoggleColor == null ? Color.valueOf("#FAFAFA") : untoggleColor.get();
    }

    public StyleableObjectProperty<Paint> unToggleColorProperty() {
        return this.untoggleColor;
    }

    public void setUnToggleColor(Paint color) {
        this.untoggleColor.set(color);
    }

    /**
     * default line color used when the button is toggled
     */
    private StyleableObjectProperty<Paint> toggleLineColor = new SimpleStyleableObjectProperty<>(
        StyleableProperties.TOGGLE_LINE_COLOR,
        JFXToggleButton.this,
        "toggleLineColor",
        Color.valueOf("#77C2BB"));

    public Paint getToggleLineColor() {
        return toggleLineColor == null ? Color.valueOf("#77C2BB") : toggleLineColor.get();
    }

    public StyleableObjectProperty<Paint> toggleLineColorProperty() {
        return this.toggleLineColor;
    }

    public void setToggleLineColor(Paint color) {
        this.toggleLineColor.set(color);
    }

    /**
     * default line color used when the button is not toggled
     */
    private StyleableObjectProperty<Paint> untoggleLineColor = new SimpleStyleableObjectProperty<>(
        StyleableProperties.UNTOGGLE_LINE_COLOR,
        JFXToggleButton.this,
        "unToggleLineColor",
        Color.valueOf("#999999"));

    public Paint getUnToggleLineColor() {
        return untoggleLineColor == null ? Color.valueOf("#999999") : untoggleLineColor.get();
    }

    public StyleableObjectProperty<Paint> unToggleLineColorProperty() {
        return this.untoggleLineColor;
    }

    public void setUnToggleLineColor(Paint color) {
        this.untoggleLineColor.set(color);
    }


    private static class StyleableProperties {
        private static final CssMetaData<JFXToggleButton, Paint> TOGGLE_COLOR =
            new CssMetaData<JFXToggleButton, Paint>("-jfx-toggle-color",
                PaintConverter.getInstance(), Color.valueOf("#009688")) {
                @Override
                public boolean isSettable(JFXToggleButton control) {
                    return control.toggleColor == null || !control.toggleColor.isBound();
                }

                @Override
                public StyleableProperty<Paint> getStyleableProperty(JFXToggleButton control) {
                    return control.toggleColorProperty();
                }
            };

        private static final CssMetaData<JFXToggleButton, Paint> UNTOGGLE_COLOR =
            new CssMetaData<JFXToggleButton, Paint>("-jfx-untoggle-color",
                PaintConverter.getInstance(), Color.valueOf("#FAFAFA")) {
                @Override
                public boolean isSettable(JFXToggleButton control) {
                    return control.untoggleColor == null || !control.untoggleColor.isBound();
                }

                @Override
                public StyleableProperty<Paint> getStyleableProperty(JFXToggleButton control) {
                    return control.unToggleColorProperty();
                }
            };

        private static final CssMetaData<JFXToggleButton, Paint> TOGGLE_LINE_COLOR =
            new CssMetaData<JFXToggleButton, Paint>("-jfx-toggle-line-color",
                PaintConverter.getInstance(), Color.valueOf("#77C2BB")) {
                @Override
                public boolean isSettable(JFXToggleButton control) {
                    return control.toggleLineColor == null || !control.toggleLineColor.isBound();
                }

                @Override
                public StyleableProperty<Paint> getStyleableProperty(JFXToggleButton control) {
                    return control.toggleLineColorProperty();
                }
            };

        private static final CssMetaData<JFXToggleButton, Paint> UNTOGGLE_LINE_COLOR =
            new CssMetaData<JFXToggleButton, Paint>("-jfx-untoggle-line-color",
                PaintConverter.getInstance(), Color.valueOf("#999999")) {
                @Override
                public boolean isSettable(JFXToggleButton control) {
                    return control.untoggleLineColor == null || !control.untoggleLineColor.isBound();
                }

                @Override
                public StyleableProperty<Paint> getStyleableProperty(JFXToggleButton control) {
                    return control.unToggleLineColorProperty();
                }
            };


        private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables,
                TOGGLE_COLOR,
                UNTOGGLE_COLOR,
                TOGGLE_LINE_COLOR,
                UNTOGGLE_LINE_COLOR
            );
            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    // inherit the styleable properties from parent
    private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        if (STYLEABLES == null) {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<>(Control.getClassCssMetaData());
            styleables.addAll(getClassCssMetaData());
            styleables.addAll(Labeled.getClassCssMetaData());
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
        return STYLEABLES;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }


}
