/*-
 * Copyright 2005-2018 Diomidis Spinellis
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

 /**
* @version Game Extension by Maria Komissa Kalafati
*/

package gr.aueb.card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class HolidayCard {

    /** Number of trees */
    private static final int NUM_TREES = 30;
    /** Number of snowflakes */
    private static final int NUM_SNOWFLAKES = 1500;
    /** Minimum tree width. */
    private static final int TREE_WIDTH = 30;
    /** Minimum tree height. */
    private static final int TREE_HEIGHT = 100;
    /** Additional variation to tree height and width */
    private static final int TREE_WOBBLE = 100;
    /** Song to play. */
    private static String musicFile = "/Jingle_Bells_full_Ab.mid";

    /** Counter for clicked red balls */
    private static int redBallClickCount = 0;

    public static void main(String[] args) {

        // Create a window and the canvas to draw onto.
        DrawPanel d = DrawPanel.getInstance();
        List<Tree> trees = new ArrayList<>();

        // Create randomly-positioned trees.
        for (int i = 0; i < NUM_TREES; i++) {
            Rectangle treeBox = new Rectangle(
                    (int) (Math.random() * DrawPanel.WIDTH),
                    (int) (Math.random() * DrawPanel.HEIGHT),
                    TREE_WIDTH + (int) (Math.random() * TREE_WOBBLE),
                    TREE_HEIGHT + (int) (Math.random() * TREE_WOBBLE));

            Tree t = new Tree(d.getCanvas(), treeBox);
            d.addDrawObject(t);
            trees.add(t); // Store trees for later red ball detection
        }

        // Add mouse listener for click detection
        d.getCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();

                // Check if the click is on a red ball
                for (Tree tree : trees) {
                    if (tree.isRedBallClicked(clickPoint)) {
                        redBallClickCount++;
                        if (redBallClickCount == 20) {
                            JOptionPane.showMessageDialog(null, "Merry Christmas!");
                        }
                        break;
                    }
                }
            }
        });

        // Start playing music
        MidiPlayer m = new MidiPlayer(musicFile);

        // Create the snowflakes.
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
            switch (i % 6) {
                case 0:
                case 1:
                    d.addDrawObject(new PointSnowFlake(d.getCanvas(), '.', 15));
                    break;
                case 2:
                    d.addDrawObject(new PointSnowFlake(d.getCanvas(), 'o', 10));
                    break;
                case 3:
                    d.addDrawObject(new PointSnowFlake(d.getCanvas(), '*', 5));
                    break;
                case 4:
                case 5:
                    d.addDrawObject(new SlashSnowFlake(d.getCanvas()));
                    break;
            }
            try {
                // Allow existing snowflakes to fall a bit, before adding more
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
