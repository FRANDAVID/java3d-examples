/*
 * $RCSfile$
 *
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 * $Revision$
 * $Date$
 * $State$
 */

package org.jdesktop.j3d.examples.offscreen_canvas3d;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;

import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.ImageComponent;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Raster;


class OffScreenCanvas3D extends Canvas3D {

  Raster drawRaster;
  boolean printing = false;

  public OffScreenCanvas3D(GraphicsConfiguration gconfig, boolean offscreenflag,
			Raster drawRaster) {

    super(gconfig, offscreenflag);
    this.drawRaster = drawRaster;
  }

  public void print(boolean toWait) {

    if (!toWait)
        printing = true;

    BufferedImage bImage = new BufferedImage(
                                200, 200 , BufferedImage.TYPE_3BYTE_BGR);

    ImageComponent2D buffer = new ImageComponent2D(
                                ImageComponent.FORMAT_RGB, bImage, true, true);
    buffer.setCapability(ImageComponent2D.ALLOW_IMAGE_READ);

    this.setOffScreenBuffer(buffer);
    this.renderOffScreenBuffer();

   if (toWait) {
	this.waitForOffScreenRendering();
	drawOffScreenBuffer();
    }
  }

  public void postSwap() {
   
    if (printing) {
        super.postSwap();
	drawOffScreenBuffer();
	printing = false;
    }
  }

  void drawOffScreenBuffer() {

    BufferedImage bImage = this.getOffScreenBuffer().getImage();
    ImageComponent2D newImageComponent = new ImageComponent2D(
	    ImageComponent.FORMAT_RGB, bImage, true, true);

    drawRaster.setImage(newImageComponent);
  }
}

