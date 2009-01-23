/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.locke.tricks.m;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.locke.Trick;
import com.locke.library.web.resources.images.mounted.MountedDynamicImageResource;

/**
 * Trick 'M'
 * 
 * @author Jonathan Locke
 */
@SuppressWarnings("serial")
public class M extends Trick {

	private static final MountedDynamicImageResource orangeBox = new MountedDynamicImageResource(
			"/images", "orange-box", 100, 100) {

		@Override
		protected boolean render(Graphics2D graphics) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, getWidth(), getHeight());
			graphics.setColor(new Color(0xda, 0x8d, 0x00));
			graphics.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
			String text = "26";
			graphics.setFont(new Font("Helvetica", Font.BOLD, 50));
			int x = (getWidth() - graphics.getFontMetrics().stringWidth(text)) / 2;
			int y = getHeight()
					- ((getHeight() - graphics.getFontMetrics().getAscent()) / 2);
			graphics.setColor(graphics.getColor().brighter());
			graphics.drawString(text, x, y);
			return true;
		}
	};

	public M() {
		add(orangeBox.newImage("image"));
	}
}
