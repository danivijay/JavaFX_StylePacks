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

import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * DrawersStack is used to show multiple drawers at once
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
@DefaultProperty(value = "content")
public class JFXDrawersStack extends StackPane {
    private List<JFXDrawer> drawers = new ArrayList<>();
    private Node content;
    private boolean holding = false;

    /**
     * creates empty drawers stack
     */
    public JFXDrawersStack() {
        final Rectangle clip = new Rectangle();
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);
    }

    /**
     * @return the content of the drawer stack (Note: the parent of the content node is that latest active drawer if
     * existed)
     */
    public Node getContent() {
        return this.content;
    }

    /**
     * set the content of the drawers stack, similar to {@link JFXDrawer#setContent(Node...)}
     *
     * @param content
     */
    public void setContent(Node content) {
        this.content = content;
        if (drawers.size() > 0) {
            drawers.get(0).setContent(content);
        } else {
            this.getChildren().add(this.content);
        }
    }

    /**
     * add JFXDrawer to the stack
     *
     * @param drawer
     */
    private void addDrawer(JFXDrawer drawer) {
        if (drawer == null) {
            return;
        }

        if (drawers.isEmpty()) {
            if (content != null) {
                drawer.setContent(content);
            }
            this.getChildren().add(drawer);
        } else {
            drawer.setContent(drawers.get(drawers.size() - 1));
            this.getChildren().add(drawer);
        }

        drawer.sidePane.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
            holding = true;
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException intEx) {
                    // TODO Auto-generated catch block
                    intEx.printStackTrace();
                }
                if (holding) {
                    holding = false;
                    if (drawers.indexOf(drawer) < drawers.size() - 1) {
                        Platform.runLater(() -> drawer.bringToFront((param) -> {
                            updateDrawerPosition(drawer);
                            return param;
                        }));
                    }
                }
            }).start();
        });

        drawer.sidePane.addEventHandler(MouseEvent.MOUSE_DRAGGED, (event) -> holding = false);
        drawer.sidePane.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> holding = false);

        drawers.add(drawer);
    }

    /**
     * update drawers position in the stack once a drawer is drawn
     *
     * @param drawer
     */
    private void updateDrawerPosition(JFXDrawer drawer) {
        int index = drawers.indexOf(drawer);
        if (index + 1 < drawers.size()) {
            if (index - 1 >= 0) {
                drawers.get(index + 1).setContent(drawers.get(index - 1));
            } else if (index == 0) {
                drawers.get(index + 1).setContent(content);
            }
        }
        if (index < drawers.size() - 1) {
            drawer.setContent(drawers.get(drawers.size() - 1));
            drawers.remove(drawer);
            drawers.add(drawer);
            this.getChildren().add(drawer);
        }
    }

    /**
     * toggle a drawer in the stack
     *
     * @param drawer the drawer to be toggled
     */
    public void toggle(JFXDrawer drawer) {
        if (!drawers.contains(drawer)) {
            addDrawer(drawer);
        }
        if (drawer.isShown() || drawer.isShowing()) {
            drawer.close();
        } else {
            updateDrawerPosition(drawer);
            drawer.open();
        }
    }

    /**
     * toggle on/off a drawer in the stack
     *
     * @param drawer the drawer to be toggled
     * @param show   true to toggle on, false to toggle off
     */
    public void toggle(JFXDrawer drawer, boolean show) {
        if (!drawers.contains(drawer)) {
            addDrawer(drawer);
        }
        if (!show) {
            if (drawer.isShown() || drawer.isShowing()) {
                drawer.close();
            }
        } else {
            if (!drawer.isShown() && !drawer.isShowing()) {
                updateDrawerPosition(drawer);
                drawer.open();
            }
        }
    }


}
