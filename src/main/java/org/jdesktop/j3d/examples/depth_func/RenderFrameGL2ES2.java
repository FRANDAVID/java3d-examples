/*
 * Copyright (c) 2016 JogAmp Community. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the JogAmp Community.
 *
 */

package org.jdesktop.j3d.examples.depth_func;

import java.awt.GraphicsConfiguration;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.OrderedGroup;
import org.jogamp.java3d.PointLight;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.RenderingAttributes;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.ScaleInterpolator;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.behaviors.mouse.MouseRotate;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.shader.Cube;
import org.jogamp.java3d.utils.shader.SimpleShaderAppearance;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

public class RenderFrameGL2ES2 extends javax.swing.JFrame
{

	DepthFuncTestGL2ES2 dpt;

	SimpleUniverse su;

	RenderingAttributes staticWFBoxRA;
	RenderingAttributes staticBoxRA;

	RenderingAttributes rotatingBoxRA;

	/** Creates new form RenderFrame */
	public RenderFrameGL2ES2(DepthFuncTestGL2ES2 _dpt)
	{
		dpt = _dpt;
		initComponents();
		initUniverse();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents()
	{

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("J3D frame");
		setBounds(400, 0, 640, 480);
	}
	// </editor-fold>//GEN-END:initComponents

	void initUniverse()
	{
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		Canvas3D c = new Canvas3D(config);
		add("Center", c);
		su = new SimpleUniverse(c);
		su.addBranchGraph(createScene());
		c.getView().setMinimumFrameCycleTime(10);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables

	BranchGroup createScene()
	{
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

		BranchGroup globalBG = new BranchGroup();
		BranchGroup rotObjectBG = new BranchGroup();
		OrderedGroup staticObjectOG = new OrderedGroup();
		BranchGroup lampsBG = new BranchGroup();
		OrderedGroup oGroup = new OrderedGroup();
		TransformGroup staticBoxRotTG = new TransformGroup();
		staticBoxRotTG.addChild(staticObjectOG);
		TransformGroup objectsTGRot = new TransformGroup();
		TransformGroup objectsTGTrans = new TransformGroup();
		Transform3D objectsTGTransT3d = new Transform3D();
		objectsTGTransT3d.setTranslation(new Vector3f(0.0f, 0.0f, -10.0f));
		objectsTGTrans.setTransform(objectsTGTransT3d);
		objectsTGRot.addChild(oGroup);
		objectsTGTrans.addChild(objectsTGRot);
		lampsBG.addChild(objectsTGTrans);

		//adding a sphere as background so there is something else than flat black, and cut cube removal as an other implication. (seeing through)
		Appearance globalSphereAppearance = new SimpleShaderAppearance();
		PolygonAttributes globalSpherePA = new PolygonAttributes();
		globalSpherePA.setCullFace(PolygonAttributes.CULL_FRONT);// so that interior of the sphere is visible.
		Material globalSphereMaterial = new Material();
		globalSphereMaterial.setEmissiveColor(.25f, .3f, .35f);
		globalSphereAppearance.setMaterial(globalSphereMaterial);
		globalSphereAppearance.setPolygonAttributes(globalSpherePA);
		Sphere globalSphere = new Sphere(6.0f, globalSphereAppearance);
		globalSphere.setBounds(bounds);
		oGroup.addChild(globalSphere);

		globalBG.addChild(lampsBG);

		// adding lamps.
		PointLight frontLamp = new PointLight(new Color3f(1.0f, 1.0f, 1.0f), new Point3f(20, 20, 20), new Point3f(0.0f, .0f, 0.f));
		lampsBG.addChild(frontLamp);
		frontLamp.setBounds(bounds);
		frontLamp.setInfluencingBounds(bounds);
		PointLight backLamp = new PointLight(new Color3f(1.0f, .0f, .0f), new Point3f(-20, -20, -20), new Point3f(0.0f, .0f, 0.f));
		lampsBG.addChild(backLamp);
		backLamp.setBounds(bounds);
		backLamp.setInfluencingBounds(bounds);

		//adding shapes.
		{
			//adding rotating and scaling cube
			//doing the rotation
			TransformGroup rotBoxTGRot = new TransformGroup();
			rotBoxTGRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			RotationInterpolator rotBoxRotInt = new RotationInterpolator(new Alpha(-1, 20000), rotBoxTGRot);
			rotBoxRotInt.setSchedulingBounds(bounds);
			rotBoxRotInt.setBounds(bounds);

			//doing the scaling
			//Transform3D scaleBoxt3d = new Transform3D();
			TransformGroup rotBoxTGScale = new TransformGroup();
			rotBoxTGScale.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			ScaleInterpolator rotBoxScaleInt = new ScaleInterpolator(
					new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 3000, 1500, 0, 3000, 1500, 0), rotBoxTGScale,
					new Transform3D(), 0.7f, 1.6f);
			rotBoxScaleInt.setSchedulingBounds(bounds);
			rotBoxScaleInt.setBounds(bounds);

			SimpleShaderAppearance rotBoxApp = new SimpleShaderAppearance();
			Material rotBoxMat = new Material();
			rotBoxMat.setDiffuseColor(.4f, .4f, .4f);
			rotBoxApp.setMaterial(rotBoxMat);
			Cube rotBox = new Cube(1.1f / 2, 1.1f / 2, 1.1f / 2);
			rotBox.setAppearance(rotBoxApp);
			rotBoxTGScale.addChild(rotBox);
			rotBoxTGRot.addChild(rotBoxTGScale);
			TransformGroup rotBoxTG = new TransformGroup();
			rotBoxTG.addChild(rotBoxTGRot);
			rotObjectBG.addChild(rotBoxTG);
			rotObjectBG.addChild(rotBoxScaleInt);
			rotObjectBG.addChild(rotBoxRotInt);
			rotBox.setBounds(bounds);

			rotatingBoxRA = new RenderingAttributes();
			//Raster Ops are not availible in GL2ES2
			//       rotatingBoxRA.setRasterOpEnable( true );
			rotatingBoxRA.setCapability(RenderingAttributes.ALLOW_RASTER_OP_WRITE);
			//        rotatingBoxRA.setRasterOp( rotatingBoxRA.ROP_XOR );
			rotBoxApp.setRenderingAttributes(rotatingBoxRA);

			rotBox.setAppearance(rotBoxApp);
		}

		//adding static back face wireframe cube
		{
			
			SimpleShaderAppearance staticWFBoxApp = new SimpleShaderAppearance();
			Cube staticWFBoxBack = new Cube();
			Material staticWFBoxMat = new Material();
			staticWFBoxMat.setDiffuseColor(0.f, 0.f, 0.f);
			staticWFBoxMat.setEmissiveColor(0.f, .4f, 0.f);
			staticWFBoxApp.setMaterial(staticWFBoxMat);
			PolygonAttributes staticWFBoxPABack = new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_FRONT, 0.0f);
			staticWFBoxApp.setPolygonAttributes(staticWFBoxPABack);
			staticWFBoxRA = new RenderingAttributes();
			staticWFBoxRA.setCapability(RenderingAttributes.ALLOW_DEPTH_TEST_FUNCTION_WRITE);
			staticWFBoxRA.setCapability(RenderingAttributes.ALLOW_DEPTH_ENABLE_WRITE);
			staticWFBoxRA.setDepthTestFunction(RenderingAttributes.GREATER);
			staticWFBoxRA.setDepthBufferWriteEnable(false);
			staticWFBoxApp.setRenderingAttributes(staticWFBoxRA);
			staticWFBoxBack.setAppearance(staticWFBoxApp);
			staticWFBoxBack.setBounds(bounds);
			staticObjectOG.addChild(staticWFBoxBack);
		}

		//adding static front face wireframe cube
		{
			
			Appearance staticWFBoxApp = new SimpleShaderAppearance();
			
			// TODO: if not geometry not shared then appearance shared what????
			//Box staticWFBox = new Box(1,1,1, Box.GENERATE_NORMALS | Box.GEOMETRY_NOT_SHARED, staticWFBoxApp);
			Cube staticWFBox = new Cube();
			staticWFBox.setAppearance(staticWFBoxApp);
			
			Material staticWFBoxMat = new Material();
			staticWFBoxMat.setDiffuseColor(0.f, 0.f, 0.f);
			staticWFBoxMat.setEmissiveColor(0.f, 1.f, 0.f);
			staticWFBoxApp.setMaterial(staticWFBoxMat);
			PolygonAttributes staticWFBoxPA = new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_BACK, 0.0f);
			staticWFBoxApp.setPolygonAttributes(staticWFBoxPA);
			staticWFBoxApp.setRenderingAttributes(staticWFBoxRA);
			staticWFBox.setBounds(bounds);
			staticObjectOG.addChild(staticWFBox);
		}

