/**
 * 
 */
package com.ttu_swri.datamodel;

import android.util.Log;

/**
 * @author kub1x
 * 
 */
public abstract class Visualization implements IVisitable {

	// TODO REFACTORING: move this class to visualization package once it's
	// clear how it works

	@Override
	public void visit(IElementlike elementlike) throws ClassCastException {
		Element element = null;

		// This visitor takes only objects of class Element
		try {
			element = (Element) elementlike;

			switch (element.Type) {
			case T_MATE:
				this.draw((ElementMate) element);
				break;

			case T_POI:
				this.draw((ElementPoi) element);
				break;

			case T_MESSAGE:
				this.draw((ElementMessage) element);
				break;

			default:
				// TODO log and throw unknown/not implemented element exception
				break;
			}

		} catch (ClassCastException e) {
			Log.e("Visualisation.visit", "Invalid cast to Element occured! ", e);
			throw e;
		}
	}

	void draw(ElementMate element) {
	}

	void draw(ElementPoi element) {
	}

	void draw(ElementMessage element) {
	}

}
