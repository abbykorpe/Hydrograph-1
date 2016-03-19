/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package com.bitwise.app.graph;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

// TODO: Auto-generated Javadoc
/**
 * The Class FileChooserEx.
 */
public class FileChooserEx {
    
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
    public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                new FileChooserEx().createUI();
            }
        };

        EventQueue.invokeLater(r);
    }

    private void createUI() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        JButton saveBtn = new JButton("Save");
        JButton openBtn = new JButton("Open");

        saveBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser saveFile = new JFileChooser();
                saveFile.showSaveDialog(null);
            }
        });

        openBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser openFile = new JFileChooser();
                openFile.showOpenDialog(null);
            }
        });

        frame.add(new JLabel("File Chooser"), BorderLayout.NORTH);
        frame.add(saveBtn, BorderLayout.CENTER);
        frame.add(openBtn, BorderLayout.SOUTH);
        frame.setTitle("File Chooser");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}