package com.eddyzou.mdlparser.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.ExpandVetoException;

import com.eddyzou.mdlparser.dao.Pair;

import com.eddyzou.mdlparser.dao.MDLObject;


/**
 * 
 * @author 73588
 *
 */
public class MDLParser {
	public MDLObject model;
	private String filePath;
	private FileReader fo;
	private BufferedReader br;
	private String propertyTemp="";
	private Pair<String,String> lastProperty=new Pair<String,String>();
	private MDLObject modelTemp;
	private FileWriter fWriter;
	private BufferedWriter bw;
	private int tabs=0;
	public MDLParser(String mdlFile,String outputPath) throws Exception {
		filePath=mdlFile;
		File f=new File(outputPath);
		if(!f.exists())
			f.createNewFile();
		fWriter=new FileWriter(f.getPath());
		bw=new BufferedWriter(fWriter);
	}
	public MDLParser(String mdlFile) throws Exception {
		filePath=mdlFile;

	}
	private int addSubObject(String line,MDLObject Obj) {
		try {
			MDLObject subObject;
			String objectName=line.split("\\{")[0].trim();
			subObject=new MDLObject(objectName);
			modelTemp=subObject;
			subObject.setParent(Obj);
			line=br.readLine();
			while(!line.contains("}")) {
				line=line.trim();
				if (line.contains("{")) {
					addSubObject(line,modelTemp);
					modelTemp=modelTemp.getParent();
				}
				else if(line.toCharArray()[0]=='\"') {
					supplymentProperty(line, modelTemp.getPropertyMap());
				}
				else {
					addProperty(line, modelTemp.getPropertyMap());
				}
				line=br.readLine();
			}
			Obj.addSubObject(subObject);
			return 1;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	private int supplymentProperty(String line,List<Pair<String,String>> map) {
		try {
			propertyTemp=propertyTemp+line;
			lastProperty.value=propertyTemp;
			map.remove(map.size()-1);
			map.add(lastProperty);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	private int addProperty(String line,List<Pair<String, String>> propertys) {
		try {
			propertyTemp="";
			lastProperty.key="";
			lastProperty.value="";
			String[] strsStrings=line.split("\\s+");
			lastProperty.key=strsStrings[0];
			lastProperty.value=strsStrings[1];
			for(int i=2;i<strsStrings.length;i++)
				lastProperty.value=lastProperty.value+" "+strsStrings[i];
			propertyTemp=lastProperty.value;
			Pair<String, String> pair=new Pair<String, String>();
			pair.key=lastProperty.key;
			pair.value=lastProperty.value;
			propertys.add(pair);
			return 1;
		}catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
	}
	public int CenterController() {
		try {
			model=new MDLObject("root");
			File mdlFile=new File(filePath);
			if(mdlFile.exists()==false) {
				System.out.println("file does not exist");
				return -1;
			}
			fo=new FileReader(mdlFile.getPath());
			br=new BufferedReader(fo);
			String line=br.readLine();
			while(line!=null&&(line.isEmpty()==false)) {
				if (line.contains("{")) {
					addSubObject(line,model);
					modelTemp=modelTemp.getParent();
				}
				else if(line.toCharArray()[0]=='\"') {
					supplymentProperty(line, modelTemp.getPropertyMap());
				}
				else {
					addProperty(line, modelTemp.getPropertyMap());
				}
				line=br.readLine();
			}
			fo.close();
			br.close();
			model=model.getSubObjects().get(0);
			return 1;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
	public MDLObject getApplication() {
		MDLObject modelSystem=new MDLObject("");
		for(MDLObject obj:model.getSubObjects())
			if(obj.getMdlObjName().equals("System"))
				modelSystem=obj;
		for(MDLObject obj:modelSystem.getSubObjects()) {

			if(obj.getMdlObjName().trim().equals("Block")) {
				List<Pair<String, String>> props=obj.getPropertyMap();
				for(Pair<String,String> p : props)
					if(p.key.equals("BlockType")&&p.value.equals("SubSystem"))
						return obj;
			}
		}
		return null;
	}

	public void outputFile(MDLObject mdlobj) throws IOException {
		String tabString="";
		for(int i=0;i<tabs;i++)
			tabString=tabString+" ";
		bw.write(tabString+mdlobj.getMdlObjName()+"  {"+"\n");
		tabs=tabs+1;
		for(int i=0;i<mdlobj.getPropertyMap().size();i++) {
			tabString="";
			for(int j=0;j<tabs;j++)
				tabString=tabString+" ";
			Pair<String, String> prop=mdlobj.getPropertyMap().get(i);
			bw.write(tabString+prop.key+"  "+prop.value+"\n");
		}
		for(MDLObject subobj:mdlobj.getSubObjects()) {
			outputFile(subobj);
		}
		tabs=tabs-1;
		tabString="";
		for(int i=0;i<tabs;i++)
			tabString=tabString+" ";
		bw.write(tabString+"}"+"\n");
	}
	public void outputDone() throws Exception {
		bw.close();
	}
}