		{// rotating the static cubes
			Transform3D boxt3d = new Transform3D();
			Transform3D tempt3d = new Transform3D();
			boxt3d.rotZ(Math.PI / 4.0f);
			tempt3d.rotX(Math.PI / 4.0f);
			boxt3d.mul(tempt3d);
			tempt3d.rotY(Math.PI / 4.0f);
			boxt3d.mul(tempt3d);
			staticBoxRotTG.setTransform(boxt3d);
		}

		// adding static flat cube
		{
			SimpleShaderAppearance boxApp = new SimpleShaderAppearance();
			Box staticBox = new Box(1, 1, 1,Box.GENERATE_NORMALS | Box.GEOMETRY_NOT_SHARED, boxApp);
			staticBox.setBounds(bounds);
			Material boxMat = new Material();
			boxMat.setDiffuseColor(.7f, .7f, .7f);
			boxApp.setMaterial(boxMat);
			staticBoxRA = new RenderingAttributes();
			staticBoxRA.setCapability(RenderingAttributes.ALLOW_DEPTH_TEST_FUNCTION_WRITE);
			staticBoxRA.setCapability(RenderingAttributes.ALLOW_DEPTH_ENABLE_WRITE);
			staticBoxRA.setDepthTestFunction(RenderingAttributes.LESS);
			staticBoxRA.setDepthBufferWriteEnable(false);
			boxApp.setRenderingAttributes(staticBoxRA);
			staticObjectOG.addChild(staticBox);
		}
		oGroup.addChild(rotObjectBG);
		oGroup.addChild(staticBoxRotTG);

		//adding the mouse rotate behavior to the group of cubes.
		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(objectsTGRot);
		objectsTGRot.addChild(behavior);
		objectsTGRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objectsTGRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		behavior.setSchedulingBounds(bounds);
		return globalBG;
	}

	public void setStaticWFObjectTestFunc(int func)
	{
		staticWFBoxRA.setDepthTestFunction(func);
	}

	public void setStaticObjectTestFunc(int func)
	{
		staticBoxRA.setDepthTestFunction(func);
	}

	public void setStaticWFObjectDBWriteStatus(boolean status)
	{
		staticWFBoxRA.setDepthBufferWriteEnable(status);
	}

	public void setStaticObjectDBWriteStatus(boolean status)
	{
		staticBoxRA.setDepthBufferWriteEnable(status);
	}

	public void setRotatingObjectROPMode(int mode)
	{
		//Raster Ops are not availible in GL2ES2
		//rotatingBoxRA.setRasterOp( mode );
	}

}