package com.eddyzou.mdlparser.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MDLObject {
	private String mdlObjName;
	private List<Pair<String, String>> propertyMap=new ArrayList<Pair<String,String>>();
	private List<MDLObject> subObjects=new ArrayList<MDLObject>();
	private MDLObject parent;
	public MDLObject(String mdlObjName) {
		this.mdlObjName=mdlObjName;
	}
	public void setMdlObjName(String mdlObjName) {
		this.mdlObjName=mdlObjName;
	}
	public String getMdlObjName() {
		return this.mdlObjName;
	}
	public void setPropertyMap(List<Pair<String, String>> map) {
		this.propertyMap=map;
	}
	public List<Pair<String, String>> getPropertyMap() {
		return this.propertyMap;
	}
	public void addSubObject(MDLObject mdlobj) {
		this.subObjects.add(mdlobj);
	}
	public List<MDLObject> getSubObjects() {
		return this.subObjects;
	}
	public MDLObject getParent() {
		return this.parent;
	}
	public void setParent(MDLObject parentObj) {
		this.parent=parentObj;
	}
	public void insertApp(MDLObject app) {
		MDLObject modelSystem=new MDLObject("");
		for(MDLObject obj:this.subObjects)
			if(obj.getMdlObjName().equals("System"))
				modelSystem=obj;
		modelSystem.subObjects.add(0,app);
	}
}
