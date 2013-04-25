package com.ttu_swri.goggles.persistence;

import java.util.Collection;

import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.ElementMessage;
import com.ttu_swri.datamodel.ElementPoi;

public interface ElementDAO {
	
//	public void saveElementPOI(ElementPoi element){
//		
//	}
//	
//	public void saveElementPOI(ElementMessage element){
//		
//	}	
//
//	public void saveElementPOI(ElementMate element){
//		
//	}
	
	public void saveElementPoi(ElementPoi elementPoi);
	
	public void saveElementPois(Collection<ElementPoi> elementPoiCollection);
	
	public void saveElementMessage(ElementMessage elementMessage);
	
	public void saveElementMessages(Collection<ElementMessage> elementMessage);

	public void saveElementMate(ElementMate elementMate);
	
	public void saveElementMates(Collection<ElementMate> elementMate);
	
	
	public ElementPoi getElementPoi(String id);
	
	public Collection<ElementPoi> getElementPois();
	
	public ElementMessage getElementMessage(String id);
	
	public Collection<ElementMessage> getElementMessages();
	
	public ElementMate getElementMate(String id);
	
	public Collection<ElementMate> getElementMates();
	
	
	
}
