package edu.ttu.swri.goggles.persistence;

import java.util.Collection;


import edu.ttu.swri.data.model.ElementMate;
import edu.ttu.swri.data.model.ElementMessage;
import edu.ttu.swri.data.model.ElementPoi;

public interface ElementDAO {
	
	public String saveElementPoi(ElementPoi elementPoi) throws RuntimeException;
	
	public void saveElementPois(Collection<ElementPoi> elementPoiCollection) throws RuntimeException;
	
	public String saveElementMessage(ElementMessage elementMessage) throws RuntimeException;
	
	public void saveElementMessages(Collection<ElementMessage> elementMessages) throws RuntimeException;

	public String saveElementMate(ElementMate elementMate) throws RuntimeException;
	
	public void saveElementMates(Collection<ElementMate> elementMate) throws RuntimeException;
	
	
	public ElementPoi getElementPoi(String id) throws RuntimeException;
	
	public Collection<ElementPoi> getElementPois();
	
	public ElementMessage getElementMessage(String id) throws RuntimeException;
	
	public Collection<ElementMessage> getElementMessages();
	
	public ElementMate getElementMate(String id);
	
	public Collection<ElementMate> getElementMates();
	
	
	
}
