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

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.behavior.JFXTimePickerBehavior;
import com.jfoenix.svg.SVGGlyph;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.time.LocalTime;

/**
 * <h1>Material Design Time Picker Skin</h1>
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2017-03-01
 */
public class JFXTimePickerSkin extends ComboBoxPopupControl<LocalTime> {
    private JFXTimePicker jfxTimePicker;
    private JFXTextField editorNode;
    // displayNode is the same as editorNode
    private TextField displayNode;
    private JFXTimePickerContent content;
    private JFXDialog dialog;

    public JFXTimePickerSkin(JFXTimePicker timePicker) {
        super(timePicker, new JFXTimePickerBehavior(timePicker));
        this.jfxTimePicker = timePicker;
        editorNode = new JFXTextField();
        editorNode.focusColorProperty().bind(timePicker.defaultColorProperty());
        editorNode.focusedProperty().addListener((obj, oldVal, newVal) -> {
            if (!newVal) {
                setTextFromTextFieldIntoComboBoxValue();
            }
        });

        // create calender or clock button
        arrow = new SVGGlyph(0,
            "clock",
            "M512 310.857v256q0 8-5.143 13.143t-13.143 5.143h-182.857q-8 0-13.143-5.143t-5.143-13.143v-36.571q0-8 5.143-13.143t13.143-5.143h128v-201.143q0-8 5.143-13.143t13.143-5.143h36.571q8 0 13.143 5.143t5.143 13.143zM749.714 512q0-84.571-41.714-156t-113.143-113.143-156-41.714-156 41.714-113.143 113.143-41.714 156 41.714 156 113.143 113.143 156 41.714 156-41.714 113.143-113.143 41.714-156zM877.714 512q0 119.429-58.857 220.286t-159.714 159.714-220.286 58.857-220.286-58.857-159.714-159.714-58.857-220.286 58.857-220.286 159.714-159.714 220.286-58.857 220.286 58.857 159.714 159.714 58.857 220.286z",
            Color.BLACK);
        ((SVGGlyph) arrow).fillProperty().bind(timePicker.defaultColorProperty());
        ((SVGGlyph) arrow).setSize(20, 20);
        arrowButton.getChildren().setAll(arrow);
        arrowButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
            CornerRadii.EMPTY,
            Insets.EMPTY)));

        //dialog = new JFXDialog(null, content, transitionType, overlayClose)
        registerChangeListener(timePicker.converterProperty(), "CONVERTER");
        registerChangeListener(timePicker.valueProperty(), "VALUE");
    }

    @Override
    protected Node getPopupContent() {
        if (content == null) {
            content = new JFXTimePickerContent(jfxTimePicker);
        }
        return content;
    }

    @Override
    public void show() {
        if (!jfxTimePicker.isOverLay()) {
            super.show();
        }

        if (content != null) {
            content.init();
            content.clearFocus();
        }

        if (jfxTimePicker.isOverLay()) {
            if (dialog == null) {
                StackPane dialogParent = jfxTimePicker.getDialogParent();
                if (dialogParent == null) {
                    dialogParent = (StackPane) getSkinnable().getScene().getRoot();
                }
                dialog = new JFXDialog(dialogParent, (Region) getPopupContent(), DialogTransition.CENTER, true);
                arrowButton.setOnMouseClicked((click) -> {
                    if (jfxTimePicker.isOverLay()) {
                        StackPane parent = jfxTimePicker.getDialogParent();
                        if (parent == null) {
                            parent = (StackPane) getSkinnable().getScene().getRoot();
                        }
                        dialog.show(parent);
                    }
                });
            }
        }
    }

    @Override
    protected void handleControlPropertyChanged(String p) {
        if ("CONVERTER".equals(p)) {
            updateDisplayNode();
        } else if ("EDITOR".equals(p)) {
            getEditableInputNode();
        } else if ("SHOWING".equals(p)) {
            if (jfxTimePicker.isShowing()) {
                show();
            } else {
                hide();
            }
        } else if ("VALUE".equals(p)) {
            updateDisplayNode();
            jfxTimePicker.fireEvent(new ActionEvent());
        } else {
            super.handleControlPropertyChanged(p);
        }
    }


    @Override
    protected TextField getEditor() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        //  added to fix android issue as the stack trace on android is
        // not the same as desktop
        if (caller.getClassName().equals(this.getClass().getName())) {
            caller = Thread.currentThread().getStackTrace()[3];
        }
        boolean parentListenerCall = caller.getMethodName().contains("lambda") && caller.getClassName()
            .equals(this.getClass()
                .getSuperclass()
                .getName());
        if (parentListenerCall) {
            return null;
        }
        return editorNode;
    }

    @Override
    protected StringConverter<LocalTime> getConverter() {
        return jfxTimePicker.getConverter();
    }

    @Override
    public Node getDisplayNode() {
        if (displayNode == null) {
            displayNode = getEditableInputNode();
            displayNode.getStyleClass().add("time-picker-display-node");
            updateDisplayNode();
        }
        displayNode.setEditable(jfxTimePicker.isEditable());
        return displayNode;
    }

    /*
     * this method is called from the behavior class to make sure
     * DatePicker button is in sync after the popup is being dismissed
     */
    public void syncWithAutoUpdate() {
        if (!getPopup().isShowing() && jfxTimePicker.isShowing()) {
            jfxTimePicker.hide();
        }
    }

}

