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

import com.jfoenix.converters.ButtonTypeConverter;
import com.jfoenix.skins.JFXButtonSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.Skin;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JFXButton is the material design implementation of a button.
 * it contains ripple effect , the effect color is set according to text fill of the button 1st
 * or the text fill of graphic node (if it was set to Label) 2nd.
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class JFXButton extends Button {

    /**
     * {@inheritDoc}
     */
    public JFXButton() {
        initialize();
        // init in scene builder workaround ( TODO : remove when JFoenix is well integrated in scenebuilder by gluon )
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraceElements.length && i < 15; i++) {
            if (stackTraceElements[i].getClassName().toLowerCase().contains(".scenebuilder.kit.fxom.")) {
                this.setText("Button");
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public JFXButton(String text) {
        super(text);
        initialize();
    }

    /**
     * {@inheritDoc}
     */
    public JFXButton(String text, Node graphic) {
        super(text, graphic);
        initialize();
    }

    private void initialize() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new JFXButtonSkin(this);
    }


    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/
    /**
     * the ripple color property of JFXButton.
     */
    private ObjectProperty<Paint> ripplerFill = new SimpleObjectProperty<>(null);

    public final ObjectProperty<Paint> ripplerFillProperty() {
        return this.ripplerFill;
    }

    /**
     * @return the ripple color
     */
    public final Paint getRipplerFill() {
        return this.ripplerFillProperty().get();
    }

    /**
     * set the ripple color
     *
     * @param ripplerFill the color of the ripple effect
     */
    public final void setRipplerFill(final Paint ripplerFill) {
        this.ripplerFillProperty().set(ripplerFill);
    }


    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    /**
     * Initialize the style class to 'jfx-button'.
     * <p>
     * This is the selector class from which CSS can be used to style
     * this control.
     */
    private static final String DEFAULT_STYLE_CLASS = "jfx-button";


    public enum ButtonType {FLAT, RAISED}

    /**
     * according to material design the button has two types:
     * - flat : only shows the ripple effect upon clicking the button
     * - raised : shows the ripple effect and change in depth upon clicking the button
     */
    private StyleableObjectProperty<ButtonType> buttonType = new SimpleStyleableObjectProperty<>(
        StyleableProperties.BUTTON_TYPE,
        JFXButton.this,
        "buttonType",
        ButtonType.FLAT);

    public ButtonType getButtonType() {
        return buttonType == null ? ButtonType.FLAT : buttonType.get();
    }

    public StyleableObjectProperty<ButtonType> buttonTypeProperty() {
        return this.buttonType;
    }

    public void setButtonType(ButtonType type) {
        this.buttonType.set(type);
    }

    private static class StyleableProperties {
        private static final CssMetaData<JFXButton, ButtonType> BUTTON_TYPE =
            new CssMetaData<JFXButton, ButtonType>("-jfx-button-type",
                ButtonTypeConverter.getInstance(), ButtonType.FLAT) {
                @Override
                public boolean isSettable(JFXButton control) {
                    return control.buttonType == null || !control.buttonType.isBound();
                }

                @Override
                public StyleableProperty<ButtonType> getStyleableProperty(JFXButton control) {
                    return control.buttonTypeProperty();
                }
            };

        private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables,
                BUTTON_TYPE
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
